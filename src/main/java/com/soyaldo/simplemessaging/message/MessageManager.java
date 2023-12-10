package com.soyaldo.simplemessaging.message;

import com.soyaldo.simplemessaging.message.types.Message;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class MessageManager {

    private final HashMap<String, Message> messages = new HashMap<>();

    public void registerManager() {
        reloadManager();
    }

    public void reloadManager() {
        clearMessages();
    }

    public boolean existMessage(String id) {
        return messages.containsKey(id);
    }

    public void addMessage(Message message) {
        messages.put(message.getId(), message);
    }

    public Message getMessage(String id) {
        return messages.get(id);
    }

    public void removeMessage(String id) {
        messages.remove(id);
    }

    public void clearMessages() {
        messages.clear();
    }

}