package by.itclass.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    protected void configure(HttpSecurity http) throws Exception {
        //метод antMatchers() позволяет настроить безопасность на определенные ресурсы приложения
        http.authorizeRequests()
                .antMatchers("/admin.html").hasAuthority("ADMIN")
                .antMatchers("/panel.html").hasRole("DEFAULT")
                .antMatchers("/main.html", "/main")
                .authenticated()
                .and()
                .formLogin();

    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager service = new JdbcUserDetailsManager(dataSource);
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin"))
                .authorities("USER", "ADMIN")
                .build();



        if (!service.userExists(admin.getUsername())) {
            service.createUser(admin);
        }

        return service;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User.builder()
//                .username("user")
//                .password("{bcrypt}$2a$12$Tpx48FFcFToBo/TqPWqILekhbyB1Fbh3Et9zCW2BS.6nyp9YN2C3C")
//                .authorities("USER")
//                .roles("DEFAULT")
//                .build();
//
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password("{bcrypt}$2a$12$KQSPAJNVHSG34KX4Uf6DiO53U0qJ53j.HrRCe.CzuA0RTHcYgPUSC")
//                .authorities("USER", "ADMIN")
//                .roles("DEFAULT")
//                .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new JdbcUserDetailsManager();
//    }


}
