package com.unison.scada.availability.global;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="general")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class General {

    @EmbeddedId
    private GeneralId generalId;

    @Column
    private Double ratedPower;

    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class GeneralId implements Serializable {
        @Column
        private int windFarmId;

        @Column
        private int turbineId;
    }
}
