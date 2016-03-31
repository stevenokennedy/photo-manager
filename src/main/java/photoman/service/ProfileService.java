package photoman.service;

import photoman.domain.Profile;
import photoman.exception.ProfileException;

public interface ProfileService 
{

	public Profile createProfile(String profileName) throws ProfileException;

	public Profile getCurrentProfile();
}
