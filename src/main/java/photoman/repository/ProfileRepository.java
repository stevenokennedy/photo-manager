package photoman.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import photoman.domain.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> 
{

}
