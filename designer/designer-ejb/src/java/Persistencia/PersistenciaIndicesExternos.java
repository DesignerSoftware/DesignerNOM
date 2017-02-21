/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.IndicesExternos;
import InterfacePersistencia.PersistenciaIndicesExternosInterface;
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
public class PersistenciaIndicesExternos implements PersistenciaIndicesExternosInterface {

    @Override
    public void crear(EntityManager em, IndicesExternos indice) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(indice);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaIndicesExternos.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, IndicesExternos indice) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(indice);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaIndicesExternos.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, IndicesExternos indice) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(indice));
            tx.commit();

        } catch (Exception e) {
            System.out.println("Error PersistenciaIndicesExternos.borrar: " + e.getMessage());
                if (tx.isActive()) {
                    tx.rollback();
                }
        }
    }

    @Override
    public List<IndicesExternos> buscarIndicesExternos(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM INDICESEXTERNOS";
            Query query = em.createNativeQuery(sql, IndicesExternos.class);
            List<IndicesExternos> listIndicesExternos = query.getResultList();
            return listIndicesExternos;
        } catch (Exception e) {
            System.out.println("Error buscarIndicesExternos PersistenciaIndicesExternos : " + e.toString());
            return null;
        }
    }

}
