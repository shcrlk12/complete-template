package com.unison.scada.availability.global.filter;

import com.unison.scada.availability.global.General;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GeneralRepository extends JpaRepository<General, General.GeneralId>{
}
