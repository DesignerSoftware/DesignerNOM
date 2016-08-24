package Persistencia;

import Entidades.UsuariosInterfases;
import InterfacePersistencia.PersistenciaUsuariosInterfasesInterface;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Administrador
 */
@Stateless
public class PersistenciaUsuariosInterfases implements PersistenciaUsuariosInterfasesInterface {

    @Override
    public UsuariosInterfases obtenerUsuarioInterfaseContabilidad(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT u FROM UsuariosInterfases u WHERE u.interfase='CONTABILIDAD'");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            UsuariosInterfases usuario = (UsuariosInterfases) query.getSingleResult();
            return usuario;
        } catch (Exception e) {
            System.out.println("Error obtenerUsuarioInterfaseContabilidad PersistenciaUsuariosInterfases : " + e.toString());
            return null;
        }
    }
}
