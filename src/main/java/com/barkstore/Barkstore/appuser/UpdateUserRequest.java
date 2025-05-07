package com.barkstore.Barkstore.appuser;


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
public class UpdateUserRequest {
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
    @Size(message = "Email can not be blank.")
    private String email;

    private String roleRequest;
    private Set<RoleRequest> roles;
    private String imageData;
}
