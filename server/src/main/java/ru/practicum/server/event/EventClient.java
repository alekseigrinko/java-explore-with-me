package ru.practicum.server.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.server.client.BaseClient;
import ru.practicum.server.event.dto.EventDto;

@Service
public class EventClient extends BaseClient {
    private static final String API_PREFIX = "/";

    @Autowired
    public EventClient(@Value("${explore-statistic.url}") String statisticUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(statisticUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getStat() {
        return get("stat");
    }

    public ResponseEntity<Object> postStat(EventDto eventDto) {
        return post("hit", eventDto);
    }
}
