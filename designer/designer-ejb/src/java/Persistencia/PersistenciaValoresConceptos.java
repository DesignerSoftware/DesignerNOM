/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.ValoresConceptos;
import InterfacePersistencia.PersistenciaValoresConceptosInterface;
import java.math.BigInteger;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import org.eclipse.persistence.exceptions.DatabaseException;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaValoresConceptos implements PersistenciaValoresConceptosInterface {

    private static Logger log = Logger.getLogger(PersistenciaValoresConceptos.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
     */
    public String crear(EntityManager em, ValoresConceptos valoresConceptos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(valoresConceptos);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaValoresConceptos.crear:  ", e);
                return e.getMessage();
            } else {
                return "Ha ocurrido un error al crear el valor Concepto";
            }
        }
    }

    public String editar(EntityManager em, ValoresConceptos valoresConceptos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(valoresConceptos);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaValoresConceptos.editar:  ", e);
                return e.getMessage();
            } else {
                return "Ha ocurrido un error al editar el valor Concepto";
            }
        }
    }

    public String borrar(EntityManager em, ValoresConceptos valoresConceptos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(valoresConceptos));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaValoresConceptos.borrar:  ", e);
                return e.getMessage();
            } else {
                return "Ha ocurrido un error al borrar el valor Concepto";
            }
        }
    }

    public List<ValoresConceptos> consultarValoresConceptos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT te FROM ValoresConceptos te");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<ValoresConceptos> valoresConceptos = query.getResultList();
            return valoresConceptos;
        } catch (Exception e) {
            log.error("Error consultarValoresConceptos ", e);
            return null;
        }
    }

    public ValoresConceptos consultarValorConcepto(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT te FROM ValoresConceptos te WHERE te.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            ValoresConceptos valoresConceptos = (ValoresConceptos) query.getSingleResult();
            return valoresConceptos;
        } catch (Exception e) {
            log.error("Error consultarValoresConceptos ", e);
            return null;
        }
    }

    public BigInteger consultarConceptoValorConcepto(EntityManager em, BigInteger concepto) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(te) FROM ValoresConceptos te WHERE te.concepto.secuencia = :concepto");
            query.setParameter("concepto", concepto);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            BigInteger conceptosSoportes = new BigInteger(query.getSingleResult().toString());
            return conceptosSoportes;
        } catch (Exception e) {
            log.error("PersistenciaValoresConceptos.consultarConceptoValorConcepto():  ", e);
            return null;
        }
    }
}
