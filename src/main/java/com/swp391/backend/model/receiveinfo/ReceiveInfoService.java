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
import org.springframework.transaction.annotation.Transactional;

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
        Pageable pageable = PageRequest.of(page, 3, Sort.by("_default").descending().and(Sort.by("id").ascending()));
        return receiveInfoRepository.findByUser(user, pageable);
    }
    
    public List<ReceiveInfo> getReceiveInfo(User user)
    {
        return receiveInfoRepository.findByUser(user);
    }
    
    public ReceiveInfo saveReceiveInfo(ReceiveInfo receiveInfo)
    {
        return receiveInfoRepository.save(receiveInfo);
    }
    
    public List<ReceiveInfo> saveReceiveInfos(List<ReceiveInfo> infos)
    {
        return receiveInfoRepository.saveAll(infos);
    }
    
    public int getMaxPage(User user)
    {
        List<ReceiveInfo> list = receiveInfoRepository.findByUser(user);
        int length = list.size();
        int page = Math.floorDiv(length, 3) + 1;
        return page;
    }
    
    public ReceiveInfo getReceiveInfo(int id)
    {
        ReceiveInfo receiveInfo = receiveInfoRepository.findById(id).orElse(null);
        return receiveInfo;
    }
    
    public void deleteReceiveInfo(int id)
    {
        receiveInfoRepository.deleteById(id);
    }
}
