package photoman.service.nef.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;

public class ExifIFD extends NefIFD 
{
	protected NefIFD makerNotes;
	
	public ExifIFD(RandomAccessFile file, int startOffset, ByteOrder bo) throws IOException 
	{
		super(file, startOffset, bo);
		processMakerNotes();
	}

	private void processMakerNotes() 
	{
		NefIFDEntry notes = entries.get(IFDLookup.IFD_SUB_IFD);
		if(notes != null)
		{
			//parse the header
			
			//Create an IFD
		}
		
	}
	
	public NefIFD getMakerNotes()
	{
		return makerNotes;
	}

}
