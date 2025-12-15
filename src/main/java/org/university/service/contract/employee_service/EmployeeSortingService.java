package org.university.service.contract.employee_service;

import org.university.entity.Employee;

import java.util.List;

public interface EmployeeSortingService {
    List<Employee> sortEmployeesBySalaryAscending(boolean isAscending);
    List<Employee> sortEmployeesByQualificationAscending(boolean isAscending);
}
