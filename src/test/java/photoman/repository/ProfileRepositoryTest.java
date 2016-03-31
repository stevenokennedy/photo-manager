package photoman.repository;

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

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PhotoMan.class)
@DatabaseSetup(ProfileRepositoryTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { ProfileRepositoryTest.DATASET })
@DirtiesContext
public class ProfileRepositoryTest 
{
	protected final static String DATASET = "classpath:datasets/it-profiles.xml";
	
	@Autowired
	private ProfileRepository profileRepo;
	
	@Test
	public void testFindOne()
	{		
		assertThat(profileRepo.findOne(1L))
			.extracting("profileName")
			.containsExactly("Primary");
		
		assertThat(profileRepo.findOne(3L))
		.extracting("profileName")
		.containsExactly("Tertiary");
	}
	
	@Test
	public void testByName()
	{
		assertThat(profileRepo.findByProfileName("Primary"))
			.extracting("profileName")
			.containsExactly("Primary");
	}
}
