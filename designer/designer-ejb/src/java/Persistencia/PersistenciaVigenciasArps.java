package Persistencia;

import InterfacePersistencia.PersistenciaVigenciasArpsInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Stateless
public class PersistenciaVigenciasArps implements PersistenciaVigenciasArpsInterface {

    @Override
    public String actualARP(EntityManager em, BigInteger secEstructura, BigInteger secCargo, Date fechaHasta) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            String fecha = formato.format(fechaHasta);
            Query query = em.createNativeQuery("SELECT VIGENCIASARPS_PKG.ConsultarPorcentaje(?, ?, to_date(?, 'dd/mm/yyyy')) FROM dual");
            query.setParameter(1, secEstructura);
            query.setParameter(2, secCargo);
            query.setParameter(3, fecha);
            String actualARP = (String) query.getSingleResult().toString();
            tx.commit();
            return actualARP;
        } catch (Exception e) {
            System.out.println("Exepcion: PersistenciaVigenciasArps.actualARP " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
            return null;
        }
    }
}
