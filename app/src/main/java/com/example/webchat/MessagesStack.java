package com.example.webchat;

import java.util.ArrayList;
import java.util.List;

public class MessagesStack {

    private static ArrayList<ChatMessage> messages;

    static {
        messages = new ArrayList<>();
    }

    public static ArrayList<ChatMessage> getAll(){
        return messages;
    };

    public static void addMessage(ChatMessage message){
        messages.add(message);
    }

    public static void addMessages(List<ChatMessage> chatMessages){
        messages.addAll(chatMessages);
    }


}
