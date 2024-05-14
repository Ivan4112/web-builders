package com.example.demo.security;

import com.example.demo.config.EmployeeDetails;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomEmployeeDetailService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(CustomEmployeeDetailService.class);


    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findEmployeeByLogin(login);
        if (employee == null) {
            logger.error("User with login '{}' not found.", login);
            throw new UsernameNotFoundException("Could not find employee");
        }else {logger.info("User with login '{}' found.", login);}

        return new EmployeeDetails(employee);

       /* Optional<Employee> employee = employeeRepository.findEmployeeByLogin(login);
        return employee.map(EmployeeDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(login + " not found"));*/
    }

    /*@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findEmployeeByLogin(username);
        if (employee != null) {
            return User.builder()
                    .username(employee.getLogin())
                    .password(employee.getPassword())
                    .roles(employee.getRole())
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }*/
}
