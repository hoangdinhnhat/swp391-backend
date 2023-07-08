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
                .groupTwo(categoryGroupService.getCategoryGroupById(3))
                .build();

        var attach2 = AttachWith.builder()
                .groupOne(categoryGroupService.getCategoryGroupById(1))
                .groupTwo(categoryGroupService.getCategoryGroupById(3))
                .build();

        var attach3 = AttachWith.builder()
                .groupOne(categoryGroupService.getCategoryGroupById(1))
                .groupTwo(categoryGroupService.getCategoryGroupById(5))
                .build();

        var attach4 = AttachWith.builder()
                .groupOne(categoryGroupService.getCategoryGroupById(2))
                .groupTwo(categoryGroupService.getCategoryGroupById(5))
                .build();

        save(attach1);
        save(attach2);
        save(attach3);
        save(attach4);
    }
}
