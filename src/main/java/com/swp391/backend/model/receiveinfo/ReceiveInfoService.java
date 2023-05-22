/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.swp391.backend.model.receiveinfo;

import com.swp391.backend.model.user.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author Lenovo
 */
@Service
@RequiredArgsConstructor
public class ReceiveInfoService {
    @Autowired
    private final ReceiveInfoRepository receiveInfoRepository;
    
    public List<ReceiveInfo> getReceiveInfo(User user, Integer page)
    {
        Pageable pageable = PageRequest.of(page, 3, Sort.by("id").ascending());
        return receiveInfoRepository.findByUser(user, pageable);
    }
    
    public ReceiveInfo saveReceiveInfo(ReceiveInfo receiveInfo)
    {
        return receiveInfoRepository.save(receiveInfo);
    }
}
