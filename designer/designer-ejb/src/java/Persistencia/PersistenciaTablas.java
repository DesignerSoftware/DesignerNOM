/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Tablas;
import InterfacePersistencia.PersistenciaTablasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br> 
 * Clase encargada de realizar operaciones sobre la tabla 'Tablas'
 * de la base de datos.
 * @author -Felipphe-
 */
@Stateless
public class PersistenciaTablas implements PersistenciaTablasInterface {
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
/*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
*/

    @Override
    public List<Tablas> buscarTablas(EntityManager em, BigInteger secuenciaMod) {
        try {
            em.clear();
            Query query = em.createQuery("select t from Tablas t where t.modulo.secuencia = :secuenciaMod "
             +" and t.tipo in ('SISTEMA','CONFIGURACION' ) "
             +" and EXISTS (SELECT p FROM Pantallas p where t = p.tabla)"
             +"order by t.nombre");
            query.setParameter("secuenciaMod", secuenciaMod);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Tablas> tablas = (List<Tablas>) query.getResultList();
            return tablas;
        } catch (Exception e) {
            System.out.println("Hola :$");
            List<Tablas> tablas = null;
            return tablas;
        }
    }
}
