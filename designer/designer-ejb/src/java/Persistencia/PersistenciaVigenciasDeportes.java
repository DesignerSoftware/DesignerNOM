/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasDeportes;
import InterfacePersistencia.PersistenciaVigenciasDeportesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'VigenciasDeportes' de
 * la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVigenciasDeportes implements PersistenciaVigenciasDeportesInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
     */
    @Override
    public void crear(EntityManager em, VigenciasDeportes vigenciasDeportes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(vigenciasDeportes);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasDeportes.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, VigenciasDeportes vigenciasDeportes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasDeportes);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasDeportes.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, VigenciasDeportes vigenciasDeportes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vigenciasDeportes));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasDeportes.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<VigenciasDeportes> deportePersona(EntityManager em, BigInteger secuenciaPersona) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(vd) FROM VigenciasDeportes vd WHERE vd.persona.secuencia = :secuenciaPersona");
            query.setParameter("secuenciaPersona", secuenciaPersona);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            if (resultado > 0) {
                Query queryFinal = em.createQuery("SELECT vd FROM VigenciasDeportes vd WHERE vd.persona.secuencia = :secuenciaPersona and vd.fechainicial = (SELECT MAX(vde.fechainicial) FROM VigenciasDeportes vde WHERE vde.persona.secuencia = :secuenciaPersona)");
                queryFinal.setParameter("secuenciaPersona", secuenciaPersona);
                queryFinal.setHint("javax.persistence.cache.storeMode", "REFRESH");
                List<VigenciasDeportes> listaVigenciasDeportes = queryFinal.getResultList();
                return listaVigenciasDeportes;
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error PersistenciaVigenciasDeportes.deportePersona" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<VigenciasDeportes> deportesTotalesSecuenciaPersona(EntityManager em, BigInteger secuenciaP) {
        try {
            em.clear();
            Query queryFinal = em.createQuery("SELECT vd FROM VigenciasDeportes vd WHERE vd.persona.secuencia = :secuenciaPersona");
            queryFinal.setParameter("secuenciaPersona", secuenciaP);
            queryFinal.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasDeportes> listaVigenciasDeportes = queryFinal.getResultList();
            return listaVigenciasDeportes;
        } catch (Exception e) {
            System.out.println("Error deportesTotalesSecuenciaPersona PersistenciaVigenciasDeportes : " + e.getMessage());
            return null;
        }
    }

    @Override
    public String consultarPrimerDeporte(EntityManager em, BigInteger secuencia) {
        String deporte;
        try {
            em.clear();
            String sql = "SELECT SUBSTR(B.NOMBRE||' '||TO_CHAR(A.FECHAINICIAL,'DD-MM-YYYY'),1,30)\n"
                    + "    FROM VIGENCIASDEPORTES A, DEPORTES B\n"
                    + "    WHERE A.persona = ?\n"
                    + "    AND A.deporte = B.secuencia\n"
                    + "    AND A.FECHAINICIAL= (SELECT MAX(V.FECHAINICIAL) FROM VIGENCIASDEPORTES V WHERE V.PERSONA = A.PERSONA)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, secuencia);
            deporte = (String) query.getSingleResult();
            return deporte;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaVigenciasDeportes.consultarPrimerDeporte()" + e.getMessage());
            deporte = "";
            return deporte;
        }
    }
}
