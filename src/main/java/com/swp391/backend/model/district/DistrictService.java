package com.swp391.backend.model.district;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DistrictService {

    private final DistrictRepository districtRepository;

    public District save(District district) {
        return districtRepository.save(district);
    }

    public void deleteById(Integer id) {
        districtRepository.deleteById(id);
    }
}
