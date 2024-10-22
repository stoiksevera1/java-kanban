package handlers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;


public class DurationTimeAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
        if (duration == null) {
            if (!jsonWriter.getSerializeNulls()) {
                jsonWriter.setSerializeNulls(true);
                jsonWriter.nullValue();
                jsonWriter.setSerializeNulls(false);
            } else {
                jsonWriter.nullValue();
            }
        } else {
            jsonWriter.value(duration.toMinutes());
        }
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        return Duration.ofMinutes(Long.parseLong(jsonReader.nextString()));
    }
}
