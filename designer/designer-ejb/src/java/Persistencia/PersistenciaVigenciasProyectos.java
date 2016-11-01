/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasProyectos;
import InterfacePersistencia.PersistenciaVigenciasProyectosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'VigenciasProyectos'
 * de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVigenciasProyectos implements PersistenciaVigenciasProyectosInterface {

    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
     */
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     *
     * @param em
     * @param vigenciasProyectos
     */
    @Override
    public void crear(EntityManager em, VigenciasProyectos vigenciasProyectos) {
        System.out.println(this.getClass().getName() + ".crear()");
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasProyectos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasProyectos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
            System.out.println("se cerro la transacción");
        }
    }

    @Override
    public void editar(EntityManager em, VigenciasProyectos vigenciasProyectos) {
        System.out.println(this.getClass().getName() + ".editar()");
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasProyectos);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasProyectos.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
            System.out.println("Se cerro la transacción");
        }
    }

    @Override
    public void borrar(EntityManager em, VigenciasProyectos vigenciasProyectos) {
        System.out.println(this.getClass().getName() + ".borrar()");
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vigenciasProyectos));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasProyectos.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
            System.out.println("Se cerro la transaccion");
        }
    }

    @Override
    public List<VigenciasProyectos> buscarVigenciasProyectos(EntityManager em) {
        System.out.println(this.getClass().getName() + ".buscarVigenciasProyectos()");
        try {
            em.clear();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VigenciasProyectos.class));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.out.println("Error en buscarVigenciasProyectos");
            e.printStackTrace();
            return null;
        }
    }

    private Long contarProyectosEmpleado(EntityManager em, BigInteger secuenciaEmpleado) {
        System.out.println(this.getClass().getName() + ".contarProyectosEmpleado()");
        em.clear();
        Query query = em.createQuery("SELECT COUNT(vp) FROM VigenciasProyectos vp WHERE vp.empleado.secuencia = :secuenciaEmpleado");
        query.setParameter("secuenciaEmpleado", secuenciaEmpleado);
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        Long resultado = (Long) query.getSingleResult();
        return resultado;
    }

    @Override
    public List<VigenciasProyectos> proyectosEmpleado(EntityManager em, BigInteger secuenciaEmpleado) {
        System.out.println(this.getClass().getName() + ".proyectosEmpleado()");
//        Long resultado = null;
//        if (resultado != null && resultado > 0) {
        try {
            /*em.clear();
                 Query query = em.createQuery("SELECT COUNT(vp) FROM VigenciasProyectos vp WHERE vp.empleado.secuencia = :secuenciaEmpleado");
                 query.setParameter("secuenciaEmpleado", secuenciaEmpleado);
                 query.setHint("javax.persistence.cache.storeMode", "REFRESH");
                 Long resultado = (Long) query.getSingleResult();*/
            Query queryFinal = em.createQuery("SELECT vp FROM VigenciasProyectos vp WHERE vp.empleado.secuencia = :secuenciaEmpleado and vp.fechainicial = (SELECT MAX(vpr.fechainicial) FROM VigenciasProyectos vpr WHERE vpr.empleado.secuencia = :secuenciaEmpleado)");
            queryFinal.setParameter("secuenciaEmpleado", secuenciaEmpleado);
            List<VigenciasProyectos> listaVigenciasProyectos = queryFinal.getResultList();
            return listaVigenciasProyectos;
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasProyectos.proyectosPersona" + e);
            return null;
        }
//        } else {
//            return null;
//        }
    }

    @Override
    public List<VigenciasProyectos> vigenciasProyectosEmpleado(EntityManager em, BigInteger secuenciaEmpleado) {
        System.out.println(this.getClass().getName() + ".vigenciasProyectosEmpleado()");
        try {
            em.clear();
            Query query = em.createQuery("SELECT vp FROM VigenciasProyectos vp WHERE vp.empleado.secuencia= :secuenciaEmpleado ORDER BY vp.fechainicial DESC");
            query.setParameter("secuenciaEmpleado", secuenciaEmpleado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasProyectos> listaVigenciasProyectos = query.getResultList();
            return listaVigenciasProyectos;
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasProyectos.vigenciasProyectosEmpleado" + e);
            return null;
        }
    }

    @Override
    public String primerProyecto(EntityManager em, BigInteger secuenciaPersona) {
        String proyecto;
        try {
            em.clear();
            String sql = "SELECT SUBSTR(p.nombreproyecto||' '||TO_CHAR(v.fechainicial,'DD-MM-YYYY'),1,30)\n"
                    + "   FROM vigenciasproyectos V,proyectos p\n"
                    + "   WHERE V.empleado = (select secuencia from empleados where persona=?) \n"
                    + "   AND   V.proyecto = p.secuencia \n"
                    + "   AND   V.FECHAINICIAL=(SELECT MAX(A.FECHAINICIAL) FROM VIGENCIASPROYECTOS A WHERE A.EMPLEADO = V.EMPLEADO)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, secuenciaPersona);
            proyecto = (String) query.getSingleResult();
            return proyecto;
        } catch (Exception e) {
            proyecto = "SIN REGISTRAR";
            return proyecto;
        }
    }
}
