package com.unison.scada.availability.global;

import com.unison.scada.availability.api.availability.entity.AvailabilityType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="original_availability_data")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OriginalAvailabilityData {

    @EmbeddedId
    private OriginalAvailabilityDataId originalAvailabilityDataId;

    @Setter
    @Column(nullable = false)
    private double time;

    @ManyToOne
    private AvailabilityType availabilityType;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;


    @Data
    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class OriginalAvailabilityDataId implements Serializable {
        @Column
        private LocalDateTime timestamp;

        @Column
        private int windFarmId;

        @Column
        private int turbineId;

    }
}
