/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos.bl;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author User
 */
@Entity
@Table(name="Factura")
public class Factura {
    private Integer id;
    private Double total;
    private Double impuesto;
    private Boolean activo;
    private Date fecha;

    public Factura() {
        activo = true;
        facturaDetalle = new HashSet<>();
        fecha = new Date();
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(Double impuesto) {
        this.impuesto = impuesto;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
        
    private Set<FacturaDetalle> facturaDetalle;

    @OneToMany(cascade=CascadeType.ALL, mappedBy="factura")
    public Set<FacturaDetalle> getFacturaDetalle() {
        return facturaDetalle;
    }    
    
    public void setFacturaDetalle(Set<FacturaDetalle> facturaDetalle) {
        this.facturaDetalle = facturaDetalle;
    }    

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
