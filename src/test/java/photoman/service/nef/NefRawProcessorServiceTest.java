package photoman.service.nef;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

import photoman.service.nef.utils.NefIFD;
import photoman.service.nef.utils.NefIFDEntry;

public class NefRawProcessorServiceTest 
{
	@Test
	public void testParsedNef() throws IOException
	{
		NefRawProcessorService s = new NefRawProcessorService();
		
		NefIFD ifd = s.parseFile(getClass().getClassLoader().getResource("DSC_0471.NEF").getFile());
		assertThat(ifd.getNumOfEntries()).isEqualTo(28);
		
		Map<String, NefIFDEntry> entries = ifd.getEntries();
		assertThat(entries).hasSize(28);
		
		for(NefIFDEntry ent : entries.values())
		{
			ent.getFieldCode();
			System.out.println(ent);
		}
		
		NefIFD subIfd = ifd.getSubIFD(0);
		OutputStream os = new FileOutputStream(new File("C:/Users/Steven/git/photo-manager/target/test-classes/", "full.jpeg"));
		os.write(subIfd.getThumbnailData());
		os.close();
		
		subIfd = ifd.getSubIFD(2);
		os = new FileOutputStream(new File("C:/Users/Steven/git/photo-manager/target/test-classes/", "thumb.jpeg"));
		os.write(subIfd.getThumbnailData());
		os.close();
	}

}


