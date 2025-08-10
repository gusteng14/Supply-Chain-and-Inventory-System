package com.barkstore.Barkstore.audit;

import lombok.Data;

@Data
public class CateogryAuditDTO {
    private String revId;
    private String action;
    private String date;
    private String categoryId;
    private String categoryName;
    private String username;
}
