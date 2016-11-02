/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.InformacionesAdicionales;
import InterfacePersistencia.PersistenciaInformacionesAdicionalesInterface;
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
 * 'InformacionesAdicionales' de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaInformacionesAdicionales implements PersistenciaInformacionesAdicionalesInterface {

//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     *
     * @param em
     * @param informacionesAdicionales
     */
    @Override
    public void crear(EntityManager em, InformacionesAdicionales informacionesAdicionales) {
        System.out.println(this.getClass().getName() + ".crear()");
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(informacionesAdicionales);
            tx.commit();
        } catch (Exception e) {
            System.out.println("error en crear");
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, InformacionesAdicionales informacionesAdicionales) {
        System.out.println(this.getClass().getName() + ".editar()");
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(informacionesAdicionales);
            tx.commit();
        } catch (Exception e) {
            System.out.println("error en editar");
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, InformacionesAdicionales informacionesAdicionales) {
        System.out.println(this.getClass().getName() + ".borrar()");
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(informacionesAdicionales));
            tx.commit();
        } catch (Exception e) {
            System.out.println("error en borrar");
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public InformacionesAdicionales buscarinformacionAdicional(EntityManager em, BigInteger secuencia) {
        em.clear();
        return em.find(InformacionesAdicionales.class, secuencia);
    }

    @Override
    public List<InformacionesAdicionales> buscarinformacionesAdicionales(EntityManager em) {
        System.out.println(this.getClass().getName() + ".buscarinformacionesAdicionales()");
        try {
            em.clear();
            javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(InformacionesAdicionales.class));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.out.println("error buscando informacion adicional");
            e.printStackTrace();
            return null;
        }
    }

    private Long conteoInformacionAdicionalPersona(EntityManager em, BigInteger secuenciaEmpleado) {
        System.out.println(this.getClass().getName() + ".conteoInformacionAdicionalPersona()");
        Long resultado = null;
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(ia) FROM InformacionesAdicionales ia WHERE ia.empleado.secuencia = :secuenciaEmpleado");
            query.setParameter("secuenciaEmpleado", secuenciaEmpleado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            resultado = (Long) query.getSingleResult();
            return resultado;
        } catch (Exception e) {
            System.out.println("Error en el conteo de informacion adicional");
            e.printStackTrace();
            return resultado;
        }
    }

    @Override
    public List<InformacionesAdicionales> informacionAdicionalPersona(EntityManager em, BigInteger secuenciaEmpleado) {
        System.out.println(this.getClass().getName() + ".informacionAdicionalPersona()");
        Long resultado = this.conteoInformacionAdicionalPersona(em, secuenciaEmpleado);
        if (resultado != null && resultado > 0) {
            try {
                /*em.clear();
                 Query query = em.createQuery("SELECT COUNT(ia) FROM InformacionesAdicionales ia WHERE ia.empleado.secuencia = :secuenciaEmpleado");
                 query.setParameter("secuenciaEmpleado", secuenciaEmpleado);
                 query.setHint("javax.persistence.cache.storeMode", "REFRESH");
                 Long resultado = (Long) query.getSingleResult();*/
                Query queryFinal = em.createQuery("SELECT ia FROM InformacionesAdicionales ia WHERE ia.empleado.secuencia = :secuenciaEmpleado and ia.fechainicial = (SELECT MAX(iad.fechainicial) FROM InformacionesAdicionales iad WHERE iad.empleado.secuencia = :secuenciaEmpleado)");
                queryFinal.setParameter("secuenciaEmpleado", secuenciaEmpleado);
                queryFinal.setHint("javax.persistence.cache.storeMode", "REFRESH");
                List<InformacionesAdicionales> listaInformacionesAdicionales = queryFinal.getResultList();
                return listaInformacionesAdicionales;
            } catch (Exception e) {
                System.out.println("Error PersistenciaInformacionesAdicionales.informacionAdicionalPersona" + e);
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public List<InformacionesAdicionales> informacionAdicionalEmpleadoSecuencia(EntityManager em, BigInteger secuenciaEmpleado) {
        System.out.println(this.getClass().getName() + ".informacionAdicionalEmpleadoSecuencia()");
        try {
            em.clear();
            Query query = em.createQuery("SELECT ia FROM InformacionesAdicionales ia WHERE ia.empleado.secuencia = :secuenciaEmpleado");
            query.setParameter("secuenciaEmpleado", secuenciaEmpleado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<InformacionesAdicionales> resultado = (List<InformacionesAdicionales>) query.getResultList();
            return resultado;
        } catch (Exception e) {
            System.out.println("Error PersistenciaInformacionesAdicionales.informacionAdicionalEmpleadoSecuencia : ");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String primeraInformacionAdicional(EntityManager em, BigInteger secuenciaEmpleado) {
        String infoAd;
        try {
            em.clear();
            String sql = "SELECT SUBSTR(I.DESCRIPCION||' '||TO_CHAR(FECHAINICIAL,'DD-MM-YYYY'),1,30) \n"
                    + "   FROM INFORMACIONESADICIONALES I \n"
                    + "   WHERE I.EMPLEADO = (select secuencia from empleados where persona=?) \n"
                    + "   AND I.FECHAINICIAL = (SELECT MAX(V.FECHAINICIAL) FROM INFORMACIONESADICIONALES V WHERE V.EMPLEADO = I.EMPLEADO)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, secuenciaEmpleado);
            infoAd = (String) query.getSingleResult();
            return infoAd;
        } catch (Exception e) {
            infoAd = "";
            return infoAd;
        }

    }
}
