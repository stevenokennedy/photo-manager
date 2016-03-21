package photoman.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class BinaryUtilsTest 
{
	@Test
	public void testUTF8String() throws IOException
	{
	
		File f = File.createTempFile("bin-util-test", "bin");
		RandomAccessFile raf = new RandomAccessFile(f, "rws");
		
		byte[] b = { 0x4D, 0x4D };
		raf.setLength(0);raf.write(b, 0, b.length);raf.seek(0);
		String s = BinaryUtils.readUTF8String(raf, 2);
		assertThat(s).isEqualTo("MM");
		
		byte[] b2 = { 0x49, 0x49 };
		raf.setLength(0);raf.write(b2, 0, b2.length);raf.seek(0);
		s = BinaryUtils.readUTF8String(raf, 2);
		assertThat(s).isEqualTo("II");
	}
	
	@Test
	public void testShort() throws IOException
	{
		File f = File.createTempFile("bin-util-test", "bin");
		RandomAccessFile raf = new RandomAccessFile(f, "rws");
		byte[] b = { 0x1, 0x2C };
		raf.setLength(0);raf.write(b, 0, b.length);raf.seek(0);
		int s = BinaryUtils.readShort(raf, ByteOrder.BIG_ENDIAN);
		assertThat(s).isEqualTo((short)300);
		
		byte[] b2 = { 0x2C, 0x1 };
		raf.setLength(0);raf.write(b2, 0, b2.length);raf.seek(0);
		s = BinaryUtils.readShort(raf, ByteOrder.LITTLE_ENDIAN);
		assertThat(s).isEqualTo((short)300);
		 
		byte[] b3 = { 0x0, 0x1 };
		raf.setLength(0);raf.write(b3, 0, b3.length);raf.seek(0);
		s = BinaryUtils.readShort(raf, ByteOrder.LITTLE_ENDIAN);
		assertThat(s).isEqualTo((short)256);
		
		byte[] b4 = { 0x0, 0x1 };
		raf.setLength(0);raf.write(b4, 0, b4.length);raf.seek(0);
		s = BinaryUtils.readShort(raf, ByteOrder.BIG_ENDIAN);
		assertThat(s).isEqualTo(1);
		
		byte[] b5 = { (byte)(0xF8 & 0xFF), 0x49 };
		raf.setLength(0);raf.write(b5, 0, b5.length);raf.seek(0);
		s = BinaryUtils.readShort(raf, ByteOrder.BIG_ENDIAN);
		assertThat(s).isEqualTo(63561);
	}
}
