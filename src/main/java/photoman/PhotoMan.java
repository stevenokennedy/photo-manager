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
	public CommandLineRunner test(PhotoRepository photoRepo, ExifRepository exifRepo, CategoryRepository catRepo)
	{
		return (args) -> 
		{
			ExifData exif1 = exifRepo.save(new ExifData(100L, 100L, new Date(), "Nikon", "D5500"));
			photoRepo.save(new Photo("f1", "C:/path/path1", exif1));
			
			ExifData exif2 = exifRepo.save(new ExifData(100L, 100L, new Date(), "Nikon", "D5500"));
			Photo p2 = photoRepo.save(new Photo("f2", "C:/path/path2", exif2));
			
			Category root = catRepo.save(new Category("home", null));
			p2.addCategory(root);
			photoRepo.save(p2);
			
		};
	}
}
