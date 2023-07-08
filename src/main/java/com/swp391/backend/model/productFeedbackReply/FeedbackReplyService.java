package com.swp391.backend.model.productFeedbackReply;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackReplyService {

    private final FeedbackReplyRepository feedbackReplyRepository;

    public FeedbackReply save(FeedbackReply feedbackReply) {
        return feedbackReplyRepository.save(feedbackReply);
    }
}
