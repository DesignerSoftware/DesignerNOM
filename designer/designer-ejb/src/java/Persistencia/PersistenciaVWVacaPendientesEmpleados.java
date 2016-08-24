/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VWVacaPendientesEmpleados;
import InterfacePersistencia.PersistenciaVWVacaPendientesEmpleadosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br> Clase encargada de realizar operaciones sobre la vista
 * 'VWVacaPendientesEmpleados' de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVWVacaPendientesEmpleados implements PersistenciaVWVacaPendientesEmpleadosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*
     * @PersistenceContext(unitName = "DesignerRHN-ejbPU") private EntityManager
     * em;
     */
    @Override
    public void crear(EntityManager em, VWVacaPendientesEmpleados vacaP) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            System.out.println("getSecuencia: "+vacaP.getSecuencia());
            System.out.println("getEstado: "+vacaP.getEstado());
            System.out.println("getDiaspendientes: "+vacaP.getDiaspendientes());
            System.out.println("getEmpleado: "+vacaP.getEmpleado());
            System.out.println("getInicialcausacion: "+vacaP.getInicialcausacion());
            System.out.println("getFinalcausacion: "+vacaP.getFinalcausacion());
            tx.begin();
            em.merge(vacaP);
            tx.commit();
        } catch (Exception e) {
            System.out.println("La vigencia no exite o esta reservada por lo cual no puede ser modificada: " + e);
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("No se puede hacer rollback porque no hay una transacción");
            }
        }
    }

    @Override
    public void editar(EntityManager em, VWVacaPendientesEmpleados vacaP) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vacaP);
            tx.commit();
        } catch (Exception e) {
            System.out.println("La vigencia no exite o esta reservada por lo cual no puede ser modificada: " + e);
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("No se puede hacer rollback porque no hay una transacción");
            }
        }
    }

    @Override
    public void borrar(EntityManager em, VWVacaPendientesEmpleados vacaP) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vacaP));
            tx.commit();
        } catch (Exception e) {
            System.out.println("La vigencia no exite o esta reservada por lo cual no puede ser modificada: " + e);
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("No se puede hacer rollback porque no hay una transacción");
            }
        }
    }

    @Override
    public List<VWVacaPendientesEmpleados> vacaEmpleadoPendientes(EntityManager em, BigInteger secuencia) {
        List<VWVacaPendientesEmpleados> listaVacaPendientesEmpleados = null;
        try {
            em.clear();
            String script = "SELECT vwv FROM VWVacaPendientesEmpleados vwv WHERE vwv.empleado = :empleado AND vwv.diaspendientes > 0";
            Query query = em.createQuery(script).setParameter("empleado", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            listaVacaPendientesEmpleados = query.getResultList();
        } catch (Exception e) {
            System.err.println("PersistenciaVWVacaPendientesEmpleados.buscarVacaPendientesEmpleados.");
            System.out.println(e);
        } finally {
            return listaVacaPendientesEmpleados;
        }
    }

    @Override
    public List<VWVacaPendientesEmpleados> vacaEmpleadoDisfrutadas(EntityManager em, BigInteger sec) {
        List<VWVacaPendientesEmpleados> listaVacaPendientesEmpleados = null;
        try {
            em.clear();
            String script = "SELECT vwv FROM VWVacaPendientesEmpleados vwv WHERE vwv.empleado = :empleado AND vwv.diaspendientes <= 0";
            Query query = em.createQuery(script).setParameter("empleado", sec);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            listaVacaPendientesEmpleados = query.getResultList();
        } catch (Exception e) {
            System.err.println("PersistenciaVWVacaPendientesEmpleados.buscarVacaPendientesEmpleadosDisfrutadas");
            System.out.println(e);
        } finally {
            return listaVacaPendientesEmpleados;
        }
    }

    @Override
    public List<VWVacaPendientesEmpleados> buscarVacaPendientesEmpleados(EntityManager em, BigInteger secuenciaEmpleado) {
        List<VWVacaPendientesEmpleados> listaVacaPendientesEmpleados = null;
        try {
            em.clear();
            listaVacaPendientesEmpleados = em.createQuery("SELECT v FROM VWVacaPendientesEmpleados v WHERE v.empleado = :empleado").setParameter("empleado", secuenciaEmpleado).getResultList();
        } catch (Exception e) {
            System.err.println("PersistenciaVWVacaPendientesEmpleados.buscarVacaPendientesEmpleados.");
            System.out.println(e);
        } finally {
            return listaVacaPendientesEmpleados;
        }
    }
}
