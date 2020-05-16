/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package superchat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 *
 * @author Luke_oNuke
 */
public class MainWindowController implements Initializable {

    //|----------------------------------------|
    //|singleton setup                         |
    //|Singleton because i need only 1 instance|
    //|----------------------------------------|
    private static MainWindowController instance = null;

    /*private MainWindowController()
    {
    
    }*/
    public static MainWindowController getInstance() {
        if (instance == null) {
            instance = new MainWindowController();
        }

        return instance;
    }

    //-----------------------------------------
    //setting the GUI controll
    /*
    *Part where we get the fxml variables
     */
    @FXML
    private Label label_;
    @FXML
    private TextField sendBox_;
    @FXML
    private TextFlow chatBox_;
    @FXML
    private Button button_;
    @FXML
    private AnchorPane ap_;

    private static Label label;
    private static TextField sendBox;
    private static TextFlow chatBox;
    private static Button button;
    private static AnchorPane ap;

    //Convert the stuff that FXML outputs into static variables
    private static void convertToStatic(Label l, TextField sb, TextFlow cb, Button but, AnchorPane ap_) {
        label = l;
        sendBox = sb;
        chatBox = cb;
        button = but;
        ap = ap_;
    }

    //Setting up global variables
    private static volatile String msgCandidate = "";
    private static final SuperChat sc = new SuperChat();

    @FXML
    /**
     * Runs when button is pressed , only used in fxml
     *
     */
    private void handleButtonAction(ActionEvent event) {
        System.out.println("Clicked send and sent: " + sendBox.getText());

        /*consoleWrite("MEME");
        consoleWriteColor("MAN", 0, 255, 0);*/
        msgCandidate = sendBox.getText();
        sendBox.setText("");
        System.out.println(msgCandidate);
    }

    @FXML
    /**
     * Runs when sendBox (the send message box) is triggered (typed in) , only
     * used in fxml
     *
     */
    private void sendBoxSendMsgMessage() {
        System.out.println("Triggered sendBox");
        String message = "Type messages here";

        if ("".equals(sendBox.getText())) {
            label.setText(message);
        } else {
            label.setText(" ");
        }

    }

    public static void saveFile() { //menu item interface for save and save as
        Window mainStage = ap.getScene().getWindow(); //get ze window

        FileChooser fileChooser = new FileChooser(); //Filechooser the class that has the file chooser
        fileChooser.setTitle("Save File"); //Title of prompt
        fileChooser.getExtensionFilters().addAll( //add filter
                new ExtensionFilter("Text File", "*.txt"), //Filters
                new ExtensionFilter("All Files", "*.*"));  //Filters
        File selectedFile = fileChooser.showSaveDialog(mainStage); //get the file
        if (selectedFile != null) { //if something is selected then
            File file; //new File
            System.out.println(selectedFile.getAbsoluteFile()); //debug
            file = selectedFile.getAbsoluteFile(); //set the File

            try {
                FileWriter Writer = new FileWriter(file); //Writer

                //get the text of the chatbox
                StringBuilder sb = new StringBuilder();
                for (Node node : chatBox.getChildren()) {
                    if (node instanceof Text) {
                        sb.append(((Text) node).getText());
                    }
                }
                String fullText = sb.toString();

                String[] chatLines = fullText.split("\n"); //split the string into lines

                for (String chatLine : chatLines) { //get a line and run the loop for every one
                    Writer.write(chatLine + "\n"); //write a line
                }

                Writer.close();
                System.out.println("Successfully wrote to the file");

            } catch (IOException e) {
                System.out.println("An error occurred");
                e.printStackTrace();
            }
        }
    }

    /**
     * Writes to console in black
     *
     * @param message The message written to chatBox
     */
    public static void consoleWrite(String message) {
        Text text = new Text(superchat.utils.SCUtils.getChatTime() + message + System.lineSeparator());

        text.setFill(Color.BLACK);

        chatBox.getChildren().add(text);
        //chatBox.setText(chatBox.getText() + System.lineSeparator() + message);
    }

    /**
     * Writes to console in color RGB
     *
     * @param message The message (text) written to chatBox
     * @param red red color component
     * @param green green color component
     * @param blue blue color component
     */
    public static void consoleWriteColor(String message, int red, int green, int blue) {
        Text text = new Text(message + System.lineSeparator());

        text.setFill(Color.rgb(red, green, blue));

        chatBox.getChildren().add(text);
        //chatBox.setText(chatBox.getText() + System.lineSeparator() + message);
    }

    /**
     * Writes to console in color RGB
     *
     * @param message The message (text) written to chatBox
     * @param red red color component
     * @param green green color component
     * @param blue blue color component
     * @param opacity the opacity of text
     */
    public static void consoleWriteColor(String message, int red, int green, int blue, double opacity) {
        Text text = new Text(message + System.lineSeparator());

        text.setFill(Color.rgb(red, green, blue, opacity));

        chatBox.getChildren().add(text);
        //chatBox.setText(chatBox.getText() + System.lineSeparator() + message);
    }

    //
    //--------------------|
    //Functions for client|
    //--------------------|
    /*
    * The part made by Petar Kresoja (Pequla)
    * Modified by Luke'oNuke to work with gui
    *
     */
    private static void consoleOut(String text) {
        MainWindowController.getInstance().consoleWrite(text);
    }

    private static void consoleCommandOut(String text) {
        consoleWriteColor(superchat.utils.SCUtils.getChatTime() + text + System.lineSeparator(), 60, 138, 0);
    }

    private static void errMessage(Exception ex) {

        MainWindowController.getInstance().consoleWriteColor("Error ocured", 255, 0, 0);
        MainWindowController.getInstance().consoleWriteColor("More info: " + ex.getMessage(), 255, 0, 0);

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            MainWindowController.getInstance().consoleWriteColor("Error ocured", 255, 0, 0);
            MainWindowController.getInstance().consoleWriteColor("More info: " + e.getMessage(), 255, 0, 0);
        }
        System.exit(0);
    }

    public static void initilised() {
        //create a listener for enter key pressed
        sendBox.setOnKeyReleased(event -> {
            String message = "Type messages here";

            if ("".equals(sendBox.getText())) {
                label.setText(message);
            } else {
                label.setText(" ");
            }

            if (event.getCode() == KeyCode.ENTER) {
                System.out.println("ENTER PRESSED");

                System.out.println("Clicked send and sent: " + sendBox.getText());

                /*consoleWrite("MEME");
                consoleWriteColor("MAN", 0, 255, 0);*/
                msgCandidate = sendBox.getText();
                sendBox.setText("");
                System.out.println(msgCandidate);
            }
        });
        //---------------------------------------

        Thread clientThread = new Thread(() -> {
            try {
                String address = null;
                String username = null;
                int port = 0;

                String[] properties = superchat.utils.SettingsUtils.settingsRead(); //read the settings and run
                address = properties[0];
                port = Integer.valueOf(properties[1]);
                username = properties[2];

                consoleOut("Connecting...");
                Socket s = new Socket(InetAddress.getByName(address), port);
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                dos.writeUTF(username);
                if (dis.readUTF().equals("NAME OK")) {

                    Scanner scn = new Scanner(System.in);

                    consoleOut("Connected to the server...");
                    consoleOut("Type in /logout to EXIT , /help for aditional commands");

                    Thread sendMessage = new Thread(() -> {
                        while (true) {
                            try {
                                String msg = "";
                                if (!"".equals(msgCandidate)) {
                                    msg = msgCandidate;
                                    msgCandidate = "";
                                }

                                if (msg.length() != 0) {

                                    /*
                                    * Command system that listens to commands and doesnt send them to the server
                                    * Works by detecting commands and not sending the packet
                                    * Uses Platform.runLater to run the commands outside the javafx thread because javaFX is a b*tch
                                     */
                                    switch (msg) {
                                        case "/logout":
                                            dos.writeUTF(msg);
                                            System.exit(0);
                                            break;
                                        //prints out socket info
                                        case "/socket":

                                            Platform.runLater(
                                                    () -> {
                                                        consoleCommandOut(s.toString());
                                                    }
                                            );

                                            msg = "";
                                            break;
                                        //TIME DR. FREEMAN, is it that time once again 
                                        case "/date":

                                            Platform.runLater(
                                                    () -> {
                                                        consoleCommandOut(superchat.utils.SCUtils.getDateCmd());
                                                    }
                                            );

                                            msg = "";
                                            break;
                                        case "/help":

                                            Platform.runLater(
                                                    () -> {
                                                        consoleCommandOut("Commands : "
                                                                + System.lineSeparator()
                                                                + "        /logout - Logout and close the application" + System.lineSeparator()
                                                                + "        /socket - Socket info (DEBUG CMD)" + System.lineSeparator()
                                                                + "        /date   - Date formated in DD/MM/YYYY" + System.lineSeparator());
                                                    }
                                            );

                                            msg = "";
                                            break;
                                        default:
                                            break;

                                    }

                                    if (msg.length() != 0) {
                                        dos.writeUTF(msg);
                                    }
                                }
                            } catch (IOException ex) {
                                errMessage(ex);
                                break;
                            }
                        }
                    });

                    Thread readMessage = new Thread(() -> {
                        String msgg;
                        String _username = null;
                        String rawMsgg;

                        _username = superchat.utils.SettingsUtils.settingsRead()[2];

                        while (true) {
                            try {
                                //consoleOut(dis.readUTF());

                                rawMsgg = dis.readUTF();
                                msgg = rawMsgg + System.lineSeparator();

                                Date d = new Date();
                                DateFormat df = new SimpleDateFormat("<kk:mm> ");
                                String date = df.format(d);

                                msgg = date + msgg;

                                Text text = new Text(msgg);

                                text.setFill(Color.BLACK);

                                //set the text to be a diffrent collor when it is yours
                                if (msgg.substring("<kk:mm> ".length(), "<kk:mm> ".length() + _username.length()).equals(_username.toUpperCase())) {

                                    text.setFill(Color.rgb(0, 171, 199));

                                }

                                Platform.runLater(
                                        () -> {
                                            chatBox.getChildren().add(text);
                                        }
                                );

                            } catch (Exception ex) {
                                errMessage(ex);
                                break;
                            }
                        }
                    });

                    sendMessage.start();
                    readMessage.start();
                } else {
                    consoleOut("Invalid or already taken username !");
                    consoleOut("Please select another one in config.properties !");
                    System.exit(0);
                }
            } catch (Exception e) {
                errMessage(e);
            }

        }
        );
        clientThread.start();
    }

//--------------------------|
//Functions for client ended|
//--------------------------|
    @FXML
    private void saveChat() {
        saveFile();
    }

    @FXML
    private void settings() {
        superchat.utils.SettingsUtils.settingsShow();
    }

    @FXML
    private void quit() {
        System.exit(0);
    }

    @FXML
    private void about() {
        superchat.utils.SCUtils.aboutShow();
    }

    @FXML
    private void reportIssues() {
        sc.openLink("https://github.com/LukeOnuke/SuperChat/issues/new");
    }

    @FXML
    private void help() {
        sc.openLink("https://github.com/LukeOnuke/SuperChat/wiki");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        convertToStatic(label_, sendBox_, chatBox_, button_, ap_);

    }

}
