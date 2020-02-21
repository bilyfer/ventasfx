package pos.fx;

import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MenuController {

    @FXML
    private Button btnMenuBar;

    @FXML
    private Button openFrm2;

    @FXML
    private Button openFrm3;

    @FXML
    private VBox dataPane;

    public void setDataPane(Node node) {
        // update VBox with new form(FXML) depends on which button is clicked
        dataPane.getChildren().setAll(node);
    }

    public VBox fadeAnimate(String url) throws IOException {
        VBox v = (VBox) FXMLLoader.load(getClass().getResource(url));
        FadeTransition ft = new FadeTransition(Duration.millis(1500));
        ft.setNode(v);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
        return v;
    }

    public void loadPane(ActionEvent event) throws IOException {
        setDataPane(fadeAnimate("FormProducto.fxml"));
    }

    public void loadPane2(ActionEvent event) throws IOException {
        setDataPane(fadeAnimate("/samplefx/view/FXML2.fxml"));
    }

    public void loadPane3(ActionEvent event) throws IOException {
        setDataPane(fadeAnimate("/samplefx/view/FXML3.fxml"));
    }
}
