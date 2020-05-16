/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package superchat.gui.settings;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author lukak
 */
public class SettingsController implements Initializable {

    @FXML
    TextField username;
    @FXML
    TextField ipa;
    @FXML
    TextField port;
    @FXML 
    private AnchorPane ap;

    @FXML
    private void save() {
        String[] settings = new String[3];
        settings[0] = ipa.getText();
        settings[1] = port.getText();
        settings[2] = username.getText();

        superchat.utils.SettingsUtils.settingsWrite(settings);
        
        try {
            Thread.sleep((long) 400.0);
        } catch (InterruptedException ex) {
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);

    }
    
    
    @FXML
    private void defaults(){
        String[] settings = superchat.utils.SettingsUtils.getDefaultSettings();
        ipa.setText(settings[0]);
        port.setText(settings[1]);
        username.setText(settings[2]);
    }
    
    @FXML
    private void cancel(){
        
        Stage stage = (Stage) ap.getScene().getWindow();
        stage.close();
    }

    //the code that runs after the window has been initilised
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String[] settings = superchat.utils.SettingsUtils.settingsRead();
        ipa.setText(settings[0]);
        port.setText(settings[1]);
        username.setText(settings[2]);
    }
}
