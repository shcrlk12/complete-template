package com.unison.scada.availability.api.windfarm;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class WindFarmOverview {
    @Id

    private Integer windFarmId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer turbineNumber;
}
