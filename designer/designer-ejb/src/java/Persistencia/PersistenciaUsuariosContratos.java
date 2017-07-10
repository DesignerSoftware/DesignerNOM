/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.UsuariosContratos;
import InterfacePersistencia.PersistenciaUsuariosContratosInterface;
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
public class PersistenciaUsuariosContratos implements PersistenciaUsuariosContratosInterface {

    @Override
    public void crear(EntityManager em, UsuariosContratos usuariots) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuariots);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaUsuariosContratos.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, UsuariosContratos usuariots) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuariots);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaUsuariosContratos.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, UsuariosContratos usuariots) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(usuariots));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaUsuariosContratos.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<UsuariosContratos> buscarUsuariosContratos(EntityManager em) {
        try {
            em.clear();
            String sql= "SELECT * FROM USUARIOSCONTRATOS";
            Query query = em.createNativeQuery(sql, UsuariosContratos.class);
            List<UsuariosContratos> usuariosTS = query.getResultList();
            return usuariosTS;
        } catch (Exception e) {
            System.out.println("Error PersistenciaUsuariosContratos.buscarUsuariosContratos" + e.getMessage());
            return null;
        }
    }

}
