package ru.practicum;



import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.StatisticService;
import ru.practicum.statistic.dto.EndpointHit;
import ru.practicum.statistic.dto.ViewStats;

import javax.persistence.EntityManager;
import java.util.List;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(locations = "classpath:application.properties")
public class StatisticTests {

    private final EntityManager em;
    private final StatisticService statisticService;

    @Test
    public void addStatistic() {

        EndpointHit endpointHit = EndpointHit.builder()
                .app("app")
                .uri("/test/1")
                .ip("192.168.1.1")
                .build();

        statisticService.add(endpointHit);

        String[] uris = {"/test/1"};

        List<ViewStats> viewStatsList = statisticService.getStatistic(
                "2022-01-01 01:01:00",
                "2030-01-01 12:01:00",
                uris, false);

        Assertions.assertTrue(viewStatsList != null);
        Assertions.assertEquals(1, viewStatsList.size());

    }

}
