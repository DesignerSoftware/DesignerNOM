/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.UsuariosTiposSueldos;
import InterfacePersistencia.PersistenciaUsuariosTiposSueldosInterface;
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
public class PersistenciaUsuariosTiposSueldos implements PersistenciaUsuariosTiposSueldosInterface {

   private static Logger log = Logger.getLogger(PersistenciaUsuariosTiposSueldos.class);

    @Override
    public void crear(EntityManager em, UsuariosTiposSueldos usuariots) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuariots);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaUsuariosTiposSueldos.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, UsuariosTiposSueldos usuariots) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuariots);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaUsuariosTiposSueldos.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, UsuariosTiposSueldos usuariots) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(usuariots));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaUsuariosTiposSueldos.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<UsuariosTiposSueldos> buscarUsuariosTiposSueldos(EntityManager em) {
        try {
            em.clear();
            String sql= "SELECT * FROM USUARIOSTIPOSSUELDOS";
            Query query = em.createNativeQuery(sql, UsuariosTiposSueldos.class);
            List<UsuariosTiposSueldos> usuariosTS = query.getResultList();
            return usuariosTS;
        } catch (Exception e) {
            log.error("Error PersistenciaUsuariosTiposSueldos.buscarUsuariosTiposSueldos ", e);
            return null;
        }
    }
}
