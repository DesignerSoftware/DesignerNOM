/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Formulascontratos;
import InterfacePersistencia.PersistenciaFormulasContratosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'FormulasContratos' de
 * la base de datos.
 *
 * @author Andres Pineda.
 */
@Stateless
public class PersistenciaFormulasContratos implements PersistenciaFormulasContratosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    /* @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
    @Override
    public void crear(EntityManager em, Formulascontratos formulascontratos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(formulascontratos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaFormulasContratos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Formulascontratos formulascontratos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(formulascontratos);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.out.println("Error PersistenciaFormulasContratos.editar: " + e);
        }
    }

    @Override
    public void borrar(EntityManager em, Formulascontratos formulascontratos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(formulascontratos));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("Error PersistenciaFormulasContratos.borrar: " + e);
            }
        }
    }

    @Override
    public List<Formulascontratos> formulasContratosParaFormulaSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query queryFinal = em.createQuery("SELECT fc FROM Formulascontratos fc WHERE fc.formula.secuencia=:secuencia");
            queryFinal.setParameter("secuencia", secuencia);
            queryFinal.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Formulascontratos> formulascontratos = queryFinal.getResultList();
            return formulascontratos;
        } catch (Exception e) {
            System.out.println("Error PersistenciaFormulasContratos.formulasContratosParaFormulaSecuencia : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Formulascontratos> formulasContratosParaContratoSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query queryFinal = em.createQuery("SELECT fc FROM Formulascontratos fc WHERE fc.contrato.secuencia=:secuencia");
            queryFinal.setParameter("secuencia", secuencia);
            queryFinal.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Formulascontratos> formulascontratos = queryFinal.getResultList();
            return formulascontratos;
        } catch (Exception e) {
            System.out.println("Error PersistenciaFormulasContratos.formulasContratosParaFormulaSecuencia : " + e.toString());
            return null;
        }
    }

    public Formulascontratos formulasContratosParaContratoFormulasContratosEntidades(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query queryFinal = em.createQuery("SELECT fc FROM Formulascontratos fc WHERE fc.secuencia=:secuencia");
            queryFinal.setParameter("secuencia", secuencia);
            queryFinal.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Formulascontratos formulascontratos = (Formulascontratos) queryFinal.getSingleResult();
            return formulascontratos;
        } catch (Exception e) {
            System.out.println("Error PersistenciaFormulasContratos.formulasContratosParaContratoFormulasContratosEntidades : " + e.toString());
            return null;
        }
    }

}
