package com.barkstore.Barkstore.requisition;

import com.barkstore.Barkstore.audit.ItemTypeAuditDTO;
import com.barkstore.Barkstore.audit.ProductAuditDTO;
import com.barkstore.Barkstore.audit.RequestHeaderAuditDTO;
import com.barkstore.Barkstore.audit.UserRevisionEntity;
import com.barkstore.Barkstore.products.ItemType;
import com.barkstore.Barkstore.products.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupplyChainService {
    @Autowired
    private HeaderRepository headerRepository;
    @Autowired
    private DetailsRepository detailsRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public RequestHeader saveHeader(HeaderDTO headerDTO) {
        RequestHeader requestHeader = new RequestHeader();
        requestHeader.setName(headerDTO.getRequestName());
        requestHeader.setDescription(headerDTO.getRequestDescription());
        headerRepository.save(requestHeader);

        return requestHeader;
    }

    public void saveDetails(RequestHeader requestHeader, List<String> itemList, List<String> qtyList, List<String> totalList ) {
        int i = 0;
        for (String str : itemList) {
            RequestDetails requestDetails = new RequestDetails();
//            System.out.println("Product " + i + ": " + str);
            requestDetails.setProduct(str);
//            System.out.println("Qty " + i + ": " + Integer.parseInt(qtyList.get(i)));
            requestDetails.setQuantity(Integer.parseInt(qtyList.get(i)));
//            System.out.println("Total " + i + ": " + Integer.parseInt(totalList.get(i)));
            requestDetails.setTotal(Integer.parseInt(totalList.get(i)));
            requestDetails.setHeaderId(requestHeader);
            detailsRepository.save(requestDetails);
            i++;
        }
    }

    public void softDeleteRequestHeader(Long id) {
        RequestHeader hdr = headerRepository.findById(id).orElseThrow(() -> new RuntimeException("Request Header not found"));
        hdr.setDeleted(true);
        headerRepository.save(hdr);
    }

    public List<RequestHeader> findAllActiveRequestHeader() {
        List<RequestHeader> active = headerRepository.findAllActive();
        return active;
    }

    public List<RequestHeader> getSoftDeletesRequestHeader() {
        List<RequestHeader> deletedFields = headerRepository.findSoftDeletes();
        return deletedFields;
    }

    public void restoreRecordRequestHeader(Long id) {
        RequestHeader hdr = headerRepository.findById(id).get();
        hdr.setDeleted(false);
        headerRepository.save(hdr);
    }

    public List<RequestHeaderAuditDTO> getAllAuditsRequestHeader() {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        AuditQuery query = auditReader.createQuery()
                .forRevisionsOfEntity(RequestHeader.class, false, true)
                .addProjection(AuditEntity.revisionNumber())
                .addProjection(AuditEntity.revisionType())
                .addProjection(AuditEntity.revisionProperty("revtstmp"))
                .addProjection(AuditEntity.property("id"))
                .addProjection(AuditEntity.property("name"))
                .addProjection(AuditEntity.revisionProperty("username"));

        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();
        List<RequestHeaderAuditDTO> resultsToString = new ArrayList<>();

        for (Object[] row : results) {
            RequestHeaderAuditDTO dto = new RequestHeaderAuditDTO();
            dto.setRevId(String.valueOf(row[0]));
            dto.setAction(String.valueOf(row[1]));
            long x = (long) row[2];
            java.sql.Date timestamp = new java.sql.Date(x);
            dto.setDate(String.valueOf(timestamp));
            dto.setHdrId(String.valueOf(row[3]));
            dto.setRequestName(String.valueOf(row[4]));
            dto.setUsername(String.valueOf(row[5]));
            resultsToString.add(dto);
        }

        return resultsToString;
    }
}
