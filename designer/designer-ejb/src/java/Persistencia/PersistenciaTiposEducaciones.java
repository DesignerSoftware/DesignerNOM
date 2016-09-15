/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.TiposEducaciones;
import InterfacePersistencia.PersistenciaTiposEducacionesInterface;
import java.util.List;

import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;

import javax.persistence.Query;
/**
 * Clase Stateless.<br> 
 * Clase encargada de realizar operaciones sobre la tabla 'TiposEducaciones'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaTiposEducaciones implements PersistenciaTiposEducacionesInterface{
    @Override
    public List<TiposEducaciones> tiposEducaciones(EntityManager em) {
        try {
            em.clear();
            String sql="SELECT * FROM TiposEducaciones  ORDER BY codigo";
            Query query = em.createNativeQuery(sql, TiposEducaciones.class);
            List<TiposEducaciones> tiposEducaciones = query.getResultList();
            return tiposEducaciones;
        } catch (Exception e) {
            System.out.println("error en PersistenciaTiposEducaciones.tiposeducaciones " + e.getMessage());
            return null;
        }
    }   

    @Override
    public void crear(EntityManager em, TiposEducaciones tipoEducacion) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tipoEducacion);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposEducaciones.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TiposEducaciones tipoEducacion) {
       em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tipoEducacion));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("Error PersistenciaTiposEducaciones.borrar: " + e);
            }
        }
    }

    @Override
    public void editar(EntityManager em, TiposEducaciones tipoEducacion) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tipoEducacion);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposEducaciones.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
}
