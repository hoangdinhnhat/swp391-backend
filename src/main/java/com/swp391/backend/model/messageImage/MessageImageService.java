package com.swp391.backend.model.messageImage;

import com.swp391.backend.model.message.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageImageService {
    private final MessageImageRepository messageImageRepository;

    public MessageImage save(MessageImage messageImage) {
        return messageImageRepository.save(messageImage);
    }

    public void delete(Integer id) {
        messageImageRepository.deleteById(id);
    }

    public List<MessageImage> getByMessage(Message message) {
        return messageImageRepository.findByMessage(message);
    }
}
