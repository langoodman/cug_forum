package com.cug.forum.base.oauth.utils;

import com.cug.forum.base.oauth.APIConfig;


public class OathConfig {
    public static String getValue(String key) {
        return APIConfig.getInstance().getValue(key);
    }
}
