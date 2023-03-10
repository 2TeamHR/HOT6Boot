package com.hotsix.titans.attendanceManagement.service;

import com.hotsix.titans.attendanceManagement.dto.LeaveCategoryDTO;
import com.hotsix.titans.attendanceManagement.dto.LeaveCategoryAndLeavePaymentHistoryDTO;
import com.hotsix.titans.attendanceManagement.entity.LeaveCategory;
import com.hotsix.titans.attendanceManagement.entity.LeaveCategoryAndLeavePaymentHistory;
import com.hotsix.titans.attendanceManagement.repository.LeaveRepository;
import com.hotsix.titans.attendanceManagement.repository.LeaveRepositoryAndLeavePaymentHistory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveService {

    private final LeaveRepositoryAndLeavePaymentHistory leaveRepositoryAndLeavePaymentHistory;
    private final LeaveRepository leaveRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public LeaveService(LeaveRepositoryAndLeavePaymentHistory leaveRepositoryAndLeavePaymentHistory, LeaveRepository leaveRepository, ModelMapper modelMapper) {
        this.leaveRepositoryAndLeavePaymentHistory = leaveRepositoryAndLeavePaymentHistory;
        this.leaveRepository = leaveRepository;
        this.modelMapper = modelMapper;
    }


    public List<LeaveCategoryAndLeavePaymentHistoryDTO> listAll() {

        List<LeaveCategoryAndLeavePaymentHistory> leavePaymentHistoryList = leaveRepositoryAndLeavePaymentHistory.findAll();

        System.out.println("leavePaymentHistoryList : " + leavePaymentHistoryList);
        return leavePaymentHistoryList.stream().map(leavePaymentHistory -> modelMapper.map(leavePaymentHistory, LeaveCategoryAndLeavePaymentHistoryDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public Object insertLeaveCategory(LeaveCategoryDTO leaveCategoryDTO) {

        int result = 0;

        try {

            LeaveCategory insertLeaveCategory = modelMapper.map(leaveCategoryDTO, LeaveCategory.class);

            leaveRepository.save(insertLeaveCategory);

            result = 1;
        } catch (Exception e) {

            throw new RuntimeException(e);
        }

        return (result > 0) ? "?????? ??????" : "?????? ??????";
    }
}
