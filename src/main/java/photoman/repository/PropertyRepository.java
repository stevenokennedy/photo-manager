package photoman.repository;

import org.springframework.data.repository.CrudRepository;

import photoman.domain.Property;

public interface PropertyRepository extends CrudRepository<Property, Long>
{
	public Property findByPropName(String propName);
}
