package Persistencia;

import Entidades.VWActualesFechas;
import InterfacePersistencia.PersistenciaVWActualesFechasInterface;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author -Felipphe-
 */
@Stateless
public class PersistenciaVWActualesFechas implements PersistenciaVWActualesFechasInterface {

    @Override
    public Date actualFechaHasta(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT vw.fechaHastaCausado FROM VWActualesFechas vw");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Date actualFechaHasta = (Date) query.getSingleResult();
            return actualFechaHasta;
        } catch (Exception e) {
            System.out.println("Exepcion: PersistenciaVWActualesFechas.actualFechaHasta: " + e);
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
            System.out.println("Error actualFechaDesde PersistenciaVWActualesFechas : " + e.toString());
            return null;
        }
    }
}
