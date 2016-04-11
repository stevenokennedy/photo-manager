package photoman.service;

import photoman.domain.Profile;
import photoman.exception.ProfileException;

public interface ProfileService 
{

	public Profile createProfile(String profileName);

	public Profile getCurrentProfile();

	public void deleteProfile(String profileName);

	public Profile changeProfile(String profileName);
}
