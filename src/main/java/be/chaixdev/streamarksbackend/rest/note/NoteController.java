package be.chaixdev.streamarksbackend.rest.note;

import be.chaixdev.streamarksbackend.model.Note;
import be.chaixdev.streamarksbackend.model.Topic;
import be.chaixdev.streamarksbackend.repository.NoteRepository;
import be.chaixdev.streamarksbackend.repository.TopicRepository;
import com.cloudant.client.api.model.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path="topic/{id}/note")
public class NoteController {

    private NoteRepository noteRepo;

    public NoteController(NoteRepository noteRepo) {
        this.noteRepo = noteRepo;

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
    public ResponseEntity<Topic> addNoteToTopic(@PathVariable String id, @RequestBody Note note) throws IOException {

        note.setDateCreated(getNow());
        note.setDateModified(getNow());
        if(note.getEndTimeStamp()==null){
            note.setEndTimeStamp(note.getStartTimestamp());
        }
        Topic topic = topics.getTopic(id);
        topic.addNote(note);

        return ResponseEntity.ok(topics.update(topic));
    }

    /* DELETE one */
    @DeleteMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> deleteTopic(@PathVariable String id, @RequestParam String rev) throws IOException {

        return ResponseEntity.ok(topics.delete(id, rev));
    }
}
