package Program;

import Utils.Debug;
import com.google.gson.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Base64;

public class InterfaceGsonSerializer<I_Cell> implements JsonSerializer<I_Cell>, JsonDeserializer<I_Cell> {

    @Override
    public JsonElement serialize(I_Cell t, Type type, JsonSerializationContext jsonSerializationContext) {
        // Add a property to the serialised class containing what kind of class it is
        JsonObject jElement = jsonSerializationContext.serialize(t).getAsJsonObject();
        jElement.addProperty("type", t.getClass().toString());
        return jElement;
    }

    @Override
    public I_Cell deserialize(JsonElement elem, Type interfaceType, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jElement = elem.getAsJsonObject();

        // Read what kind of class the object reports itself to be
        if(jElement.has("type")) {
            switch (jElement.get("type").getAsString()) {
                case "class Program.BasicCell":
                    jElement.remove("type");
                    return context.deserialize(jElement, BasicCell.class);

                case "class Program.LogoCell": // Ensures image is deserialized as well
                    if(elem.getAsJsonObject().has("cellImage")) {
                        JsonObject imobject = elem.getAsJsonObject().get("cellImage").getAsJsonObject();
                        if(imobject.has("imdata")) {
                            jElement.remove("type");
                            LogoCell logoCell = context.deserialize(jElement, LogoCell.class);
                            return (I_Cell)logoCell;
                        }
                    }
                    return (I_Cell) (new BasicCell());

                case "class Program.ImageCell":// Ensures image is deserialized as well
                    if(elem.getAsJsonObject().has("cellImage")) {
                        JsonObject imobject = elem.getAsJsonObject().get("cellImage").getAsJsonObject();
                        if(imobject.has("imdata")) {
                            jElement.remove("type");
                            ImageCell imageCell = context.deserialize(jElement, ImageCell.class);
                            return (I_Cell)imageCell;
                        }
                    }
                    return (I_Cell) (new BasicCell());

                default:
                    return (I_Cell) (new BasicCell());
            }
        }
        return null;
    }
}