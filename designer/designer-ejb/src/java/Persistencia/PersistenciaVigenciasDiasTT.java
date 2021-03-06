/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.VigenciasDiasTT;
import InterfacePersistencia.PersistenciaVigenciasDiasTTInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaVigenciasDiasTT implements PersistenciaVigenciasDiasTTInterface {

   private static Logger log = Logger.getLogger(PersistenciaVigenciasDiasTT.class);

    @Override
    public void crear(EntityManager em, VigenciasDiasTT vigenciasDiasTT) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(vigenciasDiasTT);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasDiasTT.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, VigenciasDiasTT vigenciasDiasTT) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasDiasTT);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasDiasTT.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, VigenciasDiasTT vigenciasDiasTT) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vigenciasDiasTT));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasDiasTT.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<VigenciasDiasTT> consultarDiasPorTT(EntityManager em, BigInteger secuenciaTT) {
        try {
            em.clear();
            javax.persistence.Query query = em.createQuery("SELECT vdt FROM VigenciasDiasTT vdt WHERE vdt.tipoTrabajador.secuencia = :secuenciaTipoT");
            query.setParameter("secuenciaTipoT", secuenciaTT);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasDiasTT> listaVigenciasDiasTT = query.getResultList();
            return listaVigenciasDiasTT;
        } catch (Exception e) {
            log.error("Error deportesTotalesSecuenciaPersona PersistenciaVigenciasDeportes :  ", e);
            return null;
        }
    }
}
