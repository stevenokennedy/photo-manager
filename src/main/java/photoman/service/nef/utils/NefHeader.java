package photoman.service.nef.utils;

import java.nio.ByteOrder;

public class NefHeader 
{
	private static final String LITTLE_ENDIAN_MARKER = "II";
	private static final String BIG_ENDIAN_MARKER = "MM";
	private static final int MAGIC_NUMBER = 42;
	
	private ByteOrder byteOrder;
	private int ifd1Offset;
	
	public NefHeader(String bo)
	{
		byteOrder = bo.equals(LITTLE_ENDIAN_MARKER) 
				? ByteOrder.LITTLE_ENDIAN
				: bo.equals(BIG_ENDIAN_MARKER)
					? ByteOrder.LITTLE_ENDIAN
					: null;
		if(byteOrder == null)
		{
			throw new IllegalArgumentException("Input file not recognised as valid NEF file");
		}
		
	}
	
	public void checkMagicNumber(int magicNumber)
	{
		if(magicNumber != MAGIC_NUMBER)
		{
			throw new IllegalArgumentException("Input file not recognised as valid NEF file");
		}
	}
		
	public ByteOrder getByteOrder()
	{
		return byteOrder;
	}
	
	public int getIfd1Offset()
	{
		return ifd1Offset;
	}
	
	public void setIfd1Offset(int offset)
	{
		this.ifd1Offset = offset;
	}
	
}
