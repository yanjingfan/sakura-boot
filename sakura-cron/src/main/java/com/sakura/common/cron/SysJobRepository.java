package com.sakura.common.cron;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysJobRepository extends JpaRepository<SysJobPO, Integer> {

    List<SysJobPO> findByJobStatus(Integer jobStatus);
}
