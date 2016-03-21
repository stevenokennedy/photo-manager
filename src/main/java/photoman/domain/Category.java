package photoman.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
	
	@OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE, CascadeType.MERGE } )
	private Set<Category> children = new HashSet<>();
	
	//========================================
	//===          CONSTRUCTORS            ===
	//========================================

	public Category(String name, Category parent)
	{
		this.name = name;
		this.fullName = (parent != null ? parent.getFullName() : "") + "/" + name;
		this.parent = parent;
		if(parent != null)
		{
			parent.children.add(this);
		}
	}
	
	protected Category() {}
	
	//========================================
	//===         PUBLIC METHODS           ===
	//========================================
	
	@Override
	public String toString()
	{
		return String.format("Category [id=%d, name=%s]", getId(), getFullName());
	}
	
	
	public void moveCategory(Category newParent)
	{
		this.parent = newParent;
	}
	
	public void maintainFullName()
	{
		this.fullName = (this.parent != null ? this.parent.getFullName() : "") + "/" + name;
		for(Category chld : children)
		{
			chld.maintainFullName();
		}
	}
	
	//========================================
	//===        GETTERS & SETTERS         ===
	//========================================
	
	public String getName() {
		return name;
	}
	
	public String getFullName() {
		return this.fullName;
	}
	
	public Category getParent() {
		return parent;
	}
	
	public void setName(String name) {
		this.name = name;
		maintainFullName();
	}	
	
}
