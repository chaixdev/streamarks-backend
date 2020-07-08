package be.chaixdev.streamarksbackend.repository;

import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.Document;
import com.cloudant.client.api.model.Response;
import com.cloudant.client.api.views.*;
import org.springframework.stereotype.Repository;
import be.chaixdev.streamarksbackend.model.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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

    public void save(Topic topic){
        db.save(topic);
    }

    public Topic update(Topic topic){
        db.update(topic);
        return getTopic(topic.getId());
    }
    
    public Response delete(String id, String rev){
        return db.remove(id, rev);
        
    }
}
