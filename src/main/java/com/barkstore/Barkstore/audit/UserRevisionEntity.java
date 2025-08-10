package com.barkstore.Barkstore.audit;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import java.util.Date;

@Data
@Entity
@Table(name="customrevinfo")
@RevisionEntity(UserRevisionListener.class)
public class UserRevisionEntity {
    @Id
    @GeneratedValue
    @RevisionNumber
    private int rev;

    @RevisionTimestamp
    private long revtstmp;
    private String username;
}
