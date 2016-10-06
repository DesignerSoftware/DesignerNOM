/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.HVHojasDeVida;
import Entidades.HvEntrevistas;
import InterfacePersistencia.PersistenciaHvEntrevistasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'HvEntrevistas' de la
 * base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaHvEntrevistas implements PersistenciaHvEntrevistasInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /* @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
    public void crear(EntityManager em, HvEntrevistas hvEntrevistas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(hvEntrevistas);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaHvEntrevistas.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void editar(EntityManager em, HvEntrevistas hvEntrevistas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(hvEntrevistas);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.out.println("Error PersistenciaHvEntrevistas.editar: " + e);
        }
    }

    public void borrar(EntityManager em, HvEntrevistas hvEntrevistas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(hvEntrevistas));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("Error PersistenciaHvEntrevistas.borrar: " + e);
            }
        }
    }

    public HvEntrevistas buscarHvEntrevista(EntityManager em, BigInteger secuenciaHvEntrevista) {
        try {
            em.clear();
            return em.find(HvEntrevistas.class, secuenciaHvEntrevista);
        } catch (Exception e) {
            return null;
        }
    }

    public List<HvEntrevistas> buscarHvEntrevistas(EntityManager em) {
        em.clear();
        Query query = em.createQuery("SELECT te FROM HvEntrevistas te ORDER BY te.fecha ASC ");
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        List<HvEntrevistas> listHvEntrevistas = query.getResultList();
        return listHvEntrevistas;

    }

    public List<HvEntrevistas> buscarHvEntrevistasPorEmpleado(EntityManager em, BigInteger secEmpleado) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT he FROM HVHojasDeVida hv , HvEntrevistas he , Empleados e WHERE hv.secuencia = he.hojadevida.secuencia AND e.persona.secuencia= hv.persona.secuencia AND e.secuencia = :secuenciaEmpl ORDER BY he.fecha ");
            query.setParameter("secuenciaEmpl", secEmpleado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<HvEntrevistas> listHvEntrevistas = query.getResultList();
            return listHvEntrevistas;
        } catch (Exception e) {
            System.err.println("Error en PERSISTENCIAHVENTREVISTAS ERROR " + e);
            return null;
        }
    }

    public List<HVHojasDeVida> buscarHvHojaDeVidaPorPersona(EntityManager em, BigInteger secPersona) {
        try {
            em.clear();
            String sql= "SELECT * FROM HVHOJASDEVIDA hv , PERSONAS p WHERE p.secuencia= hv.persona AND p.secuencia = ?";
            Query query = em.createNativeQuery(sql, HVHojasDeVida.class);
            query.setParameter(1, secPersona);
            List<HVHojasDeVida> hvHojasDeVIda = query.getResultList();
            return hvHojasDeVIda;
        } catch (Exception e) {
            System.out.println("Error en Persistencia HvEntrevistas buscarHvHojaDeVidaPorEmpleado " + e);
            return null;
        }
    }

    @Override
    public List<HvEntrevistas> entrevistasPersona(EntityManager em, BigInteger secuenciaHV) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(hve) FROM HvEntrevistas hve WHERE hve.hojadevida.secuencia = :secuenciaHV");
            query.setParameter("secuenciaHV", secuenciaHV);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            if (resultado > 0) {
                Query queryFinal = em.createQuery("SELECT hve FROM HvEntrevistas hve WHERE hve.hojadevida.secuencia = :secuenciaHV and hve.fecha = (SELECT MAX(hven.fecha) FROM HvEntrevistas hven WHERE hven.hojadevida.secuencia = :secuenciaHV)");
                queryFinal.setParameter("secuenciaHV", secuenciaHV);
                query.setHint("javax.persistence.cache.storeMode", "REFRESH");
                List<HvEntrevistas> listaHvEntrevistas = queryFinal.getResultList();
                return listaHvEntrevistas;
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error PersistenciaHvEntrevistas.entrevistasPersona" + e);
            return null;
        }
    }
}
