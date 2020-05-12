package marketplace.client;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Picture {
    private int id;
    private byte[] data;

    //must have because of seralization
    public Picture() {
    }

    //if it comes from the client
    public Picture(byte[] picturedata){
//        try {
//            this.data = Files.readAllBytes(pictureFile.toPath());
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        this.data = picturedata;
    }
    //if it's read from the database
    public Picture(int id, byte[] data){
        this.id = id;
        this.data = data;
    }

    public Picture(Image image) {
        try {
            BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bImage, "jpg", outputStream);
            byte[] res = outputStream.toByteArray();
            this.data = res;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getData() {
        return data;
    }

    public int getId() {
        return id;
    }

    public Image asImage() {
        InputStream inputStream = new ByteArrayInputStream(data);
        return new Image(inputStream);
    }
}
