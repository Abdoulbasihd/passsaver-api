package cm.abdev.passaver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@SpringBootApplication
@EnableSwagger2
@EnableSwagger2WebMvc
public class PasswordSaverApplication {

	public static void main(String[] args) {
		SpringApplication.run(PasswordSaverApplication.class, args);
	}

	@Bean
	public Docket publicApi() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("cm.abdev.passaver")).build();
	}

}
