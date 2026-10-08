//package com.healthai.hospital_backend.config;
//
//import com.healthai.hospital_backend.security.JwtAuthFilter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.HttpStatusEntryPoint;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Configuration
//public class SecurityConfig {
//
//	@Bean
//	PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//
//	@Bean
//	SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwtFilter) throws Exception {
//		http.csrf(c -> c.disable()).cors(c -> {
//		}).sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//				.exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
//				.authorizeHttpRequests(a -> a.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//						.requestMatchers("/api/auth/login", "/api/auth/register", "/api/health").permitAll()
//						// public emergency info (hospital finder + SOS must work without login)
//						.requestMatchers(HttpMethod.GET, "/api/hospitals/**").permitAll()
//						.requestMatchers(HttpMethod.POST, "/api/emergency").permitAll()
//						// admin-only management
//						.requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")
//						.requestMatchers(HttpMethod.PUT, "/api/settings").hasRole("ADMIN")
//						.requestMatchers(HttpMethod.POST, "/api/doctors", "/api/departments", "/api/hospitals")
//						.hasRole("ADMIN")
//						.requestMatchers(HttpMethod.PUT, "/api/doctors/**", "/api/departments/**", "/api/hospitals/**")
//						.hasRole("ADMIN")
//						// billing: admin + receptionist
//						.requestMatchers("/api/invoices/**").hasAnyRole("ADMIN", "RECEPTIONIST")
//						// prescriptions: clinical staff
//						.requestMatchers(HttpMethod.POST, "/api/prescriptions").hasAnyRole("ADMIN", "DOCTOR")
//						.requestMatchers(HttpMethod.PUT, "/api/prescriptions/**").hasAnyRole("ADMIN", "DOCTOR")
//						.anyRequest().authenticated())
//				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//		return http.build();
//	}
//
//	@Bean
//	CorsConfigurationSource corsConfigurationSource(AppProperties props) {
//		CorsConfiguration c = new CorsConfiguration();
//		c.setAllowedOrigins(props.cors().allowedOrigins());
//		c.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
//		c.setAllowedHeaders(List.of("*"));
//		c.setExposedHeaders(List.of("Content-Disposition"));
//		UrlBasedCorsConfigurationSource s = new UrlBasedCorsConfigurationSource();
//		s.registerCorsConfiguration("/**", c);
//		return s;
//	}
//}

package com.healthai.hospital_backend.config;

import com.healthai.hospital_backend.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwtFilter) throws Exception {
		http.csrf(c -> c.disable()).cors(c -> {
		}).sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
				.authorizeHttpRequests(a -> a.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.requestMatchers("/api/auth/login", "/api/auth/register", "/api/health", "/actuator/health",
								"/actuator/health/**")
						.permitAll()
						// public emergency info (hospital finder + SOS must work without login)
						.requestMatchers(HttpMethod.GET, "/api/hospitals/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/api/emergency").permitAll()
						// admin-only management
						.requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/api/settings").hasRole("ADMIN")
						.requestMatchers(HttpMethod.POST, "/api/doctors", "/api/departments", "/api/hospitals")
						.hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/api/doctors/**", "/api/departments/**", "/api/hospitals/**")
						.hasRole("ADMIN")
						// billing: admin + receptionist
						.requestMatchers("/api/invoices/**").hasAnyRole("ADMIN", "RECEPTIONIST")
						// prescriptions: clinical staff
						.requestMatchers(HttpMethod.POST, "/api/prescriptions").hasAnyRole("ADMIN", "DOCTOR")
						.requestMatchers(HttpMethod.PUT, "/api/prescriptions/**").hasAnyRole("ADMIN", "DOCTOR")
						.anyRequest().authenticated())
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource(AppProperties props) {
		CorsConfiguration c = new CorsConfiguration();
		// patterns (not exact origins) so ANY localhost port works: 5173, 5174, 3000
		// ...
		c.setAllowedOriginPatterns(props.cors().allowedOrigins());
		c.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		c.setAllowedHeaders(List.of("*"));
		c.setExposedHeaders(List.of("Content-Disposition"));
		UrlBasedCorsConfigurationSource s = new UrlBasedCorsConfigurationSource();
		s.registerCorsConfiguration("/**", c);
		return s;
	}
}