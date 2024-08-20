package com.yt.costtime;

import org.springframework.context.annotation.Bean;

public class CostTimeConfiguration {

    @Bean
    public CostTimeAspect costTimeAspect() {
        return new CostTimeAspect();
    }
}
