package classes.NoControllers;

import com.google.gson.JsonObject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.UUID;

/**
 * Created by User on 15.06.2017.
 */
public class Block {
    public String name;
    public Image texture;

    public Block(String name) {
        GameRulez gr = GameRulez.get(true);
        this.name = gr.getBlockz().get(name).name;
        this.texture = gr.getBlockz().get(name).texture;
    }

    public Block(JsonObject block) {
        this.name = block.get("name").getAsString();
        this.texture = new Image(getClass().getResource("/textures/" +  block.get("texture").getAsString() + ".png").toString(), 122, 122, true, true);
    }

    public Block() {
    }
}
