package com.swp391.backend.controllers.user;

import com.swp391.backend.controllers.authentication.AuthenticatedManager;
import com.swp391.backend.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TemporaryStorage {
    private final AuthenticatedManager authenticatedManager;
    private Map<Integer, Object> storage = new HashMap<>();

    public void saveTemporaryObject(Object obj) {
        User user = (User) authenticatedManager.getAuthenticatedUser();
        storage.put(user.getId(), obj);
    }

    public Object getTemporaryObject() {
        User user = (User) authenticatedManager.getAuthenticatedUser();
        return storage.get(user.getId());
    }
}
