package practice.support;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Component
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

	public static final String IDENTIFIER_ATTRIBUTE = CurrentTenantIdentifierResolverImpl.class.getName() + ".IDENTIFIER";

	public static final String TENANT_1_IDENTIFIER = "tenant1";
	public static final String TENANT_2_IDENTIFIER = "tenant2";

	@Override
	public String resolveCurrentTenantIdentifier() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes != null) {
			String identifier = (String) requestAttributes.getAttribute(IDENTIFIER_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
			if (identifier != null) {
				return identifier;
			}
		}
		return TENANT_1_IDENTIFIER;
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}
}
