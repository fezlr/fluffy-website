package fezlr.fluffy;

import fezlr.fluffy.auth.config.AuthPropertiesMessages;
import fezlr.fluffy.common.property.CommonProperties;
import fezlr.fluffy.mail.property.MailProperties;
import fezlr.fluffy.mail.property.MailPropertiesMessages;
import fezlr.fluffy.photo.property.CloudinaryProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableConfigurationProperties({AuthPropertiesMessages.class, CommonProperties.class, MailProperties.class, MailPropertiesMessages.class, CloudinaryProperties.class})
@SpringBootApplication
public class FluffyApplication {
	public static void main(String[] args) {
		SpringApplication.run(FluffyApplication.class, args);
    }
}
