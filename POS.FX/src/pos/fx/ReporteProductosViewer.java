/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos.fx;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.InputStream;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.swing.JRViewer;
import pos.bl.ProductosServicio;

/**
 *
 * @author User
 */
public class ReporteProductosViewer extends JFrame {
public void mostrarReporte() throws JRException {
        ProductosServicio servicio = new ProductosServicio();

        String file = "/pos/fx/ReporteProductos.jasper";
        InputStream stream = getClass().getResourceAsStream(file);

        JasperReport reporte = (JasperReport) JRLoader.loadObject(stream);
        JRBeanCollectionDataSource beanColDataSource = 
                new JRBeanCollectionDataSource(servicio.obtenerProductos(), false);

        JasperPrint print = JasperFillManager
                .fillReport(reporte, null, beanColDataSource);
        JRViewer viewer = new JRViewer(print);
        
        viewer.setOpaque(true);
        viewer.setVisible(true);

        this.add(viewer);
        this.setSize(1000, 700);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize()
                .width/2, dim.height/2-this.getSize().height/2);

        this.setVisible(true);            
    }    
}
