package practice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthorizedUserDetailsService implements UserDetailsService {

	public static final String TENANT_ID_ATTRIBUTE = AuthorizedUserDetailsService.class.getCanonicalName() + ".tenantId";
	public static final String TENANT_ID_PARAM_NAME = "tenantId";

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		String tenantId = ServletRequestUtils.getStringParameter(request, TENANT_ID_PARAM_NAME, (String) request.getAttribute(TENANT_ID_ATTRIBUTE));
		if (tenantId == null) {
			throw new UsernameNotFoundException("tenantId is Null");
		}

		User user = userRepository.findOneByTenantIdAndUsername(tenantId, username);
		if (user != null) {
			return new AuthorizedUserDetails(user);
		}
		throw new UsernameNotFoundException(username);
	}
}
