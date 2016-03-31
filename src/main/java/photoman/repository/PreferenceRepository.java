package photoman.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import photoman.domain.Preference;

public interface PreferenceRepository extends JpaRepository<Preference, Long> {

}
