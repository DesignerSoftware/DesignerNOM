/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.AdiestramientosNF;
import InterfacePersistencia.PersistenciaAdiestramientosNFInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'AdiestramientosNF' de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaAdiestramientosNF implements PersistenciaAdiestramientosNFInterface {

   private static Logger log = Logger.getLogger(PersistenciaAdiestramientosNF.class);
     /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/
    
    @Override
        public List<AdiestramientosNF> adiestramientosNF(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT aNF FROM AdiestramientosNF aNF ORDER BY aNF.desccripcion");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<AdiestramientosNF> adiestramientosNF = query.getResultList();
            return adiestramientosNF;
        } catch (Exception e) {
           log.error("Persistencia.PersistenciaAdiestramientosNF.adiestramientosNF() e: " + e);
            return null;
        }
    }

    
}
