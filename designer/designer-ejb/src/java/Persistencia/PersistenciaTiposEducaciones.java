/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.TiposEducaciones;
import InterfacePersistencia.PersistenciaTiposEducacionesInterface;
import java.util.List;

import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import javax.persistence.Query;
/**
 * Clase Stateless.<br> 
 * Clase encargada de realizar operaciones sobre la tabla 'TiposEducaciones'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaTiposEducaciones implements PersistenciaTiposEducacionesInterface{
/*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
*/
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    @Override
    public List<TiposEducaciones> tiposEducaciones(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT tE FROM TiposEducaciones tE ORDER BY tE.nombre");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposEducaciones> tiposEducaciones = query.getResultList();
            return tiposEducaciones;
        } catch (Exception e) {
            return null;
        }
    }   
}
