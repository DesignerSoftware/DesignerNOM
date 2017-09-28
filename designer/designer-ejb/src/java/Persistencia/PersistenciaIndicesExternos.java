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
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaIndicesExternos implements PersistenciaIndicesExternosInterface {

   private static Logger log = Logger.getLogger(PersistenciaIndicesExternos.class);

    @Override
    public void crear(EntityManager em, IndicesExternos indice) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(indice);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaIndicesExternos.crear:  ", e);
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
            log.error("Error PersistenciaIndicesExternos.editar:  ", e);
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
            log.error("Error PersistenciaIndicesExternos.borrar:  ", e);
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
            log.error("Error buscarIndicesExternos PersistenciaIndicesExternos :  ", e);
            return null;
        }
    }

}
