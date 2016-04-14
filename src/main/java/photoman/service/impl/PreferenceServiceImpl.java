package photoman.service.impl;

import java.io.Serializable;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import photoman.domain.Preference;
import photoman.domain.Profile;
import photoman.repository.PreferenceRepository;
import photoman.service.PreferenceService;
import photoman.service.ProfileService;

@Service
public class PreferenceServiceImpl implements PreferenceService 
{
	@Autowired
	private PreferenceRepository prefRepo;
	
	@Autowired
	private ProfileService profileService;
	
	@Override
	@Transactional
	public void setPreference(String prefName, Serializable prefValue)
	{
		Profile profile = profileService.getCurrentProfile();
		Preference pref = new Preference(profile, prefName, prefValue);
		prefRepo.save(pref);
		profileService.addPreference(pref);
	}
	
	@Override
	@Transactional
	public Serializable getPreference(String prefName)
	{
		Set<Preference> prefs = profileService.getCurrentProfile().getPreferences();
		return prefs.stream().filter((Preference p) -> p.getPreferenceName().equals(prefName)).findAny().get().getValue();
	}
}
