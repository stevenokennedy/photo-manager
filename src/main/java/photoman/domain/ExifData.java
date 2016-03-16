package photoman.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ExifData extends AbstractEntity
{
	//========================================
	//===            FIELDS                ===
	//========================================
	
	@Column(nullable = false)
	private Long imageWidth;
	
	@Column(nullable = false)
	private Long imageHeight;
	
	@Column(nullable = false)
	private Date dataTaken;
	
	@Column(nullable = false)
	private String make;
	
	@Column(nullable = false)
	private String model;
	
	private String orientation;
	
	private Long xResolution;
	
	private Long yResolution;
	
	private String resolutionUnit;
	
	private String copyright;
	
	//========================================
	//===          CONSTRUCTORS            ===
	//========================================
	
	public ExifData(Long imageWidth, Long imageHeight, Date dataTaken, String make, String model)
	{
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.dataTaken = dataTaken;
		this.make = make;
		this.model = model;
	}
	
	protected ExifData()
	{
		
	}

	//========================================
	//===          PUBLIC METHODS          ===
	//========================================
	
	@Override 
	public String toString()
	{
		return String.format("Exif [width:%d, height:%d, make:%s, model:%s",
					imageWidth, imageHeight, make, model);
	}
	
	//========================================
	//===         GETTERS & SETTERS        ===
	//========================================
	
	public Long getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(Long imageWidth) {
		this.imageWidth = imageWidth;
	}

	public Long getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(Long imageHeight) {
		this.imageHeight = imageHeight;
	}

	public Date getDataTaken() {
		return dataTaken;
	}

	public void setDataTaken(Date dataTaken) {
		this.dataTaken = dataTaken;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public Long getxResolution() {
		return xResolution;
	}

	public void setxResolution(Long xResolution) {
		this.xResolution = xResolution;
	}

	public Long getyResolution() {
		return yResolution;
	}

	public void setyResolution(Long yResolution) {
		this.yResolution = yResolution;
	}

	public String getResolutionUnit() {
		return resolutionUnit;
	}

	public void setResolutionUnit(String resolutionUnit) {
		this.resolutionUnit = resolutionUnit;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}
	
}
