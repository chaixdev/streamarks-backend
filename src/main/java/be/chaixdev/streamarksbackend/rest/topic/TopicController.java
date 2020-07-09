package be.chaixdev.streamarksbackend.rest.topic;

import be.chaixdev.streamarksbackend.model.Note;
import be.chaixdev.streamarksbackend.model.Topic;
import be.chaixdev.streamarksbackend.repository.TopicRepository;
import be.chaixdev.streamarksbackend.rest.common.ApiResponse;
import be.chaixdev.streamarksbackend.rest.common.ValidationError;
import com.cloudant.client.api.model.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static be.chaixdev.streamarksbackend.Utils.getNow;
import static be.chaixdev.streamarksbackend.rest.common.ApiResponse.*;

@RestController
@RequestMapping(path="topic")
public class TopicController {

    private TopicRepository topics;

    public TopicController(TopicRepository topics) {

        this.topics = topics;
    }
    
    /* CREATE */
    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Topic> newTopic(@RequestBody Topic topic) throws IOException {
        return ResponseEntity.ok(topics.save(topic));
    }

    /* READ all */
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Topic>> getAll() throws IOException {
        return ResponseEntity.ok(topics.getAllTopics());
    }

    /* READ one */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Topic> getOne(@PathVariable String id) throws IOException {
        return ResponseEntity.ok(topics.getTopic(id));
    }

    /* UPDATE one*/
    @PutMapping(value = "/{id}/note", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@PathVariable String id, @RequestBody Topic topic) throws IOException {

        return ResponseEntity.ok(topics.update(topic));
    }

    /* DELETE one */
    @DeleteMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> deleteTopic(@PathVariable String id, @RequestParam String rev) throws IOException {

        return ResponseEntity.ok(topics.delete(id, rev));
    }
}
