package com.mcfuturepartners.crm.api.counsel.repository;

import com.mcfuturepartners.crm.api.counsel.entity.Counsel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounselRepository extends JpaRepository<Counsel,String> {
}
