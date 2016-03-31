package photoman.domain;

import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.Transactional;

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
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import photoman.PhotoMan;
import photoman.repository.PreferenceRepository;
import photoman.repository.ProfileRepository;

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PhotoMan.class)
@DatabaseSetup(PreferenceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { PreferenceTest.DATASET })
@DirtiesContext
public class PreferenceTest 
{
	protected static final String DATASET = "classpath:datasets/it-preferences.xml";
	
	private static final String FIELD_ID = "id";
	private static final String FIELD_PREF_NAME = "preferenceName";
	private static final String FIELD_PREF_VALUE_TYPE = "valueType";
	private static final String FIELD_PREF_VALUE = "value";
	private static final String FIELD_PREF_PROFILE = "profile";
	
	@Autowired
	private PreferenceRepository prefRepo;
	
	@Autowired
	private ProfileRepository profileRepo;
	
	@Test
	@Transactional
	public void testCreatePreferences()
	{		
		Preference path = new Preference(profileRepo.findOne(1L), "Output Location", "C:/users/paths/path");
		prefRepo.save(path);
		
		assertThat(path)
			.extracting(FIELD_ID, FIELD_PREF_NAME, FIELD_PREF_VALUE_TYPE, FIELD_PREF_VALUE, FIELD_PREF_PROFILE)
			.containsExactly(2L, "Output Location", "java.lang.String", "C:/users/paths/path", profileRepo.getOne(1L));
	}
	
	@Test
	@Transactional
	public void testRemovePreferences()
	{
		Preference path = new Preference(profileRepo.findOne(1L), "Output Location", "C:/users/paths/path");
		prefRepo.save(path);
		
		assertThat(path)
			.extracting(FIELD_ID, FIELD_PREF_NAME, FIELD_PREF_VALUE_TYPE, FIELD_PREF_VALUE, FIELD_PREF_PROFILE)
			.containsExactly(3L, "Output Location", "java.lang.String", "C:/users/paths/path", profileRepo.getOne(1L));

		
		prefRepo.delete(path);
		
		assertThat(prefRepo.findAll())
			.isEmpty();
		
		//Verify that the profile is not removed
		
		assertThat(profileRepo.findAll())
			.hasSize(1);
	}
	
	@Test
	@Transactional
	public void testRemoveProfile()
	{
	
		Preference path = new Preference(profileRepo.findOne(1L), "Output Location", "C:/users/paths/path");
		prefRepo.save(path);
		
		assertThat(path)
			.extracting(FIELD_ID, FIELD_PREF_NAME, FIELD_PREF_VALUE_TYPE, FIELD_PREF_VALUE, FIELD_PREF_PROFILE)
			.containsExactly(1L, "Output Location", "java.lang.String", "C:/users/paths/path", profileRepo.getOne(1L));
	
		profileRepo.delete(1L);
		
		profileRepo.findAll();
		assertThat(profileRepo.findAll())
			.isEmpty();
		
		assertThat(prefRepo.findAll())
			.isEmpty();
	}
	
	
}
