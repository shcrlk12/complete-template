package com.unison.scada.availability.api.windfarm;


import org.springframework.data.jpa.repository.JpaRepository;

public interface WindFarmOverviewRepository extends JpaRepository<WindFarmOverview, Integer> {
    WindFarmOverview findFirstByName(String name);

}
