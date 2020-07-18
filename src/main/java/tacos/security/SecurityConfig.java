package tacos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
				.formLogin().loginPage("/login")

				.and()
				.logout()
				.logoutSuccessUrl("/")

				.and()
				.csrf()
				
				;
	}

	/*
	@Autowired
	DataSource dataSource;
	*/

	@Autowired
	//@Qualifier("userDetailsService")
	private UserDetailsService userDetailsSerivce;

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

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
		/*
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
		 */

		/* LDAP 기반 사용자 스토어 인증 */
		/*
		auth.ldapAuthentication()
				.userSearchBase("ou=people")                    // 사용자 검색 쿼리의 기준점 지정 메소드, 미지정 시 LDAP 계층의 루트부터 검색
				.userSearchFilter("(uid={0})")                  // LDAP 기본 쿼리의 필터 제공
				.groupSearchBase("ou=groups")                   // 그룹 검색 쿼리의 기준점 지정 메소드, 미지정 시 LDAP 계층의 루트부터 검색
				.groupSearchFilter("member={0}")                // LDAP 기본 쿼리의 필터 제공
				//.contextSource().url("ldap://tacocloud.com:389/dc=tacocloud,dc=com");     // LDAP 원격 서버 지정 방법
				.contextSource()
				.root("dc=tacocloud,dc=com")                    // LDAP 내장 서버 루트 경로 지정 방법
				.ldif("classpath:users.ldif")                   // LDIF 파일 경로
				.and()
				.passwordCompare()                              // LDAP 서버에 비밀번호 비교 요청
				.passwordEncoder(new BCryptPasswordEncoder())
				.passwordAttribute("userPasscode");             // LDAP에 저장된 비밀번호 속성명 지정
		 */

		/* 커스텀 사용자 명세 서비스를 이용한 사용자 인증 */
		auth.userDetailsService(userDetailsSerivce)
				.passwordEncoder(encoder());

	}
}
