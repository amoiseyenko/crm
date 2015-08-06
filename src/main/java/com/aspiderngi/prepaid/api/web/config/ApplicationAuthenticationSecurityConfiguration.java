package com.aspiderngi.prepaid.api.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.aspiderngi.prepaid.api.service.CustomerAuthenticationService_Invoke;
import com.aspiderngi.prepaid.api.service.agent.AgentAuthenticationManager;
import com.aspiderngi.prepaid.api.service.agent.AgentDetailsService;
import com.aspiderngi.prepaid.api.service.agent.AgentRestLoginFilter;
import com.aspiderngi.prepaid.api.web.security.TokenAuthenticationService;
import com.aspiderngi.prepaid.api.web.security.filter.CorsAwareAuthenticationFilter;
import com.aspiderngi.prepaid.api.web.security.filter.RestAuthenticationFilter;
import com.aspiderngi.prepaid.api.web.security.filter.RestLoginFilter;

@Configuration
@EnableWebSecurity
@Order(2)
public class ApplicationAuthenticationSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomerAuthenticationService_Invoke 	customerDetailsService;

	@Autowired
	private AgentDetailsService	agentDetailsService;

	@Autowired
	AgentAuthenticationManager agentAuthenticationManager;

	@Autowired
	private TokenAuthenticationService 				tokenAuthenticationService;

	@Autowired
	private ApplicationContext 						appContext;

	@Value("${controller.prefix}")
	private String CONTROLLER_PREFIX = "";

	public ApplicationAuthenticationSecurityConfiguration() {
		super(true);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
		.antMatchers(CONTROLLER_PREFIX + "/resources/**")
		.antMatchers(CONTROLLER_PREFIX + "/web/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		RestLoginFilter 		 loginFilter = appContext.getBean(RestLoginFilter.class, CONTROLLER_PREFIX + "/auth/login",customerDetailsService,tokenAuthenticationService, authenticationManager());
		AgentRestLoginFilter 		 adminLoginFilter = appContext.getBean(AgentRestLoginFilter.class,   "/admin/login",tokenAuthenticationService, agentAuthenticationManager);
		RestAuthenticationFilter authFilter  = appContext.getBean(RestAuthenticationFilter.class, tokenAuthenticationService);

		CorsAwareAuthenticationFilter caaf = appContext.getBean(CorsAwareAuthenticationFilter.class);

		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.headers().cacheControl();

		http
		.exceptionHandling().and()
		.anonymous().and()
		.servletApi().and()
		.authorizeRequests()
		// .antMatchers(CONTROLLER_PREFIX + "/*").permitAll()									// allow anonymous resource requests
		.antMatchers(CONTROLLER_PREFIX + "/addresses/**").authenticated()
		.antMatchers(CONTROLLER_PREFIX + "/login/**").permitAll()										// allow anonymous web
		.antMatchers(HttpMethod.GET, CONTROLLER_PREFIX + "/usages/*").authenticated()					// calls to /usages/* should be authenticated
		.antMatchers(HttpMethod.POST, CONTROLLER_PREFIX + "/customers").permitAll()						// POST calls to /customers should not be authenticated (create customer)
		.antMatchers(HttpMethod.PUT,  CONTROLLER_PREFIX + "/customers/{\\d+}/active/**").permitAll()	// PUT calls to /customers should not be authenticated (activate customer)
		.antMatchers(CONTROLLER_PREFIX + "/customers/**").authenticated()								// OTHER calls to /customers/* should be authenticated
		.antMatchers(HttpMethod.POST, CONTROLLER_PREFIX + "/auth/login").permitAll()					// allow anonymous POSTs to login

		.antMatchers("/admin/login").permitAll()			 		   			 // defined ADMIN only API area
		.antMatchers("/admin/**").hasAnyRole("ADMIN")					    // defined ADMIN only API area

		.antMatchers(CONTROLLER_PREFIX + "/details/**").authenticated()									// calls to customer details should be authenticated
		.antMatchers(CONTROLLER_PREFIX + "/issuers/**").authenticated()									// calls to issuers should be authenticated
		.antMatchers(CONTROLLER_PREFIX + "/topups/**").authenticated()									// calls to topups should be authenticated
		.antMatchers(CONTROLLER_PREFIX + "/settings/**").authenticated()
		.antMatchers(CONTROLLER_PREFIX + "/balance/**").authenticated()									// calls to the balance view should be authenticated
		.antMatchers(CONTROLLER_PREFIX + "/history/**").authenticated()									// calls to the history view should be authenticated
		.antMatchers(HttpMethod.POST, CONTROLLER_PREFIX + "/pwd-mgmt/forgot-pwd/**").permitAll()
		.antMatchers(HttpMethod.PUT, CONTROLLER_PREFIX + "/pwd-mgmt/update-pwd").permitAll()
		.antMatchers(HttpMethod.PUT, CONTROLLER_PREFIX + "/pwd-mgmt/change-pwd").authenticated()

		.anyRequest().authenticated().and()																	// All other request need to be authenticated

		.addFilterBefore(caaf, UsernamePasswordAuthenticationFilter.class)
		.addFilterBefore(adminLoginFilter, UsernamePasswordAuthenticationFilter.class)			// custom JSON based authentication by POST of {"username":"<name>","password":"<password>"} which sets the token header upon authentication
		.addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class)			// custom JSON based authentication by POST of {"username":"<name>","password":"<password>"} which sets the token header upon authentication			
		.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)			// custom Token based authentication based on the header previously given to the client
		;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customerDetailsService).passwordEncoder(new StandardPasswordEncoder());
		auth.userDetailsService(agentDetailsService).passwordEncoder(new StandardPasswordEncoder());
	}


	@Bean
	public TokenAuthenticationService tokenAuthenticationService() {
		return this.tokenAuthenticationService;
	}
}