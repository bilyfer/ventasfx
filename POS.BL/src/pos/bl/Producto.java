/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos.bl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author User
 */

@Entity
@Table(name="Producto")
public class Producto {
    private SimpleIntegerProperty id;
    private SimpleStringProperty descripcion;
    private SimpleObjectProperty categoria;
    private SimpleDoubleProperty precio;
    private SimpleIntegerProperty existencia;
    private SimpleBooleanProperty activo;
    private SimpleObjectProperty imageView;
    private byte[] imagen;    
    
    public Producto() {
        id = new SimpleIntegerProperty();
        descripcion = new SimpleStringProperty();
        categoria = new SimpleObjectProperty();
        precio = new SimpleDoubleProperty();
        existencia = new SimpleIntegerProperty();
        activo = new SimpleBooleanProperty(true);
        imageView = new SimpleObjectProperty();
        imagen = "0".getBytes();        
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Integer getId() {
        return id.get();
    }

    public void setId(Integer id) {
        this.id.set(id);
    }
    
    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }
    
    public SimpleStringProperty descripcionProperty() {
        return descripcion;
    }
    
    public Double getPrecio() {
        return precio.get();
    }

    public void setPrecio(Double precio) {
        this.precio.set(precio);
    }
    
    public SimpleDoubleProperty precioProperty() {
        return precio;
    }
    
    public Integer getExistencia() {
        return existencia.get();
    }

    public void setExistencia(Integer existencia) {
        this.existencia.set(existencia);
    }
    
    public SimpleIntegerProperty existenciaProperty() {
        return existencia;
    }
    
    public Boolean getActivo() {
        return activo.get();
    }

    public void setActivo(Boolean activo) {
        this.activo.set(activo);
    }
    
    public SimpleBooleanProperty activoProperty() {
        return activo;
    }
    
    @ManyToOne
    @JoinColumn(name="categoriaId", nullable=false)
    public Categoria getCategoria() {
        return (Categoria) categoria.get();
    }

    public void setCategoria(Categoria categoria) {
        this.categoria.set(categoria);
    }
    
    public SimpleObjectProperty categoriaProperty() {
        return categoria;
    }
    
    @Lob
    @Column(name = "imagen", columnDefinition = "LONGBLOB")
    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
        
        Image img = new Image(new ByteArrayInputStream(imagen));        
        imageViewProperty().set(img);
    }

    @Transient
    public Image getImageView() {
        Image img = new Image(new ByteArrayInputStream(imagen));        
        return img;        
    }
    
    @Transient
    public String getDescripcionCategoria() {
        return getCategoria().getDescripcion();
    }
    
    @Transient
    public InputStream getFoto() {
        return new ByteArrayInputStream(imagen);
    }
        
    public void setImageView(Image image) {  
        if (image == null) {
            setImagen("0".getBytes());
            return;
        }
        
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        
        try {
            ImageIO.write(bImage, "png", stream);
            byte[] bytes  = stream.toByteArray();
            stream.close();
            setImagen(bytes);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        imageView.set(image);
    }
    
    public SimpleObjectProperty imageViewProperty() {   
        return imageView;
    }    
    
    private Set<FacturaDetalle> facturaDetalle;

    @OneToMany(mappedBy="producto")
    public Set<FacturaDetalle> getFacturaDetalle() {
        return facturaDetalle;
    }

    public void setFacturaDetalle(Set<FacturaDetalle> facturaDetalle) {
        this.facturaDetalle = facturaDetalle;
    }    
}
