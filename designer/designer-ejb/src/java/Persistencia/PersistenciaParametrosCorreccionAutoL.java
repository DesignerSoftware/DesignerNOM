/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.ParametrosCorreccionesAutoL;
import InterfacePersistencia.PersistenciaParametrosCorreccionAutoLInterface;
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
public class PersistenciaParametrosCorreccionAutoL implements PersistenciaParametrosCorreccionAutoLInterface {

    @Override
    public void crearCorreccion(EntityManager em, ParametrosCorreccionesAutoL correccion) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(correccion);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaParametrosAutoliq.crearCorreccion : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editarCorreccion(EntityManager em, ParametrosCorreccionesAutoL correccion) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(correccion);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaParametrosAutoliq.editarCorreccion : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrarCorreccion(EntityManager em, ParametrosCorreccionesAutoL correccion) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(correccion));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaParametrosAutoliq.borrarCorreccion : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<ParametrosCorreccionesAutoL> consultarParametrosCorreccionesAutoL(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM parametroscorreccionautol P WHERE EXISTS (SELECT 'X' FROM EMPRESAS E WHERE E.SECUENCIA=P.EMPRESA)";
            Query query = em.createNativeQuery(sql, ParametrosCorreccionesAutoL.class);
            List<ParametrosCorreccionesAutoL> listaParametros = query.getResultList();
            return listaParametros;
        } catch (Exception e) {
            System.out.println("Error PersistenciaParametrosAutoliq.consultarParametrosAutoliqPorEmpresas : " + e.toString());
            return null;
        }
    }

}
