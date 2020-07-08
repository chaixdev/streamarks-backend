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
    public ResponseEntity<List<Topic>> newTopic(@RequestBody Topic topic) throws IOException {

        String datetime = getNow();
        topic.setDateCreated(datetime);
        topic.setDateModified(datetime);

        topic.setNotes(new ArrayList<>());
        topics.save(topic);
        return ResponseEntity.ok(topics.getAllTopics());
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
    public ResponseEntity addNoteToTopic(@PathVariable String id, @RequestBody Note note) throws IOException {

        if(note.getInterval()==null){
            return errorResponseEntity(
                    HttpStatus.BAD_REQUEST,
                    Collections.singletonList(new ValidationError("$.interval", "interval can not be missing"))
            );
        }

        Topic topic = topics.getTopic(id);

        note.setDateCreated(getNow());
        note.setDateModified(getNow());

        topic.addNote(note);

        return ResponseEntity.ok(topics.update(topic));
    }

    /* DELETE one */
    @DeleteMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> deleteTopic(@PathVariable String id, @RequestParam String rev) throws IOException {

        return ResponseEntity.ok(topics.delete(id, rev));
    }

    private String getNow() {
        DateTimeFormatter isoInstant = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        return ZonedDateTime.now().format(isoInstant);
    }
}
