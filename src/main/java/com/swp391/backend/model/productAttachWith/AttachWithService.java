package com.swp391.backend.model.productAttachWith;

import com.swp391.backend.model.categoryGroup.CategoryGroup;
import com.swp391.backend.model.categoryGroup.CategoryGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttachWithService {
    private final AttachWithRepository attachWithRepository;
    private final CategoryGroupService categoryGroupService;

    public List<CategoryGroup> getAttachWith(CategoryGroup cg) {
        List<CategoryGroup> attach1 = attachWithRepository.findByGroupOne(cg)
                .stream()
                .map(c -> c.getGroupTwo())
                .collect(Collectors.toList());

        List<CategoryGroup> attach2 = attachWithRepository.findByGroupTwo(cg)
                .stream()
                .map(c -> c.getGroupOne())
                .collect(Collectors.toList());

        attachWithRepository.findByGroupTwo(cg)
                .forEach(c -> {
                    List<CategoryGroup> temp = attachWithRepository.findByGroupOne(c.getGroupOne())
                            .stream()
                            .filter(d -> d.getGroupTwo().getId() != c.getGroupTwo().getId())
                            .map(d -> d.getGroupTwo())
                            .collect(Collectors.toList());
                    attach2.addAll(temp);
                });

        attach1.addAll(attach2);

        return attach1;
    }

    public AttachWith save(AttachWith attachWith) {
        return attachWithRepository.save(attachWith);
    }

    public void delete(Integer id) {
        attachWithRepository.deleteById(id);
    }

    public void init() {
        var attach1 = AttachWith.builder()
                .groupOne(categoryGroupService.getCategoryGroupById(1))
                .groupTwo(categoryGroupService.getCategoryGroupById(4))
                .build();

        var attach2 = AttachWith.builder()
                .groupOne(categoryGroupService.getCategoryGroupById(1))
                .groupTwo(categoryGroupService.getCategoryGroupById(6))
                .build();

        var attach3 = AttachWith.builder()
                .groupOne(categoryGroupService.getCategoryGroupById(1))
                .groupTwo(categoryGroupService.getCategoryGroupById(7))
                .build();

        var attach4 = AttachWith.builder()
                .groupOne(categoryGroupService.getCategoryGroupById(1))
                .groupTwo(categoryGroupService.getCategoryGroupById(9))
                .build();

        var attach5 = AttachWith.builder()
                .groupOne(categoryGroupService.getCategoryGroupById(1))
                .groupTwo(categoryGroupService.getCategoryGroupById(10))
                .build();

        var attach6 = AttachWith.builder()
                .groupOne(categoryGroupService.getCategoryGroupById(1))
                .groupTwo(categoryGroupService.getCategoryGroupById(11))
                .build();

        var attach7 = AttachWith.builder()
                .groupOne(categoryGroupService.getCategoryGroupById(1))
                .groupTwo(categoryGroupService.getCategoryGroupById(13))
                .build();

        var attach8 = AttachWith.builder()
                .groupOne(categoryGroupService.getCategoryGroupById(2))
                .groupTwo(categoryGroupService.getCategoryGroupById(5))
                .build();

        var attach9 = AttachWith.builder()
                .groupOne(categoryGroupService.getCategoryGroupById(2))
                .groupTwo(categoryGroupService.getCategoryGroupById(6))
                .build();

        var attach10 = AttachWith.builder()
                .groupOne(categoryGroupService.getCategoryGroupById(2))
                .groupTwo(categoryGroupService.getCategoryGroupById(8))
                .build();

        var attach11 = AttachWith.builder()
                .groupOne(categoryGroupService.getCategoryGroupById(2))
                .groupTwo(categoryGroupService.getCategoryGroupById(9))
                .build();

        var attach12 = AttachWith.builder()
                .groupOne(categoryGroupService.getCategoryGroupById(2))
                .groupTwo(categoryGroupService.getCategoryGroupById(10))
                .build();

        var attach13 = AttachWith.builder()
                .groupOne(categoryGroupService.getCategoryGroupById(2))
                .groupTwo(categoryGroupService.getCategoryGroupById(12))
                .build();

        var attach14 = AttachWith.builder()
                .groupOne(categoryGroupService.getCategoryGroupById(2))
                .groupTwo(categoryGroupService.getCategoryGroupById(13))
                .build();

        var attach15 = AttachWith.builder()
                .groupOne(categoryGroupService.getCategoryGroupById(3))
                .groupTwo(categoryGroupService.getCategoryGroupById(4))
                .build();

        var attach16 = AttachWith.builder()
                .groupOne(categoryGroupService.getCategoryGroupById(3))
                .groupTwo(categoryGroupService.getCategoryGroupById(5))
                .build();

        var attach17 = AttachWith.builder()
                .groupOne(categoryGroupService.getCategoryGroupById(3))
                .groupTwo(categoryGroupService.getCategoryGroupById(6))
                .build();

        var attach18 = AttachWith.builder()
                .groupOne(categoryGroupService.getCategoryGroupById(3))
                .groupTwo(categoryGroupService.getCategoryGroupById(7))
                .build();

        var attach19 = AttachWith.builder()
                .groupOne(categoryGroupService.getCategoryGroupById(3))
                .groupTwo(categoryGroupService.getCategoryGroupById(8))
                .build();

        var attach20 = AttachWith.builder()
                .groupOne(categoryGroupService.getCategoryGroupById(3))
                .groupTwo(categoryGroupService.getCategoryGroupById(9))
                .build();

        var attach21 = AttachWith.builder()
                .groupOne(categoryGroupService.getCategoryGroupById(3))
                .groupTwo(categoryGroupService.getCategoryGroupById(10))
                .build();

        var attach22 = AttachWith.builder()
                .groupOne(categoryGroupService.getCategoryGroupById(3))
                .groupTwo(categoryGroupService.getCategoryGroupById(11))
                .build();

        var attach23 = AttachWith.builder()
                .groupOne(categoryGroupService.getCategoryGroupById(3))
                .groupTwo(categoryGroupService.getCategoryGroupById(12))
                .build();

        var attach24 = AttachWith.builder()
                .groupOne(categoryGroupService.getCategoryGroupById(3))
                .groupTwo(categoryGroupService.getCategoryGroupById(13))
                .build();

        save(attach1);
        save(attach2);
        save(attach3);
        save(attach4);
        save(attach5);
        save(attach6);
        save(attach7);
        save(attach8);
        save(attach9);
        save(attach10);
        save(attach11);
        save(attach12);
        save(attach13);
        save(attach14);
        save(attach15);
        save(attach16);
        save(attach17);
        save(attach18);
        save(attach19);
        save(attach20);
        save(attach21);
        save(attach22);
        save(attach23);
        save(attach24);
    }
}
