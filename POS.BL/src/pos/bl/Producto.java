/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos.bl;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
    
    public Producto() {
        id = new SimpleIntegerProperty();
        descripcion = new SimpleStringProperty();
        categoria = new SimpleObjectProperty();
        precio = new SimpleDoubleProperty();
        existencia = new SimpleIntegerProperty();
        activo = new SimpleBooleanProperty(true);
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
}
