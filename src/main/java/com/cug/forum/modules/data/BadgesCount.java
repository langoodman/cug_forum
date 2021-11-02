package com.cug.forum.modules.data;

import java.io.Serializable;

public class BadgesCount implements Serializable {
    private static final long serialVersionUID = 8276459939240769498L;

    private int messages; // 消息数量

    public int getMessages() {
        return messages;
    }

    public void setMessages(int messages) {
        this.messages = messages;
    }
}
