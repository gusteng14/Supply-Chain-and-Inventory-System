package com.barkstore.Barkstore.appuser;

import com.barkstore.Barkstore.registration.token.ConfirmationToken;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import java.util.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity

public class MyUser /*implements UserDetails*/ {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;


//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(nullable = false, unique = true)
//    private Integer empNo;

    private String firstName;
    private String lastName;
    private String middleName;
    private String contactNo;
    private String username;
    private String email;
    private String password;

    @Transient
    private String roleRequest;

    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant lastUpdatedOn;

//    @Enumerated(EnumType.STRING)
//    private MyUserRole myUserRole;
//    private Boolean locked = false;
//    private Boolean enabled = false;

    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;
    //private Set<Role> roles = new HashSet<>();

    public MyUser(String firstName, String lastName, String middleName, String contactNo, String username, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.contactNo = contactNo;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public MyUser(String firstName, String lastName, String middleName, String contactNo, String username, String email, String password, String roleRequest) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.contactNo = contactNo;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roleRequest = roleRequest;
    }

    public MyUser(String firstName, String lastName, String middleName, String contactNo, String username, String email, String password, Set<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.contactNo = contactNo;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

//    public MyUser(String firstName, String lastName, String middleName, String contactNo, String username, String email, String password, MyUserRole myUserRole) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.middleName = middleName;
//        this.contactNo = contactNo;
//        this.username = username;
//        this.email = email;
//        this.password = password;
//        this.myUserRole = myUserRole;
//    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ADMIN");
//        return Collections.singletonList(authority);
//    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getPassword() {
        return password;
    }


    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }


    public String getLastName() {
        return lastName;
    }


    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public String getRoleRequest() {
        return roleRequest;
    }

    public void setRoleRequest(String roleRequest) {
        this.roleRequest = roleRequest;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

//    @Override
//    public boolean isAccountNonLocked() {
//        return !locked;
//    }


    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean hasRole(String roleName) {
        Iterator<Role> iterator = roles.iterator();

        while (iterator.hasNext()) {
            Role role = iterator.next();
            if (role.getName().equals(roleName)) {
                return true;
            }
        }
        return false;
    }

//    @Override
//    public boolean isEnabled() {
//        return enabled;
//    }
}
