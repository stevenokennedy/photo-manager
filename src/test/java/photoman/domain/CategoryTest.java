package photoman.domain;

import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

public class CategoryTest 
{
	
	@Test
	public void basicTests()
	{
		//Test root category
		Category cat = new Category("root", null);
		assertThat(cat.getName())
			.isEqualTo("root");
		
		assertThat(cat.getFullName())
			.isEqualTo("/root");
		
		assertThat(cat.getParent())
			.isNull();
		
		assertThat(cat).hasToString("Category [id=null, name=/root]");
		
		
		//Test child category
		Category cat2 = new Category("child", cat);
		assertThat(cat2.getName())
			.isEqualTo("child");
	
		assertThat(cat2.getFullName())
			.isEqualTo("/root/child");
	
		assertThat(cat2.getParent())
			.isEqualTo(cat);
		
		//Test rename category
		Category cat3 = new Category("sub-child", cat2);
		assertThat(cat3.getName())
			.isEqualTo("sub-child");
	
		assertThat(cat3.getFullName())
			.isEqualTo("/root/child/sub-child");
	
		assertThat(cat3.getParent())
			.isEqualTo(cat2);
		
		cat3.setName("new-sub-child");
		assertThat(cat3.getName())
		.isEqualTo("new-sub-child");

		assertThat(cat3.getFullName())
			.isEqualTo("/root/child/new-sub-child");

		assertThat(cat3.getParent())
			.isEqualTo(cat2);
		
		assertThat(cat3).hasToString("Category [id=null, name=/root/child/new-sub-child]");
		
		//test rename the root category
		cat.setName("newRoot");
				
		assertThat(cat.getFullName())
			.isEqualTo("/newRoot");

		assertThat(cat).hasToString("Category [id=null, name=/newRoot]");
		
		assertThat(cat2).hasToString("Category [id=null, name=/newRoot/child]");
		
		assertThat(cat3).hasToString("Category [id=null, name=/newRoot/child/new-sub-child]");
		
		//test move category
		cat3.moveCategory(cat);
		
		assertThat(cat3.getFullName())
			.isEqualTo("/newRoot/new-sub-child");

		assertThat(cat3.getParent())
			.isEqualTo(cat);
	
		assertThat(cat3).hasToString("Category [id=null, name=/newRoot/new-sub-child]");
		
	}
}
