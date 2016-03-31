package photoman.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import photoman.domain.Preference;
import photoman.domain.Profile;
import photoman.service.PreferenceService;
import photoman.service.ProfileService;

@Service
public class PreferenceServiceImpl implements PreferenceService 
{
	@Autowired
	private ProfileService profileService;
	
	public void setPreference(String prefName, Serializable prefValue)
	{
		Profile profile = profileService.getCurrentProfile();
		Preference pref = new Preference(profile, prefName, prefValue);
	}
}
