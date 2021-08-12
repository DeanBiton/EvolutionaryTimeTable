import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class SchoolHourJavaFXApplication extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = getFirstAppFXMLLoader();
        Parent root = getFirstAppRoot(fxmlLoader);
        Scene scene = new Scene(root,500, 400);

        Controller controller = fxmlLoader.getController();
        controller.setPrimaryStage(primaryStage);

        primaryStage.setTitle("Players Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private FXMLLoader getFirstAppFXMLLoader() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("firstAPP.fxml");
        fxmlLoader.setLocation(url);
        return fxmlLoader;
    }

    private Parent getFirstAppRoot(FXMLLoader fxmlLoader) throws IOException {
        return (Parent) fxmlLoader.load(fxmlLoader.getLocation().openStream());
    }
}
