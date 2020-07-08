package be.chaixdev.streamarksbackend.rest;

import be.chaixdev.streamarksbackend.model.Note;
import be.chaixdev.streamarksbackend.model.Subject;
import be.chaixdev.streamarksbackend.model.Topic;
import be.chaixdev.streamarksbackend.repository.TopicRepository;
import be.chaixdev.streamarksbackend.rest.dto.TopicDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TopicController {

    private TopicRepository topics;

    public TopicController(TopicRepository topics) {

        this.topics = topics;
    }

    @GetMapping(value = "topic", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Topic>> getAll() throws IOException {

        return ResponseEntity.ok(topics.getAllTopics());
    }

    @PostMapping(value = "topic", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Topic>> newTopic(@RequestBody Topic topic) throws IOException {

        DateTimeFormatter isoInstant = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        String datetime = ZonedDateTime.now().format(isoInstant);
        topic.setDateCreated(datetime);
        topic.setDateModified(datetime);

        topic.setNotes(new ArrayList<>());
        topics.save(topic);
        return ResponseEntity.ok(topics.getAllTopics());
    }

    @PostMapping(value = "topic/{id}/note", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Topic> addNoteToTopic(@RequestParam String id, @RequestBody Note note) throws IOException {

        note.setDateCreated(ZonedDateTime.now().toString());
        note.setDateModified(ZonedDateTime.now().toString());

        Topic topic = topics.getTopic(id);
        topic.addNote(note);

        return ResponseEntity.ok(topics.update(topic));
    }
}
