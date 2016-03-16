package photoman.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class Photo 
{
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String fileName;
	
	private String path;
	
}
