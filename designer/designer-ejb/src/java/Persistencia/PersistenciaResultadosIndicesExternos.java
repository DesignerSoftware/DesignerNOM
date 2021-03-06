/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.IndicesExternos;
import Entidades.ResultadosIndicesExternos;
import InterfacePersistencia.PersistenciaResultadosIndicesExternosInterface;
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
public class PersistenciaResultadosIndicesExternos implements PersistenciaResultadosIndicesExternosInterface {

   private static Logger log = Logger.getLogger(PersistenciaResultadosIndicesExternos.class);

    @Override
    public void crear(EntityManager em, ResultadosIndicesExternos resultados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(resultados);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaResultadosIndicesExternos.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, ResultadosIndicesExternos resultados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(resultados);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaResultadosIndicesExternos.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, ResultadosIndicesExternos resultados) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(resultados));
            tx.commit();

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            log.error("Error PersistenciaResultadosIndicesExternos.borrar:  ", e);
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
            log.error("Error buscarIndicesExternos PersistenciaResultadosIndicesExternos :  ", e);
            return null;
        }
    }

    @Override
    public List<ResultadosIndicesExternos> buscarResultadosIndicesExternos(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM RESULTADOSINDICESEXTERNOS";
            Query query = em.createNativeQuery(sql, ResultadosIndicesExternos.class);
            List<ResultadosIndicesExternos> listResultadosIndicesExternos = query.getResultList();
            return listResultadosIndicesExternos;
        } catch (Exception e) {
            log.error("Error buscarResultadosIndicesExternos PersistenciaResultadosIndicesExternos :  ", e);
            return null;
        }
    }

}
