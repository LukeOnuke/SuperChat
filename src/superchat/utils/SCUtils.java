/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package superchat.utils;

import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import superchat.SuperChat;

/**
 *
 * @author lukak
 */
public abstract class SCUtils extends Application {

    public static String getChatTime() {

        String date = "";

        Date d = new Date();
        DateFormat df = new SimpleDateFormat("<kk:mm> ");
        date = df.format(d);
        return date;
    }
    
    public static String getDateCmd() {

        String date = "";

        Date d = new Date();
        DateFormat df = new SimpleDateFormat("dd / MM / yyyy");
        date = df.format(d);
        return date;
    }

    /**
     * Show the about window
     */
    public static void aboutShow() {
        Parent root = null; //new parrent
        try {
            root = FXMLLoader.load(superchat.gui.about.AboutController.class.getResource("About.fxml")); //read the fxml
        } catch (Exception ex) {
            Logger.getLogger(SCUtils.class.getName()).log(Level.SEVERE, null, ex); //if shit hits the fan
        }

        Scene scene = new Scene(root); //new scene with the parrent

        Stage stage = new Stage(); //new window
        stage.setScene(scene); //set the windows, scene

        stage.show(); //show the window

        //get the widths and heights
        double width = stage.getWidth();
        double height = stage.getHeight();

        //set the max widths and heights
        stage.setMaxHeight(height);
        stage.setMaxWidth(width);
        stage.setMinHeight(height);
        stage.setMinWidth(width);

        stage.getIcons().add(
                new Image(
                        SuperChat.class.getResourceAsStream("icon.png"))
        );
    }

}
