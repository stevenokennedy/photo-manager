package photoman.service.nef.utils;

import java.nio.ByteOrder;

public class NefHeader 
{	
	private ByteOrder byteOrder;
	private int ifd1Offset;
	
	public NefHeader(ByteOrder bo)
	{
		byteOrder = bo;
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
