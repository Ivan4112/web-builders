package com.example.demo.config;

import com.example.demo.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

public class EmployeeDetails implements UserDetails {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeDetails.class);

    private final Employee employee;

    public EmployeeDetails(Employee employee) {
        this.employee = employee;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = employee.getRole();
        if(role == null){
//            logger.error("User with role not found.");
            throw new UsernameNotFoundException("Could not find employee");
        } else {
//            logger.info("User with login '{}' found.", employee.getLogin());
//            logger.info("User with password '{}' found.", employee.getPassword());
            logger.info("User with role '{}' found.", role);
        }
        List<SimpleGrantedAuthority> authority = new ArrayList<>();
        authority.add(new SimpleGrantedAuthority(role));


        return authority;
    }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public String getUsername() {
        return employee.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
