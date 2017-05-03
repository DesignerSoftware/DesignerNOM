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
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaPermisosPantallas implements PersistenciaPermisosPantallasInterface {

    @Override
    public void crear(EntityManager em, PermisosPantallas permisosp) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(permisosp);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPermisosPantallas.crear: " + e.getMessage());
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
            System.out.println("Error PersistenciaPermisosPantallas.editar: " + e.getMessage());
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
            System.out.println("Error PersistenciaPermisosPantallas.borrar: " + e.getMessage());
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
            System.out.println("Persistencia.PersistenciaPermisosPantallas.consultarPermisosPorPerfil()" + e.getMessage());
            return null;
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
