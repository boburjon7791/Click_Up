package com.example.demo;

import com.example.demo.configuration.MyApplicationListener;
import com.example.demo.repositories.AuthUserRepository;
import com.example.demo.service.MultimediaService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;

@Slf4j
@EnableAsync
@EnableScheduling
@SpringBootApplication
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")})
@RequiredArgsConstructor
public class DemoApplication {
	private final MyApplicationListener myApplicationListener;
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private final MultimediaService multimediaService;

	@PostConstruct
	public void init(){
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tashkent"));
		log.info("Spring boot running with Asia/Tashkent time zone - {}", LocalDateTime.now());
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	@Scheduled(cron = "0 0 * * * *")
	public void images(){
		RowMapper<String> rowMapper = BeanPropertyRowMapper.newInstance(String.class);
		String sql = "select profile_image from auth_user";
		List<String> images = namedParameterJdbcTemplate.query(sql, rowMapper);
		multimediaService.deleteImagesIfExists(images);
	}
	@Bean
	public JavaMailSender javaMailSender(){
		return new JavaMailSenderImpl();
	}
	@Bean
	public SecurityScheme securityScheme(){
		return new SecurityScheme().type(SecurityScheme.Type.HTTP)
				.bearerFormat("jwt")
				.scheme("bearer");
	}
	@Bean
	public OpenAPI openApi(){
        return new OpenAPI().addSecurityItem(new SecurityRequirement()
				.addList("Bearer Authentication"))
				.components(new Components().addSecuritySchemes(
						"Bearer Authentication",this.securityScheme()
				)).info(info());
	}
	@Bean
	public Info info(){
		return new Info().title("Click Up - Rest API")
				.description("Some custom description of API")
				.version("1.0")
				.contact(new Contact().name("Soliyev Boburjon")
						.email("soliyevboburjon95@gmail.com")
						.url("https://localhost:8080"))
				.license(new License().name("License of API").url(myApplicationListener.hostName));
	}
	@Bean
	public GroupedOpenApi admin(){
		return GroupedOpenApi.builder()
				.pathsToMatch(
						"/**"
				).group("admin")
				.build();
	}
	@Bean
	public GroupedOpenApi user(){
		return GroupedOpenApi.builder()
				.pathsToMatch(
						"/api.multimedia/**",
						"/api.auth/register/**",
						"/api.auth/activate/**",
						"/api.auth/login-1",
						"/api.auth/login-2",
						"/api.auth/logout/**",
						"/api.auth/get/**",
						"/api.auth/create/**",
						"/api.auth/initialize/**",
						"/api.card/create",
						"/api.card/get/**",
						"/api.card/disable/**",
						"/api.card/my-cards/**",
						"/api.location/get/**",
						"/api.role/get/**",
						"/api.services/get/**",
						"/api.service-types/get/**",
						"/api.transaction/create/**",
						"/api.transaction/get/**"
				).group("user").build();
	}
}
