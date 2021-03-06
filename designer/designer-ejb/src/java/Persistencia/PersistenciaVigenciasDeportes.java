/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasDeportes;
import InterfacePersistencia.PersistenciaVigenciasDeportesInterface;
import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
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

    private static Logger log = Logger.getLogger(PersistenciaVigenciasDeportes.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
     */
    @Override
    public String crear(EntityManager em, VigenciasDeportes vigenciasDeportes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(vigenciasDeportes);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasDeportes.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLException || e instanceof SQLSyntaxErrorException || e instanceof RuntimeException) {
                return e.toString();
            } else {
                return "ERROR_PERSISTENCIA";
            }
        }
    }

    @Override
    public String editar(EntityManager em, VigenciasDeportes vigenciasDeportes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasDeportes);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasDeportes.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLException || e instanceof SQLSyntaxErrorException || e instanceof RuntimeException) {
                return e.toString();
            } else {
                return "ERROR_PERSISTENCIA";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, VigenciasDeportes vigenciasDeportes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vigenciasDeportes));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            log.error("Error PersistenciaVigenciasDeportes.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLException || e instanceof SQLSyntaxErrorException || e instanceof RuntimeException) {
                return e.toString();
            } else {
                return "ERROR_PERSISTENCIA";
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
            log.error("Error PersistenciaVigenciasDeportes.deportePersona ", e);
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
            log.error("Error deportesTotalesSecuenciaPersona PersistenciaVigenciasDeportes :  ", e);
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
            if (e.getMessage().contains("did not retrieve any entities")) {
                log.trace("PersistenciaVigenciasDeportes.consultarPrimerDeporte(): " + e);
            } else {
                log.error("PersistenciaVigenciasDeportes.consultarPrimerDeporte():  ", e);
            }
            deporte = "";
            return deporte;
        }
    }
}
