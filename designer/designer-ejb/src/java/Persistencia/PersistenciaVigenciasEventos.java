/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasEventos;
import InterfacePersistencia.PersistenciaVigenciasEventosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'VigenciasEventos' de
 * la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVigenciasEventos implements PersistenciaVigenciasEventosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
     */
    @Override
    public void crear(EntityManager em, VigenciasEventos vigenciasEventos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(vigenciasEventos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasEventos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, VigenciasEventos vigenciasEventos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasEventos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasEventos.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, VigenciasEventos vigenciasEventos) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vigenciasEventos));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasEventos.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<VigenciasEventos> eventosEmpleado(EntityManager em, BigInteger secuenciaEmpl) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(ve) FROM VigenciasEventos ve WHERE ve.empleado.secuencia = :secuenciaEmpl");
            query.setParameter("secuenciaEmpl", secuenciaEmpl);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            if (resultado > 0) {
                Query queryFinal = em.createQuery("SELECT ve FROM VigenciasEventos ve WHERE ve.empleado.secuencia = :secuenciaEmpl and ve.fechainicial = (SELECT MAX(vev.fechainicial) FROM VigenciasEventos vev WHERE vev.empleado.secuencia = :secuenciaEmpl)");
                queryFinal.setParameter("secuenciaEmpl", secuenciaEmpl);
                List<VigenciasEventos> listaVigenciasEventos = queryFinal.getResultList();
                return listaVigenciasEventos;
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasEventos.eventosPersona" + e);
            return null;
        }
    }

    @Override
    public List<VigenciasEventos> vigenciasEventosSecuenciaEmpleado(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT ve FROM VigenciasEventos ve WHERE ve.empleado.secuencia = :secuenciaEmpl");
            query.setParameter("secuenciaEmpl", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasEventos> resultado = (List<VigenciasEventos>) query.getResultList();
            return resultado;
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasEventos.vigenciasEventosSecuenciaEmpleado" + e);
            return null;
        }
    }

    @Override
    public String primerEvento(EntityManager em, BigInteger secPersona) {
        String evento;
        try {
            em.clear();
            String sql = "SELECT SUBSTR(B.DESCRIPCION ||' '|| TO_CHAR(FECHAINICIAL,'DD-MM-YYYY'),1,30)\n"
                    + "    FROM vigenciaseventos A, EVENTOS B\n"
                    + "    WHERE A.empleado = (select secuencia from empleados where persona=?) \n"
                    + "    AND A.evento = B.secuencia\n"
                    + "    AND A.FECHAINICIAL = (SELECT MAX(V.FECHAINICIAL) FROM vigenciaseventos  V WHERE V.EMPLEADO = A.EMPLEADO)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, secPersona);
            evento = (String) query.getSingleResult();
            return evento;
        } catch (Exception e) {
            evento = "SIN REGISTRAR";
            return evento;
        }
    }
}
