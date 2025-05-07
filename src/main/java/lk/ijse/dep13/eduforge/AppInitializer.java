package lk.ijse.dep13.eduforge;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import lk.ijse.dep13.eduforge.converter.LecturerTypeConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
public class AppInitializer implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(AppInitializer.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Bucket defaultBucket(@Value("${application.firebase.storage.bucket}") String storageBucket) throws IOException {
        InputStream serviceAccount = new ClassPathResource("/eduforge-1997-firebase-adminsdk-fbsvc-f6c0cea422.json").getInputStream();

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket(storageBucket)
                .build();

        FirebaseApp.initializeApp(options);
        return StorageClient.getInstance().bucket();
    };

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new LecturerTypeConverter());
    }
}
