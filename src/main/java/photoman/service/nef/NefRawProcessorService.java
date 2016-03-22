package photoman.service.nef;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import photoman.service.RawProcessorService;
import photoman.service.nef.utils.NefHeader;
import photoman.service.nef.utils.NefIFD;
import photoman.utils.BinaryUtils;

public class NefRawProcessorService implements RawProcessorService 
{
	public Image extactJpeg(String fileName)
	{
		Image img = null;
		return img;
		
	}
	
	public NefIFD parseFile(String fileName) throws IOException
	{
		File img = new File(fileName);
		if(!(img.exists() && img.isFile() && img.canRead()))
		{
			throw new IllegalArgumentException("Please ensure the image exists and is readable");
		}

		RandomAccessFile inp = new RandomAccessFile(img, "r");
		NefHeader header = readTiffHeader(inp);
		return readTiffIFD(inp, header.getIfd1Offset(), header);
	}

	private NefHeader readTiffHeader(RandomAccessFile inp) throws IOException
	{
		NefHeader header  = new NefHeader(BinaryUtils.readUTF8String(inp, 2));
		header.checkMagicNumber(BinaryUtils.readShort(inp, header.getByteOrder()));
		header.setIfd1Offset(BinaryUtils.readInt(inp, header.getByteOrder()));
		return header;		
	}
	
	private NefIFD readTiffIFD(RandomAccessFile inp, int offset, NefHeader header) throws IOException
	{
		inp.seek(offset);
		NefIFD ifd = new NefIFD(inp, offset, header.getByteOrder());
		return ifd;
	}
}
