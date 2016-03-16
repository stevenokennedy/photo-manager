package photoman.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractEntity 
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	public Long getId()
	{
		return this.id;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		if(this.id == null || obj == null || !(this.getClass().equals(obj.getClass())))
		{
			return false;
		}
		return this.id.equals(((AbstractEntity) obj).getId());
	}
	
	@Override
	public int hashCode()
	{
		return id == null ? 0 : id.hashCode();
	}
}
