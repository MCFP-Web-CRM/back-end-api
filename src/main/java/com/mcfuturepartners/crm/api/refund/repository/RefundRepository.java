package com.mcfuturepartners.crm.api.refund.repository;

import com.mcfuturepartners.crm.api.refund.dto.RefundDto;
import com.mcfuturepartners.crm.api.refund.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refund,Long> {
}
