/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.MotivosCambiosCargos;
import InterfacePersistencia.PersistenciaMotivosCambiosCargosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'MotivosCambiosCargos'
 * de la base de datos. (Para verificar que esta asociado a una VigenciaCargo,
 * se realiza la operacion sobre la tabla VigenciasCargos)
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaMotivosCambiosCargos implements PersistenciaMotivosCambiosCargosInterface {

   private static Logger log = Logger.getLogger(PersistenciaMotivosCambiosCargos.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/
    @Override
    public void crear(EntityManager em, MotivosCambiosCargos motivoCambioCargo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(motivoCambioCargo);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaMotivosCambiosCargos.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, MotivosCambiosCargos motivoCambioCargo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(motivoCambioCargo);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaMotivosCambiosCargos.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, MotivosCambiosCargos motivoCambioCargo) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(motivoCambioCargo));
            tx.commit();

        } catch (Exception e) {
            log.error("Error PersistenciaMotivosCambiosCargos.borrar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public MotivosCambiosCargos buscarMotivoCambioCargo(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            return em.find(MotivosCambiosCargos.class, secuencia);
        } catch (Exception e) {
            log.error("PersistenciaMotivosCambiosCargos.buscarMotivoCambioCargo():  ", e);
            return null;
        }
    }

    @Override
    public List<MotivosCambiosCargos> buscarMotivosCambiosCargos(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT m FROM MotivosCambiosCargos m");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<MotivosCambiosCargos> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            log.error("PersistenciaMotivosCambiosCargos.buscarMotivosCambiosCargos():  ", e);
            return null;
        }
    }

    @Override
    public BigInteger verificarBorradoVigenciasCargos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            Query query = em.createQuery("SELECT count(vc) FROM VigenciasCargos vc WHERE vc.motivocambiocargo.secuencia =:secMotivoCambioCargo");
            query.setParameter("secMotivoCambioCargo", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            retorno = new BigInteger(query.getSingleResult().toString());

        } catch (Exception e) {
            log.error("ERROR EN PersistenciaMotivosCambiosCargos verificarBorrado ERROR :  ", e);
        } finally {
            return retorno;
        }
    }
}
