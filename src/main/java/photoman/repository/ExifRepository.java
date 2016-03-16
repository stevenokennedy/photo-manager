package photoman.repository;

import org.springframework.data.repository.CrudRepository;

import photoman.domain.ExifData;

public interface ExifRepository extends CrudRepository<ExifData, Long> 
{

}
