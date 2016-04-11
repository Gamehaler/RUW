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
public class Controller {

    // TODO: downloading image in a background thread

    /* --- Downloading image --- */
    public static void downloadImage(String imageUrl, String destinationFile) throws IOException {

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

    public static void setAsWallpaper(String destinationFile) {
        User32.INSTANCE.SystemParametersInfo(0x0014, 0, destinationFile , 1);
    }

    public interface User32 extends Library {
        User32 INSTANCE = (User32) Native.loadLibrary("user32",User32.class, W32APIOptions.DEFAULT_OPTIONS);
        boolean SystemParametersInfo (int one, int two, String s ,int three);
    }

}
