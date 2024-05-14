package com.example.demo.service;

import com.example.demo.model.Brigade;
import com.example.demo.model.Employee;
import com.example.demo.model.Position;
import com.example.demo.repository.BrigadeRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.PositionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private final EmployeeRepository employeeRepository;
    @Autowired
    private final BrigadeRepository brigadeRepository;
    @Autowired
    private final PositionRepository positionRepository;

    public void addEmployee(Employee employee) {
        Optional<Brigade> alreadyExistBrigade = brigadeRepository.findById(employee.getIdBrigade().getId());
        Brigade brigade = alreadyExistBrigade.orElseThrow(() -> new RuntimeException("Brigade not found"));
        Optional<Position> alreadyExistPosition = positionRepository.findById(employee.getIdPosition().getId());
        Position position = alreadyExistPosition.orElseThrow(() -> new RuntimeException("Position not found"));

        employee.setIdBrigade(brigade);
        employee.setIdPosition(position);
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employeeRepository.save(employee);
    }

    public boolean updateEmployee(Integer employeeId, String name,
                                  String lastName, String login,
                                  String password, String number) {

        Employee employee = employeeRepository.findById(employeeId).orElse(null);

        if (employee == null) {
            return false;
        }

        // Update employee details
        employee.setName(name);
        employee.setLastName(lastName);
        employee.setLogin(login);
        employee.setPassword(passwordEncoder.encode(password));
        employee.setPhoneNumber(number);

        employeeRepository.save(employee);

        return true;
    }
}
