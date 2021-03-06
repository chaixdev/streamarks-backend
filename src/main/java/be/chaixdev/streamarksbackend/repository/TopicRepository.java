package be.chaixdev.streamarksbackend.repository;

import be.chaixdev.streamarksbackend.rest.common.NotFoundException;
import be.chaixdev.streamarksbackend.utils.UUIDUtil;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.Document;
import com.cloudant.client.api.model.Response;
import com.cloudant.client.api.views.*;
import com.cloudant.client.org.lightcouch.DocumentConflictException;
import com.cloudant.client.org.lightcouch.NoDocumentException;
import org.springframework.stereotype.Repository;
import be.chaixdev.streamarksbackend.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static be.chaixdev.streamarksbackend.utils.UUIDUtil.shortUUID;
import static be.chaixdev.streamarksbackend.utils.Utils.getNow;

@Repository
public class TopicRepository{

    private final Database db;

    public TopicRepository(Database db) {

        this.db = db;
    }

    // crud to couch
    public List<Topic> getAllTopics() throws IOException {
        ViewRequest<String, String> viewrequest = db.getViewRequestBuilder("Topic", "byUser")
                .newRequest(Key.Type.STRING, String.class)
                .includeDocs(true).build();

        ViewResponse<String, String> response = viewrequest.getResponse();

        return response.getRows().stream().map(row-> row.getDocumentAsType(Topic.class)).collect(Collectors.toList());

    }

    public Topic getTopic(String id){
        try{
            return db.find(Topic.class, id);
        } catch(NoDocumentException nde){
            throw new NotFoundException(String.format("Topic %s not found", id));
        }
    }

    public Topic save(Topic topic){
        topic.setId(shortUUID());
        topic.setDateCreated(getNow());
        topic.setDateModified(getNow());

        if(topic.getNotes()==null){
            topic.setNotes(new ArrayList<>());
        }

        for(Note note:topic.getNotes()){
            if(note.getId()==null){
                note.setId(UUIDUtil.shortUUID());
            }
        }

        db.save(topic);
        return getTopic(topic.getId());
    }

    public Topic update(Topic topic){
        try{
            topic.setDateModified(getNow());
            db.update(topic);
        }catch (DocumentConflictException dce){
            topic.setRevision(getLatestRevisionForDocument(topic.getId()));
            update(topic);
        }
        return getTopic(topic.getId());
    }
    
    public Response delete(String id, String rev){
        Response response;
        try{
            response = db.remove(id, rev);

        }
        catch (DocumentConflictException dce){
            String latestRev = getLatestRevisionForDocument(id);
            response = delete(id, latestRev);
        }

        return response;
    }

    private String getLatestRevisionForDocument(String id){
        try{
            return db.find(Document.class, id).getRevision();
        } catch(NoDocumentException nde){
            throw new NotFoundException(String.format("Document %s not found", id));
        }
    }
}
