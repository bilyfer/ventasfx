package pos.fx.Menu;

import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import pos.bl.Usuario;
import pos.fx.POSFX;

public class SidePanelController implements Initializable {

    @FXML
    JFXListView listView;
    
    private AbrirFormularioCallback callback;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Usuario usuarioAutenticado = POSFX.getUsuarioAutenticado();
        String role = usuarioAutenticado.getRole();
        
        switch(role) {
            case "Administrador": {
                listView.getItems().add(new Label("Inicio"));
                listView.getItems().add(new Label("Productos"));
                listView.getItems().add(new Label("Facturas"));
                listView.getItems().add(new Label("Reporte de Productos"));   
                listView.getItems().add(new Label("Reporte de Facturas"));                 
                break;
            }
            case "Cajero": {
                listView.getItems().add(new Label("Facturas"));  
                listView.getItems().add(new Label("Reporte de Facturas"));                
                break;
            }
            case "Inventario": {
                listView.getItems().add(new Label("Productos"));
                listView.getItems().add(new Label("Reporte de Productos"));                   
                break;
            }
        }
        

        listView.getItems().add(new Label("Cerrar Sesión")); 
        listView.getItems().add(new Label("Salir"));   
        
        listView.setOnMouseClicked(event -> {
            Label label = (Label) listView.getSelectionModel().getSelectedItem();
            
            if (label.getText().equals("Salir")) {
                System.exit(0);
            } else {
                callback.abrirFormulario(label.getText());    
            }
        });
    }

    public void setCallback(AbrirFormularioCallback callback) {
        this.callback = callback;
    }
}
