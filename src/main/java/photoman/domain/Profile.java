package photoman.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
public class Profile extends AbstractEntity 
{
	@Column(nullable = false)
	private String profileName;
	
	@OneToMany(mappedBy = "profile")
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Set<Preference> preferences = new HashSet<>();
	
	public Profile(String profileName)
	{
		this.profileName = profileName;
	}
	
	protected Profile() { }
	
	public String getProfileName() 	{
		return profileName;
	}
	
	public Set<Preference> getPreferences() {
		return preferences;
	}
	
}
