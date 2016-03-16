package photoman.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import photoman.domain.Photo;

public interface PhotoRepository extends CrudRepository<Photo, Long> 
{
	List<Photo> findByFileName(String lastName);
	Photo findByPath(String path);
}
