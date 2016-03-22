package photoman.service.nef.utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.neo4j.cypher.internal.compiler.v2_2.perty.recipe.formatErrors;

import photoman.utils.BinaryUtils;

public class NefIFDEntry
{
	
	//========================================
	//===           CONSTANTS              ===
	//========================================
	
	public static final Map<Integer, FieldType> FIELD_TYPES = new HashMap<>();
	static
	{
		FIELD_TYPES.put(1, FieldType.BYTE);
		FIELD_TYPES.put(2, FieldType.ASCII);
		FIELD_TYPES.put(3, FieldType.SHORT);
		FIELD_TYPES.put(4, FieldType.LONG);
		FIELD_TYPES.put(5, FieldType.RATIONAL);
		FIELD_TYPES.put(6, FieldType.SBYTE);
		FIELD_TYPES.put(7, FieldType.UNDEFINED);
		FIELD_TYPES.put(8, FieldType.SSHORT);
		FIELD_TYPES.put(9, FieldType.SLONG);
		FIELD_TYPES.put(10, FieldType.SRATIONAL);
		FIELD_TYPES.put(11, FieldType.FLOAT);
		FIELD_TYPES.put(12, FieldType.DOUBLE);
	};
	
	//========================================
	//===            FIELDS                ===
	//========================================
	
	private RandomAccessFile file;
	private ByteOrder bo;
	
	private int entryStartOffset;
	private int fieldCode;
	private int fieldType;
	private int valueCount;
	private List<Object> values = new ArrayList<>();
	
	private FieldType type;
	private boolean lookupRequired;
	//========================================
	//===         CONSTRUCTORS             ===
	//========================================
	
	public NefIFDEntry(RandomAccessFile file, ByteOrder bo, int entryStartOffset) throws IOException
	{
		this.file = file;
		this.bo = bo;
		this.entryStartOffset = entryStartOffset;
		initEntry();
	}
	
	//========================================
	//===         PUBLIC METHODS           ===
	//========================================
	
	@Override
	public String toString()
	{
		return "NefIFDEntry [" + IFDLookup.ENTRY.get(fieldCode) + "(0x" + Integer.toHexString(fieldCode) 
					+ "), type:" + getFieldType()
					+ " count:" + valueCount 
					+ " values: " + values + "]"; 
	}
	
	//========================================
	//===        PRIVATE METHODS           ===
	//========================================
	
	private void initEntry() throws IOException
	{
		long currentFilePos = file.getFilePointer();
		
		//Get the entries values from the file
		file.seek(entryStartOffset);
		this.fieldCode = BinaryUtils.readShort(file, bo);
		this.fieldType = BinaryUtils.readShort(file, bo);
		this.valueCount = BinaryUtils.readInt(file, bo);
		
		this.type = FIELD_TYPES.get(fieldType);
		this.lookupRequired = (type.numBytes * valueCount) > 4;
		
		deriveValue(); 
		
		//Return the file pointer to where it was
		file.seek(currentFilePos);
	}
	
	private void deriveValue() throws IOException
	{		
		switch(type)
		{
			case UNDEFINED:
			case SBYTE:
			case BYTE:
				processByte();
				break;
			case ASCII:
				processAsciiString();
				break;
			case SHORT:
			case SSHORT:
				processShort();
				break;
			case LONG:
			case SLONG:
				processLong();
				break;
			case RATIONAL:
			case SRATIONAL:
				processRational();
				break;
		}
	}
	
	private void processByte() throws IOException
	{
		byte[] bytes;
		if(lookupRequired)
		{
			bytes = new byte[valueCount];
			int lookupOffset = BinaryUtils.readInt(file, bo);
			//Don't record the file pointer until after we've read the lookup value
			long currentPos = file.getFilePointer();
			file.seek(lookupOffset);
			file.read(bytes);
			file.seek(currentPos);
		}
		else
		{
			//If it's not a lookup then we'll always read 4 bytes, but only
			//store 'valueCount' values
			bytes = new byte[4];
			file.read(bytes);
		}
		for(int i = 0; i < valueCount; i++)
		{
			this.values.add(bytes[i]);
		}
	}
	
	private void processAsciiString() throws IOException
	{
		if(lookupRequired)
		{
			int lookupOffset = BinaryUtils.readInt(file, bo);
			//Don't record the file pointer until after we've read the lookup value
			long currentPos = file.getFilePointer();
			file.seek(lookupOffset);
			this.values.add(BinaryUtils.readUTF8String(file, valueCount));
			file.seek(currentPos);
		}
		else
		{
			this.values.add(BinaryUtils.readUTF8String(file, 4));
		}
	}
	
	private void processShort() throws IOException
	{
		if(lookupRequired)
		{
			int lookupOffset = BinaryUtils.readInt(file, bo);
			//Don't record the file pointer until after we've read the lookup value
			long currentPos = file.getFilePointer();
			file.seek(lookupOffset);
			for(int i = 0; i < valueCount; i++)
			{
				this.values.add(BinaryUtils.readShort(file, bo));
			}	
			//IntStream.iterate(0, i -> i + 1).limit(valueCount - 1).forEach(n -> this.values.add(BinaryUtils.readShort(file, bo)));
			file.seek(currentPos);
		}
		else
		{
			long currentPos = file.getFilePointer();
			for(int i = 0; i < valueCount; i++)
			{
				this.values.add(BinaryUtils.readShort(file, bo));
			}
			//Make sure we get to the end if there's just a single short
			file.seek(currentPos + 4);
		}
	}
	
	private void processLong() throws IOException
	{
		if(lookupRequired)
		{
			int lookupOffset = BinaryUtils.readInt(file, bo);
			//Don't record the file pointer until after we've read the lookup value
			long currentPos = file.getFilePointer();
			file.seek(lookupOffset);
			for(int i = 0; i < valueCount; i++)
			{
				values.add(BinaryUtils.readInt(file, bo));
			}
			file.seek(currentPos);
		}
		else
		{
			//We know that there can only be one value here
			values.add(BinaryUtils.readInt(file, bo));
		}
	}
	
	private void processRational() throws IOException
	{
		//Lookup always required for RATIONAL since it needs 8 bytes min.
		int lookupOffset = BinaryUtils.readInt(file, bo);
		//Don't record the file pointer until after we've read the lookup value
		long currentPos = file.getFilePointer();
		file.seek(lookupOffset);
		for(int i = 0; i < valueCount; i++)
		{
			double numerator = BinaryUtils.readInt(file, bo);
			double denominator = BinaryUtils.readInt(file, bo);
			values.add(numerator/denominator);
		}
		file.seek(currentPos);
	}
	
	private void processFloat() throws IOException
	{
		
	}
	
	private void processWithLookup(BooleanSupplier lookupFunction, BooleanSupplier nonLookupFunction) throws IOException
	{
		if(lookupRequired)
		{
			int lookupOffset = BinaryUtils.readInt(file, bo);
			//Don't record the file pointer until after we've read the lookup value
			long currentPos = file.getFilePointer();
			file.seek(lookupOffset);
			
			lookupFunction.getAsBoolean();
			
			file.seek(currentPos);
		}
		else
		{
			nonLookupFunction.getAsBoolean();
		}
	}
	
	//========================================
	//===        GETTERS & SETTERS         ===
	//========================================

	public int getEntryStartOffset() { 
		return entryStartOffset;
	}

	public String getFieldName() { 
		return  IFDLookup.ENTRY.get(fieldCode);
	}
	
	public int getFieldCode() { 
		return fieldCode;
	}

	public String getFieldType() {
		return FIELD_TYPES.get(fieldType).name();
	}

	public int getValueCount() {
		return valueCount;
	}

	public List<Object> getValues() {
		return values;
	}
	
	//========================================
	//===      INNER CLASSES & ENUMS       ===
	//========================================
	
	private enum FieldType
	{
		BYTE(1), ASCII(1), SHORT(2), LONG(4), RATIONAL(8), SBYTE(1), UNDEFINED(1), SSHORT(2), SLONG(4), SRATIONAL(8), FLOAT(4), DOUBLE(8);
		
		public final int numBytes;
		
		FieldType(int numBytes)
		{
			this.numBytes = numBytes;
		}
		
	}
}
