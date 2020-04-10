/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos.bl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author User
 */
public class FacturasServicio {
    public ArrayList<Factura> obtenerUltimasFacturasEmitidas() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        Transaction tx = session.beginTransaction();
        
        Criteria query = session.createCriteria(Factura.class);
        query.addOrder(Order.desc("fecha"));
        query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        query.setMaxResults(10);

        List<Factura> resultado = query.list();
        
        tx.commit();
        session.close();
 
        return new ArrayList<>(resultado);
    }
    
    public ArrayList<Factura> obtenerFacturas(Date fechaInicial, Date fechaFinal) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        Transaction tx = session.beginTransaction();
        
        Criteria query = session.createCriteria(Factura.class);
        query.add(Restrictions.ge("fecha", fechaInicial)); 
        query.add(Restrictions.le("fecha", fechaFinal)); 
        query.add(Restrictions.eq("activo", true));
        query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        List<Factura> resultado = query.list();
        
        tx.commit();
        session.close();
 
        return new ArrayList<>(resultado);
    }
    
    public void guardar(Factura factura) {                        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        try {
            session.saveOrUpdate(factura);
            
            for(FacturaDetalle detalle: factura.getFacturaDetalle()) {
                Integer id = detalle.getProducto().getId();
                
                Criteria query = session.createCriteria(Producto.class);
                query.add(Restrictions.eq("id", id));
                query.setMaxResults(1);
                
                Producto productoExistente = (Producto) query.uniqueResult();
                
                Integer nuevaExistencia = 
                        productoExistente.getExistencia() - detalle.getCantidad();
                
                productoExistente.setExistencia(nuevaExistencia);
                
                session.saveOrUpdate(productoExistente);
            }

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }
    }    
}
