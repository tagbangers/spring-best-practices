package practice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;
import java.util.UUID;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Autowired
	public void configureGlobal(UserDetailsService userDetailsService, AuthenticationManagerBuilder auth) throws Exception {
		// @formatter:off
		auth
			.userDetailsService(userDetailsService);
		// @formatter:on
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// @formatter:off
		web.ignoring()
			.antMatchers("/webjars/**");
		// @formatter:on
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		String rememberMeKey = UUID.randomUUID().toString();

		//@formatter:off
		http
			.authorizeRequests()
			.antMatchers("/login**").permitAll()
			.anyRequest()
			.authenticated()
		.and()
			.formLogin()
			.loginPage("/login")
			.failureUrl("/login?error").permitAll()
		.and()
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
		.and()
			.rememberMe()
			.key(rememberMeKey)
			.rememberMeServices(new MultiTenantRememberMeServices(rememberMeKey, userDetailsService(), multiTenantTokenRepository()))
		.and()
			.csrf().disable()
			.headers().frameOptions().sameOrigin();
		//@formatter:on
	}

	@Bean
	public MultiTenantTokenRepository multiTenantTokenRepository() {
		MultiTenantTokenRepository repository = new MultiTenantTokenRepository();
		repository.setDataSource(dataSource);
		return repository;
	}
}
