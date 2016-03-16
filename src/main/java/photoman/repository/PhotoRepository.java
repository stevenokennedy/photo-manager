package photoman.repository;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import photoman.domain.Photo;

public interface PhotoRepository extends CrudRepository<Photo, Long> 
{
	Set<Photo> findByFileName(String lastName);
	Photo findByPath(String path);
}
