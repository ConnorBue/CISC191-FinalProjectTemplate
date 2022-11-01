package edu.sdccd.cisc191.template;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.ArrayList;


/**Connor Bue
 * September 30, 2022,
 * Professor Andrew Huang
 * -
 * This program runs a game called Treasure Hunt
 * The goal is to find all the treasure in the 5x5 plot of spaces.
 * Once all treasure is found a victory message is displayed with
 * the players score and the player can start a new game. After
 * each game the profile name is saved with the score. The
 * leaderboard displays the scores from highest to lowest.
 **/



public class Server extends Application{

    private InfoCenter infoCenter;
    private TileBoard tileBoard;
    private Leaderboard leaderboard;

    @Override
    public void start(Stage primaryStage)
    {
        try {
            BorderPane root = new BorderPane();
            Scene scene = new Scene(root, UIDimensions.APP_WIDTH, UIDimensions.APP_HEIGHT);
            initLayout(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    //initializes both portions of the gui
    private void initLayout(BorderPane root)
    {
        initInfoCenter(root);
        initTileBoard(root);

    }

    //initialize info center portion of panel
    private void initInfoCenter(BorderPane root)
    {
        leaderboard = new Leaderboard();
        infoCenter = new InfoCenter();
        infoCenter.setStartButtonOnAction(profileEntered());
        infoCenter.additionalGameButton(startAnotherGame());
        infoCenter.openLeaderboardAction(openLeaderboard());
        root.getChildren().add(infoCenter.getStackPane());
    }




    //first time the game starts, asks for profile name and once it gets one it starts the game
    private EventHandler<ActionEvent> profileEntered()
    {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String profileName = infoCenter.getProfileField();
                tileBoard.setUserName(profileName);

                infoCenter.updateTitle("Find the treasure!\n Best score possible: 80");
                tileBoard.addAllTiles();
                infoCenter.hideProfileGrid();
                infoCenter.hideLeaderboardButton();
            }
        };
    }

    //starts the leaderboard button, opens the leaderboard when pressed
    //also sorts the profiles text file to show the highest scores at the top
    private EventHandler<ActionEvent> openLeaderboard() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                ArrayList<ProfileData> profileListUnsorted;
                try {
                    profileListUnsorted = leaderboard.readAllProfiles();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }

                leaderboard.displayLeaderboard(leaderboard.sortProfile(profileListUnsorted));

            }
        };
    }



    //starts the button to start a new game after one has been finished
    private EventHandler<ActionEvent> startAnotherGame() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tileBoard.restartGame();

            }
        };
    }


    //initialize the game board portion of the gui
    private void initTileBoard(BorderPane root)
    {
        tileBoard = new TileBoard(infoCenter);
        root.getChildren().add(tileBoard.getStackPane());
    }

    //fallback launch
    public static void main(String[] args) {
            launch(args);
        }
    }
    //end class Server
