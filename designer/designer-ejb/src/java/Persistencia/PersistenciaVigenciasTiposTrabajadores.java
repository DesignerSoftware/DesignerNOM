/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.VigenciasTiposTrabajadores;
import InterfacePersistencia.PersistenciaVigenciasTiposTrabajadoresInterface;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla
 * 'VigenciasTiposTrabajadores' de la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaVigenciasTiposTrabajadores implements PersistenciaVigenciasTiposTrabajadoresInterface {

   private static Logger log = Logger.getLogger(PersistenciaVigenciasTiposTrabajadores.class);

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     *
     * @param em
     * @return
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
     */
    @Override
    public boolean crear(EntityManager em, VigenciasTiposTrabajadores vigenciasTiposTrabajadores) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            log.error("Entro en el try de PersistenciaVigenciasTiposTrabajadores.crear()");
            tx.begin();
            em.persist(vigenciasTiposTrabajadores);
            tx.commit();
            log.error("Ya creo V tipoTrabajador");
            return true;
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaVigenciasTiposTrabajadores.crear()" + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
            return false;
        }
    }

    @Override
    public void editar(EntityManager em, VigenciasTiposTrabajadores vigenciasTiposTrabajadores) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciasTiposTrabajadores);
            tx.commit();
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaVigenciasTiposTrabajadores.editar()" + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, VigenciasTiposTrabajadores vigenciasTiposTrabajadores) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vigenciasTiposTrabajadores));
            tx.commit();
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaVigenciasTiposTrabajadores.borrar()" + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<VigenciasTiposTrabajadores> buscarVigenciasTiposTrabajadores(EntityManager em) {
        try {
            em.clear();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VigenciasTiposTrabajadores.class));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaVigenciasTiposTrabajadores.buscarVigenciasTiposTrabajadores()" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<VigenciasTiposTrabajadores> buscarVigenciasTiposTrabajadoresEmpleado(EntityManager em, BigInteger secEmpleado) {
        try {
            em.clear();
            String sql = "SELECT * FROM VigenciasTiposTrabajadores  WHERE empleado = ? ORDER BY fechavigencia DESC";
            Query query = em.createNativeQuery(sql, VigenciasTiposTrabajadores.class);
            query.setParameter(1, secEmpleado);
            List<VigenciasTiposTrabajadores> vigenciasTiposTrabajadores = query.getResultList();
            return vigenciasTiposTrabajadores;
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaVigenciasTiposTrabajadores.buscarVigenciasTiposTrabajadoresEmpleado()" + e.getMessage());
            return null;
        }
    }

    @Override
    public VigenciasTiposTrabajadores buscarVigenciasTiposTrabajadoresSecuencia(EntityManager em, BigInteger secVTT) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT v FROM VigenciasTiposTrabajadores v WHERE v.secuencia = : secuencia").setParameter("secuencia", secVTT);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            VigenciasTiposTrabajadores vigenciasTiposTrabajadores = (VigenciasTiposTrabajadores) query.getSingleResult();
            return vigenciasTiposTrabajadores;
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaVigenciasTiposTrabajadores.buscarVigenciasTiposTrabajadoresSecuencia()" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<VigenciasTiposTrabajadores> buscarEmpleados(EntityManager em) {
        try{
        em.clear();
        Query query = em.createQuery("SELECT vtt FROM VigenciasTiposTrabajadores vtt "
                + "WHERE vtt.fechavigencia = (SELECT MAX(vttt.fechavigencia) "
                + "FROM VigenciasTiposTrabajadores vttt)");
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        return query.getResultList();
            
        }catch(Exception e){
            log.error("Persistencia.PersistenciaVigenciasTiposTrabajadores.buscarEmpleados()" + e.getMessage());
            return null;
        }
    }

    @Override
    public VigenciasTiposTrabajadores buscarVigenciaTipoTrabajadorRestriccionUN(EntityManager em, BigInteger empleado, Date fechaVigencia, BigInteger tipoTrabajador) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vtt FROM VigenciasTiposTrabajadores vtt WHERE vtt.empleado.secuencia = :empleado AND vtt.fechavigencia=:fechaVigencia AND vtt.tipotrabajador.secuencia=:tipoTrabajador");
            query.setParameter("empleado", empleado);
            query.setParameter("fechaVigencia", fechaVigencia);
            query.setParameter("tipoTrabajador", tipoTrabajador);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            VigenciasTiposTrabajadores obj = (VigenciasTiposTrabajadores) query.getSingleResult();
            return obj;
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaVigenciasTiposTrabajadores.buscarVigenciaTipoTrabajadorRestriccionUN()" + e.getMessage());
            return null;
        }
    }
}
