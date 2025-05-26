package com.barkstore.Barkstore.appuser;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RoleRequest {
    private String name;
    private Set<AuthorityRequest> authorities;
    private String auths;

}
