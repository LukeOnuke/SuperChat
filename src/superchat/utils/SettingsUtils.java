/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package superchat.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import superchat.MainWindowController;
import superchat.SuperChat;

/**
 *
 * @author lukak
 */
public class SettingsUtils {

    /**
     * Shows the settings window and sets it up to work
     */
    public static void settingsShow() {
        Parent root = null; //new parrent
        try {
            root = FXMLLoader.load(superchat.gui.settings.SettingsController.class.getResource("Settings.fxml")); //read the fxml
        } catch (Exception ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex); //if shit hits the fan
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

    /**
     * Read settings and returns them in a String[]
     *
     * @return String[] that contains settings in order of 0: address, 1: port,
     * 2: username
     *
     */
    public static String[] settingsRead() {
        String address = null;
        String port = null;
        String username = null;

        try {
            File config = new File("config.properties");
            Properties prop = new Properties();
            if (!config.exists()) {
                try (OutputStream output = new FileOutputStream(config)) {
                    prop.setProperty("host.adress", "localhost");
                    prop.setProperty("host.port", "765");
                    prop.setProperty("username", "username");
                    prop.store(output, "SimpleChat configuration file" + System.lineSeparator() + "Created by: Pequla ( https://pequla.github.io/ ) and LukeOnuke (https://github.com/LukeOnuke)");
                }
            }
            try (InputStream input = new FileInputStream(config)) {
                prop.load(input);
                address = prop.getProperty("host.adress");
                port = prop.getProperty("host.port");
                username = prop.getProperty("username");
            }
        } catch (IOException ex) {

        }

        //package it up into a array and return it
        String[] returning = new String[3];
        returning[0] = address;
        returning[1] = port;
        returning[2] = username;

        return returning;

    }

    /**
     * Write the settings to the config.properties file
     *
     * @param settings String[] that needs to contain 0: address, 1: port, 2:
     * username
     */
    public static void settingsWrite(String[] settings) {
        File config = new File("config.properties");

        //config.delete();
        Properties prop = new Properties();

        try (OutputStream output = new FileOutputStream(config)) {
            prop.setProperty("host.adress", settings[0]);
            prop.setProperty("host.port", settings[1]);
            prop.setProperty("username", settings[2]);
            prop.store(output, "SimpleChat configuration file" + System.lineSeparator() + "Created by: Pequla ( https://pequla.github.io/ ) and LukeOnuke (https://github.com/LukeOnuke)");
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public static String[] getDefaultSettings() {
        String[] settings = new String[3];
        settings[0] = "localhost";
        settings[1] = "765";
        settings[2] = "username";

        return settings;
    }
}
