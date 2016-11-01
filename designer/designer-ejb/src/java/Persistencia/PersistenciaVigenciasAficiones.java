/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasAficiones;
import InterfacePersistencia.PersistenciaVigenciasAficionesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'VigenciasAficiones'
 * de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVigenciasAficiones implements PersistenciaVigenciasAficionesInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
     */
    @Override
    public void crear(EntityManager em, VigenciasAficiones vigenciasAficiones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(vigenciasAficiones);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasAficiones.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, VigenciasAficiones vigenciasAficiones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasAficiones);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasAficiones.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, VigenciasAficiones vigenciasAficiones) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vigenciasAficiones));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasAficiones.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public VigenciasAficiones buscarvigenciaAficion(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            BigInteger in = (BigInteger) secuencia;
            return em.find(VigenciasAficiones.class, in);
        } catch (Exception e) {
            System.out.println("Error buscarvigenciaAficion : " + e.toString());
            return null;
        }
    }

    @Override
    public List<VigenciasAficiones> aficionesPersona(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(va) FROM VigenciasAficiones va WHERE va.persona.secuencia = :secuenciaPersona");
            query.setParameter("secuenciaPersona", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            if (resultado > 0) {
                Query queryFinal = em.createQuery("SELECT va FROM VigenciasAficiones va WHERE va.persona.secuencia = :secuenciaPersona and va.fechainicial = (SELECT MAX(vaf.fechainicial) FROM VigenciasAficiones vaf WHERE vaf.persona.secuencia = :secuenciaPersona)");
                queryFinal.setParameter("secuenciaPersona", secuencia);
                query.setHint("javax.persistence.cache.storeMode", "REFRESH");
                List<VigenciasAficiones> listVigenciasAficiones = queryFinal.getResultList();
                return listVigenciasAficiones;
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasAficiones.aficionesPersona" + e);
            return null;
        }
    }

    @Override
    public List<VigenciasAficiones> aficionesTotalesSecuenciaPersona(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            String sql = "SELECT * FROM VIGENCIASAFICIONES WHERE PERSONA = ?";
            Query queryFinal = em.createNativeQuery(sql, VigenciasAficiones.class);
            queryFinal.setParameter(1, secuencia);
            List<VigenciasAficiones> listVigenciasAficiones = queryFinal.getResultList();
            return listVigenciasAficiones;
        } catch (Exception e) {
            System.out.println("Error aficionesTotalesSecuenciaPersona PersistenciaVigenciasAficiones : " + e.toString());
            return null;
        }
    }

    @Override
    public String primeraAficion(EntityManager em, BigInteger secuencia) {
        String aficion;
        try {
            em.clear();
            String sql = "SELECT  SUBSTR (B.DESCRIPCION ||' '||TO_CHAR(A.FECHAINICIAL,'DD-MM-YYYY'),1,30)\n"
                    + "   FROM VIGENCIASAFICIONES A,AFICIONES B\n"
                    + "   WHERE A.PERSONA = ?\n"
                    + "   AND A.AFICION = B.SECUENCIA\n"
                    + "   AND A.FECHAINICIAL = (SELECT MAX(AI.FECHAINICIAL) FROM VIGENCIASAFICIONES AI WHERE AI.PERSONA =A.PERSONA)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, secuencia);
            aficion = (String) query.getSingleResult();
            return aficion;
        } catch (Exception e) {
            aficion = "SIN REGISTRAR";
            return aficion;
        }

    }
}
