package photoman.service.nef.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.aries.util.IORuntimeException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import photoman.service.nef.lookup.IFDLookup;
import photoman.service.nef.lookup.Lookup;
import photoman.utils.BinaryUtils;

public class NefIFD 
{
	private static final int IFD_ENTRY_COUNT_SIZE = 2;
	private static final int IFD_ENTRY_SIZE = 12;
	
	private SeekableByteChannel input;
	private ByteOrder bo;
	protected Lookup lookup;
	
	protected int numOfEntries;
	
	protected Map<String, NefIFDEntry> entries;
	
	protected List<NefIFD> subIFDs = new ArrayList<>();
	
	protected ExifIFD exifData;
	
	protected byte[] thumbnailData;
	
	protected Document xmpData;
	
	public NefIFD(SeekableByteChannel input, int startOffset, ByteOrder bo, Lookup lookup)
	{
		try
		{
			this.input = input;
			this.bo = bo;
			this.lookup = lookup;
			
			input.position(startOffset);
			this.numOfEntries = BinaryUtils.readShort(input, bo);
			
			int endOffset = startOffset + IFD_ENTRY_COUNT_SIZE + (IFD_ENTRY_SIZE * numOfEntries);
			
			entries = new HashMap<>(numOfEntries);
			for(int i = 0; i < numOfEntries; i++)
			{
				NefIFDEntry entry = new NefIFDEntry(input, bo, startOffset + IFD_ENTRY_COUNT_SIZE + (IFD_ENTRY_SIZE * i), lookup);
				entries.put(entry.getFieldName(), entry);
			}
			processSubIFDs();
			processExifData();
			processXMPData();
	
			//Move the pointer to after this IFD
			input.position(endOffset);
		}
		catch(IOException ioe)
		{
			//Rethrow IOExceptions as RuntimeIOExceptions
			throw new IORuntimeException(ioe);
		}
	}
	
	private void processSubIFDs() throws IOException
	{
		NefIFDEntry subEntries = entries.get(IFDLookup.IFD_SUB_IFD);
		if(subEntries != null)
		{
			for(Object val : subEntries.getValues())
			{
				int offset = (Integer) val;
				this.subIFDs.add(new NefIFD(input, offset, bo, lookup));
			}
		}
	}
	
	private void processExifData() throws IOException
	{
		NefIFDEntry exif = entries.get(IFDLookup.IFD_EXIF_OFFSET);
		if(exif != null)
		{
			this.exifData = new ExifIFD(input, (int)exif.getValues().get(0), bo, lookup);
		}
	}
	
	private void processXMPData()
	{
		NefIFDEntry xmp = entries.get(IFDLookup.IFD_APPLICATION_NOTES);
		if(xmp != null)
		{
			String xmpString = new String((byte[])xmp.getValues().get(0));
			try 
			{
				xmpData = DocumentHelper.parseText(xmpString);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void processThumbnailInfo() throws IOException
	{
		NefIFDEntry thumbnailOffset = entries.get(IFDLookup.IFD_THUMBNAIL_OFFSET);
		NefIFDEntry thumbnailLength = entries.get(IFDLookup.IFD_THUMBNAIL_LENGTH);
		if(thumbnailOffset != null && thumbnailLength != null)
		{
			long originalPos = input.position();
			input.position((int)thumbnailOffset.getValues().get(0));
			thumbnailData = new byte[(int)thumbnailLength.getValues().get(0)];
			input.read(ByteBuffer.wrap(thumbnailData));
			input.position(originalPos);
		}
	}

	public int getNumOfEntries() {
		return numOfEntries;
	}
	
	public ByteOrder getBo() {
		return bo;
	}
	
	public Map<String, NefIFDEntry> getEntries()
	{
		return this.entries;
	}
	
	public NefIFD getSubIFD(int index)
	{
		return subIFDs.get(index);
	}
	
	public ExifIFD getExifData()
	{
		return exifData;
	}
	
	public Document getXmpData()
	{
		return xmpData;
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
