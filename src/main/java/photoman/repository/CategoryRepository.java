package photoman.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import photoman.domain.Category;
import photoman.domain.Photo;

public interface CategoryRepository extends JpaRepository<Category, Long> 
{
	public Category findByFullName(String categoryName);
	
	@Query("select c from Category c where c.parent = ?1")
	public Set<Category> findSubCategories(Category category);
	 
	@Query("select p from Photo p INNER JOIN p.categories c "
			+ "where c = ?1")
	public Set<Photo> findPhotosByCategory(Category category);
}
