package com.blockeng.mock.fixture.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.blockeng.entity.User;

/**
 * @author qiang
 */
public class FixtureTemplateLoader implements TemplateLoader {
    @Override
    public void load() {
        Fixture.of(User.class).addTemplate("valid", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("username", random("qiang", "haigen", "chenlong"));
            add("realName", random("nerd", "geek"));
            add("email", "${username}@gmail.com");
        }});
    }
}