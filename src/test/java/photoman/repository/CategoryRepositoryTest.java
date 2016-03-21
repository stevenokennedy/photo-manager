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

import photoman.PhotoMan;
import photoman.domain.Category;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import javax.transaction.Transactional;

import org.assertj.core.groups.Tuple;

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
	TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PhotoMan.class)
@DatabaseSetup(CategoryRepositoryTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { CategoryRepositoryTest.DATASET })
@DirtiesContext
public class CategoryRepositoryTest 
{
	protected static final String DATASET = "classpath:datasets/it-cat-repo.xml";
	
	public static final String FIELD_ID = "id";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_FULL_NAME = "fullName";
	public static final String FIELD_PARENT = "parent";
	
	@Autowired
	private CategoryRepository repo;
	
	@Test
	public void testFindCategories()
	{
		assertThat(repo.findAll())
			.extracting(FIELD_ID, FIELD_NAME, FIELD_FULL_NAME, FIELD_PARENT)
			.containsExactly(new Tuple(1L, "root", "/root", null),
					new Tuple(2L, "A", "/root/A", repo.findOne(1L)),
					new Tuple(3L, "B", "/root/B", repo.findOne(1L)),
					new Tuple(4L, "C", "/root/C", repo.findOne(1L)),
					new Tuple(5L, "D", "/root/A/D", repo.findOne(2L)),
					new Tuple(6L, "E", "/root/A/E", repo.findOne(2L)),
					new Tuple(7L, "F", "/root/B/F", repo.findOne(3L)),
					new Tuple(8L, "G", "/root/C/G", repo.findOne(4L)),
					new Tuple(9L, "H", "/root/C/G/H", repo.findOne(8L)));
	}
	
	@Test
	@Transactional
	public void testFindSubCategories()
	{
		//Check root children
		assertThat(repo.findSubCategories(repo.findOne(1L)))
			.extracting(FIELD_FULL_NAME, FIELD_PARENT)
			.containsExactly(new Tuple("/root/A", repo.findOne(1L)),
					new Tuple("/root/B", repo.findOne(1L)),
					new Tuple("/root/C", repo.findOne(1L)));
		
		//Check after new category added
		repo.save(new Category("I", repo.findOne(1L)));
		
		assertThat(repo.findSubCategories(repo.findOne(1L)))
			.extracting(FIELD_FULL_NAME, FIELD_PARENT)
			.containsExactly(new Tuple("/root/A", repo.findOne(1L)),
				new Tuple("/root/B", repo.findOne(1L)),
				new Tuple("/root/C", repo.findOne(1L)),
				new Tuple("/root/I", repo.findOne(1L)));
		
		//Check when no subcategories
		assertThat(repo.findSubCategories(repo.findOne(5L))).isEmpty();
		
		//Check when removing a subcategory
		assertThat(repo.findSubCategories(repo.findByFullName("/root/A")))
			.extracting(FIELD_FULL_NAME, FIELD_PARENT)
			.containsExactly(new Tuple("/root/A/D", repo.findByFullName("/root/A")),
				new Tuple("/root/A/E", repo.findByFullName("/root/A")));
		
		Category a = repo.findByFullName("/root/A");
		Category e = repo.findByFullName("/root/A/E"); 
		repo.delete(e);
		
		a = repo.findByFullName("/root/A");
		Set<Category> cats = repo.findSubCategories(repo.findByFullName("/root/A"));
		
		assertThat(repo.findSubCategories(repo.findByFullName("/root/A")))
			.extracting(FIELD_FULL_NAME, FIELD_PARENT)
			.containsExactly(new Tuple("/root/A/D", repo.findByFullName("/root/A")));
		
		//Check recursive delete 
		assertThat(repo.findAll())
			.extracting(FIELD_ID, FIELD_NAME, FIELD_FULL_NAME, FIELD_PARENT)
			.containsExactly(new Tuple(1L, "root", "/root", null),
					new Tuple(2L, "A", "/root/A", repo.findOne(1L)),
					new Tuple(3L, "B", "/root/B", repo.findOne(1L)),
					new Tuple(4L, "C", "/root/C", repo.findOne(1L)),
					new Tuple(5L, "D", "/root/A/D", repo.findOne(2L)),
					new Tuple(7L, "F", "/root/B/F", repo.findOne(3L)),
					new Tuple(8L, "G", "/root/C/G", repo.findOne(4L)),
					new Tuple(9L, "H", "/root/C/G/H", repo.findOne(8L)),
					new Tuple(10L, "I", "/root/I", repo.findOne(1L)));
		
		repo.delete(repo.findByFullName("/root/C"));
		
		assertThat(repo.findAll())
			.extracting(FIELD_ID, FIELD_NAME, FIELD_FULL_NAME, FIELD_PARENT)
			.containsExactly(new Tuple(1L, "root", "/root", null),
					new Tuple(2L, "A", "/root/A", repo.findOne(1L)),
					new Tuple(3L, "B", "/root/B", repo.findOne(1L)),
					new Tuple(5L, "D", "/root/A/D", repo.findOne(2L)),
					new Tuple(7L, "F", "/root/B/F", repo.findOne(3L)),
					new Tuple(10L, "I", "/root/I", repo.findOne(1L)));
		
	}
	
	@Test
	public void testRenameCategory()
	{
		Category c = repo.findOne(4L);
		assertThat(c)
			.extracting(FIELD_NAME, FIELD_FULL_NAME)
			.containsExactly("C", "/root/C");
		
		repo.save(new Category("G2", c));
		
		c.setName("C1");
		repo.save(c);
		assertThat(c)
			.extracting(FIELD_NAME, FIELD_FULL_NAME)
			.containsExactly("C1", "/root/C1");
		
		//Check to ensure full names of subcategories are correct after change in parent
		assertThat(repo.findOne(8L))
			.extracting(FIELD_NAME, FIELD_FULL_NAME)
			.containsExactly("G", "/root/C1/G");
		
		assertThat(repo.findOne(9L))
			.extracting(FIELD_NAME, FIELD_FULL_NAME)
			.containsExactly("H", "/root/C1/G/H");
		
		assertThat(repo.findByFullName("/root/C1/G2"))
			.extracting(FIELD_NAME, FIELD_FULL_NAME)
			.containsExactly("G2", "/root/C1/G2");
		
	}
	
	@Test
	public void testMoveCategory()
	{
		
	}
}
