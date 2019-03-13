package dragAndDrop;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;

public class dragAndDrop extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Stage window = primaryStage;
        Scene scene1;

        StackPane sp = new StackPane();
        Rectangle tile = new Rectangle(100,100);
        tile.setStrokeType(StrokeType.OUTSIDE);
        tile.setStroke(Color.BLACK);
        tile.setFill(Color.WHITE);

        sp.getChildren().add(tile);

        tile.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            double rectPosX = tile.getLayoutX()+ tile.getWidth()/2;
            double rectPosY = tile.getLayoutY() + tile.getHeight()/2;
            double posX = event.getSceneX();
            double posY =event.getSceneY();


            tile.setTranslateX(posX-rectPosX);
            tile.setTranslateY(posY-rectPosY);


        });

        scene1 = new Scene(sp, 500,400);

        window.setTitle("Hello World");
        window.setScene(scene1);
        window.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
