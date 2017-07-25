package Persistencia;

import Entidades.UsuariosInterfases;
import InterfacePersistencia.PersistenciaUsuariosInterfasesInterface;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class PersistenciaUsuariosInterfases implements PersistenciaUsuariosInterfasesInterface {

   private static Logger log = Logger.getLogger(PersistenciaUsuariosInterfases.class);

    @Override
    public UsuariosInterfases obtenerUsuarioInterfaseContabilidad(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT u FROM UsuariosInterfases u WHERE u.interfase='CONTABILIDAD'");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            UsuariosInterfases usuario = (UsuariosInterfases) query.getSingleResult();
            return usuario;
        } catch (Exception e) {
            log.error("Error obtenerUsuarioInterfaseContabilidad PersistenciaUsuariosInterfases : " + e.getMessage());
            return null;
        }
    }
}
