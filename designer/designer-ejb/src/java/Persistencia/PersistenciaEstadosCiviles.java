/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.EstadosCiviles;
import InterfacePersistencia.PersistenciaEstadosCivilesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless Clase encargada de realizar operaciones sobre la tabla
 * 'EstadosCiviles' de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaEstadosCiviles implements PersistenciaEstadosCivilesInterface {

   private static Logger log = Logger.getLogger(PersistenciaEstadosCiviles.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    /* @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
    @Override
    public void crear(EntityManager em, EstadosCiviles estadosCiviles) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(estadosCiviles);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaEstadosCiviles.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, EstadosCiviles estadosCiviles) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(estadosCiviles);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaEstadosCiviles.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, EstadosCiviles estadosCiviles) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(estadosCiviles));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                log.error("Error PersistenciaEstadosCiviles.borrar: " + e);
            }
        }
    }

    @Override
    public EstadosCiviles buscarEstadoCivil(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            return em.find(EstadosCiviles.class, secuencia);
        } catch (Exception e) {
            log.error("Error buscarEstadoCivil PersistenciaEstadosCiviles : " + e.toString());
            return null;
        }
    }

    @Override
    public List<EstadosCiviles> consultarEstadosCiviles(EntityManager em) {
        try{
        em.clear();
        String sql ="SELECT * FROM ESTADOSCIVILES ORDER BY CODIGO";
        Query query = em.createNativeQuery(sql, EstadosCiviles.class);
        List<EstadosCiviles> listEstadosCiviles = query.getResultList();
        return listEstadosCiviles;
        } catch(Exception e){
            log.error("error consultarEstadosCiviles PersistenciaEstadosCivilires : " + e.toString());
            return null;
        }
    }

    public BigInteger contadorVigenciasEstadosCiviles(EntityManager em, BigInteger secuencia) {
        BigInteger retorno;
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*) FROM vigenciasestadosciviles WHERE estadocivil = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            log.error("PERSISTENCIAESTADOSCIVILES contadorVigenciasEstadosCiviles = " + retorno);
            return retorno;
        } catch (Exception e) {
            log.error("ERROR PERSISTENCIAESTADOSCIVILES contadorVigenciasEstadosCiviles  ERROR = " + e);
            retorno = new BigInteger("-1");
            return retorno;
        }
    }
}
