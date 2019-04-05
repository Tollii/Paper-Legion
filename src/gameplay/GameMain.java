//  ██████╗ ██╗███╗   ██╗ █████╗ ██████╗ ██╗   ██╗    ██╗    ██╗ █████╗ ██████╗ ███████╗ █████╗ ██████╗ ███████╗  //
//  ██╔══██╗██║████╗  ██║██╔══██╗██╔══██╗╚██╗ ██╔╝    ██║    ██║██╔══██╗██╔══██╗██╔════╝██╔══██╗██╔══██╗██╔════╝  //
//  ██████╔╝██║██╔██╗ ██║███████║██████╔╝ ╚████╔╝     ██║ █╗ ██║███████║██████╔╝█████╗  ███████║██████╔╝█████╗    //
//  ██╔══██╗██║██║╚██╗██║██╔══██║██╔══██╗  ╚██╔╝      ██║███╗██║██╔══██║██╔══██╗██╔══╝  ██╔══██║██╔══██╗██╔══╝    //
//  ██████╔╝██║██║ ╚████║██║  ██║██║  ██║   ██║       ╚███╔███╔╝██║  ██║██║  ██║██║     ██║  ██║██║  ██║███████╗  //
//  ╚═════╝ ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝        ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝     ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝  //
//                                                                                                                //

//   ######## ########    ###    ##     ##       ##   ########     //
//      ##    ##         ## ##   ###   ###     ####   ##    ##     //
//      ##    ##        ##   ##  #### ####       ##       ##       //
//      ##    ######   ##     ## ## ### ##       ##      ##        //
//      ##    ##       ######### ##     ##       ##     ##         //
//      ##    ##       ##     ## ##     ##       ##     ##         //
//      ##    ######## ##     ## ##     ##     ######   ##         //


package gameplay;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import menus.Main;

import static database.Variables.*;

public class GameMain extends Application {

    GameLogic game;
    private Pane root;
    private final double windowWidth = screenWidth;
    private final double windowHeight = screenHeight;
    private String gameTitle = "PAPER LEGION";

    public void start(Stage window) throws Exception {
        // Sets static variables for players and opponent id.

        db.getPlayers();

        SetUp setUp = new SetUp();
        setUp.importUnitTypes();

        root = new Pane();
        Scene scene = new Scene(root, windowWidth, windowHeight);

        game = new GameLogic();

        //Pane gridPane = game.createGrid(); //creates the grid

        game.addObstacles();

        game.placementPhaseStart(); //starts the placement phase

        window.setTitle(gameTitle);
        window.setScene(scene);
        window.show();
    }

    ////SHUTDOWN METHODS////
    @Override
    public void stop() {
        // Executed when the application shuts down. User is logged out and database connection is closed.
        game.surrender();
        Main.closeAndLogout();
    }

    ////MAIN USED FOR SHUTDOWN HOOK////
    public void main(String[] args) {
        System.out.println("SHUTDOWN HOOK CALLED");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Main.closeAndLogout();
        }));
        launch(args);
    }
}