package Program;

import com.google.gson.*;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Base64;

public class BufferedImageGsonSerializer<Image> implements JsonSerializer<Image>, JsonDeserializer<Image> {

    public static final String BASE_64_CONVERSION_TYPE = "png";

    @Override
    public JsonElement serialize(Image bufferedImage, Type type, JsonSerializationContext jsonSerializationContext) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        String imageStr = "";
        try {
            ImageIO.write((RenderedImage) bufferedImage, BASE_64_CONVERSION_TYPE, byteStream);
            imageStr = Base64.getEncoder().encodeToString(byteStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        JsonObject element = new JsonObject();
        element.addProperty("imdata", imageStr);
        return element;
    }

    @Override
    public Image deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if(jsonElement.getAsJsonObject().has("imdata")) {
            String imageStr = jsonElement.getAsJsonObject().get("imdata").getAsString();
            byte[] decodedBytes = Base64.getDecoder().decode(imageStr);
            try {
                Image buff = (Image) ImageIO.read(new ByteArrayInputStream(decodedBytes));
                return buff;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
