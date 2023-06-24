package com.swp391.backend.model.messageImage;

import com.swp391.backend.model.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageImageRepository extends JpaRepository<MessageImage, Integer> {
    List<MessageImage> findByMessage(Message message);
}
