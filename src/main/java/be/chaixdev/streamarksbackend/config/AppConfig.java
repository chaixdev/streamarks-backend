package be.chaixdev.streamarksbackend.config;

import be.chaixdev.streamarksbackend.json.Json;
import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.org.lightcouch.Document;
import com.cloudant.client.org.lightcouch.NoDocumentException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static springfox.documentation.builders.PathSelectors.any;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
@EnableConfigurationProperties(CouchdbProperties.class)
public class AppConfig {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("be.chaixdev.streamarksbackend")).build();
    }

    @Bean
    public CloudantClient client(CouchdbProperties properties) throws MalformedURLException {
         return ClientBuilder.url(new URL(properties.getUrl()))
              .username(properties.getUsername())
              .password(properties.getPassword())
              .build();
    }

    @Bean
    public Database database(CloudantClient client, CouchdbProperties properties) throws IOException {
        List<String> allDbs = client.getAllDbs();

        Database database = client.database(properties.getDbname(), true);
        verifyDesignDocs(database);


        return database;
    }

    private void verifyDesignDocs(Database db) throws IOException {

        Map design = Json.decode(new FileInputStream("./src/main/resources/design_topic.json"), Map.class);

        try{
            Map fromCouch = db.find(Map.class, "_design/Topic");
        } catch (NoDocumentException nde){
            db.save(design);
        }
    }

    public static String readFile(String fileName) throws IOException {
        StringBuilder json = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine();
            while (line != null) {
                json.append(line).append("\n");
                line = reader.readLine();
            }
        }
        return json.toString();
    }



}
