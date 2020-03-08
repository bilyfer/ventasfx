/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos.fx;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import pos.bl.Categoria;
import pos.bl.CategoriasServicio;
import pos.bl.Producto;

/**
 * FXML Controller class
 *
 * @author User
 */
public class NuevoEditarProductoController implements Initializable {
    @FXML
    Button btnCancelar;
    
    @FXML
    JFXTextField txtId;
    
    @FXML
    JFXTextField txtDescripcion;    

    @FXML
    JFXComboBox cmbCategoria;    
    
    @FXML
    JFXTextField txtPrecio;
    
    @FXML
    JFXTextField txtExistencia;

    @FXML
    JFXCheckBox chActivo;   
    
    @FXML
    ImageView imgViewImagen;
    
    private FormProductoController controller;
    private Producto producto;
    private CategoriasServicio categoriasServicio;
    private ObservableList<Categoria> data;
    
    public void setController(FormProductoController controller) {
        this.controller = controller;
    }
    
    public void setProducto(Producto producto) {
        this.producto = producto;
        
        txtId.textProperty().bindBidirectional(producto.idProperty(), new NumberStringConverter());
        txtDescripcion.textProperty().bindBidirectional(producto.descripcionProperty());        
        cmbCategoria.valueProperty().bindBidirectional(producto.categoriaProperty());        
        
        cmbCategoria.setConverter(new StringConverter<Categoria>() {
            @Override
            public String toString(Categoria categoria) {
                return categoria == null ? "" : categoria.getDescripcion();
            }

            @Override
            public Categoria fromString(String string) {
                if (data == null) {
                    return null;
                }
                
                for(Categoria categoria: data) {
                    if (categoria.getDescripcion().equals(string)) {
                        return categoria;
                    }
                }
                
                return null;
            }
        
        });
        
        txtPrecio.textProperty().bindBidirectional(producto.precioProperty(), new NumberStringConverter());        
        txtExistencia.textProperty().bindBidirectional(producto.existenciaProperty(), new NumberStringConverter());        
        chActivo.selectedProperty().bindBidirectional(producto.activoProperty());        
        
        imgViewImagen.imageProperty().bind(producto.imageViewProperty());
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categoriasServicio = new CategoriasServicio();
        
        data = FXCollections.observableArrayList(categoriasServicio.obtenerCategorias());
       
        cmbCategoria.setItems(data);
    }    
    
    public void aceptar() {
        String resultado = controller.guardar(producto);
        if (resultado.equals("")) {
            cerrar();   
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Productos");
            alert.setHeaderText("Errores de validación de datos");
            alert.setContentText(resultado);
            alert.showAndWait();
        }
    }
    
    public void cancelar() {
        cerrar();
    }
    
    public void agregarImagen() {
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter extensiones = 
          new FileChooser.ExtensionFilter(
            "Imagenes", "*.jpg", "*.png");
        
        fc.getExtensionFilters().add(extensiones);
        
        File archivo = fc.showOpenDialog(null);
        
        if (archivo != null) {
            Image image = new Image(archivo.toURI().toString());
            producto.setImageView(image);
        }
        
    }
    
    public void removerImagen() {
        producto.setImageView(null);
    }

    private void cerrar() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
    
}
