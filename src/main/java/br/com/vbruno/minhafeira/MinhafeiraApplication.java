package br.com.vbruno.minhafeira;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "br.com.vbruno.minhafeira.repository")
@OpenAPIDefinition(info = @Info(title = "Minha Feira - API", version = "1", description = "API desenvolvida para auxiliar na realização e controle das feiras dos usuários"))
public class MinhafeiraApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinhafeiraApplication.class, args);
	}

}
