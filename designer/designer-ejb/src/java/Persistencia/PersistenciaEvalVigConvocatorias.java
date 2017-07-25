/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.Evalvigconvocatorias;
import InterfacePersistencia.PersistenciaEvalVigConvocatoriasInterface;
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
public class PersistenciaEvalVigConvocatorias implements PersistenciaEvalVigConvocatoriasInterface {

   private static Logger log = Logger.getLogger(PersistenciaEvalVigConvocatorias.class);

    @Override
    public void crear(EntityManager em, Evalvigconvocatorias evalvigconvocatoria) {
       em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(evalvigconvocatoria);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaEvalVigConvocatorias.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Evalvigconvocatorias evalvigconvocatoria) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(evalvigconvocatoria);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaEvalVigConvocatorias.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Evalvigconvocatorias evalvigconvocatoria) {
       em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(evalvigconvocatoria));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                log.error("Error PersistenciaEvalConvocatorias.borrar: " + e);
            }
        }
    }

    @Override
    public List<Evalvigconvocatorias> consultarEvalConvocatorias(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM EVALVIGCONVOCATORIAS";
            Query query = em.createNativeQuery(sql, Evalvigconvocatorias.class);
            List<Evalvigconvocatorias> evalvigconvocatorias = query.getResultList();
            return evalvigconvocatorias;
        } catch (Exception e) {
            log.error("Error en PersistenciaEvalConvocatorias.consultarEvalConvocatorias ERROR" + e);
            return null;
        }
    }

}
