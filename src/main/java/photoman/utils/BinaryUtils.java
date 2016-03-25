package photoman.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;

import org.apache.aries.util.IORuntimeException;
import org.apache.commons.lang3.ArrayUtils;

public class BinaryUtils 
{
	private static final String LITTLE_ENDIAN_MARKER = "II";
	private static final String BIG_ENDIAN_MARKER = "MM";
	private static final int TIFF_MAGIC_NUMBER = 42;
	private static final String NIKON_MAGIC_VALUE_STR;
	static 
	{
		String str = "";
		try {
			byte[] bytes = { 78, 105, 107, 111, 110, 0 };
			str = new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {}
		NIKON_MAGIC_VALUE_STR = str;
	}
	
	
	
	//========================================
	//===        PUBLIC METHODS            ===
	//========================================
	
	public static byte[] readByteArray(SeekableByteChannel channel, int length)
	{
		try {
			byte[] bytes = new byte[length];
			ByteBuffer buf = ByteBuffer.wrap(bytes);
			channel.read(buf);
			return bytes;
		} 
		catch (IOException ioe)
		{
			throw new IORuntimeException("Error reading bytes " + length + " from channel: " + channel.toString(), ioe);
		}
	}
	
	public static String readUTF8String(SeekableByteChannel channel, int length)
	{
		byte[] bytes = readBasicBytes(channel, length, ByteOrder.BIG_ENDIAN);
		return new String(bytes, Charset.forName("utf-8"));
		
	}
	
	public static int readShort(SeekableByteChannel channel, ByteOrder bo)
	{
		byte[] bytes = readBasicBytes(channel, 2, bo);
		
		int val = 0;
		int multiple = 0;
		for(int i = bytes.length - 1; i >= 0; i--)
		{
			val += (bytes[i] & 0xFF) << multiple;
			multiple += 8;
		}
		return val & 0xFFFF;
	}
	
	public static int readInt(SeekableByteChannel channel, ByteOrder bo)
	{
		byte[] bytes = readBasicBytes(channel, 4, bo);
		
		int val = 0;
		int multiple = 0;
		for(int i = bytes.length - 1; i >= 0; i--)
		{
			val += (bytes[i] & 0xFF) << multiple;
			multiple += 8;
		}
		return val & 0xFFFFFFFF;
	}
	
	public static float readFloat(SeekableByteChannel channel, ByteOrder bo)
	{
		byte[] bytes = readBasicBytes(channel, 4, bo);
		ByteBuffer buf = ByteBuffer.wrap(bytes);
		return buf.getFloat();
	}
	
	public static double readDouble(SeekableByteChannel channel, ByteOrder bo)
	{
		byte[] bytes = readBasicBytes(channel, 8, bo);
		ByteBuffer buf = ByteBuffer.wrap(bytes);
		return buf.getDouble();
	}
	
	public static ByteOrder readByteOrder(SeekableByteChannel channel)
	{
		String boString = readUTF8String(channel, 2);
		ByteOrder byteOrder = boString.equals(LITTLE_ENDIAN_MARKER) 
			? ByteOrder.LITTLE_ENDIAN
			: boString.equals(BIG_ENDIAN_MARKER)
				? ByteOrder.LITTLE_ENDIAN
				: null;
		if(byteOrder == null)
		{
			throw new IllegalArgumentException("Input file not recognised as valid NEF file");
		}
		
		return byteOrder;
	}
	
	public static void readTiffMagicNumber(SeekableByteChannel channel, ByteOrder bo)
	{
		int magicNumber = BinaryUtils.readShort(channel, bo);
		if(magicNumber != TIFF_MAGIC_NUMBER)
		{
			throw new IllegalArgumentException("Input file not recognised as valid NEF file");
		}
	}
	
	public static void readNikonMagicValue(SeekableByteChannel channel)
	{
		String magicValue = BinaryUtils.readUTF8String(channel, 6);
		if(!magicValue.equals(NIKON_MAGIC_VALUE_STR))
		{
			throw new IllegalArgumentException("Input file not recognised as valid NEF file");
		}
	}
	
	//========================================
	//===        PRIVATE METHODS           ===
	//========================================
	
	private static byte[] readBasicBytes(SeekableByteChannel channel, int length, ByteOrder bo)
	{
		try
		{
			byte[] bytes = new byte[length];
			ByteBuffer buf = ByteBuffer.wrap(bytes);
			channel.read(buf);
			if(bo.equals(ByteOrder.LITTLE_ENDIAN))
			{
				ArrayUtils.reverse(bytes);
			}
			return bytes;
		}
		catch(IOException ioe)
		{
			throw new IORuntimeException("Error reading bytes " + length + " from channel: " + channel.toString(), ioe);
		}
	}
}
