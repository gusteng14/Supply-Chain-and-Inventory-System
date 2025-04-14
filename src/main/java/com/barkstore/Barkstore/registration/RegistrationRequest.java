package com.barkstore.Barkstore.registration;

import com.barkstore.Barkstore.appuser.RoleRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@ToString

public class RegistrationRequest {
    @Valid

    @NotEmpty
    @NotBlank
    @Size(message = "First name can not be blank.")
    private String firstName;

    @NotEmpty
    @NotBlank
    @Size(message = "Last name can not be blank.")
    private String lastName;

    @NotEmpty
    @NotBlank
    @Size(message = "Middle name can not be blank.")
    private String middleName;

    @NotEmpty
    @NotBlank
//    @Size(min=11, message = "Contact number length must be 11.")
    private String contactNo;

    @NotEmpty
    @NotBlank
    @Size(min=6, max=30, message = "Username length must be between 6 to 30 characters.")
    private String username;

    @NotEmpty
    @NotBlank
    @Size(message = "Email can not be blank.")
    private String email;

    @NotEmpty
    @NotBlank
    @Size(min = 8, max = 100, message = "Password must be between 8 to 100 characters.")
    private String password;

    private String roleRequest;
    private Set<RoleRequest> roles;
    private String imageData;



    public String getRoleRequest() {
        return roleRequest;
    }

    public void setRoleRequest(String roleRequest) {
        this.roleRequest = roleRequest;
    }

    public Set<RoleRequest> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleRequest> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }







}


