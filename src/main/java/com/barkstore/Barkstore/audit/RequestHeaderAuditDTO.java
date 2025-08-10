package com.barkstore.Barkstore.audit;

import lombok.Data;

@Data
public class RequestHeaderAuditDTO {
    private String revId;
    private String action;
    private String date;
    private String hdrId;
    private String requestName;
    private String username;
}
