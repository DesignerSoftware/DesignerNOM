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
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaUsuariosTiposSueldos implements PersistenciaUsuariosTiposSueldosInterface {

    @Override
    public void crear(EntityManager em, UsuariosTiposSueldos usuariots) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuariots);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaUsuariosTiposSueldos.crear: " + e.getMessage());
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
            System.out.println("Error PersistenciaUsuariosTiposSueldos.editar: " + e.getMessage());
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
            System.out.println("Error PersistenciaUsuariosTiposSueldos.borrar: " + e.getMessage());
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
            System.out.println("Error PersistenciaUsuariosTiposSueldos.buscarUsuariosTiposSueldos" + e.getMessage());
            return null;
        }
    }
}
