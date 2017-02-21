/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.ObjetosDB;
import Entidades.Personas;
import InterfacePersistencia.PersistenciaObjetosDBInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'ObjetosDB'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaObjetosDB implements PersistenciaObjetosDBInterface{
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

    @Override
    public ObjetosDB obtenerObjetoTabla(EntityManager em, String nombreTabla) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(obj) FROM ObjetosDB obj WHERE obj.nombre = :nombreTabla");
            query.setParameter("nombreTabla", nombreTabla);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            if (resultado > 0) {
                Query query2 = em.createQuery("SELECT obj FROM ObjetosDB obj WHERE obj.nombre = :nombreTabla");
                query2.setParameter("nombreTabla", nombreTabla);
                query2.setHint("javax.persistence.cache.storeMode", "REFRESH");
                ObjetosDB objetosDB = (ObjetosDB) query2.getSingleResult();
                return objetosDB;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("Excepcion en verificarRastroTabla " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<ObjetosDB> consultarObjetoDB(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT ob FROM ObjetosDB ob ORDER BY ob.tipo ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<ObjetosDB> todosObjetos = query.getResultList();
            return todosObjetos;
        } catch (Exception e) {
            System.err.println("Error: PersistenciaObjetosDB consultarObjetoDB ERROR " + e.getMessage());
            return null;
        }
        
        
    }
    
}
