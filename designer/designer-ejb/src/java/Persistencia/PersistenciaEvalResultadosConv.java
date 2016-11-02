/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.EvalResultadosConv;
import InterfacePersistencia.PersistenciaEvalResultadosConvInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'EvalResultadosConv'
 * de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaEvalResultadosConv implements PersistenciaEvalResultadosConvInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/
    @Override
    public List<EvalResultadosConv> pruebasAplicadasPersona(EntityManager em, BigInteger secuenciaEmpleado) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(er) FROM EvalResultadosConv er WHERE er.empleado.secuencia = :secuenciaEmpleado");
            query.setParameter("secuenciaEmpleado", secuenciaEmpleado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            if (resultado > 0) {
                Query queryFinal = em.createQuery("SELECT er FROM EvalResultadosConv er WHERE er.empleado.secuencia = :secuenciaEmpleado and er.fechaperiododesde = (SELECT MAX(ere.fechaperiododesde) FROM EvalResultadosConv ere WHERE ere.empleado.secuencia = :secuenciaEmpleado)");
                queryFinal.setParameter("secuenciaEmpleado", secuenciaEmpleado);
                query.setHint("javax.persistence.cache.storeMode", "REFRESH");
                List<EvalResultadosConv> listaPruebasAplicadas = queryFinal.getResultList();
                return listaPruebasAplicadas;
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error PersistenciaEvalResultadosConv.pruebasAplicadasPersona" + e);
            return null;
        }
    }

    @Override
    public void crear(EntityManager em, EvalResultadosConv evalresconvocatoria) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(evalresconvocatoria);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaEvalResultadosConv.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, EvalResultadosConv evalresconvocatoria) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(evalresconvocatoria);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaEvalResultadosConv.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, EvalResultadosConv evalresconvocatoria) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(evalresconvocatoria));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("Error PersistenciaEvalConvocatorias.borrar: " + e);
            }
        }
    }

    @Override
    public List<EvalResultadosConv> consultarEvalResultadosConvocatorias(EntityManager em,BigInteger secuenciaEmpleado) {
        try {
            em.clear();
            String sql = "SELECT * FROM EvalResultadosConv WHERE empleado = ? \n" ;
            Query query = em.createNativeQuery(sql, EvalResultadosConv.class);
            query.setParameter(1, secuenciaEmpleado);
            List<EvalResultadosConv> evalresconv = query.getResultList();
            return evalresconv;
        } catch (Exception e) {
            System.out.println("Error en PersistenciaEvalConvocatorias.consultarEvalConvocatorias ERROR" + e);
            return null;
        }
    }

    @Override
    public String primerPruebaAplicada(EntityManager em, BigInteger secuenciaEmpleado) {
       String pruebas;
        try {
            em.clear();
            String sql = "SELECT * FROM EvalResultadosConv WHERE empleado = ? \n" ;
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, secuenciaEmpleado);
            pruebas = (String) query.getSingleResult();
            return pruebas;
        } catch (Exception e) {
           pruebas="";
            return pruebas;
        }
    }
}
