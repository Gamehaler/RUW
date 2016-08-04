package sample;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;

/**
 * Created by markosojat1@gmail.com on 4.8.2016..
 */
public class Controller {
    public static void setAsWallpaper(String destinationFile) {
        User32.INSTANCE.SystemParametersInfo(0x0014, 0, destinationFile , 1);
    }

    public interface User32 extends Library {
        User32 INSTANCE = (User32) Native.loadLibrary("user32",User32.class, W32APIOptions.DEFAULT_OPTIONS);
        boolean SystemParametersInfo (int one, int two, String s ,int three);
    }
}