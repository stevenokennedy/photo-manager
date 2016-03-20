package photoman.domain;

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

import javax.transaction.Transactional;

import org.assertj.core.groups.Tuple;

import photoman.PhotoMan;
import photoman.domain.Category;
import photoman.domain.Photo;
import photoman.repository.CategoryRepository;
import photoman.repository.ExifRepository;
import photoman.repository.PhotoRepository;
import photoman.repository.it.PhotoRepositoryTest;

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PhotoMan.class)
@DatabaseSetup(PhotoTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { PhotoTest.DATASET })
@DirtiesContext
public class PhotoTest 
{
	protected static final String DATASET = "classpath:datasets/it-photos.xml";
	
	@Autowired
	private PhotoRepository repo;
	
	@Autowired
	private ExifRepository exifRepo;
	
	@Autowired
	private CategoryRepository catRepo;
	
	@Test
	public void baseEqualityAndHashCodeTests()
	{
		Photo p1 = new Photo("file1", "path/path1", null);
		Photo p2 = new Photo("file2", "path/path2", null);
		Photo p3 = new Photo("file3", "path/path3", null);
		
		repo.save(p1);
		repo.save(p2);
		repo.save(p3);
		
		//Basic equality tests
		assertThat(p1.equals(p1)).isTrue();
		assertThat(p1.equals(p2)).isFalse();
		
		//Id based equality tests
		Photo p4 = repo.findOne(2L);
		Photo p5 = repo.findByPath("path/path1");
		System.out.println(p4);
		System.out.println(p5);
		
		assertThat(p4.equals(p5)).isTrue();
		
		//Null tests
		assertThat(p1.equals(null)).isFalse();
		
		//Retrieved domain object & non-persisted with same details
		Photo p6 = new Photo("file1", "/path/path1", null);
		assertThat(p4.equals(p6)).isFalse();
		assertThat(p6.equals(p4)).isFalse();
		
		//Different classes, same id
		assertThat(p4.equals(exifRepo.findOne(1L))).isFalse();
		
		int p1hash = p1.hashCode();
		int p6hash = p6.hashCode();
		int p4hash = p4.hashCode();
		int p5hash = p5.hashCode();
		int p3hash = p3.hashCode();
		
		assertThat(p1hash == p6hash).isFalse();
		assertThat(p4hash == p5hash).isTrue();
		assertThat(p4hash == p3hash).isFalse();
	}
	
	@Test
	@Transactional
	public void testBasicProperties()
	{
		Photo ph = repo.findByPath("path/path6");
		assertThat(ph)
			.extracting(PhotoRepositoryTest.FIELD_NAME, PhotoRepositoryTest.FIELD_PATH, PhotoRepositoryTest.FIELD_EXIF)
			.containsExactly("item6", "path/path6", exifRepo.findOne(1L));
		
		ph.setFileName("rename6");
		repo.save(ph);
		
		assertThat(repo.findByPath("path/path6"))
			.extracting(PhotoRepositoryTest.FIELD_NAME, PhotoRepositoryTest.FIELD_PATH, PhotoRepositoryTest.FIELD_EXIF)
			.containsExactly("rename6", "path/path6", exifRepo.findOne(1L));
		
		assertThat(repo.findByFileName("rename6"))
			.extracting(PhotoRepositoryTest.FIELD_NAME, PhotoRepositoryTest.FIELD_PATH, PhotoRepositoryTest.FIELD_EXIF)
			.containsExactly(new Tuple("rename6", "path/path6", exifRepo.findOne(1L)));
		
		ph.setPath("path/repath6");
		repo.save(ph);
		
		assertThat(repo.findByFileName("rename6"))
			.extracting(PhotoRepositoryTest.FIELD_NAME, PhotoRepositoryTest.FIELD_PATH, PhotoRepositoryTest.FIELD_EXIF)
			.containsExactly(new Tuple("rename6", "path/repath6", exifRepo.findOne(1L)));
		
		assertThat(repo.findByPath("path/repath6"))
			.extracting(PhotoRepositoryTest.FIELD_NAME, PhotoRepositoryTest.FIELD_PATH, PhotoRepositoryTest.FIELD_EXIF)
			.containsExactly("rename6", "path/repath6", exifRepo.findOne(1L));
		
		assertThat(ph.getCategories())
			.isEmpty();
		
		Category c1 = new Category("root", null);
		catRepo.save(c1);
		ph.addCategory(c1);
		repo.save(ph);
		
		assertThat(ph.getCategories())
			.containsExactly(c1);
		
		assertThat(ph.toString())
			.isEqualTo("Photo [id=1, name=rename6, path=path/repath6, Exif [id=1, width=100, height=100, dateTaken=2016-01-01 10:41:26, make=Nikon, model=D5500]]");
		
		ph.setExif(null);
		repo.save(ph);
		
		assertThat(repo.findByPath("path/repath6"))
			.extracting(PhotoRepositoryTest.FIELD_NAME, PhotoRepositoryTest.FIELD_PATH, PhotoRepositoryTest.FIELD_EXIF)
			.containsExactly("rename6", "path/repath6", null);
		
		assertThat(ph)
			.extracting(PhotoRepositoryTest.FIELD_NAME, PhotoRepositoryTest.FIELD_PATH, PhotoRepositoryTest.FIELD_EXIF)
			.containsExactly("rename6", "path/repath6", null);
		
		assertThat(ph.toString())
			.isEqualTo("Photo [id=1, name=rename6, path=path/repath6, <null>]");
		
		assertThat(catRepo.findPhotosByCategory(c1))
			.contains(ph);
		
		ph.removeCategory(c1);
		repo.save(ph);
		
		assertThat(catRepo.findPhotosByCategory(c1))
			.isEmpty();
		
		assertThat(ph.getCategories())
			.isEmpty();
		
	}
}
