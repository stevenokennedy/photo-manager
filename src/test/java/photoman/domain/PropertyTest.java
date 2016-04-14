package photoman.domain;

import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

public class PropertyTest
{
	@Test
	public void basicPropertyTests()
	{
		Property p = new Property("test", "testValue");
		
		assertThat(p.getPropName())
			.isEqualTo("test");
		
		assertThat(p.getPropValue())
			.isEqualTo("testValue");
		
		assertThat(p.getPropValueAsString())
			.isEqualTo("testValue");
		
		p.setPropValue("val");
		assertThat(p.getPropValue())
			.isEqualTo("val");
		
		p.setPropName("newName");
		
		assertThat(p.getPropName())
			.isEqualTo("newName");
	}
}
