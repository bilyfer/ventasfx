/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos.fx;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.Initializable;
import net.sf.jasperreports.engine.JRException;

/**
 * FXML Controller class
 *
 * @author User
 */
public class FormReporteProductosController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    

    public void generarReporte() {
        ReporteProductosViewer reporteViewer = new ReporteProductosViewer();
        try {        
            reporteViewer.mostrarReporte();
        } catch (JRException ex) {
            Logger.getLogger(FormReporteProductosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
