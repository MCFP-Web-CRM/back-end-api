package com.mcfuturepartners.crm.api.funnel.repository;

import com.mcfuturepartners.crm.api.funnel.entity.Funnel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FunnelRepository extends JpaRepository<Funnel, Long> {
}
