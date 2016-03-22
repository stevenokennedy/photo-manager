package photoman.service.nef.utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import photoman.utils.BinaryUtils;
import scala.sys.process.processInternal;

public class NefIFD 
{
	private static final int IFD_ENTRY_COUNT_SIZE = 2;
	private static final int IFD_ENTRY_SIZE = 12;
	
	private RandomAccessFile file;
	private ByteOrder bo;
	
	private int numOfEntries;
	
	private Map<String, NefIFDEntry> entries;
	
	private List<NefIFD> subIFDs = new ArrayList<>();
	
	private NefIFD exif;
	
	private NefIFD makerNote;
	
	private byte[] thumbnailData;
	
	public NefIFD(RandomAccessFile file, int startOffset, ByteOrder bo) throws IOException
	{
		this.file = file;
		this.bo = bo;
		
		file.seek(startOffset);
		this.numOfEntries = BinaryUtils.readShort(file, bo);
		
		int endOffset = startOffset + IFD_ENTRY_COUNT_SIZE + (IFD_ENTRY_SIZE * numOfEntries);
		
		entries = new HashMap<>(numOfEntries);
		for(int i = 0; i < numOfEntries; i++)
		{
			NefIFDEntry entry = new NefIFDEntry(file, bo, startOffset + IFD_ENTRY_COUNT_SIZE + (IFD_ENTRY_SIZE * i));
			entries.put(entry.getFieldName(), entry);
		}
		processSubIFDs();

		//Move the pointer to after this IFD
		file.seek(endOffset);
	}
	
	private void processSubIFDs() throws IOException
	{
		NefIFDEntry subEntries = entries.get(IFDLookup.IFD_SUB_IFD);
		if(subEntries != null)
		{
			for(Object val : subEntries.getValues())
			{
				int offset = (Integer) val;
				this.subIFDs.add(new NefIFD(file, offset, bo));
			}
		}
	}
	
	private void processThumbnailInfo() throws IOException
	{
		NefIFDEntry thumbnailOffset = entries.get(IFDLookup.IFD_THUMBNAIL_OFFSET);
		NefIFDEntry thumbnailLength = entries.get(IFDLookup.IFD_THUMBNAIL_LENGTH);
		if(thumbnailOffset != null && thumbnailLength != null)
		{
			long originalPos = file.getFilePointer();
			file.seek((int)thumbnailOffset.getValues().get(0));
			thumbnailData = new byte[(int)thumbnailLength.getValues().get(0)];
			file.readFully(thumbnailData);
			file.seek(originalPos);
		}
	}

	public int getNumOfEntries() {
		return numOfEntries;
	}
	
	public Map<String, NefIFDEntry> getEntries()
	{
		return this.entries;
	}
	
	public NefIFD getSubIFD(int index)
	{
		return subIFDs.get(index);
	}
	
	public byte[] getThumbnailData() throws IOException
	{
		if(thumbnailData == null)
		{
			processThumbnailInfo();
		}
		return thumbnailData;
	}
	
}
