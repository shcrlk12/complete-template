package com.unison.scada.availability.api.availability;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Variable")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Variable {

    @Id
    private String name;

    @Column(nullable = false)
    private String groupName;

    @Column(nullable = true)
    private String mappingOpcId;

    @Column(nullable = false)
    private boolean isOpcCommunicate;

    @Column(nullable = true)
    private boolean unit;

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
