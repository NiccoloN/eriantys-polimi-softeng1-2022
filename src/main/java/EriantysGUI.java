import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class EriantysGUI extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Eriantys");

        StackPane stackPane = new StackPane();
        Scene scene = new Scene(stackPane, 800, 600);

        stage.setScene(scene);
        stage.show();
    }
}
