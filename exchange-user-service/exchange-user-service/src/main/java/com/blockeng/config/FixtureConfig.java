package com.blockeng.config;

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author qiang
 */
@Configuration
public class FixtureConfig {

    @PostConstruct
    public void loadTemplates() {
        FixtureFactoryLoader.loadTemplates("com.blockeng.mock.fixture.template");
    }
}
