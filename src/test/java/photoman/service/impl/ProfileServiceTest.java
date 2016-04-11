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
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import static org.assertj.core.api.Assertions.*;

import photoman.PhotoMan;
import photoman.exception.ProfileException;
import photoman.repository.ProfileRepository;
import photoman.service.ProfileService;

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PhotoMan.class)
@DatabaseSetup(ProfileServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { ProfileServiceTest.DATASET })
@DirtiesContext
public class ProfileServiceTest
{
	
	protected static final String DATASET = "classpath:datasets/it-profile-service.xml";
	
	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private ProfileRepository profileRepo;
	
	@Test
	public void testBascicServices()
	{		
		//Initial Get including create of default profile
		assertThat(profileService.getCurrentProfile())
			.extracting("profileName")
			.containsExactly("Default");
		
		//Create new profile
		assertThat(profileService.createProfile("test"))
			.extracting("profileName")
			.containsExactly("test");
		
		//Verify new profile set as current
		assertThat(profileService.getCurrentProfile())
			.extracting("profileName")
			.containsExactly("test");
		
		//Change service back to old one
		assertThat(profileService.changeProfile("Default"))
			.extracting("profileName")
			.containsExactly("Default");
		
		assertThat(profileService.getCurrentProfile())
			.extracting("profileName")
			.containsExactly("Default");
		
		//Delete Profile
		assertThat(profileRepo.count())
		.isEqualTo(2);
		
		profileService.deleteProfile("test");
		
		assertThat(profileRepo.count())
			.isEqualTo(1);
	}
}
