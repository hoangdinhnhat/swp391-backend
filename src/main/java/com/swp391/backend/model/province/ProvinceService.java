package com.swp391.backend.model.province;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProvinceService {

    private final ProvinceRepository provinceRepository;

    public Province save(Province province) {
        return provinceRepository.save(province);
    }

    public void deleteById(Integer id) {
        provinceRepository.deleteById(id);
    }
}
