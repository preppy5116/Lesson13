package sbrt.concurrent.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class JSONController {

    private final ObjectMapper objectMapper;

    public JSONController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.objectMapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
    }

    @GetMapping(value = "/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<String> getJSONChunks() {
        List<Student> studentList = createStudentList();
        List<String> jsonList = new ArrayList<>();

        for (Student user : studentList) {
            try {
                String json = objectMapper.writeValueAsString(user);
                JsonNode jsonNode = objectMapper.readTree(json);
                jsonNode.fieldNames().forEachRemaining(fieldName -> {
                    jsonList.add("{\"" + fieldName + "\":\"" + jsonNode.get(fieldName).asText() + "\"}");
                });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }


        return Flux.fromIterable(jsonList)
                .delayElements(Duration.ofSeconds(5));
    }
    private List<Student> createStudentList() {
        return Arrays.asList(
                Student.builder()
                        .firstName("Резанов")
                        .secondName("Владимир")
                        .dateOfBirth(LocalDate.of(1990, 11, 23))
                        .address("г.Челябинск, пр. Космонавтов, 3")
                        .averageScore(4.1)
                        .faculty("Инфокоммуникационные технологии")
                        .build(),
                Student.builder()
                        .firstName("Орлова")
                        .secondName("Екатерина")
                        .dateOfBirth(LocalDate.of(1995, 2, 4))
                        .address("г.Одинцово, ул. Ленина, 68")
                        .averageScore(4.4)
                        .faculty("Менеджмент")
                        .build(),
                Student.builder()
                        .firstName("Федоров")
                        .secondName("Андрей")
                        .dateOfBirth(LocalDate.of(1993, 7, 13))
                        .address("г.Курск, ул. Балканская, 71")
                        .averageScore(4.3)
                        .faculty("Технология машиностроения")
                        .build()
        );
    }
}
