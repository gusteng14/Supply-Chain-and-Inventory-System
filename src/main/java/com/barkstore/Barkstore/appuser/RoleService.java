package com.barkstore.Barkstore.appuser;

import com.barkstore.Barkstore.Supplier.Supplier;
import com.barkstore.Barkstore.audit.RoleAuditDTO;
import com.barkstore.Barkstore.audit.SupplierAuditDTO;
import com.barkstore.Barkstore.audit.UserRevisionEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private MyUserService userService;
    @PersistenceContext
    private EntityManager entityManager;

    public void createRole(RoleRequest roleRequest, List<String> auths) {
        Role role = new Role();
        role.setName(roleRequest.getName());

        Set<Authority> authorities = saveAuthorityToRole(auths);

        System.out.println("Auths: " + authorities);
        role.setAuthorities(authorities);
        roleRepository.save(role);
    }

    public Set<Authority> saveAuthorityToRole(List<String> auths) {
        Set<Authority> authorities = new HashSet<>();
        for (String str : auths) {
            Authority authority = authorityRepository.findByName(str).get();
            authorities.add(authority);
        }
        return authorities;
    }

    public void showSavedRoleAuth(Long id) {
        Role role = roleRepository.findById(id).get();
        Set<Authority> authorities = new HashSet<>();
        System.out.println("Saved auths: ");
        for (Authority auth : role.getAuthorities()) {
            System.out.println(auth.getName());
        }

    }

    public List<String> showAuthForUpdate(Long id) {
        List<Authority> authorities = authorityRepository.findAll();
        Role role = roleRepository.findById(id).get();
        List<String> availableAuths = new ArrayList<>();
        authorities.removeAll(role.getAuthorities());
        for (Authority authority : authorities) {
            availableAuths.add(authority.getName());
        }
//        System.out.println("Available Permissions: " + availableAuths);

        return availableAuths;
    }

    public void softDelete(Long id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        role.setDeleted(true);
        roleRepository.save(role);
    }

    public List<Role> findAllActive() {
        List<Role> active = roleRepository.findAllActive();
        return active;
    }

    public List<Role> getSoftDeletes() {
        List<Role> deletedFields = roleRepository.findSoftDeletes();
        return deletedFields;
    }

    public void restoreRecord(Long id) {
        Role role = roleRepository.findById(id).get();
        role.setDeleted(false);
        roleRepository.save(role);
    }

    public List<RoleAuditDTO> getAllAudits() {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        AuditQuery query = auditReader.createQuery()
                .forRevisionsOfEntity(Role.class, false, true);

        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();
        List<RoleAuditDTO> resultsToString = new ArrayList<>();

        for (Object[] row : results) {
            Role entity = (Role) row[0];
            UserRevisionEntity revisionEntity = (UserRevisionEntity) row[1];
            RevisionType revisionType = (RevisionType) row[2];

            RoleAuditDTO dto = new RoleAuditDTO();
            dto.setRevId(String.valueOf(revisionEntity.getRev()));
            dto.setAction(String.valueOf(revisionType));
            java.sql.Date date = new java.sql.Date(revisionEntity.getRevtstmp());
            dto.setDate(String.valueOf(date));
            dto.setRoleId(String.valueOf(entity.getId()));
            dto.setRoleName(entity.getName());
            dto.setUsername(revisionEntity.getUsername());
            resultsToString.add(dto);
        }
        return resultsToString;
    }
}
