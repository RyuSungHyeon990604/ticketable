package com.example.modulecommon.argumentresolver;

import com.example.modulecommon.annotation.LoginUser;
import com.example.modulecommon.entity.AuthUser;
import org.apache.catalina.User;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(LoginUser.class) && parameter.getParameterType().equals(AuthUser.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		Long memberId = Long.valueOf(webRequest.getHeader("memberId"));
		String memberRole = webRequest.getHeader("role");
		return new AuthUser(memberId, memberRole);
	}
}
