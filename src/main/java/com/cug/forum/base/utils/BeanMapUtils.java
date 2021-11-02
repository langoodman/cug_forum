package com.cug.forum.base.utils;

import com.cug.forum.modules.data.*;
import com.cug.forum.modules.entity.*;
import com.cug.forum.base.lang.Consts;
import org.springframework.beans.BeanUtils;

public class BeanMapUtils {
    private static String[] USER_IGNORE = new String[]{"password", "extend", "roles"};

    public static UserVO copy(User po) {
        if (po == null) {
            return null;
        }
        UserVO ret = new UserVO();
        BeanUtils.copyProperties(po, ret, USER_IGNORE);
        return ret;
    }

    public static AccountProfile copyPassport(User po) {
        AccountProfile passport = new AccountProfile(po.getId(), po.getUsername());
        passport.setName(po.getName());
        passport.setEmail(po.getEmail());
        passport.setAvatar(po.getAvatar());
        passport.setLastLogin(po.getLastLogin());
        passport.setStatus(po.getStatus());
        return passport;
    }

    public static CommentVO copy(Comment po) {
        CommentVO ret = new CommentVO();
        BeanUtils.copyProperties(po, ret);
        return ret;
    }

    public static PostVO copy(Post po) {
        PostVO d = new PostVO();
        BeanUtils.copyProperties(po, d);
        return d;
    }

    public static MessageVO copy(Message po) {
        MessageVO ret = new MessageVO();
        BeanUtils.copyProperties(po, ret);
        return ret;
    }

    public static FavoriteVO copy(Favorite po) {
        FavoriteVO ret = new FavoriteVO();
        BeanUtils.copyProperties(po, ret);
        return ret;
    }

    public static PostTagVO copy(PostTag po) {
        PostTagVO ret = new PostTagVO();
        BeanUtils.copyProperties(po, ret);
        return ret;
    }

    public static TagVO copy(Tag po) {
        TagVO ret = new TagVO();
        BeanUtils.copyProperties(po, ret);
        return ret;
    }

    public static String[] postOrder(String order) {
        String[] orders;
        switch (order) {
            case Consts.order.HOTTEST:
                orders = new String[]{"comments", "views", "created"};
                break;
            case Consts.order.FAVOR:
                orders = new String[]{"favors", "created"};
                break;
            default:
                orders = new String[]{"weight", "created"};
                break;
        }
        return orders;
    }
}
