package com.barkstore.Barkstore.audit;

import lombok.Data;

@Data
public class SupplierAuditDTO {
    private String revId;
    private String action;
    private String date;
    private String supplierId;
    private String supplierName;
    private String username;
}
