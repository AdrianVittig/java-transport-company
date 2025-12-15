package org.university.service.impl.transport_service_impl;

import org.university.dao.CompanyDao;
import org.university.dao.TransportDao;
import org.university.entity.Company;
import org.university.entity.Transport;
import org.university.exception.DAOException;
import org.university.service.contract.transport_service.TransportReportService;
import org.university.util.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TransportReportServiceImpl implements TransportReportService {
    private final TransportDao transportDao;
    private final CompanyDao companyDao;

    public TransportReportServiceImpl(TransportDao transportDao, CompanyDao companyDao) {
        this.transportDao = transportDao;
        this.companyDao = companyDao;
    }

    @Override
    public int getTransportsCount() {
        return transportDao.getAllTransports().size();
    }

    @Override
    public BigDecimal getTotalTransportRevenue() {
        return transportDao.getAllTransports()
                .stream()
                .filter(t -> t.getPaymentStatus() == PaymentStatus.PAID)
                .map(transport -> {
                    BigDecimal price = transport.getTotalPrice();
                    return price != null ? price : BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Map<Long, Integer> getTransportsCountByDriver() {
        return transportDao.getAllTransports()
                .stream()
                .filter(transport -> transport.getEmployee() != null
                && transport.getEmployee().getId() != null)
                .collect(Collectors.groupingBy(
                        transport -> transport.getEmployee().getId(),
                        Collectors.collectingAndThen(
                                Collectors.counting(),
                                Long::intValue
                        )
                ));
    }

    @Override
    public BigDecimal getCompanyRevenueForAPeriod(Long companyId, LocalDate startDate, LocalDate endDate) {
        Company company = companyDao.getCompanyById(companyId);
        if(company == null){
            throw new DAOException("Company with id " + companyId + " does not exist");
        }

        return transportDao.getAllTransports()
                .stream()
                .filter(transport -> transport.getCompany() != null
                && transport.getCompany().getId().equals(companyId))
                .filter(t -> t.getPaymentStatus() == PaymentStatus.PAID)
                .filter(transport -> {
                    LocalDate transportDate = transport.getDepartureDate();
                    return transportDate != null
                            && !transportDate.isBefore(startDate)
                            && !transportDate.isAfter(endDate);
                })
                .map(transport -> {
                    BigDecimal price = transport.getTotalPrice();
                    return price != null ? price : BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Map<Long, BigDecimal> getDriverRevenue() {
        Map<Long, BigDecimal> result = new HashMap<>();
        for(Transport transport : transportDao.getAllTransports()){
            if(transport.getEmployee() == null
                    || transport.getEmployee().getId() == null
                    || transport.getPaymentStatus() != PaymentStatus.PAID){
                continue;
            }
            Long driverId = transport.getEmployee().getId();
            BigDecimal driverRevenue = transport.getTotalPrice();
            if(driverRevenue == null){
                driverRevenue = BigDecimal.ZERO;
            }
            result.merge(driverId, driverRevenue, BigDecimal::add);
        }
        return result;
    }
}
