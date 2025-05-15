package com.example.moduleticket.feign;

import com.example.moduleticket.global.exception.ErrorCode;
import com.example.moduleticket.global.exception.ServerException;
import com.example.moduleticket.global.exception.UnknownException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.Response.Body;
import feign.RetryableException;
import feign.Util;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FeignClientErrorDecoder implements ErrorDecoder {

	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public Exception decode(String s, Response response) {
		try {

			Body responseBody = response.body();
			if (responseBody == null) {
				return new RuntimeException("No content returned from server");
			}

			String body = Util.toString(response.body().asReader());
			Map<String, String> map = mapper.readValue(body, Map.class);
			log.info("error : {}",map);
			if(map.get("code") != null) {
				try {
					return new ServerException(ErrorCode.valueOf(map.get("code")));
				} catch (IllegalArgumentException e) {
					log.error("error : {}",map);
					return new UnknownException("정의되지 않은 에러 코드입니다: " + map.get("code"));
				}
			}
			return new UnknownException("Feign 호출 중 알 수 없는 오류가 발생했습니다. status = " + response.status());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
