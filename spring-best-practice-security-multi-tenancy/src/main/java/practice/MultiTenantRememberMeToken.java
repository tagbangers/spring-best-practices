package practice;

import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import java.util.Date;

public class MultiTenantRememberMeToken extends PersistentRememberMeToken {

	private final String tenantId;

	public MultiTenantRememberMeToken(String tenantId, String username, String series, String tokenValue, Date date) {
		super(username, series, tokenValue, date);
		this.tenantId = tenantId;
	}

	public String getTenantId() {
		return tenantId;
	}
}
