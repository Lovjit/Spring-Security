package com.springmvc.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.springmvc.handler.AuthenticationSuccess;

@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	AuthenticationSuccess authenticationSuccess;
	
	@Autowired
    UserDetailsService detailsService;
	
	@Autowired
	DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder managerBuilder) throws Exception {
//        managerBuilder
//                .inMemoryAuthentication()
//                .withUser("user1").password("pass1").roles("USER")
//                .and()
//                .withUser("user2").password("pass2").roles("ADMIN");
    	managerBuilder
        .userDetailsService(detailsService);
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        	.antMatchers("/home").hasRole("USER")
        	.antMatchers("/register").permitAll()
        	.antMatchers("/userRegisterSave").permitAll()
        	.antMatchers("/registrationConfirmation").permitAll()
        	.anyRequest().authenticated()
        	.and()
            .rememberMe().tokenRepository(persistentTokenRepository())
        	.and()
            .formLogin()
            	.successHandler(authenticationSuccess)
                .loginPage("/login").permitAll()
                .loginProcessingUrl("/loginProcessing")
            	.and().csrf().disable()
            	;
        
       
    }
    
    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }
    
    
    
    
}
