package photoman.exception;

public class ProfileException extends Exception 
{
	private static final long serialVersionUID = 1L; 
	
	public ProfileException(String message) 
	{
		super(message);
	}
	
	public static final ProfileException getProfileExistsException(String profileName)
	{
		return new ProfileException("Profile " + 
					(profileName == null ? "<null>" : profileName) + " already exists");
	}
	
	public static final ProfileException getProfileDoesntExistException(String profileName)
	{
		return new ProfileException("Profile " + 
					(profileName == null ? "<null>" : profileName) + " does not exist");
	}
	
}
