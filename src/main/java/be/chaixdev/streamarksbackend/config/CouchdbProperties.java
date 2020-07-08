package be.chaixdev.streamarksbackend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "couchdb")
@Getter
@Setter
public class CouchdbProperties {
    private String url;
    private String username;
    private String password;
    private String dbname;

}
