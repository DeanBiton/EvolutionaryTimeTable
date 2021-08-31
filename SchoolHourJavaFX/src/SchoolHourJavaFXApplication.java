import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;

public class SchoolHourJavaFXApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        int windowMinHeight = 500;
        int windowMinWidth = 350;

        FXMLLoader fxmlLoader = getSceneFXMLLoader("main");
        Parent root = getFirstAppRoot(fxmlLoader);
        Scene scene = new Scene(root, Screen.getPrimary().getBounds().getWidth() / 2, Screen.getPrimary().getBounds().getHeight() / 2);

        MainController controller = fxmlLoader.getController();
        controller.setPrimaryStage(primaryStage);

        primaryStage.setTitle("Players Manager");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(windowMinHeight);
        primaryStage.setMinWidth(windowMinWidth);

        primaryStage.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                controller.initializeComponentsSizes();
            }
        });

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                controller.Stop(new ActionEvent());
            }
        });

        primaryStage.show();
    }

    private FXMLLoader getSceneFXMLLoader(String fxmlFileName) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(fxmlFileName + ".fxml");
        fxmlLoader.setLocation(url);
        return fxmlLoader;
    }

    private Parent getFirstAppRoot(FXMLLoader fxmlLoader) throws IOException {
        return (Parent) fxmlLoader.load(fxmlLoader.getLocation().openStream());
    }
}
