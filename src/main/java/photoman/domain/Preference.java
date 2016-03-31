package photoman.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Preference extends AbstractEntity
{
	@ManyToOne
	private Profile profile;
	
	@Column(nullable = false)
	private String preferenceName;
	
	@Column(nullable = false)
	private String valueType; 
	
	@Column
	private Serializable value;
	
	public Preference(Profile profile, String preferenceName, Serializable value)
	{
		this.profile = profile;
		this.preferenceName = preferenceName;
		this.valueType = value.getClass().getName();
		this.value = value;
	}
	
	protected Preference() {}
}
