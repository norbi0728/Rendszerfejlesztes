package marketplace.model;

import java.io.File;
import java.nio.file.Files;

public class Picture {
    private int id;
    private byte[] data;

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

    public byte[] getData() {
        return data;
    }
}
