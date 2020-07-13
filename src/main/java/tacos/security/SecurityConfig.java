package tacos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * HTTP 보안 구성 메소드
	 * @param http
	 * @throws Exception
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/design", "/orders").access("hasRole('ROLE_USER')")
				.antMatchers("/", "/**").access("permitAll")
				.and()
				.httpBasic();
	}

	@Autowired
	DataSource dataSource;

	/**
	 * 사용자 인증 정보 구성 메소드
	 * @param auth
	 * @throws Exception
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		/* 인메모리 사용자 스토어에 사용자 정의 방법 */
		/*
		auth.inMemoryAuthentication()
				.withUser("user1")
				.password("{noop}password1")
				.authorities("ROLE_USER")   // == .roles("USER")
				.and()
				.withUser("user2")
				.password("{noop}password2")
				.authorities("ROLE_USER");
		*/

		/* JDBC 기반의 사용자 스토어로 인증 1. 스프링 시큐리티 제공 쿼리 사용 */
		/*
		auth.jdbcAuthentication()
				.dataSource(dataSource);
		*/
		/* JDBC 기반의 사용자 스토어로 인증 2. 사용자 정보 쿼리 커스터마이징 */
		auth.jdbcAuthentication()
				.dataSource(dataSource)
				.usersByUsernameQuery(
						"SELECT username, password, enabled " +
						"FROM users " +
						"WHERE username = ? "
				)
				.authoritiesByUsernameQuery(
						"SELECT username, authority " +
						"FROM authorities " +
						"WHERE username = ? "
				)
				.passwordEncoder(new NoEncodingPasswordEncoder());

	}
}
