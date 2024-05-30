package com.unison.scada.availability.api.availability.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="availability_data")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AvailabilityData {

    @EmbeddedId
    private AvailabilityDataId availabilityDataId;

    @Setter
    @Column(nullable = false)
    private int time;

    @ManyToOne
    private AvailabilityType availabilityType;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = true)
    @Setter
    private LocalDateTime updatedAt;

    @Column(nullable = true)
    @Setter
    private String updatedBy;


    @Column(nullable = true)
    @ColumnDefault(value = "0")
    private Boolean changed;

    @Data
    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class AvailabilityDataId implements Serializable {
        @Column
        private LocalDateTime timestamp;

        @Column
        private int turbineId;

        @Column
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID uuid;
    }
}
