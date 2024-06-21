package com.unison.scada.availability.api.availability.variable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="variable")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Variable {
    @Id
    @Column(name="uuid")
    private UUID uuid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    @ColumnDefault("1")
    private boolean isActive;

    @Column(nullable = false)
    @ColumnDefault("0")
    private boolean isDelete;

    @Column(nullable = false)
    @ColumnDefault("1")
    private boolean isSave;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

}
