package photoman.service.nef;

import java.awt.Image;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import photoman.service.RawProcessorService;
import photoman.service.nef.lookup.IFDLookup;
import photoman.service.nef.utils.NefHeader;
import photoman.service.nef.utils.NefIFD;
import photoman.utils.BinaryUtils;

@Service
public class NefRawProcessorService implements RawProcessorService 
{
	public Image extactJpeg(String fileName)
	{
		Image img = null;
		return img;
		
	}
	
	public NefIFD parseFile(String fileName) throws IOException
	{
		Set<OpenOption> options = new HashSet<>();
		options.add(StandardOpenOption.READ);
		Path img = FileSystems.getDefault().getPath(fileName);
		//File img = new File(fileName);
		if(!(Files.exists(img) && Files.isRegularFile(img) && Files.isReadable(img)))
		{
			throw new IllegalArgumentException("Please ensure the image exists and is readable");
		}

		SeekableByteChannel inp = Files.newByteChannel(img, options);
		NefHeader header = readTiffHeader(inp);
		return readTiffIFD(inp, header.getIfd1Offset(), header);
	}

	private NefHeader readTiffHeader(SeekableByteChannel inp) throws IOException
	{
		NefHeader header  = new NefHeader(BinaryUtils.readByteOrder(inp));
		BinaryUtils.readTiffMagicNumber(inp,header.getByteOrder());
		header.setIfd1Offset(BinaryUtils.readInt(inp, header.getByteOrder()));
		return header;		
	}
	
	private NefIFD readTiffIFD(SeekableByteChannel inp, int offset, NefHeader header) throws IOException
	{
		NefIFD ifd = new NefIFD(inp, offset, header.getByteOrder(), new IFDLookup());
		return ifd;
	}
}
