package photoman.service.nef;

import java.io.IOException;
import java.util.List;

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
		
		NefIFD ifd = s.parseFile(getClass().getClassLoader().getResource("nikon_d5500_01.nef").getFile());
		assertThat(ifd.getNumOfEntries()).isEqualTo(28);
		
		List<NefIFDEntry> entries = ifd.getEntries();
		assertThat(entries).hasSize(28);
		
		for(NefIFDEntry ent : entries)
		{
			ent.getFieldCode();
			System.out.println(ent);
		}
	}

}
