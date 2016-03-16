package photoman.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Category extends AbstractEntity
{
	//========================================
	//===            FIELDS                ===
	//========================================
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false, unique = true)
	private String fullName;
	
	@ManyToOne
	private Category parent;
	
	@OneToMany(mappedBy = "parent")
	private Set<Category> children;
	
	@ManyToMany(mappedBy = "categories")
	private Set<Photo> photos;
	
	//========================================
	//===          CONSTRUCTORS            ===
	//========================================

	public Category(String name, Category parent)
	{
		this.name = name;
		this.fullName = (parent != null ? parent.getName() : "") + "/" + name;
		this.parent = parent;
	}
	
	protected Category() {}
	
	//========================================
	//===        GETTERS & SETTERS         ===
	//========================================
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Set<Photo> getPhotos() {
		return photos;
	}
	
	
	
}
