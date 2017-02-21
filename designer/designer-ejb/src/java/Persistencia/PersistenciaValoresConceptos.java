/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Persistencia;

import Entidades.ValoresConceptos;
import Entidades.ValoresConceptos;
import InterfacePersistencia.PersistenciaValoresConceptosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaValoresConceptos implements PersistenciaValoresConceptosInterface {

     /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
/*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
*/

    public void crear(EntityManager em, ValoresConceptos valoresConceptos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(valoresConceptos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaValoresConceptos.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void editar(EntityManager em, ValoresConceptos valoresConceptos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(valoresConceptos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaValoresConceptos.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void borrar(EntityManager em, ValoresConceptos valoresConceptos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(valoresConceptos));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaValoresConceptos.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
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
            System.out.println("Error consultarValoresConceptos" + e.getMessage());
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
            System.out.println("Error consultarValoresConceptos" + e.getMessage());
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
            System.out.println("Persistencia.PersistenciaValoresConceptos.consultarConceptoValorConcepto()" + e.getMessage());
            return null;
        }
    }
}
