package photoman.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

public class ByteArrayChannel implements SeekableByteChannel
{

	private ByteArrayInputStream input;
	
	public ByteArrayChannel(ByteArrayInputStream input)
	{
		this.input = input;
	}
	
	@Override
	public void close() throws IOException 
	{
		input.close();
	}

	@Override
	public boolean isOpen() 
	{
		return input.available() > 0;
	}

	@Override
	public long position() throws IOException 
	{
		int pos = 0;
		try 
		{
			Field f = input.getClass().getDeclaredField("pos");
			f.setAccessible(true);
			pos = f.getInt(input);
		} 
		catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) 
		{
			//We're pretty sure this won't happen, since we know the field exists
			e.printStackTrace();
		}
		return pos;
	}

	@Override
	public SeekableByteChannel position(long newPosition) throws IOException 
	{
		input.reset();
		input.skip(newPosition);
		return this;
	}

	@Override
	public int read(ByteBuffer dst) throws IOException 
	{
		return input.read(dst.array());
	}

	@Override
	public long size() throws IOException 
	{
		int count = 0;
		try 
		{
			Field f = input.getClass().getDeclaredField("count");
			f.setAccessible(true);
			count = f.getInt(input);
		} 
		catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) 
		{
			//We're pretty sure this won't happen, since we know the field exists
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public SeekableByteChannel truncate(long size) throws IOException {
		// Not Implemented
		return null;
	}

	@Override
	public int write(ByteBuffer src) throws IOException {
		// Not implemented
		return 0;
	}

}
