package photoman.service.nef.utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;

public class ExifIFD extends NefIFD 
{
	public ExifIFD(RandomAccessFile file, int startOffset, ByteOrder bo) throws IOException 
	{
		super(file, startOffset, bo);
	}

}
