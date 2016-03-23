package photoman.service.nef.utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import org.apache.aries.util.IORuntimeException;

import photoman.utils.BinaryUtils;

public class NefIFDEntry
{
	
	//========================================
	//===           CONSTANTS              ===
	//========================================
	
	//List of FieldTypes for all types indicating name, number of bytes per value 
	//and processing function to read the value
	private final FieldType TYPE_BYTE = new FieldType("BYTE", 1, this::processByte);
	private final FieldType TYPE_ASCII = new FieldType("ASCII", 1, this::processAsciiString);
	private final FieldType TYPE_SHORT = new FieldType("SHORT", 2, this::processShort);
	private final FieldType TYPE_LONG = new FieldType("LONG", 4, this::processLong);
	private final FieldType TYPE_RATIONAL = new FieldType("RATIONAL", 8, this::processRational);
	private final FieldType TYPE_SBYTE = new FieldType("SBYTE", 1, this::processByte);
	private final FieldType TYPE_UNDEFINED = new FieldType("UNDEFINED", 1, this::processByte);
	private final FieldType TYPE_SSHORT = new FieldType("SSHORT", 2, this::processShort);
	private final FieldType TYPE_SLONG = new FieldType("SLONG", 4, this::processLong);
	private final FieldType TYPE_SRATIONAL = new FieldType("SRATIONAL", 8, this::processRational);
	private final FieldType TYPE_FLOAT = new FieldType("FLOAT", 4, this::processFloat);
	private final FieldType TYPE_DOUBLE = new FieldType("DOUBLE", 8, this::processDouble);
	
	//Map of field codes to field types 
	public final Map<Integer, FieldType> FIELD_TYPES = new HashMap<>();
	{
		FIELD_TYPES.put(1, TYPE_BYTE);
		FIELD_TYPES.put(2, TYPE_ASCII);
		FIELD_TYPES.put(3, TYPE_SHORT);
		FIELD_TYPES.put(4, TYPE_LONG);
		FIELD_TYPES.put(5, TYPE_RATIONAL);
		FIELD_TYPES.put(6, TYPE_SBYTE);
		FIELD_TYPES.put(7, TYPE_UNDEFINED);
		FIELD_TYPES.put(8, TYPE_SSHORT);
		FIELD_TYPES.put(9, TYPE_SLONG);
		FIELD_TYPES.put(10, TYPE_SRATIONAL);
		FIELD_TYPES.put(11, TYPE_FLOAT);
		FIELD_TYPES.put(12, TYPE_DOUBLE);
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
		boolean isByteArray = values != null && values.size() > 0 && values.get(0).getClass().getSimpleName().equals("byte[]");
		return "NefIFDEntry [" + IFDLookup.ENTRY.get(fieldCode) + "(0x" + Integer.toHexString(fieldCode) 
					+ "), type:" + getFieldType()
					+ " count:" + valueCount 
					+ " values: " +  (isByteArray ? Arrays.toString((byte[])values.get(0)) : 
								values) + "]"; 
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
		
		type.process(); 
		
		//Return the file pointer to where it was
		file.seek(currentFilePos);
	}
	
	private void processByte(Void v)
	{
		processWithLookup(n -> this.values.add(BinaryUtils.readByteArray(file, valueCount)));
	}
	
	private void processAsciiString(Void v)
	{
		processWithLookup(n -> this.values.add(BinaryUtils.readUTF8String(file, valueCount)));
	}
	
	private void processShort(Void v)
	{
		processWithLookup(n -> IntStream.range(0, valueCount)
				.forEach(
					(i)->values.add(BinaryUtils.readShort(file, bo))
				));
	}
	
	private void processLong(Void v)
	{
		processWithLookup(n -> IntStream.range(0, valueCount)
				.forEach(
					(i)->values.add(BinaryUtils.readInt(file, bo))
				));
	}
	
	private void processRational(Void v)
	{
		processWithLookup(n -> IntStream.range(0, valueCount)
				.forEach(
					(i)-> 
					{
						double numerator = BinaryUtils.readInt(file, bo);
						double denominator = BinaryUtils.readInt(file, bo);
						values.add(numerator/denominator);
					}
				));
	}
	
	private void processFloat(Void v)
	{
		processWithLookup(n -> IntStream.range(0, valueCount)
				.forEach(
					(i)->values.add(BinaryUtils.readFloat(file, bo))
				));
	}
	
	private void processDouble(Void v)
	{
		processWithLookup(n -> IntStream.range(0, valueCount)
				.forEach(
					(i)->values.add(BinaryUtils.readDouble(file, bo)))
				);
	}
	
	private void processWithLookup(Consumer<Void> func)
	{
		try
		{
			if(lookupRequired)
			{
				int lookupOffset = BinaryUtils.readInt(file, bo);
				
				//Don't record the file pointer until after we've read the lookup value
				long currentPos = file.getFilePointer();
				file.seek(lookupOffset);
				
				//run our function 
				func.accept(null);
				
				//Revert back to where we were after we read the offset value
				file.seek(currentPos);
			}
			else
			{
				long currentPos = file.getFilePointer();
				
				//run our function 
				func.accept(null);
				
				//Make sure we always end up 4 bytes from where we started, even if we read less
				//e.g. 1 short, 1 byte etc
				file.seek(currentPos + 4);
			}
		}
		catch(IOException ioe)
		{
			//Rethrow checked IOExceptions as unchecked IORuntimeExceptions
			throw new IORuntimeException("Unable to Seek to location in file", ioe);
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
		return FIELD_TYPES.get(fieldType).name;
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
	
	private class FieldType
	{		
		public final String name; 
		public final int numBytes;
		public final Consumer<Void> func;
		
		FieldType(String name, int numBytes, Consumer<Void> func)
		{
			this.name = name;
			this.numBytes = numBytes;
			this.func = func;
		}
		
		public void process()
		{
			//Run the processing function associated with this type
			this.func.accept(null);
		}
		
	}
	
}
