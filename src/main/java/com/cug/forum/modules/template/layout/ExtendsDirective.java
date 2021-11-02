package com.cug.forum.modules.template.layout;

import com.cug.forum.modules.template.DirectiveHandler;
import com.cug.forum.modules.template.TemplateDirective;
import com.cug.forum.config.SiteOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExtendsDirective extends TemplateDirective {
    @Autowired
    private SiteOptions siteOptions;

    @Override
    public String getName() {
        return "layout.extends";
    }

    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        String theme = siteOptions.getValue("theme");
        String layoutName =  handler.getString("name");
        layoutName = layoutName.startsWith("/") ? theme + layoutName : theme + "/" + layoutName;
        handler.bodyResult();
        handler.getEnv().include(layoutName, null, true);
    }

}
