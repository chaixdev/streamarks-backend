package be.chaixdev.streamarksbackend.service;

import be.chaixdev.streamarksbackend.model.Note;
import be.chaixdev.streamarksbackend.model.Topic;
import be.chaixdev.streamarksbackend.repository.TopicRepository;
import org.springframework.stereotype.Component;

import java.util.List;

import static be.chaixdev.streamarksbackend.utils.UUIDUtil.shortUUID;

@Component
public class NoteService {

    private TopicRepository repository;

    public NoteService(TopicRepository repository) {


        this.repository = repository;
    }

    public List<Note> getNotesForTopic(String topicId) {
        return repository.getTopic(topicId).getNotes();
    }

    public Note getNote(String topicId, String noteId) {

        return getNotesForTopic(topicId)
                .stream()
                .filter(note -> note.getId().equals(noteId)).findAny()
                .orElseThrow(() -> new RuntimeException(String.format("note with id %s not found on topic %s", noteId, topicId)));
    }

    public Note append(String topicId, Note note) {
        Topic topic = repository.getTopic(topicId);
        note.setId(shortUUID());
        topic.getNotes().add(note);
        repository.save(topic);
        return getNote(topicId, note.getId());
    }


    public Note update(String topicId, Note note) {

        Topic topic = repository.getTopic(topicId);
        List<Note> notesForTopic = topic.getNotes();
        Note old = getNote(topicId, note.getId());
        notesForTopic.remove(old);
        notesForTopic.add(note);

        repository.update(topic);
        return getNote(topicId,note.getId());


    }

    public void delete(String topicId, String noteId) {
        Topic topic = repository.getTopic(topicId);
        topic.getNotes().remove(getNote(topicId, noteId));

        repository.update(topic);

    }
}
