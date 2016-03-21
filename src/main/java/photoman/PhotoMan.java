package photoman;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import photoman.domain.Category;
import photoman.domain.ExifData;
import photoman.domain.Photo;
import photoman.repository.CategoryRepository;
import photoman.repository.ExifRepository;
import photoman.repository.PhotoRepository;
import photoman.utils.BinaryUtils;

@SpringBootApplication
@EnableJpaRepositories
public class PhotoMan 
{
	private static final Logger logger = LoggerFactory.getLogger(PhotoMan.class);
	
	public static void main(String[] args)
	{
		SpringApplication.run(PhotoMan.class, args);
	}
	
	@Bean
	public BinaryUtils binaryUtils()
	{
		return new BinaryUtils();
	}
	
	/*@Bean
	public CommandLineRunner test(PhotoRepository photoRepo, ExifRepository exifRepo, CategoryRepository catRepo)
	{
		return (args) -> 
		{
			Category root = catRepo.save(new Category("home", null));
			Category test1 = catRepo.save(new Category("test1", root));
			Category test2 = catRepo.save(new Category("test2", root));
			
			ExifData exif1 = exifRepo.save(new ExifData(100L, 100L, new Date(), "Nikon", "D5500"));
			Photo p1 = photoRepo.save(new Photo("f1", "C:/path/path1", exif1));
			
			ExifData exif2 = exifRepo.save(new ExifData(100L, 100L, new Date(), "Nikon", "D5500"));
			Photo p2 = photoRepo.save(new Photo("f2", "C:/path/path2", exif2));
			
			ExifData exif3 = exifRepo.save(new ExifData(100L, 100L, new Date(), "Nikon", "D5500"));
			Photo p3 = photoRepo.save(new Photo("f2", "C:/path/path3", exif3));
			
			ExifData exif4 = exifRepo.save(new ExifData(100L, 100L, new Date(), "Nikon", "D5500"));
			Photo p4 = photoRepo.save(new Photo("f2", "C:/path/path4", exif4));
			
			ExifData exif5 = exifRepo.save(new ExifData(100L, 100L, new Date(), "Nikon", "D5500"));
			Photo p5 = photoRepo.save(new Photo("f2", "C:/path/path5", exif5));
			
			p1.addCategory(root);
			p2.addCategory(test1);
			p3.addCategory(test1);
			p4.addCategory(test1);
			p5.addCategory(test1);
			p1.addCategory(test2);
			p2.addCategory(test2);
			
			photoRepo.save(p1);
			photoRepo.save(p2);
			photoRepo.save(p3);
			photoRepo.save(p4);
			photoRepo.save(p5);
			
			logger.info(p1.getExif().toString());
			logger.info(p1.getCategories().toString());
			
			logger.info(catRepo.findPhotosByCategory(test1).toString());
			logger.info(test1.getParent().toString());
			logger.info(catRepo.findSubCategories(root).toString());
			
		};
	}*/
}
