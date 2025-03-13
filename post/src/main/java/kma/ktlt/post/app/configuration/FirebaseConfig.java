package kma.ktlt.post.app.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class FirebaseConfig {
    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {
        // Load Firebase Admin SDK credentials from JSON file in resources
        ClassPathResource serviceAccountFile = new ClassPathResource("key-firebase.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccountFile.getInputStream()))
                .setDatabaseUrl("https://notification-d53ec-default-rtdb.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(options);

        return FirebaseMessaging.getInstance();
    }

}
