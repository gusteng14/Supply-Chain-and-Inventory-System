package com.barkstore.Barkstore.audit;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserRevisionListener implements RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {
        if (revisionEntity instanceof UserRevisionEntity) {
            UserRevisionEntity userRevisionEntity = (UserRevisionEntity) revisionEntity;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                userRevisionEntity.setUsername(authentication.getName()); // Get username from Spring Security
            } else {
                userRevisionEntity.setUsername("anonymous"); // Fallback for unauthenticated users
            }
        }
    }
}
