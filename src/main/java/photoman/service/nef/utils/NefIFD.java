package photoman.service.nef.utils;

import java.io.IOError;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import photoman.utils.BinaryUtils;

public class NefIFD 
{
	private static final int IFD_ENTRY_COUNT_SIZE = 2;
	private static final int IFD_ENTRY_SIZE = 12;
	
	private int numOfEntries;
	
	private final RandomAccessFile file;
	
	private final ByteOrder bo;
	
	private List<NefIFDEntry> entries;
	
	public NefIFD(RandomAccessFile file, int startOffset, ByteOrder bo) throws IOException
	{
		this.file = file;
		this.bo = bo;
		this.numOfEntries = BinaryUtils.readShort(file, bo);
		
		int endOffset = startOffset + IFD_ENTRY_COUNT_SIZE + (IFD_ENTRY_SIZE * numOfEntries);
		
		entries = new ArrayList<>(numOfEntries);
		for(int i = 0; i < numOfEntries; i++)
		{
			entries.add(new NefIFDEntry(file, bo, startOffset + IFD_ENTRY_COUNT_SIZE + (IFD_ENTRY_SIZE * i)));
		}

		//Move the pointer to after this IFD
		file.seek(endOffset);
	}
	
	

	public int getNumOfEntries() {
		return numOfEntries;
	}
	
	public List<NefIFDEntry> getEntries()
	{
		return this.entries;
	}
	
}
