package photoman.utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

import org.apache.commons.lang3.ArrayUtils;

public class BinaryUtils 
{
	public static String readUTF8String(RandomAccessFile file, int length, long offset) throws IOException
	{
		file.seek(offset);
		return readUTF8String(file, length);
	}
	
	public static String readUTF8String(RandomAccessFile file, int length) throws IOException
	{
		byte[] bytes = readBasicBytes(file, length, ByteOrder.BIG_ENDIAN);
		return new String(bytes, Charset.forName("utf-8"));
		
	}
	
	public static int readShort(RandomAccessFile file, ByteOrder bo, long offset) throws IOException
	{
		file.seek(offset);
		return readShort(file, bo);
	}
	
	public static int readShort(RandomAccessFile file, ByteOrder bo) throws IOException
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
	
	public static int readInt(RandomAccessFile file, ByteOrder bo, long offset) throws IOException
	{
		file.seek(offset);
		return readInt(file, bo);
	}
	
	public static int readInt(RandomAccessFile file, ByteOrder bo) throws IOException
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
	
	private static byte[] readBasicBytes(RandomAccessFile file, int length, ByteOrder bo) throws IOException
	{
		byte[] bytes = new byte[length];
		file.readFully(bytes);
		if(bo.equals(ByteOrder.LITTLE_ENDIAN))
		{
			ArrayUtils.reverse(bytes);
		}
		return bytes;
	}
}
