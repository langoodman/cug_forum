package com.cug.forum.web.controller.site.user;

import com.cug.forum.base.lang.Consts;
import com.cug.forum.base.lang.Result;
import com.cug.forum.base.utils.FileKit;
import com.cug.forum.base.utils.FilePathUtils;
import com.cug.forum.base.utils.ImageUtils;
import com.cug.forum.modules.data.AccountProfile;
import com.cug.forum.modules.data.UserVO;
import com.cug.forum.modules.service.SecurityCodeService;
import com.cug.forum.modules.service.UserService;
import com.cug.forum.web.controller.site.posts.UploadController;
import com.cug.forum.web.controller.BaseController;
import com.cug.forum.web.controller.site.Views;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/settings")
public class SettingsController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityCodeService securityCodeService;

    @GetMapping(value = "/profile")
    public String view(ModelMap model) {
        AccountProfile profile = getProfile();
        UserVO view = userService.get(profile.getId());
        model.put("view", view);
        return view(Views.SETTINGS_PROFILE);
    }

    @GetMapping(value = "/email")
    public String email() {
        return view(Views.SETTINGS_EMAIL);
    }

    @GetMapping(value = "/avatar")
    public String avatar() {
        return view(Views.SETTINGS_AVATAR);
    }

    @GetMapping(value = "/password")
    public String password() {
        return view(Views.SETTINGS_PASSWORD);
    }

    @PostMapping(value = "/profile")
    public String updateProfile(String name, String signature, ModelMap model) {
        Result data;
        AccountProfile profile = getProfile();

        try {
            UserVO user = new UserVO();
            user.setId(profile.getId());
            user.setName(name);
            user.setSignature(signature);

            putProfile(userService.update(user));

            // put ????????????
            UserVO view = userService.get(profile.getId());
            model.put("view", view);

            data = Result.success();
        } catch (Exception e) {
            data = Result.failure(e.getMessage());
        }
        model.put("data", data);
        return view(Views.SETTINGS_PROFILE);
    }

    @PostMapping(value = "/email")
    public String updateEmail(String email, String code, ModelMap model) {
        Result data;
        AccountProfile profile = getProfile();
        try {
            Assert.hasLength(email, "?????????????????????");
            Assert.hasLength(code, "??????????????????");

            securityCodeService.verify(String.valueOf(profile.getId()), Consts.CODE_BIND, code);
            // ????????????????????????????????????????????????????????????????????????
            AccountProfile p = userService.updateEmail(profile.getId(), email);
            putProfile(p);

            data = Result.success();
        } catch (Exception e) {
            data = Result.failure(e.getMessage());
        }
        model.put("data", data);
        return view(Views.SETTINGS_EMAIL);
    }

    @PostMapping(value = "/password")
    public String updatePassword(String oldPassword, String password, ModelMap model) {
        Result data;
        try {
            AccountProfile profile = getProfile();
            userService.updatePassword(profile.getId(), oldPassword, password);

            data = Result.success();
        } catch (Exception e) {
            data = Result.failure(e.getMessage());
        }
        model.put("data", data);
        return view(Views.SETTINGS_PASSWORD);
    }

    @PostMapping("/avatar")
    @ResponseBody
    public UploadController.UploadResult updateAvatar(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        UploadController.UploadResult result = new UploadController.UploadResult();
        AccountProfile profile = getProfile();

        // ?????????
        if (null == file || file.isEmpty()) {
            return result.error(UploadController.errorInfo.get("NOFILE"));
        }

        String fileName = file.getOriginalFilename();

        // ????????????
        if (!FileKit.checkFileType(fileName)) {
            return result.error(UploadController.errorInfo.get("TYPE"));
        }

        // ????????????
        try {
            String ava100 = Consts.avatarPath + getAvaPath(profile.getId(), 240);
            byte[] bytes = ImageUtils.screenshot(file, 240, 240);
            String path = storageFactory.get().writeToStore(bytes, ava100);

            AccountProfile user = userService.updateAvatar(profile.getId(), path);
            putProfile(user);

            result.ok(UploadController.errorInfo.get("SUCCESS"));
            result.setName(fileName);
            result.setPath(path);
            result.setSize(file.getSize());
        } catch (Exception e) {
            result.error(UploadController.errorInfo.get("UNKNOWN"));
        }
        return result;
    }

    private String getAvaPath(long uid, int size) {
        String base = FilePathUtils.getAvatar(uid);
        return String.format("/%s_%d.jpg", base, size);
    }
}
