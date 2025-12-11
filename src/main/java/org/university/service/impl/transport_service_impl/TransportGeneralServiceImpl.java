package org.university.service.impl.transport_service_impl;

import org.university.dao.TransportDao;
import org.university.dto.TransportDto;
import org.university.entity.Transport;
import org.university.exception.DAOException;
import org.university.service.contract.transport_service.*;
import org.university.util.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TransportGeneralServiceImpl implements TransportGeneralService {
    private final TransportCRUDSystemService crud;
    private final TransportPricingSystemService pricing;
    private final TransportPaymentSystemService payment;
    private final TransportFilterService filter;
    private final TransportSortingService sorting;
    private final TransportReportService report;

    public TransportGeneralServiceImpl(TransportCRUDSystemService crud, TransportPricingSystemService pricing, TransportPaymentSystemService payment, TransportFilterService filter, TransportSortingService sorting, TransportReportService report) {
        this.crud = crud;
        this.pricing = pricing;
        this.payment = payment;
        this.filter = filter;
        this.sorting = sorting;
        this.report = report;
    }


    @Override
    public Transport mapToEntity(TransportDto transportDto) {
        return crud.mapToEntity(transportDto);
    }

    @Override
    public TransportDto mapToDto(Transport transport) {
        return crud.mapToDto(transport);
    }

    @Override
    public TransportDto createTransport(TransportDto transportDto) {
        crud.createTransport(transportDto);
        return transportDto;
    }

    @Override
    public TransportDto getTransportById(Long id) throws DAOException {
        return crud.getTransportById(id);
    }

    @Override
    public Set<TransportDto> getAllTransports() {
        return crud.getAllTransports();
    }

    @Override
    public TransportDto updateTransport(Long id, TransportDto transportDto) throws DAOException {
        return crud.updateTransport(id, transportDto);
    }

    @Override
    public void deleteTransport(Long id) throws DAOException {
        crud.deleteTransport(id);
    }

    @Override
    public Long getCompanyIdForTransport(Long transportId) throws DAOException {
        return crud.getCompanyIdForTransport(transportId);
    }

    @Override
    public Long getEmployeeIdForTransport(Long transportId) throws DAOException {
        return crud.getEmployeeIdForTransport(transportId);
    }

    @Override
    public Long getCustomerIdForTransport(Long transportId) throws DAOException {
        return crud.getCustomerIdForTransport(transportId);
    }

    @Override
    public Long getVehicleIdForTransport(Long transportId) throws DAOException {
        return crud.getVehicleIdForTransport(transportId);
    }



    @Override
    public List<Transport> filterTransportsByDestination(String destination) {
        return filter.filterTransportsByDestination(destination);
    }

    @Override
    public void markTransportAsPaid(Long transportId) {
        payment.markTransportAsPaid(transportId);
    }

    @Override
    public BigDecimal calculateCustomerDebt(Long customerId) throws DAOException {
        return payment.calculateCustomerDebt(customerId);
    }

    @Override
    public void paySingleTransport(Long transportId) throws DAOException {
        payment.paySingleTransport(transportId);
    }

    @Override
    public Set<Long> getUnpaidTransportIdsForCustomer(Long customerId) throws DAOException {
        return payment.getUnpaidTransportIdsForCustomer(customerId);
    }

    @Override
    public BigDecimal calculateTotalPrice(Transport transport) throws DAOException {
        return pricing.calculateTotalPrice(transport);
    }

    @Override
    public List<Transport> sortTransportsByDestinationAscending() {
        return sorting.sortTransportsByDestinationAscending();
    }

    @Override
    public List<Transport> sortTransportsByDestinationDescending() {
        return sorting.sortTransportsByDestinationDescending();
    }

    @Override
    public int getTransportsCount() {
        return report.getTransportsCount();
    }

    @Override
    public BigDecimal getTotalTransportRevenue() {
        return report.getTotalTransportRevenue();
    }

    @Override
    public Map<Long, Integer> getTransportsCountByDriver() {
        return report.getTransportsCountByDriver();
    }

    @Override
    public BigDecimal getCompanyRevenueForAPeriod(Long companyId, LocalDate startDate, LocalDate endDate) {
        return report.getCompanyRevenueForAPeriod(companyId, startDate, endDate);
    }

    @Override
    public Map<Long, BigDecimal> getDriverRevenue() {
        return report.getDriverRevenue();
    }
}
