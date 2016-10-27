package InterfacePersistencia;

import Entidades.ConfiguracionCorreo;
import java.math.BigInteger;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaConfiguracionCorreoInterface {
    public ConfiguracionCorreo consultarConfiguracionServidorCorreo(EntityManager eManager, BigInteger secuenciaEmpresa);
}
