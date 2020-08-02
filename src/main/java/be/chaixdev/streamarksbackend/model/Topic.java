package be.chaixdev.streamarksbackend.model;

import com.cloudant.client.api.model.Document;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class Topic extends Document {

    private final String type = "Topic";
    private Subject subject;

    private List<String> tag;
    private List<Note> notes;

    private String dateCreated;
    private String dateModified;

//    private List<String> folderPath;

    public void addNote(Note note){
        this.getNotes().add(note);
    }

}
