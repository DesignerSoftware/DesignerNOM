/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import javax.ejb.Stateless;
import Entidades.VigenciasReformasLaborales;
import InterfacePersistencia.PersistenciaVigenciasReformasLaboralesInterface;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import org.apache.log4j.Logger;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla
 * 'VigenciasReformasLaborales' de la base de datos.
 *
 * @author Andres Pineda
 */
@Stateless
public class PersistenciaVigenciasReformasLaborales implements PersistenciaVigenciasReformasLaboralesInterface {

   private static Logger log = Logger.getLogger(PersistenciaVigenciasReformasLaborales.class);

//    private final static Logger logger = Logger.getLogger("connectionSout");
    private Date fechaDia;
    private final SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     *
     * @param em
     * @param vigenciaRefLab
     * @return
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
    @Override
    public boolean crear(EntityManager em, VigenciasReformasLaborales vigenciaRefLab) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(vigenciaRefLab);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            return false;
        }
    }

    @Override
    public void editar(EntityManager em, VigenciasReformasLaborales vigenciaRefLab) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vigenciaRefLab);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, VigenciasReformasLaborales vigenciaRefLab) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(vigenciaRefLab));
            tx.commit();
        } catch (Exception e) {
//            PropertyConfigurator.configure("log4j.properties");
//            logger.error("Metodo: borrar - PersistenciaVigenciasReformasLaborales - Fecha : " + format.format(fechaDia) + " - Error :  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }

    }

    @Override
    public List<VigenciasReformasLaborales> buscarVigenciasRefLab(EntityManager em) {
        try {
            em.clear();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VigenciasReformasLaborales.class));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            log.error("PersistenciaVigenciasReformasLaborales.buscarVigenciasRefLab():  ", e);
            return null;
        }
    }

    @Override
    public List<VigenciasReformasLaborales> buscarVigenciasReformasLaboralesEmpleado(EntityManager em, BigInteger secEmpleado) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vrl FROM VigenciasReformasLaborales vrl WHERE vrl.empleado.secuencia = :secuenciaEmpl ORDER BY vrl.fechavigencia DESC");
            query.setParameter("secuenciaEmpl", secEmpleado);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<VigenciasReformasLaborales> vigenciasRefLab = query.getResultList();
            return vigenciasRefLab;
        } catch (Exception e) {
            log.error("PersistenciaVigenciasReformasLaborales.buscarVigenciasReformasLaboralesEmpleado():  ", e);
            return null;
        }
    }

    @Override
    public VigenciasReformasLaborales buscarVigenciaReformaLaboralSecuencia(EntityManager em, BigInteger secVRL) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT v FROM VigenciasReformasLaborales v WHERE v.secuencia = :secuencia").setParameter("secuencia", secVRL);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            VigenciasReformasLaborales vigenciaRefLab = (VigenciasReformasLaborales) query.getSingleResult();
            return vigenciaRefLab;
        } catch (Exception e) {
            log.error("PersistenciaVigenciasReformasLaborales.buscarVigenciaReformaLaboralSecuencia():  ", e);
            return null;
        }
    }
}
