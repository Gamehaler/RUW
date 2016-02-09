package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class Main extends Application implements EventHandler<ActionEvent> {

    private Button downloadRadnomImageBtn, setAsWallpaperBtn;
    private Text downloadStatusTxt, noPreviewText;
    private ImageView previewImg;
    private String imageUrl = "https://source.unsplash.com/random/1920x1080";
    private String destinationFile = Paths.get(".").toAbsolutePath().normalize().toString() + "\\image.png";

    File imageFile = new File(destinationFile);
    Image image = new Image(imageFile.toURI().toString());

    /* --- Main method --- */
    public static void main(String[] args){
        launch(args);
    }

    /* --- Setting up the Stage --- */
    @Override
    public void start(Stage primaryStage) throws Exception{
        // Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("RUW - Random Unsplash Wallpaper");

        Scene scene = new Scene(addGridPane(), 532, 500);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /* --- Handling button click events --- */
    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == downloadRadnomImageBtn) {
            try {
                downloadStatusTxt.setText("Downloading, please wait...");
                Controller.downloadImage(imageUrl, destinationFile);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                downloadStatusTxt.setText("Done!");
                Image image2 = new Image(imageFile.toURI().toString());
                previewImg.setImage(image2);
                noPreviewText.setText("");
            }
        } else if (event.getSource() == setAsWallpaperBtn) {
            Controller.setAsWallpaper(destinationFile);
        }
    }

    /* --- Application layout --- */
    public GridPane addGridPane() {

        // Setting up GridPane
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(16, 16, 16, 16));
        grid.setVgap(8);
        grid.setHgap(10);

        // Setting up GridPane items
        downloadRadnomImageBtn = new Button("Download image");
        downloadRadnomImageBtn.setOnAction(this);
        setAsWallpaperBtn = new Button("Set as wallpaper");
        setAsWallpaperBtn.setOnAction(this);
        setAsWallpaperBtn.setPrefWidth(500);
        downloadStatusTxt = new Text("Status");

        // Download button on column 1, row 1
        GridPane.setConstraints(downloadRadnomImageBtn, 0, 0);

        // Status text on column 2, row 1
        GridPane.setConstraints(downloadStatusTxt, 1, 0);

        // Set as wallpaper button on column 1, row 2
        GridPane.setConstraints(setAsWallpaperBtn, 0, 1, 3, 1);

        // resizes the image to have width of 500 while preserving the ratio and using
        // higher quality filtering method; this ImageView is also cached to
        // improve performance
        previewImg = new ImageView();

        previewImg.setFitWidth(500);
        previewImg.setPreserveRatio(true);
        previewImg.setSmooth(true);
        previewImg.setCache(true);

        // Preview image on columns 1 and 2, row 1
        GridPane.setConstraints(previewImg, 0, 2, 3, 1);

        if (imageFile.exists()) {

            previewImg.setImage(image);
            noPreviewText = new Text("");
            GridPane.setConstraints(noPreviewText, 0, 2, 3, 1);
            grid.getChildren().addAll(downloadRadnomImageBtn, downloadStatusTxt, setAsWallpaperBtn, previewImg, noPreviewText);
        } else {
            noPreviewText = new Text("No preview available\nPlease download the image first");
            GridPane.setConstraints(noPreviewText, 0, 2, 3, 1);
            grid.getChildren().addAll(downloadRadnomImageBtn, downloadStatusTxt, setAsWallpaperBtn, previewImg, noPreviewText);
        }

        return grid;
    }
}

/*
Customized image downloads

Completely random image:
https://source.unsplash.com/random/1920x1080

------------------------------------------

Image from a specified category:
https://source.unsplash.com/category/nature/1920x1080

Categories
 - buildings
 - food
 - nature
 - people
 - technology
 - objects

------------------------------------------

Images taken by a specified user:
https://source.unsplash.com/user/erondu/1920x1080

------------------------------------------

Images liked by a specified user:
https://source.unsplash.com/user/jackie/likes/1920x1080

 */
