package com.barkstore.Barkstore.appuser;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Data
public class UserRequest {
    public String username;
    public String password;
    private Set<RoleRequest> roles;

}
