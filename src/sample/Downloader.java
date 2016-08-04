package sample;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Created by domin on 2/9/2016.
 */
public class Downloader implements Runnable {
    private String imageUrl = null;
    private String destinationFile = null;
    private Main main;

    public Downloader(String imageUrl, String destinationFile, Main main) {
        this.imageUrl = imageUrl;
        this.destinationFile = destinationFile;
        this.main = main;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDestinationFile() {
        return destinationFile;
    }

    public void setDestinationFile(String destinationFile) {
        this.destinationFile = destinationFile;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }


    /* --- Downloads and saves image --- */
    @Override
    public void run() {
        if (main != null && destinationFile != null && imageUrl != null) {
            try {
                downloadImage();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                main.onImageDownloaded();
            }
        }
    }

    /* --- Downloading image --- */
    public void downloadImage() throws IOException {

        InputStream is = null;
        OutputStream os = null;

        try {
            URL url = new URL(imageUrl);
            is = url.openStream();
            os = new FileOutputStream(destinationFile);

            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            is.close();
            os.close();
        }
    }

}
