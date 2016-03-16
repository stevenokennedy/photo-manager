package photoman.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@Entity
public class Photo extends AbstractEntity 
{
	//========================================
	//===            FIELDS                ===
	//========================================
	
	@Column(nullable = false)
	private String fileName;
	
	@Column(nullable = false, unique = true)
	private String path;
	
	@OneToOne(cascade=CascadeType.REMOVE)
	private ExifData exif;

	@ManyToMany
	@JoinTable(name = "photo_categories", 
			   joinColumns = @JoinColumn(name = "photo_id", referencedColumnName = "id"), 
			   inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
	private Set<Category> categories;
	
	//========================================
	//===          CONSTRUCTORS            ===
	//========================================
	
	public Photo(String filename, String path, ExifData exif)
	{
		this.fileName = filename;
		this.path = path;
		this.exif = exif;
		this.categories = new HashSet<Category>();
	}
	
	protected Photo(){}
	
	//========================================
	//===          PUBLIC METHODS          ===
	//========================================
	
	public void addCategory(Category category)
	{
		categories.add(category);
	}
	
	//========================================
	//===        GETTERS & SETTERS         ===
	//========================================
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public ExifData getExif() {
		return exif;
	}

	public void setExif(ExifData exif) {
		this.exif = exif;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	
}
