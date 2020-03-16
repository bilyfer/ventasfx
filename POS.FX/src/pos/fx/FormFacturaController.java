/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos.fx;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import pos.bl.Categoria;
import pos.bl.CategoriasServicio;
import pos.bl.Factura;
import pos.bl.FacturaDetalle;
import pos.bl.FacturasServicio;
import pos.bl.Producto;
import pos.bl.ProductosServicio;

/**
 * FXML Controller class
 *
 * @author User
 */
public class FormFacturaController implements Initializable {
    @FXML
    TableView tableView;
    
    @FXML
    TabPane tabPaneProductos;
    
    @FXML
    TableColumn<Producto, String> colDescripcion;

    @FXML
    TableColumn<Producto, Double> colPrecio;
    
    @FXML
    TableColumn colEliminar;
    
    @FXML
    Label lblTotal;
    
    @FXML
    Label lblImpuesto;
    
    ObservableList<Producto> data;
    
    Factura nuevaFactura;
    
    ProductosServicio servicioProductos;
    CategoriasServicio servicioCategorias;
    FacturasServicio servicioFacturas;
    
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        servicioProductos = new ProductosServicio();
        servicioCategorias = new CategoriasServicio();
        servicioFacturas = new FacturasServicio();
        
        colDescripcion.setCellValueFactory(new PropertyValueFactory("descripcion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        
        definirColumnaEliminar();
        
        data = FXCollections.observableArrayList();
        tableView.setItems(data);
        tableView.refresh();
        
        crearTabs();
        nuevaFactura();
    }    
    
    public void nuevaFactura() {
        nuevaFactura = new Factura();
        data.clear();
        calcularFactura();
    }
    
    public void guardar() {
        servicioFacturas.guardar(nuevaFactura);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Factura Guardada");
        alert.showAndWait();
        
        nuevaFactura();
    }
    
    private void definirColumnaEliminar() {
        colEliminar.setCellFactory(param -> new TableCell<String, String>() {
            final JFXButton btn = new JFXButton("Eliminar");
            
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    btn.getStyleClass().add("jfx-button-danger-outline");
                    btn.setOnAction(event -> {
                        tableView.getSelectionModel().select(getTableRow().getItem());
                        Producto producto = (Producto) getTableRow().getItem();
                        removerProducto(producto);
                    });
                    setGraphic(btn);
                    setText(null);
                }
            }            
        });        
    }        

    private void crearTabs() {
        ArrayList<Categoria> categorias = servicioCategorias.obtenerCategorias();
        ArrayList<Producto> productos = servicioProductos.obtenerProductosActivos();
        
        for(Categoria categoria: categorias) {
            TilePane tilePane = new TilePane();
            tilePane.setPadding(new Insets(10, 10, 10, 10));
            tilePane.setHgap(10);
            tilePane.setVgap(10);

            List<Producto> productosPorCategoria = productos.stream()
                .filter(p -> Objects.equals(p.getCategoria().getId(), categoria.getId()))
                    .collect(Collectors.toList()); 
            
            for(Producto producto: productosPorCategoria) {
                VBox vbox = new VBox();
                Label lblDescripcion = new Label();
                Label lblPrecio = new Label();
                
                lblDescripcion.setText(producto.getDescripcion());
                lblPrecio.setText(producto.getPrecio().toString());
                
                ImageView imagen = new ImageView(producto.getImageView());
                imagen.setUserData(producto);
                imagen.setFitWidth(100);
                imagen.setPreserveRatio(true);
                
                vbox.getChildren().add(imagen);
                vbox.getChildren().add(lblDescripcion);
                vbox.getChildren().add(lblPrecio);
                vbox.setAlignment(Pos.CENTER);
                
                imagen.addEventHandler(MouseEvent.MOUSE_CLICKED, 
                        new EventHandler<MouseEvent>() {                         
                    @Override
                    public void handle(MouseEvent event) {
                        Object object = event.getSource();
                        ImageView image = (ImageView)object;
                        Producto userData = (Producto) image.getUserData();
                        
                        agregarProducto(userData);
                    }                            
                });
                
                tilePane.getChildren().add(vbox);
            }
            
            Tab tab = new Tab(categoria.getDescripcion(), tilePane);
            
            tabPaneProductos.getTabs().add(tab);
        }        
    }
    
    private void agregarProducto(Producto producto) {
        data.add(producto);
        calcularFactura();
    }
    
    private void removerProducto(Producto producto) {
        data.remove(producto);
        calcularFactura();
    }
    
    private void calcularFactura() {
        Double total = 0.0;
        Double impuesto = 0.0;
        
        nuevaFactura.getFacturaDetalle().clear();
        
        for(Producto producto: data) {
            FacturaDetalle detalle = new FacturaDetalle();
            detalle.setFactura(nuevaFactura);
            detalle.setProducto(producto);
            detalle.setCantidad(1);
            detalle.setPrecio(producto.getPrecio());
            
            nuevaFactura.getFacturaDetalle().add(detalle);
            
            total += producto.getPrecio();
        }
        
        impuesto = total - (total / 1.15);
        
        nuevaFactura.setTotal(total);
        nuevaFactura.setImpuesto(impuesto);
        
        lblTotal.setText("TOTAL: " + formatoMoneda(total));
        lblImpuesto.setText("Impuesto: " + formatoMoneda(impuesto));
    }
 
    private String formatoMoneda(Double valor) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(valor);
    }
}
