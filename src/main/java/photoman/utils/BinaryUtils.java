package photoman.utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

import org.apache.aries.util.IORuntimeException;
import org.apache.commons.lang3.ArrayUtils;

public class BinaryUtils 
{
	
	//========================================
	//===        PUBLIC METHODS            ===
	//========================================
	
	public static byte[] readByteArray(RandomAccessFile file, int length)
	{
		try {
			byte[] bytes = new byte[length];
			file.read(bytes);
			return bytes;
		} 
		catch (IOException ioe)
		{
			throw new IORuntimeException("Error reading bytes " + length + " from file: " + file.toString(), ioe);
		}
	}
	
	public static String readUTF8String(RandomAccessFile file, int length)
	{
		byte[] bytes = readBasicBytes(file, length, ByteOrder.BIG_ENDIAN);
		return new String(bytes, Charset.forName("utf-8"));
		
	}
	
	public static int readShort(RandomAccessFile file, ByteOrder bo)
	{
		byte[] bytes = readBasicBytes(file, 2, bo);
		
		int val = 0;
		int multiple = 0;
		for(int i = bytes.length - 1; i >= 0; i--)
		{
			val += (bytes[i] & 0xFF) << multiple;
			multiple += 8;
		}
		return val & 0xFFFF;
	}
	
	public static int readInt(RandomAccessFile file, ByteOrder bo)
	{
		byte[] bytes = readBasicBytes(file, 4, bo);
		
		int val = 0;
		int multiple = 0;
		for(int i = bytes.length - 1; i >= 0; i--)
		{
			val += (bytes[i] & 0xFF) << multiple;
			multiple += 8;
		}
		return val & 0xFFFFFFFF;
	}
	
	public static float readFloat(RandomAccessFile file, ByteOrder bo)
	{
		byte[] bytes = readBasicBytes(file, 4, bo);
		ByteBuffer buf = ByteBuffer.wrap(bytes);
		return buf.getFloat();
	}
	
	public static double readDouble(RandomAccessFile file, ByteOrder bo)
	{
		byte[] bytes = readBasicBytes(file, 8, bo);
		ByteBuffer buf = ByteBuffer.wrap(bytes);
		return buf.getDouble();
	}
	
	//========================================
	//===        PRIVATE METHODS           ===
	//========================================
	
	private static byte[] readBasicBytes(RandomAccessFile file, int length, ByteOrder bo)
	{
		try
		{
			byte[] bytes = new byte[length];
			file.readFully(bytes);
			if(bo.equals(ByteOrder.LITTLE_ENDIAN))
			{
				ArrayUtils.reverse(bytes);
			}
			return bytes;
		}
		catch(IOException ioe)
		{
			throw new IORuntimeException("Error reading bytes " + length + " from file: " + file.toString(), ioe);
		}
	}
}
