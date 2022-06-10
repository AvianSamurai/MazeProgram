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

        // Convert image to base64
        try {
            ImageIO.write((RenderedImage) bufferedImage, BASE_64_CONVERSION_TYPE, byteStream);
            imageStr = Base64.getEncoder().encodeToString(byteStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // Create element to represent the image and add the base64 data to it
        JsonObject element = new JsonObject();
        element.addProperty("imdata", imageStr);
        return element;
    }

    @Override
    public Image deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if(jsonElement.getAsJsonObject().has("imdata")) {
            // Get the base64 image data
            String imageStr = jsonElement.getAsJsonObject().get("imdata").getAsString();
            // Decode it to bytes
            byte[] decodedBytes = Base64.getDecoder().decode(imageStr);
            try {
                // Try to read an image from the decoded bytes
                Image buff = (Image) ImageIO.read(new ByteArrayInputStream(decodedBytes));
                return buff;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
