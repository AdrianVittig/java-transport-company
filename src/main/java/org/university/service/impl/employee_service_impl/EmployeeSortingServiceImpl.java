package org.university.service.impl.employee_service_impl;

import org.university.dao.EmployeeDao;
import org.university.entity.Company;
import org.university.entity.Employee;
import org.university.service.contract.employee_service.EmployeeSortingService;
import org.university.util.DriverQualifications;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class EmployeeSortingServiceImpl implements EmployeeSortingService {
    private final EmployeeDao employeeDao;

    public EmployeeSortingServiceImpl(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    private int getQualificationIndex(Employee employee) {
        if(employee.getDriverQualifications() == null ||
        employee.getDriverQualifications().isEmpty()) {
            return -1;
        }

        return employee.getDriverQualifications()
                .stream()
                .mapToInt(Enum::ordinal)
                .max()
                .orElse(-1);
    }

    @Override
    public List<Employee> sortEmployeesBySalaryAscending() {
        return employeeDao.getAllEmployees()
                .stream()
                .sorted(Comparator.comparing(employee -> {
                    BigDecimal employeeSalary = employee.getSalary();
                    return employeeSalary != null ? employeeSalary : BigDecimal.ZERO;
                }))
                .toList();
    }

    @Override
    public List<Employee> sortEmployeesBySalaryDescending() {
        return employeeDao.getAllEmployees()
                .stream()
                .sorted(
                        Comparator.comparing((Employee e) -> {
                            BigDecimal employeeSalary = e.getSalary();
                            return employeeSalary != null ? employeeSalary : BigDecimal.ZERO;
                        }).reversed()
                )
                .toList();
    }

    @Override
    public List<Employee> sortEmployeesByQualificationAscending() {
        return employeeDao.getAllEmployees()
                .stream()
                .sorted(Comparator.comparingInt(this::getQualificationIndex))
                .toList();
    }

    @Override
    public List<Employee> sortEmployeesByQualificationDescending() {
        return employeeDao.getAllEmployees()
                .stream()
                .sorted(Comparator.comparingInt(this::getQualificationIndex).reversed())
                .toList();
    }
}
