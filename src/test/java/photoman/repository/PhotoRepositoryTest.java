package photoman.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.assertj.core.groups.Tuple;
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
import photoman.domain.ExifData;
import photoman.domain.Photo;
import photoman.repository.ExifRepository;
import photoman.repository.PhotoRepository;

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PhotoMan.class)
@DatabaseSetup(PhotoRepositoryTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { PhotoRepositoryTest.DATASET })
@DirtiesContext
public class PhotoRepositoryTest 
{
	protected static final String DATASET = "classpath:datasets/it-photo-repo.xml";
	
	public static final String FIELD_ID = "id";
	public static final String FIELD_NAME = "fileName";
	public static final String FIELD_PATH = "path";
	public static final String FIELD_EXIF = "exif";
	
	private static final String FIELD_WIDTH = "imageWidth";
	private static final String FIELD_HEIGHT = "imageHeight";
	private static final String FIELD_DATE = "dateTaken";
	private static final String FIELD_MAKE = "make";
	private static final String FIELD_MODEL = "model";
	
	
	@Autowired
	private PhotoRepository repo;
	
	@Autowired
	private ExifRepository exifRepo;
	
	@Test
	public void testFindOne()
	{
		assertThat(repo.findOne(1L))
			.extracting(FIELD_ID, FIELD_NAME, FIELD_PATH, FIELD_EXIF)
			.containsExactly(1L, "item1", "path/path1", exifRepo.findOne(1L));
		
		assertThat(repo.findOne(4L))
			.extracting(FIELD_ID, FIELD_NAME, FIELD_PATH, FIELD_EXIF)
			.containsExactly(4L, "item4", "path/path4", null);
	}
	
	@Test
	public void testFindAll()
	{
		assertThat(repo.findAll())
			.hasSize(4)
			.extracting(FIELD_ID, FIELD_NAME, FIELD_PATH, FIELD_EXIF)
			.containsExactly(new Tuple(1L, "item1", "path/path1", exifRepo.findOne(1L)),
					new Tuple(2L, "item2", "path/path2", exifRepo.findOne(2L)),
					new Tuple(3L, "item3", "path/path3", exifRepo.findOne(3L)),
					new Tuple(4L, "item4", "path/path4", null));
			
	}
	
	@Test
	public void testAddPhoto() throws ParseException
	{
		ExifData exif5 = new ExifData(20L, 20L, new Date(), "Nikon", "D5500");
		exifRepo.save(exif5);
		repo.save(new Photo("item5", "path/path5", exif5));
		
		assertThat(repo.findAll())
		.hasSize(5)
		.extracting(FIELD_ID, FIELD_NAME, FIELD_PATH, FIELD_EXIF)
		.containsExactly(new Tuple(1L, "item1", "path/path1", exifRepo.findOne(1L)),
				new Tuple(2L, "item2", "path/path2", exifRepo.findOne(2L)),
				new Tuple(3L, "item3", "path/path3", exifRepo.findOne(3L)),
				new Tuple(4L, "item4", "path/path4", null),
				new Tuple(5L, "item5", "path/path5", exifRepo.findOne(4L)));
	}
	
	@Test
	public void testDeletePhoto() throws ParseException
	{
		SimpleDateFormat df = new SimpleDateFormat();
		df.applyPattern("yyyy-MM-dd hh:mm:ss");
		
		repo.delete(3L);
		assertThat(repo.findAll())
		.hasSize(3)
		.extracting(FIELD_ID, FIELD_NAME, FIELD_PATH, FIELD_EXIF)
		.containsExactly(new Tuple(1L, "item1", "path/path1", exifRepo.findOne(1L)),
				new Tuple(2L, "item2", "path/path2", exifRepo.findOne(2L)),
				new Tuple(4L, "item4", "path/path4", null));
		
		//Verify delete cascade
		assertThat(exifRepo.findAll())
			.hasSize(2)
			.extracting(FIELD_ID, FIELD_WIDTH, FIELD_HEIGHT, FIELD_MAKE, FIELD_MODEL)
			.containsExactly(new Tuple(1L, 100L, 100L, "Nikon", "D5500"),
					new Tuple(2L, 400L, 100L, "Nikon", "D5300"));
		
		
	}
	
	@Test
	public void testFindByFileName()
	{
		assertThat(repo.findByFileName("item3"))
			.hasSize(1)
			.extracting(FIELD_ID, FIELD_NAME)
			.containsExactly(new Tuple(3L, "item3"));
		
		assertThat(repo.findByFileName("item4"))
			.hasSize(1)
			.extracting(FIELD_ID, FIELD_NAME)
			.containsExactly(new Tuple(4L, "item4"));
		
		assertThat(repo.findByFileName("item"))
			.hasSize(0);
	}
	
	@Test
	public void findByPath()
	{
		assertThat(repo.findByPath("path/path2"))
			.extracting(FIELD_ID, FIELD_NAME)
			.containsExactly(2L, "item2");
		
		assertThat(repo.findByPath("no-path/path2"))
			.isNull();
	}
}
