package org.university.service.impl.employee_service_impl;

import org.university.dao.EmployeeDao;
import org.university.entity.Employee;
import org.university.service.contract.employee_service.EmployeeFilterService;
import org.university.util.DriverQualifications;

import java.math.BigDecimal;
import java.util.List;

public class EmployeeFilterServiceImpl implements EmployeeFilterService {
    private final EmployeeDao employeeDao;
    public EmployeeFilterServiceImpl(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }
    @Override
    public List<Employee> filterEmployeeByQualification(DriverQualifications qualification) {
        return employeeDao.filterByQualification(qualification);
    }

    @Override
    public List<Employee> filterEmployeesBySalary(BigDecimal salary) {
        return employeeDao.filterByMinSalary(salary);
    }
}
