package com.example.moduleauth.domain.auth.service;


import com.example.moduleauth.domain.auth.dto.response.RecaptchaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ReCaptchaService {
	
	private final WebClient webClient;
	private final String secretKey;
	
	public ReCaptchaService(
		WebClient.Builder builder,
		@Value("${recaptcha.verify.url}") String verifyUrl,
		@Value("${recaptcha.secret.key}") String secretKey
	) {
		this.webClient = builder.baseUrl(verifyUrl).build();
		this.secretKey = secretKey;
	}
	
	public boolean isValid(String token) {
		if (token == null || token.isEmpty()) {
			return false;
		}
		
		RecaptchaResponse response = webClient.get()
			.uri(uriBuilder -> uriBuilder
				.queryParam("secret", secretKey)
				.queryParam("response", token)
				.build())
			.retrieve()
			.bodyToMono(RecaptchaResponse.class)
			.block();
		
		return response != null && response.isSuccess();
	}
}
