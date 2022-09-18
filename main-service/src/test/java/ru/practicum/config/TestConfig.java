package ru.practicum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.statistic.StatisticService;

@Configuration
public class TestConfig {

    @Bean
    public StatisticService statisticService() {
        return new StatisticServiceStub();
    }

}
