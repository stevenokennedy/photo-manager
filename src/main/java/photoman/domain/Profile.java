package photoman.domain;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
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
	public List<Preference> preferences;
	
	public Profile(String profileName)
	{
		this.profileName = profileName;
	}
	
	protected Profile() { }
	
}
