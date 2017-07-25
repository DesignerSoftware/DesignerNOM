package Persistencia;

import InterfacePersistencia.PersistenciaVWActualesFechasInterface;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

/**
 *
 * @author -Felipphe-
 */
@Stateless
public class PersistenciaVWActualesFechas implements PersistenciaVWActualesFechasInterface {

   private static Logger log = Logger.getLogger(PersistenciaVWActualesFechas.class);

    @Override
    public Date actualFechaHasta(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vw.fechaHastaCausado FROM VWActualesFechas vw");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Date actualFechaHasta = (Date) query.getSingleResult();
            return actualFechaHasta;
        } catch (Exception e) {
            log.error("Exepcion: PersistenciaVWActualesFechas.actualFechaHasta: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Date actualFechaDesde(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vw.fechaDesdeCausado FROM VWActualesFechas vw");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Date actualFechaHasta = (Date) query.getSingleResult();
            return actualFechaHasta;
        } catch (Exception e) {
            log.error("Error actualFechaDesde PersistenciaVWActualesFechas : " + e.getMessage());
            return null;
        }
    }
}
