package Persistencia;

import Entidades.VWMensajeSAPBOV8;
import InterfacePersistencia.PersistenciaVWMensajeSAPBOV8Interface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class PersistenciaVWMensajeSAPBOV8 implements PersistenciaVWMensajeSAPBOV8Interface {

   private static Logger log = Logger.getLogger(PersistenciaVWMensajeSAPBOV8.class);

    @Override
    public List<VWMensajeSAPBOV8> buscarListaErroresSAPBOV8(EntityManager em) {
        try {
            String sql = "select * from  VWMensajeSAPBOV8 where CODCOMP in (SELECT EM.CODIGOALTERNATIVO\n"
                    + " FROM USUARIOSESTRUCTURAS UE, USUARIOS U, EMPRESAS EM\n"
                    + " WHERE U.SECUENCIA=UE.USUARIO\n"
                    + " AND EM.SECUENCIA=UE.EMPRESA\n"
                    + " AND (U.ALIAS=USER \n"
                    + "  OR USER=(SELECT UI.ALIAS FROM USUARIOS UI WHERE PROPIETARIO='S')))";
            Query query = em.createNativeQuery(sql, VWMensajeSAPBOV8.class);
            List<VWMensajeSAPBOV8> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            log.error("Error buscarListaErroresSAPBOV8 PersistenciaVWMensajesAPBOV8 : " + e.getMessage());
            return null;
        }
    }

}
