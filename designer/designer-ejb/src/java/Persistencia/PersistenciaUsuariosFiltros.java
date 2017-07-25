/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.UsuariosFiltros;
import InterfacePersistencia.PersistenciaUsuariosFiltrosInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaUsuariosFiltros implements PersistenciaUsuariosFiltrosInterface {

   private static Logger log = Logger.getLogger(PersistenciaUsuariosFiltros.class);

    @Override
    public void crear(EntityManager em, UsuariosFiltros usuarioF) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuarioF);
            tx.commit();
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaUsuariosFiltros.crear()" + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, UsuariosFiltros usuarioF) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuarioF);
            tx.commit();
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaUsuariosFiltros.editar()" + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, UsuariosFiltros usuarioF) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(usuarioF));
            tx.commit();
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaUsuariosFiltros.borrar()" + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<UsuariosFiltros> consultarUsuariosFiltros(EntityManager em, BigInteger secUsuarioEstructura) {
        em.clear();
        try{
            String sql = "SELECT * FROM USUARIOSFILTROS WHERE USUARIOESTRUCTURA = ?";
            Query query = em.createNativeQuery(sql, UsuariosFiltros.class);
            query.setParameter(1,secUsuarioEstructura);
            List<UsuariosFiltros> lista = query.getResultList();
            return lista;
        }catch(Exception e){
            log.error("error PersistenciaUsuariosFiltros.consultarUsuariosFiltros()" + e.getMessage());
            return null;
        }
    }

    @Override
    public BigDecimal contarUsuariosFiltros(EntityManager em, BigInteger secUsuarioEstructura) {
       em.clear();
        try{
            String sql = "SELECT COUNT(*) FROM USUARIOSFILTROS WHERE USUARIOESTRUCTURA = ?";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1,secUsuarioEstructura);
            BigDecimal count = (BigDecimal) query.getSingleResult();
            return count;
        }catch(Exception e){
            log.error("error PersistenciaUsuariosFiltros.contarUsuariosFiltros()" + e.getMessage());
            return null;
        }
    }

    @Override
    public void crearFiltroUsuario(EntityManager em, BigInteger secUsuarioVista) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
      try{
         String sql = "SELECT OBJETODB FROM USUARIOSVISTAS WHERE SECUENCIA = ?" ;
         Query query = em.createNativeQuery(sql);
         query.setParameter(1, secUsuarioVista);
         BigDecimal aux = (BigDecimal) query.getSingleResult();
         if(aux != null){
           tx.begin();
            String sqlQuery = "call USUARIOS_PKG.CrearVistaFiltro(?)";
            Query query2 = em.createNativeQuery(sqlQuery);
            query2.setParameter(1, aux);
            query2.executeUpdate();
            tx.commit();
           tx.begin();
            String sqlQuery1 = "call USUARIOS_PKG.crearfiltrousuario(?,?)";
            Query query3 = em.createNativeQuery(sqlQuery1);
            query3.setParameter(1, secUsuarioVista);
            query3.setParameter(2, aux);
            query3.executeUpdate();
            tx.commit();
         }
      }catch(Exception e){
          log.error("Error PersistenciaUsuariosFiltros.crearFiltroUsuario() : " + e.getMessage());
      }
    }

}
