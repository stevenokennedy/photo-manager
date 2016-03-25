package photoman.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class BinaryUtilsTest 
{
	@Test
	public void testUTF8StringFromFile() throws IOException
	{
		Set<OpenOption> options = new HashSet<>();
		options.add(StandardOpenOption.CREATE);
		options.add(StandardOpenOption.READ);
		options.add(StandardOpenOption.WRITE);
		SeekableByteChannel raf = Files.newByteChannel(Files.createTempFile("bin-util-test", "bin"), options);
		
		byte[] b = { 0x4D, 0x4D };
		raf.truncate(0);raf.write(ByteBuffer.wrap(b));raf.position(0);
		String s = BinaryUtils.readUTF8String(raf, 2);
		assertThat(s).isEqualTo("MM");
		
		byte[] b2 = { 0x49, 0x49 };
		raf.truncate(0);raf.write(ByteBuffer.wrap(b2));raf.position(0);
		s = BinaryUtils.readUTF8String(raf, 2);
		assertThat(s).isEqualTo("II");
	}
	
	@Test
	public void testUTF8StringFromByteArray()
	{
		byte[] b = { 0x54, 0x65, 0x73, 0x74, 0x20, 0x53, 0x74, 0x72, 0x69, 0x6e, 0x67};
		SeekableByteChannel ba = new ByteArrayChannel(new ByteArrayInputStream(b));
		String s = BinaryUtils.readUTF8String(ba, 11);
		assertThat(s).isEqualTo("Test String");
	}
	
	@Test
	public void testShortFromFile() throws IOException
	{
		Set<OpenOption> options = new HashSet<>();
		options.add(StandardOpenOption.CREATE);
		options.add(StandardOpenOption.READ);
		options.add(StandardOpenOption.WRITE);
		SeekableByteChannel raf = Files.newByteChannel(Files.createTempFile("bin-util-test", "bin"), options);
		
		byte[] b = { 0x1, 0x2C };
		raf.truncate(0);raf.write(ByteBuffer.wrap(b));raf.position(0);
		int s = BinaryUtils.readShort(raf, ByteOrder.BIG_ENDIAN);
		assertThat(s).isEqualTo((short)300);
		
		byte[] b2 = { 0x2C, 0x1 };		
		raf.truncate(0);raf.write(ByteBuffer.wrap(b2));raf.position(0);
		s = BinaryUtils.readShort(raf, ByteOrder.LITTLE_ENDIAN);
		assertThat(s).isEqualTo((short)300);
		 
		byte[] b3 = { 0x0, 0x1 };
		raf.truncate(0);raf.write(ByteBuffer.wrap(b3));raf.position(0);
		s = BinaryUtils.readShort(raf, ByteOrder.LITTLE_ENDIAN);
		assertThat(s).isEqualTo((short)256);
		
		byte[] b4 = { 0x0, 0x1 };
		raf.truncate(0);raf.write(ByteBuffer.wrap(b4));raf.position(0);
		s = BinaryUtils.readShort(raf, ByteOrder.BIG_ENDIAN);
		assertThat(s).isEqualTo(1);
		
		byte[] b5 = { (byte)(0xF8 & 0xFF), 0x49 };
		raf.truncate(0);raf.write(ByteBuffer.wrap(b5));raf.position(0);
		s = BinaryUtils.readShort(raf, ByteOrder.BIG_ENDIAN);
		assertThat(s).isEqualTo(63561);
	}
	
	@Test
	public void testShortFromByteArray() throws IOException
	{
		byte[] b = { 0x0, (byte) 0xEA };
		SeekableByteChannel ba = new ByteArrayChannel(new ByteArrayInputStream(b));
		int s = BinaryUtils.readShort(ba, ByteOrder.BIG_ENDIAN);
		assertThat(s).isEqualTo((short)234);
		
		byte[] b2 = { 0x29, 0x04 };
		ba = new ByteArrayChannel(new ByteArrayInputStream(b2));
		int s2 = BinaryUtils.readShort(ba, ByteOrder.LITTLE_ENDIAN);
		assertThat(s2).isEqualTo((short)1065);
		
	}
	
	@Test
	public void testReadTiffMagicNumber()
	{
		byte[] b = { 0x0, 0x2A };
		SeekableByteChannel ba = new ByteArrayChannel(new ByteArrayInputStream(b));
		BinaryUtils.readTiffMagicNumber(ba, ByteOrder.BIG_ENDIAN);
		
		byte[] b2 = { 0x2A, 0x00 };
		ba = new ByteArrayChannel(new ByteArrayInputStream(b2));
		BinaryUtils.readTiffMagicNumber(ba, ByteOrder.LITTLE_ENDIAN);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testReadBadTiffMagicNumber()
	{
		byte[] b = { 0x0, 0x3A };
		SeekableByteChannel ba = new ByteArrayChannel(new ByteArrayInputStream(b));
		BinaryUtils.readTiffMagicNumber(ba, ByteOrder.BIG_ENDIAN);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testReadBadTiffMagicNumber2()
	{
		byte[] b = { 0x1, 0x2A };
		SeekableByteChannel ba = new ByteArrayChannel(new ByteArrayInputStream(b));
		BinaryUtils.readTiffMagicNumber(ba, ByteOrder.LITTLE_ENDIAN);
	}
	
	@Test
	public void testReadNikonMagicValue()
	{
		byte[] b = { 78, 105, 107, 111, 110, 0 };
		SeekableByteChannel ba = new ByteArrayChannel(new ByteArrayInputStream(b));
		BinaryUtils.readNikonMagicValue(ba);
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testReadBadNikonMagicValue()
	{
		byte[] b = { 110, 105, 107, 111, 110, 23 };
		SeekableByteChannel ba = new ByteArrayChannel(new ByteArrayInputStream(b));
		BinaryUtils.readNikonMagicValue(ba);
		
	}
}
