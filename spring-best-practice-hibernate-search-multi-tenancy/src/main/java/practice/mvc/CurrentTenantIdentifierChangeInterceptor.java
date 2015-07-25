package practice.mvc;

import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import practice.support.CurrentTenantIdentifierResolverImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class CurrentTenantIdentifierChangeInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Map<String, Object> pathVariables = (Map<String, Object>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		if (pathVariables.containsKey("tenant")) {
			request.setAttribute(CurrentTenantIdentifierResolverImpl.IDENTIFIER_ATTRIBUTE, pathVariables.get("tenant"));
		}
		return true;
	}
}
