/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.SoausentismosProrrogas;
import InterfacePersistencia.PersistenciaSoausentismosProrrogasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones de consulta para las SoausentismosProrrogas.
 * <strong>Entidad Virtual</strong>
 */
@Stateless
public class PersistenciaSoausentismosProrrogas implements PersistenciaSoausentismosProrrogasInterface {

   private static Logger log = Logger.getLogger(PersistenciaSoausentismosProrrogas.class);
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

    @Override
    public List<SoausentismosProrrogas> prorrogas(EntityManager em, BigInteger secEmpleado, BigInteger secuenciaCausa, BigInteger secuenciaAusentismo) {
        try {
            em.clear();
            String sqlQuery = "SELECT soa.secuencia, soa.fechafinaus+1 finsiguiente, ca.DESCRIPCION,\n"
                    + "(select dc.CODIGO from DIAGNOSTICOSCATEGORIAS dc where dc.secuencia = soa.diagnosticocategoria) codigodiagnostico,\n"
                    + "(select dc.DESCRIPCION from DIAGNOSTICOSCATEGORIAS dc where dc.secuencia = soa.diagnosticocategoria) descripciondiagnostico,\n"
                    + "soa.fecha, soa.fechafinaus, soa.dias, soa.horas, \n"
                    + "nvl(soa.numerocertificado,'Falta # Certificado')||': '||soa.fecha||'->'||soa.fechafinaus numerocertificado\n"
                    + "FROM CAUSASAUSENTISMOS ca, SOAUSENTISMOS soa\n"
                    + "WHERE ca.secuencia = soa.causa\n"
                    + "and soa.secuencia <> nvl( ? ,0)\n"
                    + "and soa.empleado = ?\n"
                    + "and soa.causa = ?\n"
                    + "ORDER BY soa.FECHA DESC";
            Query query = em.createNativeQuery(sqlQuery, "prorrogas");
            query.setParameter(1, secuenciaAusentismo);
            query.setParameter(2, secEmpleado);
            query.setParameter(3, secuenciaCausa);
            List<SoausentismosProrrogas> prorrogas = query.getResultList();
            return prorrogas;

        } catch (Exception e) {
            log.error("PersistenciaSoausentismosProrrogas.prorrogas():  ", e);
            return null;
        }
    }
}
