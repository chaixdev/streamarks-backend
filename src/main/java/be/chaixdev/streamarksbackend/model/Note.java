package be.chaixdev.streamarksbackend.model;

import lombok.Data;

@Data
public class Note {
    private String id;
    private String body;
    private long time;
}
