package ru.practicum.server.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.server.client.BaseClient;
import ru.practicum.server.event.dto.HitDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class EventClient extends BaseClient {

    private static final String API_PREFIX = "";

    @Autowired
    public EventClient(@Value("${statistic.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> postHit(HitDto hitDto) {
        return post("/hit", hitDto);
    }

    public ResponseEntity<Object> getHits(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }

    public long getViews(long eventId) {
        String uri = "events/" + eventId;
        Map<String, Object> parameters = Map.of(
                "uris", uri
        );
        ResponseEntity<Object> response = get("/views?uri={uri}", parameters);
        String bodyResponse = response.getBody().toString();
        return Integer.parseInt(bodyResponse.replaceAll("[^0-9]", ""));
    }
}
