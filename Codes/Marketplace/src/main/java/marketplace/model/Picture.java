package marketplace.model;

import java.io.File;
import java.nio.file.Files;

public class Picture {
    private int id;
    private byte[] data;

    //if it comes from the client
    public Picture(File pictureFile){
        try {
            this.data = Files.readAllBytes(pictureFile.toPath());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //if it's read from the database
    public Picture(int id, byte[] data){
        this.id = id;
       this.data = data;
    }
}
