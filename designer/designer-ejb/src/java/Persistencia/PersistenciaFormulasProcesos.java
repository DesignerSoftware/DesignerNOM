/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.FormulasProcesos;
import InterfacePersistencia.PersistenciaFormulasProcesosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla
 * 'FormulasProcesos' de la base de datos.
 * @author Andres Pineda
 */
@Stateless
public class PersistenciaFormulasProcesos implements PersistenciaFormulasProcesosInterface{
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/

    @Override
    public void crear(EntityManager em,FormulasProcesos formulasProcesos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(formulasProcesos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaFormulasProcesos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em,FormulasProcesos formulasProcesos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(formulasProcesos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaFormulasProcesos.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em,FormulasProcesos formulasProcesos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(formulasProcesos));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("Error PersistenciaFormulasProcesos.borrar: " + e);
            }
        }
    }

    @Override
    public List<FormulasProcesos> formulasProcesosParaFormulaSecuencia(EntityManager em,BigInteger secuencia) {
        try {
            em.clear();
            Query queryFinal = em.createQuery("SELECT fp FROM FormulasProcesos fp WHERE fp.formula.secuencia=:secuencia");
            queryFinal.setParameter("secuencia", secuencia);
            queryFinal.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<FormulasProcesos> formulasProcesos = queryFinal.getResultList();
            return formulasProcesos;
        } catch (Exception e) {
            System.out.println("Error PersistenciaFormulasProcesos.formulasProcesosParaFormulaSecuencia : " + e.toString());
            return null;
        }
    }
        
    @Override 
    public List<FormulasProcesos> formulasProcesosParaProcesoSecuencia(EntityManager em,BigInteger secuencia) {
        try {
            em.clear();
            Query queryFinal = em.createQuery("SELECT fp FROM FormulasProcesos fp WHERE fp.proceso.secuencia=:secuencia");
            queryFinal.setParameter("secuencia", secuencia);
            queryFinal.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<FormulasProcesos> formulasProcesos = queryFinal.getResultList();
            return formulasProcesos;
        } catch (Exception e) {
            System.out.println("Error PersistenciaFormulasProcesos.formulasProcesosParaProcesoSecuencia : " + e.toString());
            return null;
        }
    }
}
