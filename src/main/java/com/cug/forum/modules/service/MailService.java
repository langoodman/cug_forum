package com.cug.forum.modules.service;

import java.util.Map;

public interface MailService {
    void config();
    void sendTemplateEmail(String to, String title, String template, Map<String, Object> content);
}
