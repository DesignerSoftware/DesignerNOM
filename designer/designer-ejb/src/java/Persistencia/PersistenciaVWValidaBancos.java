package Persistencia;

import Entidades.VWValidaBancos;
import InterfacePersistencia.PersistenciaVWValidaBancosInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class PersistenciaVWValidaBancos implements PersistenciaVWValidaBancosInterface {

   private static Logger log = Logger.getLogger(PersistenciaVWValidaBancos.class);

    @Override
    public VWValidaBancos validarDocumentoVWValidaBancos(EntityManager em, BigInteger documento) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT v FROM VWValidaBancos v WHERE v.codigoprimario=:documento");
            VWValidaBancos validacion = (VWValidaBancos) query.getSingleResult();
            return validacion;
        } catch (Exception e) {
            log.error("Error validarDocumentoVWValidaBancos PersistenciaVWValidaBancos : " + e.getMessage());
            return null;
        }
    }
}
