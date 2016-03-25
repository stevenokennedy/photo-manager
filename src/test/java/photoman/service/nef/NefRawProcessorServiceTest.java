package photoman.service.nef;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.junit.Test;

import photoman.service.nef.utils.ExifIFD;
import photoman.service.nef.utils.NefIFD;
import photoman.service.nef.utils.NefIFDEntry;

public class NefRawProcessorServiceTest 
{
	@Test
	public void testParsedNef() throws IOException
	{
		NefRawProcessorService s = new NefRawProcessorService();
		
		NefIFD ifd = s.parseFile(getClass().getClassLoader().getResource("DSC_0471.NEF").getFile().replaceFirst("^/(.:/)", "$1"));
		assertThat(ifd.getNumOfEntries()).isEqualTo(28);
		
		Map<String, NefIFDEntry> entries = ifd.getEntries();
		assertThat(entries).hasSize(28);
		
		for(NefIFDEntry ent : entries.values())
		{
			ent.getFieldCode();
			System.out.println(ent);
		}
		
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setSuppressDeclaration(true);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		XMLWriter writer = new XMLWriter(out, format);
		writer.write(ifd.getXmpData());
		System.out.println(out.toString());
		
		System.out.println("-----Exif Data------");
		ExifIFD exif = ifd.getExifData();
		for(NefIFDEntry ent : exif.getEntries().values())
		{
			System.out.println(ent);
		}
		
		System.out.println("-----Maker Note------");
		NefIFD maker = exif.getMakerNotes();
		for(NefIFDEntry ent : maker.getEntries().values())
		{
			System.out.println(ent);
		}
		
		
		NefIFD subIfd = ifd.getSubIFD(0);
		OutputStream os = new FileOutputStream(new File(System.getProperty("user.home") + "/git/photo-manager/target/test-classes/", "full.jpeg"));
		os.write(subIfd.getThumbnailData());
		os.close();
		
		subIfd = ifd.getSubIFD(2);
		os = new FileOutputStream(new File(System.getProperty("user.home") + "/git/photo-manager/target/test-classes/", "thumb.jpeg"));
		os.write(subIfd.getThumbnailData());
		os.close();
		
	}

}


