package Persistencia;

import Entidades.VWValidaBancos;
import InterfacePersistencia.PersistenciaVWValidaBancosInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class PersistenciaVWValidaBancos implements PersistenciaVWValidaBancosInterface {

    @Override
    public VWValidaBancos validarDocumentoVWValidaBancos(EntityManager em, BigInteger documento) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT v FROM VWValidaBancos v WHERE v.codigoprimario=:documento");
            VWValidaBancos validacion = (VWValidaBancos) query.getSingleResult();
            return validacion;
        } catch (Exception e) {
            System.out.println("Error validarDocumentoVWValidaBancos PersistenciaVWValidaBancos : " + e.toString());
            return null;
        }
    }
}
