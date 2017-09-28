/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.PermisosPantallas;
import InterfacePersistencia.PersistenciaPermisosPantallasInterface;
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
public class PersistenciaPermisosPantallas implements PersistenciaPermisosPantallasInterface {

   private static Logger log = Logger.getLogger(PersistenciaPermisosPantallas.class);

    @Override
    public void crear(EntityManager em, PermisosPantallas permisosp) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(permisosp);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPermisosPantallas.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, PermisosPantallas permisosp) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(permisosp);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPermisosPantallas.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, PermisosPantallas permisosp) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(permisosp));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaPermisosPantallas.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<PermisosPantallas> consultarPermisosPorPerfil(EntityManager em, BigInteger secPerfil) {
        try {
            em.clear();
            String sql = "SELECT PP.* \n"
                    + "FROM PERMISOSPANTALLAS PP \n"
                    + "WHERE PP.PERFIL = ?";
            Query query = em.createNativeQuery(sql, PermisosPantallas.class);
            query.setParameter(1, secPerfil);
            List<PermisosPantallas> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            log.error("PersistenciaPermisosPantallas.consultarPermisosPorPerfil():  ", e);
            return null;
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
