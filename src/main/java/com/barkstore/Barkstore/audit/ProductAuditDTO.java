package com.barkstore.Barkstore.audit;

import lombok.Data;

@Data
public class ProductAuditDTO {
    private String revId;
    private String action;
    private String date;
    private String productId;
    private String productName;
    private String username;
}
