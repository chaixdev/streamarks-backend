package be.chaixdev.streamarksbackend.model;

import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
public class User {

    private String email;
    private List<Topic> topics;
    private Set<String> userDefinedTags;
}
