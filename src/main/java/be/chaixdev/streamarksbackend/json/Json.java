package be.chaixdev.streamarksbackend.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

/**
 * helper class for working with json directly
 **/
public class Json {

    public static final ObjectMapper jackson;

    static {

        jackson = new ObjectMapper();
        instrumentObjectMapper(jackson);
    }

    private Json() {
        // dont allow instance
    }

    public static void instrumentObjectMapper(ObjectMapper objectMapper) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new SimpleModule()
                .addSerializer(JsonObject.class, new JsonObjectSerializer())
                .addDeserializer(JsonObject.class, new JsonObjectDeserializer())
                .addDeserializer(JsonArray.class, new JsonArrayDeserializer())
                .addSerializer(JsonArray.class, new JsonArraySerializer())
                .addSerializer(byte[].class, new ByteArraySerializer())
        );
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.disable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
        objectMapper.disable(MapperFeature.USE_GETTERS_AS_SETTERS);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
    }

    public static String encode(Object object) {
        try {
            return jackson.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonEncodeException(e);
        }
    }

    public static <T> T decode(InputStream json, Class<T> clazz) {
        try {
            return jackson.readValue(json, clazz);
        } catch (IOException e) {
            throw new JsonDecodeException(e);
        }
    }

    public static <T> T decode(String json, TypeReference<T> typeRef) {
        try {
            return jackson.readValue(json, typeRef);
        } catch (IOException e) {
            throw new JsonDecodeException(e);
        }
    }

    public static <T> T decode(String json, Class<T> clazz) {
        try {
            return jackson.readValue(json, clazz);
        } catch (IOException e) {
            throw new JsonDecodeException(e);
        }
    }

    static Object checkAndCopy(Object val, boolean copy) {
        if (val == null) {
            // OK
        } else if (val instanceof Number && !(val instanceof BigDecimal)) {
            // OK
        } else if (val instanceof Boolean) {
            // OK
        } else if (val instanceof String) {
            // OK
        } else if (val instanceof Character) {
            // OK
        } else if (val instanceof CharSequence) {
            val = val.toString();
        } else if (val instanceof JsonObject) {
            if (copy) {
                val = ((JsonObject) val).copy();
            }
        } else if (val instanceof JsonArray) {
            if (copy) {
                val = ((JsonArray) val).copy();
            }
        } else if (val instanceof Map) {
            if (copy) {
                val = (new JsonObject((Map) val)).copy();
            } else {
                val = new JsonObject((Map) val);
            }
        } else if (val instanceof List) {
            if (copy) {
                val = (new JsonArray((List) val)).copy();
            } else {
                val = new JsonArray((List) val);
            }
        } else if (val instanceof byte[]) {
            val = Base64.getEncoder().encodeToString((byte[]) val);
        } else if (val instanceof Instant) {
            val = ISO_INSTANT.format((Instant) val);
        } else {
            throw new IllegalStateException("Illegal type in JsonObject: " + val.getClass());
        }
        return val;
    }

    static <T> Stream<T> asStream(Iterator<T> sourceIterator) {
        Iterable<T> iterable = () -> sourceIterator;
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static boolean isArray(Object obj) {
        return obj != null && obj.getClass().isArray() || obj instanceof Collection || obj instanceof JsonArray;
    }

    public static class JsonObjectDeserializer extends JsonDeserializer<JsonObject> {

        @Override
        public JsonObject deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
            return new JsonObject(jp.getCodec().readTree(jp).toString());
        }
    }

    public static class JsonArrayDeserializer extends JsonDeserializer<JsonArray> {

        @Override
        public JsonArray deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
            return new JsonArray(jp.getCodec().readTree(jp).toString());
        }
    }

    private static class JsonObjectSerializer extends JsonSerializer<JsonObject> {
        @Override
        public void serialize(JsonObject value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeObject(value.getMap());
        }
    }

    private static class JsonArraySerializer extends JsonSerializer<JsonArray> {
        @Override
        public void serialize(JsonArray value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeObject(value.getList());
        }
    }

    private static class ByteArraySerializer extends JsonSerializer<byte[]> {
        private final Base64.Encoder BASE64 = Base64.getEncoder();

        @Override
        public void serialize(byte[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeString(BASE64.encodeToString(value));
        }
    }

}
