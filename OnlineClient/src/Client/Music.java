package Client;


import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Music implements Runnable {
    Player player = null;

    @Override
    public void run() {

        File file = new File("123.mp3");


        try {
            player = new Player(new FileInputStream(file));
        } catch (FileNotFoundException | JavaLayerException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(1);
            player.play();
            System.out.println(2);
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(123);
        player.close();

    }


}
