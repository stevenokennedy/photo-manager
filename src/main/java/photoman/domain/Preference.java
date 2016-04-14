package photoman.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

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

	public String getPreferenceName() {
		return preferenceName;
	}

	public void setPreferenceName(String preferenceName) {
		this.preferenceName = preferenceName;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public Serializable getValue() {
		return value;
	}

	public void setValue(Serializable value) {
		this.value = value;
	}
	
}
