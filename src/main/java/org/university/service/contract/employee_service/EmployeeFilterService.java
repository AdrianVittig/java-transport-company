package org.university.service.contract.employee_service;

import org.university.entity.Employee;
import org.university.util.DriverQualifications;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeeFilterService {
    List<Employee> filterEmployeeByQualification(DriverQualifications qualification);
    List<Employee> filterEmployeesBySalary(BigDecimal salary);
}
