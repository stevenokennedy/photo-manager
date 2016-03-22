package photoman.service.nef.utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		deriveValue(fieldType, valueCount); 
		
		//Return the file pointer to where it was
		file.seek(currentFilePos);
	}
	
	private void deriveValue(int fieldType, int valueCount) throws IOException
	{
		FieldType type = FIELD_TYPES.get(fieldType);
					
		//Should the value be treated as a value or a lookup
		boolean lookupRequired = (type.numBytes * valueCount) > 4;
		
		switch(type)
		{
			case BYTE:
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
				break;
			case ASCII:
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
				break;
			case SHORT:
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
				break;
			case LONG:
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
				break;
			case RATIONAL:
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
				break;
		}
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
		BYTE(1), ASCII(1), SHORT(2), LONG(4), RATIONAL(8);
		
		public final int numBytes;
		
		FieldType(int numBytes)
		{
			this.numBytes = numBytes;
		}
		
	}
}
