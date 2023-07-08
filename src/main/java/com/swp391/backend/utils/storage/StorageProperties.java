package com.swp391.backend.utils.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String avatarLocation = "avatars";
    private String productImageLocation = "products/image";
    private String productVideoLocation = "products/video";
    private String productFeedbackImageLocation = "products/feedbacks/image";
    private String productFeedbackVideoLocation = "products/feedbacks/video";

    private String messageImageLocation = "messages/image";
    private String messageVideoLocation = "messages/video";

    public String getAvatarLocation() {
        return avatarLocation;
    }

    public String getProductImageLocation() {
        return productImageLocation;
    }

    public String getProductVideoLocation() {
        return productVideoLocation;
    }

    public String getProductFeedbackImageLocation() {
        return productFeedbackImageLocation;
    }

    public String getProductFeedbackVideoLocation() {
        return productFeedbackVideoLocation;
    }

    public String getMessageImageLocation() {
        return messageImageLocation;
    }

    public String getMessageVideoLocation() {
        return messageVideoLocation;
    }

}
