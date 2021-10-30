package com.mcfuturepartners.crm.api.notice.repository;

import com.mcfuturepartners.crm.api.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
