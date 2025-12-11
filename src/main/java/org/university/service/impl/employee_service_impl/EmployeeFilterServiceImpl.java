package org.university.service.impl.employee_service_impl;

import org.university.dao.EmployeeDao;
import org.university.entity.Employee;
import org.university.service.contract.employee_service.EmployeeFilterService;
import org.university.util.DriverQualifications;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EmployeeFilterServiceImpl implements EmployeeFilterService {
    private final EmployeeDao employeeDao;
    public EmployeeFilterServiceImpl(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }
    @Override
    public List<Employee> filterEmployeeByQualification(DriverQualifications qualification) {
        return employeeDao.getAllEmployees()
                .stream()
                .filter(employee -> {
                    Set<DriverQualifications> employeeQualifications = employee.getDriverQualifications();
                    return employeeQualifications != null && employeeQualifications.contains(qualification);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Employee> filterEmployeesBySalary(BigDecimal salary) {
        return employeeDao.getAllEmployees()
                .stream()
                .filter(employee -> {
                    if(employee.getSalary() == null){
                        return false;
                    }
                    return employee.getSalary().compareTo(salary) >= 0;
                })
                .collect(Collectors.toList());
    }
}
