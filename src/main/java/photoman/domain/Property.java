package photoman.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Property extends AbstractEntity
{
	@Column
	private String propName;
	
	@Column
	private Serializable propValue;
	
	public Property(String propName, Serializable propValue)
	{
		this.propName = propName;
		this.propValue = propValue;
	}
	
	protected Property() { }

	public String getPropName() {
		return propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public Serializable getPropValue() {
		return propValue;
	}
	
	public String getPropValueAsString()
	{
		return propValue.toString();
	}

	public void setPropValue(Serializable propValue) {
		this.propValue = propValue;
	} 
	
	
}
