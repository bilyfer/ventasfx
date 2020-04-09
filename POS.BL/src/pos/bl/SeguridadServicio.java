/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos.bl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author User
 */
public class SeguridadServicio {
    public Usuario autenticar(String usuario, String contrasena) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        String contrasenaEncriptada = digest(contrasena);

        try {
            Criteria query = session.createCriteria(Usuario.class);
            query.add(Restrictions.eq("nombre", usuario));
            query.add(Restrictions.eq("contrasena", contrasenaEncriptada));
            query.setMaxResults(1);

            Usuario usuarioExistente = (Usuario) query.uniqueResult();
            session.close();
            
            return usuarioExistente;
        } catch (Exception e) {
            tx.rollback();
            session.close();
            System.out.println(e.getMessage());
        }            
        
        return null;
    }
    
    private String encodeHex(byte[] digest) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return sb.toString();
    }
    
    private String digest(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] buffer = input.getBytes("UTF-8");
            md.update(buffer);
            byte[] digest = md.digest();

            return encodeHex(digest);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }    
}
