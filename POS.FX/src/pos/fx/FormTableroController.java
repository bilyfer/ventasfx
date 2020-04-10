/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos.fx;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pos.bl.Factura;
import pos.bl.FacturaDetalle;
import pos.bl.FacturasServicio;

/**
 * FXML Controller class
 *
 * @author User
 */
public class FormTableroController implements Initializable {

    @FXML
    private BarChart<?, ?> barChartVentas;

    @FXML
    private PieChart pieChartVentas;
    
    @FXML
    private TableView tableView;

    @FXML
    private TableColumn<Factura, Integer> colId;

    @FXML
    private TableColumn<Factura, Date> colFecha;

    @FXML
    private TableColumn<Factura, Double> colImpuesto;

    @FXML
    private TableColumn<Factura, Double> colTotal;

    @FXML
    private TableColumn<Factura, Boolean> colActivo;    
    
    FacturasServicio facturasServicio;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        facturasServicio = new FacturasServicio();
        
        cargarDatosdelBarChart();
        cargarDatosdelPieChart();
        cargarDatosUltimasFacturasEmitidas();
    }    

    private void cargarDatosUltimasFacturasEmitidas() {
        ArrayList<Factura> facturas 
                = facturasServicio.obtenerUltimasFacturasEmitidas();
        
        colId.setCellValueFactory(new PropertyValueFactory("id"));
        colFecha.setCellValueFactory(new PropertyValueFactory("fecha"));
        colImpuesto.setCellFactory(param -> new TableCell<Factura, Double>() {
             @Override
             public void updateItem(Double item, boolean empty) {
                 super.updateItem(item, empty);
                 if (empty) {
                     setGraphic(null);
                     setText(null);
                 } else {
                     Factura factura = (Factura) getTableRow().getItem();
                     DecimalFormat df = new DecimalFormat("#,##0.00");                    
                     setText(df.format(factura.getImpuesto()));
                 }
             }            
         });    

        colTotal.setCellFactory(param -> new TableCell<Factura, Double>() {
             @Override
             public void updateItem(Double item, boolean empty) {
                 super.updateItem(item, empty);
                 if (empty) {
                     setGraphic(null);
                     setText(null);
                 } else {
                     Factura factura = (Factura) getTableRow().getItem();
                     DecimalFormat df = new DecimalFormat("#,##0.00");                    
                     setText(df.format(factura.getTotal()));                    
                 }
             }            
         });

        colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));

        ObservableList<Factura> data = FXCollections.observableArrayList(facturas);
        
        tableView.setItems(data);
    }

    private void cargarDatosdelBarChart() {
        ArrayList<Factura> facturas = obtenerFacturas();

        BarChart.Series series = new XYChart.Series<>();

        if (facturas.size() > 0) {
            Date fechaFactura = facturas.get(0).getFecha();
            Date fecha = fechaFactura;
            String Ultimokey = (fecha.getYear() + fecha.getMonth() + fecha.getDate()) + "-" + 
                    obtenerTextoDia(fecha.getDay()) + " " + fecha.getDate();

            HashMap<String, Double> ventas = new HashMap<>();
            Double total = 0.0;

            for(Factura factura : facturas) {
                fecha = factura.getFecha();
                String keyActual = (fecha.getYear() + fecha.getMonth() + fecha.getDate()) + "-" + 
                    obtenerTextoDia(fecha.getDay()) + " " + fecha.getDate();
                
                if (!Ultimokey.equals(keyActual)){                    
                    ventas.put(Ultimokey, total);
                    
                    Ultimokey = keyActual;                    
                    total = 0.0;
                }
                total += factura.getTotal();
            }

            ventas.put(Ultimokey, total);
            
            SortedSet<String> keys = new TreeSet<>(ventas.keySet());

            keys.forEach((key) -> { 
                Double value = ventas.get(key);
                String[] arr = key.split("-");
                series.getData().add(new XYChart.Data(arr[1], value));
            });

            barChartVentas.getData().addAll(series);
        }
    }

    private void cargarDatosdelPieChart() {
        ArrayList<Factura> facturas = obtenerFacturas();
        HashMap<String, Double> ventas = new HashMap<>();
        Double totalVentas = 0.0;
        
        for(Factura factura : facturas) {            
            ArrayList<FacturaDetalle> detalle = new ArrayList<>();
            detalle.addAll(factura.getFacturaDetalle());
                
            for(FacturaDetalle item: detalle) {
                String key = item.getProducto().getCategoria().getDescripcion();
                
                Double total = (item.getCantidad() * item.getPrecio());
                
                if (ventas.containsKey(key)) {
                    Double valor = ventas.get(key);
                    ventas.put(key, valor + total);
                } else {
                    ventas.put(key, total);
                }                
            }
            
            totalVentas += factura.getTotal();
        }

        ObservableList<PieChart.Data> pieChartData =
            FXCollections.observableArrayList();
        
        final Double finalTotalVentas = totalVentas;
        
        ventas.forEach((String k, Double v) -> { 
            Double porcentaje = (v / finalTotalVentas) * 100;
            DecimalFormat df = new DecimalFormat("#,##0.00");                    
            String texto = k + " " + df.format(porcentaje);
                    
            PieChart.Data data = new PieChart.Data(texto, porcentaje);
            pieChartData.add(data);
        }); 
        
        pieChartVentas.setData(pieChartData);
    }
    
    private ArrayList<Factura> obtenerFacturas() {
        ArrayList<Factura> facturas = new ArrayList<>();
        
        try {
            LocalDate date = LocalDate.now().minusDays(7);
            
            Date fechaInicial = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .parse(getFecha(date) + " 00:00:00");
            
            date = LocalDate.now();

            Date fechaFinal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .parse(getFecha(date) + " 23:59:59");
            
            facturas = facturasServicio.obtenerFacturas(fechaInicial, fechaFinal);
            
        } catch (ParseException ex) {
            Logger.getLogger(FormTableroController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return facturas;
    }

    private String getFecha(LocalDate date) {
        String fecha = 
                Integer.toString(date.getYear()) + "-" +
                Integer.toString(date.getMonthValue()) + "-" +
                Integer.toString(date.getDayOfMonth());
        
        return fecha;
    }
       
    private String obtenerTextoDia(Integer dia) {
        String textoDia = "";
        
        switch(dia) {
            case 0:
                textoDia = "Domingo";
                break;
            case 1:
                textoDia = "Lunes";
                break;
            case 2:
                textoDia = "Martes";
                break;
            case 3:
                textoDia = "Miercoles";
                break;
            case 4:
                textoDia = "Jueves";
                break;
            case 5:
                textoDia = "Viernes";
                break;  
            case 6:
                textoDia = "Sabado";
                break;                  
        }
        
        return textoDia;
    }
    
}
