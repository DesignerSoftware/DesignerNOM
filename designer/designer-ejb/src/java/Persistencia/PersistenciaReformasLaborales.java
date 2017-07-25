/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.ReformasLaborales;
import InterfacePersistencia.PersistenciaReformasLaboralesInterface;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'ReformasLaborales' de
 * la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaReformasLaborales implements PersistenciaReformasLaboralesInterface {

   private static Logger log = Logger.getLogger(PersistenciaReformasLaborales.class);

    private final static Logger logger = Logger.getLogger("connectionSout");
    private Date fechaDia;
    private final SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    @Override
    public void crear(EntityManager em, ReformasLaborales reformaLaboral) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(reformaLaboral);
            tx.commit();
        } catch (Exception e) {
            PropertyConfigurator.configure("log4j.properties");
            logger.error("Metodo: crear - PersistenciaReformasLaborales - Fecha : " + format.format(fechaDia) + " - Error : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, ReformasLaborales reformaLaboral) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(reformaLaboral);
            tx.commit();
        } catch (Exception e) {
            PropertyConfigurator.configure("log4j.properties");
            logger.error("Metodo: editar - PersistenciaReformasLaborales - Fecha : " + format.format(fechaDia) + " - Error : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, ReformasLaborales reformaLaboral) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(reformaLaboral));
            tx.commit();
        } catch (Exception e) {
            PropertyConfigurator.configure("log4j.properties");
            logger.error("Metodo: borrar - PersistenciaReformasLaborales - Fecha : " + format.format(fechaDia) + " - Error : " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<ReformasLaborales> buscarReformasLaborales(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT e FROM ReformasLaborales e ORDER BY e.codigo ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<ReformasLaborales> reformaLista = (List<ReformasLaborales>) query.getResultList();
            return reformaLista;
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaReformasLaborales.buscarReformasLaborales()" + e.getMessage());
            return null;
        }
    }

    @Override
    public ReformasLaborales buscarReformaSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT e FROM ReformasLaborales e WHERE e.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            ReformasLaborales reformaL = (ReformasLaborales) query.getSingleResult();
            return reformaL;
        } catch (Exception e) {
            PropertyConfigurator.configure("log4j.properties");
            logger.error("Metodo: buscarReformaSecuencia - PersistenciaReformasLaborales - Fecha : " + format.format(fechaDia) + " - Error : " + e.toString());
            ReformasLaborales reformaL = null;
            return reformaL;
        }
    }

    @Override
    public String obtenerCheckIntegralReformaLaboral(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            String sql = "SELECT REFORMASLABORALES_PKG.CheckIntegral(?) FROM dual";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, secuencia);
            String variable = (String) query.getSingleResult();
            if (variable == null) {
                variable = "N";
            }
            return variable;
        } catch (Exception e) {
            PropertyConfigurator.configure("log4j.properties");
            logger.error("Metodo: obtenerCheckIntegralReformaLaboral - PersistenciaReformasLaborales - Fecha : " + format.format(fechaDia) + " - Error : " + e.toString());
            return "N";
        }
    }
}
