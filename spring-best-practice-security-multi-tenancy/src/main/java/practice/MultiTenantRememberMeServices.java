package practice;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;

public class MultiTenantRememberMeServices extends AbstractRememberMeServices {

	private MultiTenantTokenRepository tokenRepository = new MultiTenantTokenRepository();
	private SecureRandom random;

	public static final int DEFAULT_SERIES_LENGTH = 16;
	public static final int DEFAULT_TOKEN_LENGTH = 16;

	private int seriesLength = DEFAULT_SERIES_LENGTH;
	private int tokenLength = DEFAULT_TOKEN_LENGTH;

	public MultiTenantRememberMeServices(
			String key,
			UserDetailsService userDetailsService,
			MultiTenantTokenRepository tokenRepository) {
		super(key, userDetailsService);
		random = new SecureRandom();
		this.tokenRepository = tokenRepository;
	}

	@Override
	protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request, HttpServletResponse response) {
		if (cookieTokens.length != 2) {
			throw new InvalidCookieException("Cookie token did not contain " + 2
					+ " tokens, but contained '" + Arrays.asList(cookieTokens) + "'");
		}

		final String presentedSeries = cookieTokens[0];
		final String presentedToken = cookieTokens[1];

		MultiTenantRememberMeToken token = tokenRepository.getTokenForSeries(presentedSeries);

		if (token == null) {
			// No series match, so we can't authenticate using this cookie
			throw new CookieTheftException(
					"No persistent token found for series id: " + presentedSeries);
		}

		// We have a match for this user/series combination
		if (!presentedToken.equals(token.getTokenValue())) {
			// Token doesn't match series value. Delete all logins for this user and throw
			// an exception to warn them.
			tokenRepository.removeUserTokens(token.getUsername(), token.getTenantId());

			throw new CookieTheftException(
					messages.getMessage(
							"PersistentTokenBasedRememberMeServices.cookieStolen",
							"Invalid remember-me token (Series/token) mismatch. Implies previous cookie theft attack."));
		}

		if (token.getDate().getTime() + getTokenValiditySeconds() * 1000L < System
				.currentTimeMillis()) {
			throw new RememberMeAuthenticationException("Remember-me login has expired");
		}

		// Token also matches, so login is valid. Update the token value, keeping the
		// *same* series number.
		if (logger.isDebugEnabled()) {
			logger.debug("Refreshing persistent login token for " +
					"tenantId '" + token.getTenantId() + "', " +
					"user '" + token.getUsername() + "', " +
					"series '" + token.getSeries() + "'");
		}

		MultiTenantRememberMeToken newToken = new MultiTenantRememberMeToken(
				token.getTenantId(), token.getUsername(), token.getSeries(), generateTokenData(), new Date());

		try {
			tokenRepository.updateToken(newToken.getSeries(), newToken.getTokenValue(), newToken.getDate());
			addCookie(newToken, request, response);
		}
		catch (Exception e) {
			logger.error("Failed to update token: ", e);
			throw new RememberMeAuthenticationException(
					"Autologin failed due to data access problem");
		}

		request.setAttribute(AuthorizedUserDetailsService.TENANT_ID_ATTRIBUTE, newToken.getTenantId());
		return getUserDetailsService().loadUserByUsername(token.getUsername());
	}

	@Override
	protected void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
		String username = successfulAuthentication.getName();

		logger.debug("Creating new persistent login for user " + username);

		MultiTenantRememberMeToken token = new MultiTenantRememberMeToken(
				extractTenantId(successfulAuthentication), username, generateSeriesData(), generateTokenData(), new Date());
		try {
			tokenRepository.createNewToken(token);
			addCookie(token, request, response);
		}
		catch (Exception e) {
			logger.error("Failed to save persistent token ", e);
		}
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		super.logout(request, response, authentication);

		if (authentication != null) {
			tokenRepository.removeUserTokens(authentication.getName(), extractTenantId(authentication));
		}
	}

	protected String extractTenantId(Authentication authentication) {
		AuthorizedUserDetails userDetails = (AuthorizedUserDetails) authentication.getPrincipal();
		return userDetails.getTenantId();
	}

	protected String generateSeriesData() {
		byte[] newSeries = new byte[seriesLength];
		random.nextBytes(newSeries);
		return new String(Base64.encode(newSeries));
	}

	protected String generateTokenData() {
		byte[] newToken = new byte[tokenLength];
		random.nextBytes(newToken);
		return new String(Base64.encode(newToken));
	}

	private void addCookie(MultiTenantRememberMeToken token, HttpServletRequest request,
						   HttpServletResponse response) {
		setCookie(new String[] { token.getSeries(), token.getTokenValue() },
				getTokenValiditySeconds(), request, response);
	}

	public void setSeriesLength(int seriesLength) {
		this.seriesLength = seriesLength;
	}

	public void setTokenLength(int tokenLength) {
		this.tokenLength = tokenLength;
	}

	@Override
	public void setTokenValiditySeconds(int tokenValiditySeconds) {
		Assert.isTrue(tokenValiditySeconds > 0,
				"tokenValiditySeconds must be positive for this implementation");
		super.setTokenValiditySeconds(tokenValiditySeconds);
	}
}
