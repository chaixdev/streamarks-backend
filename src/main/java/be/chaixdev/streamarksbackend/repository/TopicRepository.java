package be.chaixdev.streamarksbackend.repository;

import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.Document;
import com.cloudant.client.api.model.Response;
import com.cloudant.client.api.views.*;
import com.cloudant.client.org.lightcouch.DocumentConflictException;
import org.springframework.stereotype.Repository;
import be.chaixdev.streamarksbackend.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static be.chaixdev.streamarksbackend.Utils.getNow;

@Repository
public class TopicRepository{

    private Database db;

    public TopicRepository(Database db) {

        this.db = db;
    }

    // crud to couch
    public List<Topic> getAllTopics() throws IOException {
        ViewRequest<String, String> viewrequest = db.getViewRequestBuilder("topic", "byUser")
                .newRequest(Key.Type.STRING, String.class)
                .includeDocs(true).build();

        ViewResponse<String, String> response = viewrequest.getResponse();
        List<Document> docs = response.getDocs();
        return response.getRows().stream().map(row-> row.getDocumentAsType(Topic.class)).collect(Collectors.toList());

    }

    public Topic getTopic(String id){
        return db.find(Topic.class, id);
    }

    public Topic save(Topic topic){

        topic.setDateCreated(getNow());
        topic.setDateModified(getNow());

        if(topic.getNotes()==null){
            topic.setNotes(new ArrayList<>());
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
        Document document = db.find(Document.class, id);
        return document.getRevision();
    }
}
