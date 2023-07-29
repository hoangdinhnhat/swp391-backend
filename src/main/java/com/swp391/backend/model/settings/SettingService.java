package com.swp391.backend.model.settings;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SettingService {
    private final SettingRepository settingRepository;

    public Setting save(Setting setting) {
        return settingRepository.save(setting);
    }

    public Setting getById(Integer id) {
        return settingRepository.findById(id).orElse(null);
    }

    public List<Setting> getAll() {
        Sort sort = Sort.by("id").ascending();
        return settingRepository.findAll(sort);
    }

    public List<Setting> saveAll(List<Setting> settings) {
        return settingRepository.saveAll(settings);
    }

    public void delete(Integer id) {
        settingRepository.deleteById(id);
    }

    public void init() {
        var setting1 = Setting.builder()
                .name("Number of products on sale")
                .value(40)
                .build();

        var setting2 = Setting.builder()
                .name("Number of related products")
                .value(10)
                .build();

        var setting3 = Setting.builder()
                .name("Number of bundled products")
                .value(6)
                .build();

        var setting4 = Setting.builder()
                .name("Number of comments on each page of product")
                .value(3)
                .build();

        var setting5 = Setting.builder()
                .name("FlashSale")
                .value(1)
                .build();

        save(setting1);
        save(setting2);
        save(setting3);
        save(setting4);
        save(setting5);
    }
}
