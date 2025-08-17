package com.barkstore.Barkstore.Supplier;

import com.barkstore.Barkstore.audit.SupplierAuditDTO;
import com.barkstore.Barkstore.audit.UserRevisionEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public void createSupplier(SupplierRequest supplierRequest) {
        Supplier supplier = new Supplier();
        supplier.setName(supplierRequest.getName());
        supplier.setEmail(supplierRequest.getEmail());
        supplier.setContactNo(supplierRequest.getContactNo());
        supplier.setAddressLine1(supplierRequest.getAddressLine1());
        supplier.setAddressLine2(supplierRequest.getAddressLine2());
        supplier.setAddressLine3(supplierRequest.getAddressLine3());
        supplier.setAgentName(supplierRequest.getAgentName());
        supplier.setAgentContactNo(supplierRequest.getAgentContactNo());
        supplierRepository.save(supplier);
    }

    public void editSupplier(Supplier supplier, SupplierRequest supplierRequest) {
        supplier.setName(supplierRequest.getName());
        supplier.setEmail(supplierRequest.getEmail());
        supplier.setContactNo(supplierRequest.getContactNo());
        supplier.setAddressLine1(supplierRequest.getAddressLine1());
        supplier.setAddressLine2(supplierRequest.getAddressLine2());
        supplier.setAddressLine3(supplierRequest.getAddressLine3());
        supplier.setAgentName(supplierRequest.getAgentName());
        supplier.setAgentContactNo(supplierRequest.getAgentContactNo());
        supplierRepository.save(supplier);
    }

    public void softDelete(Long id) {
        Supplier supplier = supplierRepository.findById(id).orElseThrow(() -> new RuntimeException("Supplier not found"));
        supplier.setDeleted(true);
        supplierRepository.save(supplier);
    }

    public List<Supplier> findAllActive() {
        List<Supplier> active = supplierRepository.findAllActive();
        return active;
    }

    public List<Supplier> getSoftDeletes() {
        List<Supplier> deletedFields = supplierRepository.findSoftDeletes();
        return deletedFields;
    }

    public void restoreRecord(Long id) {
        Supplier supplier = supplierRepository.findById(id).get();
        supplier.setDeleted(false);
        supplierRepository.save(supplier);
    }

    public List<SupplierAuditDTO> getAllAudits() {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        AuditQuery query = auditReader.createQuery()
                .forRevisionsOfEntity(Supplier.class, false, true);

        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();
        List<SupplierAuditDTO> resultsToString = new ArrayList<>();

        for (Object[] row : results) {
            Supplier entity = (Supplier) row[0];
            UserRevisionEntity revisionEntity = (UserRevisionEntity) row[1];
            RevisionType revisionType = (RevisionType) row[2];

            SupplierAuditDTO dto = new SupplierAuditDTO();
            dto.setRevId(String.valueOf(revisionEntity.getRev()));
            dto.setAction(String.valueOf(revisionType));
            java.sql.Date date = new java.sql.Date(revisionEntity.getRevtstmp());
            dto.setDate(String.valueOf(date));
            dto.setSupplierId(String.valueOf(entity.getId()));
            dto.setSupplierName(entity.getName());
            dto.setUsername(revisionEntity.getUsername());
            resultsToString.add(dto);
        }
        return resultsToString;
    }

    public int activeSupplierCount() {
        List<Supplier> suppliers = supplierRepository.findAllActive();

        int count = suppliers.size();

        return count;
    }
}
