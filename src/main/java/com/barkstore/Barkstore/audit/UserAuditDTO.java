package com.barkstore.Barkstore.audit;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class UserAuditDTO {
    private String revId;
    private String action;
    private String date;
    private String empNo;
    private String fullName;
    private String username;
}
