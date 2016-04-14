package photoman.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;

import static org.assertj.core.api.Assertions.*;

import photoman.PhotoMan;
import photoman.service.PreferenceService;

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PhotoMan.class)
@DirtiesContext
public class PreferenceServiceTest 
{
	protected static final String DATASET = "classpath:datasets/it-preference-service.xml";

	@Autowired
	private PreferenceService prefService;
	
	@Test
	public void testPreferences()
	{
		prefService.setPreference("test", "value");
		
		assertThat(prefService.getPreference("test"))
			.isEqualTo("value");
	}
}
