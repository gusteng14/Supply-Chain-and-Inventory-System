package com.barkstore.Barkstore.audit;

import lombok.Data;

@Data
public class ItemTypeAuditDTO {
    private String revId;
    private String action;
    private String date;
    private String itemTypeId;
    private String itemTypeName;
    private String username;
}
