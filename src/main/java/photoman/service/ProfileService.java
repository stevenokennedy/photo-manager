package photoman.service;

import photoman.domain.Preference;
import photoman.domain.Profile;

public interface ProfileService 
{

	public Profile createProfile(String profileName);

	public Profile getCurrentProfile();

	public void deleteProfile(String profileName);

	public Profile changeProfile(String profileName);

	public void addPreference(Preference pref);
}
