package be.chaixdev.streamarksbackend.rest.note;

import be.chaixdev.streamarksbackend.model.Note;
import be.chaixdev.streamarksbackend.rest.common.ValidationError;
import be.chaixdev.streamarksbackend.rest.note.validators.AppendNoteValidator;
import be.chaixdev.streamarksbackend.service.NoteService;
import com.cloudant.client.api.model.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static be.chaixdev.streamarksbackend.rest.common.ApiResponse.*;

@RestController
@RequestMapping(path="topic/{topicId}/note")
public class NoteController {

    private NoteService service;

    public NoteController(NoteService service) {

        this.service = service;
    }

    /* CREATE */
    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity append(@PathVariable String topicId,@RequestParam String rev,@RequestBody Note note) throws IOException {

        List<ValidationError> errors = new AppendNoteValidator().validate(note);

        if(!errors.isEmpty()){
            return errorResponseEntity(HttpStatus.BAD_REQUEST, errors);
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(
                        HttpHeaders.LOCATION,
                        String.format("/topic/%s/note/%s",topicId,note.getId()))
                .body(service.append(topicId, note));
    }

    /* READ all */
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Note>> getAll(@PathVariable String topicId) throws IOException {
        return ResponseEntity.ok(service.getNotesForTopic(topicId));
    }

    /* UPDATE one*/
    @PutMapping(value = "/{noteId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Note> update(@PathVariable String topicId, @PathVariable String noteId, @RequestBody Note note) throws IOException {
        List<ValidationError> errors = new ArrayList<>();
        if(!noteId.equals(note.getId())){
            errors.add(new ValidationError("id", "The note id in request path and id in request body don't match."));
        }

        if(!errors.isEmpty()){
            return errorResponseEntity(HttpStatus.BAD_REQUEST, errors);
        }

        return ResponseEntity.ok(service.update(topicId, note));

    }

    /* DELETE one */
    @DeleteMapping(value = "/{noteId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteNote(@PathVariable String topicId, @PathVariable String noteId) throws IOException {
        service.delete(topicId, noteId);
        return ResponseEntity.ok().build();
    }
}
