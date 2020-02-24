package pos.fx.Menu;

import pos.fx.POSFX;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXDrawersStack;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainController implements Initializable, AbrirFormularioCallback {

    @FXML
    private JFXDrawer drawer;
    
    @FXML
    private JFXDrawersStack drawerStack;

    @FXML
    private JFXHamburger hamburger;

    @FXML
    private AnchorPane root;

    StackPane pane;
    
    VBox form;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (!POSFX.isSplashLoaded) {
            loadSplashScreen();
        }
    
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pos/fx/Menu/sidepanel.fxml"));
            VBox box = loader.load();
            SidePanelController controller = loader.getController();
            controller.setCallback(this);
            drawer.setSidePane(box);
            
            FitControlsToWindow();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburger);
        transition.setRate(-1);
        
        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            transition.setRate(transition.getRate() * -1);
            //transition.play();
            drawerStack.toggle(drawer);
        });         
    }

    private void loadSplashScreen() {
        try {
            POSFX.isSplashLoaded = true;

            pane = FXMLLoader.load(getClass().getResource(("/pos/fx/Menu/splash.fxml")));
            root.getChildren().setAll(pane);

            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), pane);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setCycleCount(1);

            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), pane);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setCycleCount(1);

            fadeIn.play();

            fadeIn.setOnFinished((e) -> {
                fadeOut.play();
            });

            fadeOut.setOnFinished((e) -> {
                try {
                    AnchorPane parentContent = FXMLLoader.load(getClass().getResource(("/pos/fx/Menu/main.fxml")));
                    root.getChildren().setAll(parentContent.getChildren());           
                } catch (IOException ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void abrirFormulario(String nombreFormulario) {
        try {
            form = FXMLLoader.load(getClass().getResource(("/pos/fx/FormProducto.fxml")));
            drawerStack.setContent(form);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    private void FitControlsToWindow() {
        Stage stage = POSFX.getStage();
   
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            ResizeControls(stage, false);
            //System.out.println("Height: " + stage.getHeight() + " Width: " + stage.getWidth());
        };
        
        stage.widthProperty().removeListener(stageSizeListener);
        stage.heightProperty().removeListener(stageSizeListener);
        
        stage.widthProperty().addListener(stageSizeListener);
        stage.heightProperty().addListener(stageSizeListener); 
        
        ResizeControls(stage, true);
    }
    
    private void ResizeControls(Stage stage, Boolean firstTime) {
        Integer left = firstTime ? 65 : 35;
        root.setPrefHeight(stage.getHeight());
        drawerStack.setPrefWidth(stage.getWidth());
        hamburger.setLayoutX(stage.getWidth() - hamburger.getWidth() - left);
        drawer.setPrefHeight(stage.getHeight() - 34);

        if (pane != null) {
            pane.setPrefWidth(stage.getWidth());  
            pane.setPrefHeight(stage.getHeight());
        }

        if (form != null) {
            form.setPrefWidth(stage.getWidth());
            form.setPrefHeight(stage.getHeight());
        }
    }
}
