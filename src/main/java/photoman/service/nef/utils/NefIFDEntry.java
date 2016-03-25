package photoman.service.nef.utils;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import org.apache.aries.util.IORuntimeException;

import photoman.service.nef.lookup.Lookup;
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
	
	private SeekableByteChannel input;
	private ByteOrder bo;
	
	private int entryStartOffset;
	private int fieldCode;
	private int fieldType;
	private int valueCount;
	private List<Object> values = new ArrayList<>();
	
	private FieldType type;
	private boolean lookupRequired;
	
	private Lookup lookup;
	//========================================
	//===         CONSTRUCTORS             ===
	//========================================
	
	public NefIFDEntry(SeekableByteChannel input, ByteOrder bo, int entryStartOffset, Lookup lookup) throws IOException
	{
		this.input = input;
		this.bo = bo;
		this.entryStartOffset = entryStartOffset;
		this.lookup = lookup;
		initEntry();
	}
	
	//========================================
	//===         PUBLIC METHODS           ===
	//========================================
	
	@Override
	public String toString()
	{
		boolean isByteArray = values != null && values.size() > 0 && values.get(0).getClass().getSimpleName().equals("byte[]");
		return "NefIFDEntry [" + lookup.get(fieldCode) + "(0x" + Integer.toHexString(fieldCode) 
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
		long currentFilePos = input.position();
		
		//Get the entries values from the file
		input.position(entryStartOffset);
		this.fieldCode = BinaryUtils.readShort(input, bo);
		this.fieldType = BinaryUtils.readShort(input, bo);
		this.valueCount = BinaryUtils.readInt(input, bo);
		
		this.type = FIELD_TYPES.get(fieldType);
		this.lookupRequired = (type.numBytes * valueCount) > 4;
		
		type.process(); 
		
		//Return the file pointer to where it was
		input.position(currentFilePos);
	}
	
	private void processByte(Void v)
	{
		processWithLookup(n -> this.values.add(BinaryUtils.readByteArray(input, valueCount)));
	}
	
	private void processAsciiString(Void v)
	{
		processWithLookup(n -> this.values.add(BinaryUtils.readUTF8String(input, valueCount)));
	}
	
	private void processShort(Void v)
	{
		processWithLookup(n -> IntStream.range(0, valueCount)
				.forEach(
					(i)->values.add(BinaryUtils.readShort(input, bo))
				));
	}
	
	private void processLong(Void v)
	{
		processWithLookup(n -> IntStream.range(0, valueCount)
				.forEach(
					(i)->values.add(BinaryUtils.readInt(input, bo))
				));
	}
	
	private void processRational(Void v)
	{
		processWithLookup(n -> IntStream.range(0, valueCount)
				.forEach(
					(i)-> 
					{
						double numerator = BinaryUtils.readInt(input, bo);
						double denominator = BinaryUtils.readInt(input, bo);
						values.add(numerator/denominator);
					}
				));
	}
	
	private void processFloat(Void v)
	{
		processWithLookup(n -> IntStream.range(0, valueCount)
				.forEach(
					(i)->values.add(BinaryUtils.readFloat(input, bo))
				));
	}
	
	private void processDouble(Void v)
	{
		processWithLookup(n -> IntStream.range(0, valueCount)
				.forEach(
					(i)->values.add(BinaryUtils.readDouble(input, bo)))
				);
	}
	
	private void processWithLookup(Consumer<Void> func)
	{
		try
		{
			if(lookupRequired)
			{
				int lookupOffset = BinaryUtils.readInt(input, bo);
				
				//Don't record the file pointer until after we've read the lookup value
				long currentPos = input.position();
				input.position(lookupOffset);
				
				//run our function 
				func.accept(null);
				
				//Revert back to where we were after we read the offset value
				input.position(currentPos);
			}
			else
			{
				long currentPos = input.position();
				
				//run our function 
				func.accept(null);
				
				//Make sure we always end up 4 bytes from where we started, even if we read less
				//e.g. 1 short, 1 byte etc
				input.position(currentPos + 4);
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
		String name = lookup.get(fieldCode);
		if(name == null)
		{
			name = Lookup.UNKNOWN + fieldCode;
		}
		return name;
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
