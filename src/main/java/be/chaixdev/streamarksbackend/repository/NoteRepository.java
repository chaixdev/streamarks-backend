package be.chaixdev.streamarksbackend.repository;

import be.chaixdev.streamarksbackend.model.Note;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.Document;
import com.cloudant.client.api.model.Response;
import com.cloudant.client.api.views.Key;
import com.cloudant.client.api.views.ViewRequest;
import com.cloudant.client.api.views.ViewResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class NoteRepository {

    private Database db;
    private TopicRepository topicRepository;

    public NoteRepository(Database db, TopicRepository topicRepository) {

        this.db = db;
        this.topicRepository = topicRepository;
    }

    // crud to couch
    public List<Note> getAllNotes(String topicId) throws IOException {
        return topicRepository.getTopic(topicId).getNotes();
    }

    public List<Note> append(String topicId, Note note) throws IOException {
        topicRepository.getTopic(topicId).getNotes().add(note);
        db.save(note);
        return getAllNotes(topicId);
    }

    public Note update(String topicId, Note note){
        db.update(note);
        return getNote(note.getId());
    }

    public Response delete(String id, String rev){
        return db.remove(id, rev);

    }
}
