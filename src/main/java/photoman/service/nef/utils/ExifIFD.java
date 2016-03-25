package photoman.service.nef.utils;

import java.io.ByteArrayInputStream;
import java.nio.ByteOrder;
import java.nio.channels.SeekableByteChannel;

import photoman.service.nef.lookup.IFDLookup;
import photoman.service.nef.lookup.Lookup;
import photoman.service.nef.lookup.MakerLookup;
import photoman.utils.BinaryUtils;
import photoman.utils.ByteArrayChannel;

public class ExifIFD extends NefIFD 
{
	protected NefIFD makerNotes;
	protected int makerVersion;
	
	public ExifIFD(SeekableByteChannel input, int startOffset, ByteOrder bo, Lookup lookup) 
	{
		super(input, startOffset, bo, lookup);
		processMakerNotes();
	}

	private void processMakerNotes() 
	{
		NefIFDEntry notes = entries.get(IFDLookup.IFD_MAKER_NOTES);
		if(notes != null)
		{
			byte[] rawMakerNotes = (byte[])(notes.getValues().get(0));
			SeekableByteChannel notesChannel = new ByteArrayChannel(new ByteArrayInputStream(rawMakerNotes));
			//parse the maker header
			readMakerHeader(notesChannel);
			//Create an IFD
		}
		
	}
	
	private void readMakerHeader(SeekableByteChannel notesChannel)
	{
		BinaryUtils.readNikonMagicValue(notesChannel);
		this.makerVersion = BinaryUtils.readShort(notesChannel, this.getBo());
		int spacer = BinaryUtils.readShort(notesChannel, this.getBo());
		ByteOrder makerBo = BinaryUtils.readByteOrder(notesChannel);
		BinaryUtils.readTiffMagicNumber(notesChannel, makerBo);

		//We have 10 bytes ahead of the 8 stanard IFD directory header from the Nikon Magic Value, 
		//maker version & spacer etc
		int makerOffset = BinaryUtils.readInt(notesChannel, makerBo) + 10;
		this.makerNotes = new NefIFD(notesChannel, makerOffset, makerBo, new MakerLookup());
				
		
		
		
		
	}
	
	public NefIFD getMakerNotes()
	{
		return makerNotes;
	}

}
