package com.hotsix.titans.electronicApproval.repository;

import com.hotsix.titans.electronicApproval.entity.EALeave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EALeaveRepository extends JpaRepository<EALeave, Integer> {

    List<EALeave> findAll();
}