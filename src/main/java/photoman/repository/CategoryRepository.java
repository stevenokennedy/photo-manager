package photoman.repository;

import org.springframework.data.repository.CrudRepository;

import photoman.domain.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> 
{
	public Category findByFullName(String categoryName);
}
