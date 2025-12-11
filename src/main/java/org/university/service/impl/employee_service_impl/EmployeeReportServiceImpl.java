package org.university.service.impl.employee_service_impl;

import org.university.dao.EmployeeDao;
import org.university.dao.TransportDao;
import org.university.entity.Employee;
import org.university.entity.Transport;
import org.university.service.contract.employee_service.EmployeeReportService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class EmployeeReportServiceImpl implements EmployeeReportService {
    private final TransportDao transportDao;
    private final EmployeeDao employeeDao;

    public EmployeeReportServiceImpl(TransportDao transportDao, EmployeeDao employeeDao) {
        this.transportDao = transportDao;
        this.employeeDao = employeeDao;
    }

    @Override
    public BigDecimal getAverageTransportRevenuePerEmployee(Long employeeId) {
        Employee employee = employeeDao.getEmployeeById(employeeId);
        if(employee == null){
            return BigDecimal.ZERO;
        }
        List<Transport> employeeTransports = transportDao
                .getAllTransports()
                .stream()
                .filter(transport ->
                        transport.getEmployee() != null
                                && transport.getEmployee().getId().equals(employeeId)
                                && transport.getId() != null)
                .toList();

        if(employeeTransports.isEmpty()){
            return BigDecimal.ZERO;
        }

        BigDecimal totalRevenue = employeeTransports.stream()
                .map(transport -> transport.getTotalPrice()
                        != null ? transport.getTotalPrice()
                        : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalRevenue.divide(BigDecimal.valueOf(employeeTransports.size()), RoundingMode.HALF_UP);


    }
}
