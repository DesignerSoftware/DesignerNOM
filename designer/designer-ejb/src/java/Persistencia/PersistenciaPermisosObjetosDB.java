/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.PermisosObjetosDB;
import InterfacePersistencia.PersistenciaPermisosObjetosDBInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Stateless
public class PersistenciaPermisosObjetosDB implements PersistenciaPermisosObjetosDBInterface {

    @Override
    public void crear(EntityManager em, PermisosObjetosDB permisosp) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(permisosp);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPermisosObjetosDB.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, PermisosObjetosDB permisosp) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(permisosp);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPermisosObjetosDB.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, PermisosObjetosDB permisosp) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(permisosp));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaPermisosObjetosDB.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<PermisosObjetosDB> consultarPermisosPorPerfil(EntityManager em, BigInteger secPerfil) {
        try {
            em.clear();
            String sql = "SELECT * FROM PERMISOSOBJETOSDB WHERE  PERFIL = ?";
            Query query = em.createNativeQuery(sql, PermisosObjetosDB.class);
            query.setParameter(1, secPerfil);
            List<PermisosObjetosDB> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaPermisosObjetosDB.consultarPermisosPorPerfil()" + e.getMessage());
            return null;
        }
    }

}
