package com.cug.forum.modules.template.directive;

import com.cug.forum.base.lang.Consts;
import com.cug.forum.config.SiteOptions;
import com.cug.forum.modules.template.DirectiveHandler;
import com.cug.forum.modules.template.TemplateDirective;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ControlsDirective extends TemplateDirective {
    @Autowired
    private SiteOptions siteOptions;

    @Override
    public String getName() {
        return "controls";
    }

    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        String control = handler.getString("name");

        if (StringUtils.isBlank(control)) {
            return;
        }

        String value = BeanUtils.getProperty(siteOptions.getControls(), control);
        if ("true".equalsIgnoreCase(value)) {
            handler.render();
        } else {
            // 当控制器 post 为关闭时, 继续判断角色
            if ("post".equalsIgnoreCase(control) && SecurityUtils.getSubject() != null && SecurityUtils.getSubject().hasRole(Consts.ROLE_ADMIN)) {
                handler.render();
            }
        }
    }
}
