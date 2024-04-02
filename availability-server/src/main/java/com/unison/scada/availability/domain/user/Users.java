package com.unison.scada.availability.domain.user;

import com.unison.scada.availability.global.config.security.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
//@Table(name = "\"User\"")
public class Users {
    @Id
    private String id;

    @Column(nullable = false, length = 64)
    //SHA-256
    private String pw;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    @ColumnDefault("true")
    private boolean isActive;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isDelete;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @ColumnDefault("'System'")
    private String createdBy;

    @UpdateTimestamp
    @Column(nullable = true)
    private LocalDateTime updatedAt;

    @Column(nullable = true)
    private String updatedBy;


}
