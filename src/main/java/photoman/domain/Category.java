package photoman.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;

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
	//===         PUBLIC METHODS           ===
	//========================================

	@Override
	public String toString()
	{
		return String.format("Category [id:%d, name:'%s'", getId(), fullName);
	}
	
	//========================================
	//===        GETTERS & SETTERS         ===
	//========================================
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}	
	
	public Category getParent() {
		return parent;
	}
	
}
