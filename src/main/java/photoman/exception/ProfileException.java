package photoman.exception;

public class ProfileException extends RuntimeException 
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
	
	
	public static final ProfileException getDeleteLastProfileException(String profileName)
	{
		return new ProfileException("Attempting to delete the last available profile " + 
					(profileName == null ? "<null>" : profileName));
	}
	
}
