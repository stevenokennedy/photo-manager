package photoman.domain;

import java.util.Date;

import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

public class ExifDataTest 
{
	@Test
	public void testProperties()
	{
		Date d = new Date();
		ExifData e = new ExifData(10L, 20L, d, "N", "D");
		assertThat(e.getImageWidth()).isEqualTo(10L);
		assertThat(e.getImageHeight()).isEqualTo(20L);
		assertThat(e.getDateTaken()).isEqualTo(d);
		assertThat(e.getMake()).isEqualTo("N");
		assertThat(e.getModel()).isEqualTo("D");
		
		
		e.setCopyright("Copyright String");
		e.setOrientation(ExifData.ORIENTATION_LANDSCAPE);
		e.setResolutionUnit(ExifData.RES_UNIT_INCH);
		e.setxResolution(1024L);
		e.setyResolution(768L);
		
		assertThat(e.getCopyright()).isEqualTo("Copyright String");
		assertThat(e.getOrientation()).isEqualTo("Landscape");
		assertThat(e.getResolutionUnit()).isEqualTo("inch");
		assertThat(e.getxResolution()).isEqualTo(1024L);
		assertThat(e.getyResolution()).isEqualTo(768L);
		
		Date d2 = new Date();
		e.setImageWidth(100L);
		e.setImageHeight(200L);
		e.setDateTaken(d2);
		e.setMake("C");
		e.setModel("50D");
		
		assertThat(e.getImageWidth()).isEqualTo(100L);
		assertThat(e.getImageHeight()).isEqualTo(200L);
		assertThat(e.getDateTaken()).isEqualTo(d2);
		assertThat(e.getMake()).isEqualTo("C");
		assertThat(e.getModel()).isEqualTo("50D");
		
	}
	

}
