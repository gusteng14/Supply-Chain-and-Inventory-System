package com.barkstore.Barkstore.audit;

import lombok.Data;

@Data
public class RoleAuditDTO {
    private String revId;
    private String action;
    private String date;
    private String roleId;
    private String roleName;
    private String username;
}
