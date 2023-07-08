package com.swp391.backend.model.ward;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WardService {

    private final WardRepository wardRepository;

    public Ward save(Ward ward) {
        return wardRepository.save(ward);
    }

    public void deleteById(Integer id) {
        wardRepository.deleteById(id);
    }
}
