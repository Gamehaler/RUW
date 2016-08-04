package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Paths;

public class Main extends Application implements EventHandler<ActionEvent> {

    private Button downloadRadnomImageBtn, setAsWallpaperBtn, generateURLBtn;
    private Text downloadStatusTxt, noPreviewText, xText, urlText;
    private TextField resWidthText, resHeightText;
    private ImageView previewImg;
    private ChoiceBox<String> selectCategoryCB;
    private String basicUrl = "https://source.unsplash.com/";
    private String category;
    private String resolution;
    private String imageUrl;
    private String destinationFile = Paths.get(".").toAbsolutePath().normalize().toString() + "\\image.jpg";
    private Downloader controller;
    private Thread t;

    File imageFile = new File(destinationFile);
    Image image = new Image(imageFile.toURI().toString());

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void init() throws Exception {
        super.init();
        controller = new Downloader(null, destinationFile, this);
        t = new Thread();
    }

    /* --- Setting up the Stage --- */
    @Override
    public void start(Stage primaryStage) throws Exception {
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
            t = new Thread(controller);
            t.start();
            downloadStatusTxt.setText("Downloading...");
            downloadRadnomImageBtn.setDisable(true);
        } else if (event.getSource() == setAsWallpaperBtn) {
            Controller.setAsWallpaper(destinationFile);
        } else if (event.getSource() == generateURLBtn) {
            resolution = resWidthText.getText() + "x" + resHeightText.getText();

            if (selectCategoryCB.getValue().equals("Random")) {
                category = selectCategoryCB.getValue().toLowerCase() + "/";
            } else {
                category = "category/" + selectCategoryCB.getValue().toLowerCase() + "/";
            }

            imageUrl = basicUrl + category + resolution;
            controller.setImageUrl(imageUrl);
            urlText.setText(imageUrl);
            downloadRadnomImageBtn.setDisable(false);
        }
    }

    /* --- Called when image is downloaded --- */
    public void onImageDownloaded() {
        downloadStatusTxt.setText("Done!");
        Image image2 = new Image(imageFile.toURI().toString());
        previewImg.setImage(image2);
        noPreviewText.setText("");
        downloadRadnomImageBtn.setDisable(false);
    }

    /* --- Application layout --- */
    public GridPane addGridPane() {

        // Setting up GridPane
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(16, 16, 16, 16));
        grid.setVgap(8);
        grid.setHgap(10);

        // Setting up GridPane items

        /* ======================== ROW 1 SETUP ======================== */
        resWidthText = new TextField("1920");
        resWidthText.setPrefWidth(125);
        xText = new Text(" x ");
        xText.setTextAlignment(TextAlignment.CENTER);
        xText.setWrappingWidth(125);
        resHeightText = new TextField("1080");
        resHeightText.setPrefWidth(125);

        selectCategoryCB = new ChoiceBox<>();
        selectCategoryCB.getItems().add("Random");
        selectCategoryCB.getItems().add("Food");
        selectCategoryCB.getItems().add("Nature");
        selectCategoryCB.getItems().add("People");
        selectCategoryCB.getItems().add("Technology");
        selectCategoryCB.getItems().add("Objects");
        selectCategoryCB.setValue("Random");

        // Width text columns 1 , row 1
        GridPane.setConstraints(resWidthText, 0, 0);
        // x text column 2, row 1
        GridPane.setConstraints(xText, 1, 0);
        // Height text column 3, row 1
        GridPane.setConstraints(resHeightText, 2, 0);
        // Category selector column 4, row 1
        GridPane.setConstraints(selectCategoryCB, 3, 0);
        /* ======================== ROW 1 SETUP ======================== */


        /* ======================== ROW 2 SETUP ======================== */
        generateURLBtn = new Button("Generate URL");
        generateURLBtn.setOnAction(this);
        urlText = new Text("");

        // Generate URL button on column 1, row 2
        GridPane.setConstraints(generateURLBtn, 0, 1);
        // Generate URL text on column 2, row 2, colspan 3, rowspan 1
        GridPane.setConstraints(urlText, 1, 1, 3, 1);
        /* ======================== ROW 2 SETUP ======================== */


        /* ======================== ROW 3 SETUP ======================== */
        downloadRadnomImageBtn = new Button("Download image");
        downloadRadnomImageBtn.setOnAction(this);
        downloadRadnomImageBtn.setDisable(true);
        downloadStatusTxt = new Text("Status");

        // Download button on column 1, row 3
        GridPane.setConstraints(downloadRadnomImageBtn, 0, 2);
        // Status text on column 2, row 3
        GridPane.setConstraints(downloadStatusTxt, 1, 2);
        /* ======================== ROW 3 SETUP ======================== */


        /* ======================== ROW 4 SETUP ======================== */
        setAsWallpaperBtn = new Button("Set as wallpaper");
        setAsWallpaperBtn.setPrefWidth(500);
        setAsWallpaperBtn.setOnAction(this);

        // Set as wallpaper button on column 1, row 4, colspan 4, rowspan 1
        GridPane.setConstraints(setAsWallpaperBtn, 0, 3, 4, 1);
        /* ======================== ROW 4 SETUP ======================== */


        /* ======================== ROW 5 SETUP ======================== */
        // Resizes the image to have the width of 500 while preserving the ratio and using
        // higher quality filtering method; this ImageView is also cached to
        // improve performance
        previewImg = new ImageView();
        previewImg.setFitWidth(500);
        previewImg.setPreserveRatio(true);
        previewImg.setSmooth(true);
        previewImg.setCache(true);

        // Preview image on column 1, row 5, colspan 4, rowspan 1
        GridPane.setConstraints(previewImg, 0, 4, 4, 1);
        /* ======================== ROW 5 SETUP ======================== */

        if (imageFile.exists()) {

            previewImg.setImage(image);
            noPreviewText = new Text("");
            GridPane.setConstraints(noPreviewText, 0, 5, 4, 1);
            grid.getChildren().addAll(downloadRadnomImageBtn, downloadStatusTxt, setAsWallpaperBtn, previewImg,
                    noPreviewText, resWidthText, xText, resHeightText, generateURLBtn, selectCategoryCB, urlText);
        } else {
            noPreviewText = new Text("No preview available\nPlease download the image first");
            GridPane.setConstraints(noPreviewText, 0, 5, 4, 1);
            grid.getChildren().addAll(downloadRadnomImageBtn, downloadStatusTxt, setAsWallpaperBtn, previewImg,
                    noPreviewText, resWidthText, xText, resHeightText, generateURLBtn, selectCategoryCB, urlText);
        }

        return grid;
    }

    @Override
    public void stop() throws Exception {
        try {
            t.join();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        super.stop();
    }
}

/*
Customized image download URLs

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
