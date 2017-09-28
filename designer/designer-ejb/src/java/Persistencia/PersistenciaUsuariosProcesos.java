/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.UsuariosProcesos;
import InterfacePersistencia.PersistenciaUsuariosProcesosInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaUsuariosProcesos implements PersistenciaUsuariosProcesosInterface {

    private static Logger log = Logger.getLogger(PersistenciaUsuariosTiposSueldos.class);

    @Override
    public void crear(EntityManager em, UsuariosProcesos usuarioproceso) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuarioproceso);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaUsuariosProcesos.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, UsuariosProcesos usuarioproceso) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuarioproceso);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaUsuariosProcesos.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, UsuariosProcesos usuarioproceso) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(usuarioproceso));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaUsuariosProcesos.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<UsuariosProcesos> buscarUsuariosProcesos(EntityManager em) {
       try {
            em.clear();
            String sql= "SELECT * FROM USUARIOSPROCESOS";
            Query query = em.createNativeQuery(sql, UsuariosProcesos.class);
            List<UsuariosProcesos> listaUsuariosP = query.getResultList();
            return listaUsuariosP;
        } catch (Exception e) {
            log.error("Error PersistenciaUsuariosProcesos.buscarUsuariosProcesos ", e);
            return null;
        }
    }
}
