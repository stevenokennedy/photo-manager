package photoman.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import photoman.domain.Profile;
import photoman.domain.Property;
import photoman.exception.ProfileException;
import photoman.repository.ProfileRepository;
import photoman.repository.PropertyRepository;
import photoman.service.ProfileService;

@Service
public class ProfileServiceImpl implements ProfileService 
{
	private static final String CURRENT_PROFILE_PROPERTY = "currentProfile";
	private static final String DEFAULT_PROFILE_NAME = "Default";
	
	@Autowired
	private ProfileRepository profileRepo;
	
	@Autowired
	private PropertyRepository propertyRepo;
	
	private Profile currentProfile;
	
	@Transactional
	@Override
	public Profile createProfile(String profileName) throws ProfileException
	{
		if(profileRepo.findByProfileName(profileName) != null)
		{
			throw ProfileException.getProfileExistsException(profileName);
		}
		setCurrentProfile(new Profile(profileName));
		return currentProfile;
	}
	
	@Transactional
	@Override
	public Profile getCurrentProfile()
	{
		//If we haven't determined the current profile yet
		if(currentProfile == null)
		{
			//Get the profile property
			Property profileProp = propertyRepo.findByPropName(CURRENT_PROFILE_PROPERTY);
			if(profileProp == null)
			{
				//If we've never set a profile, then use the default one
				profileProp = new Property(CURRENT_PROFILE_PROPERTY, DEFAULT_PROFILE_NAME);
			}
			
			//Get the profile based on the current profile property value
			String profileName = profileProp.getPropValueAsString();
			Profile profile = profileRepo.findByProfileName(profileName);
			if(profile == null)
			{
				//If the default profile doesn't exist yet, then create it
				profile = new Profile(profileName);
			}
			setCurrentProfile(profile);
		}
		return currentProfile;
	}
	
	@Transactional
	public Profile changeProfile(String profileName) throws ProfileException
	{
		Profile profile = profileRepo.findByProfileName(profileName);
		if(profile == null)
		{
			throw ProfileException.getProfileDoesntExistException(profileName);
		}
		this.setCurrentProfile(profile);
		return profile;
	}
	
	@Transactional
	public void deleteProfile(String profileName) throws ProfileException
	{
		Profile profile = profileRepo.findByProfileName(profileName);
		if(profile == null)
		{
			throw ProfileException.getProfileDoesntExistException(profileName);
		}
		profileRepo.delete(profile);
	}
	
	private void setCurrentProfile(Profile profile)
	{
		this.currentProfile = profile;
		propertyRepo.save(new Property(CURRENT_PROFILE_PROPERTY, profile.getProfileName()));
	}
}
