/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.AdiestramientosF;
import InterfacePersistencia.PersistenciaAdiestramientosFInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones sobre la tabla 'AdiestramientosF' de la base de datos.
 * @author Betelgeuse
 */
@Stateless
public class PersistenciaAdiestramientosF implements PersistenciaAdiestramientosFInterface {

   private static Logger log = Logger.getLogger(PersistenciaAdiestramientosF.class);
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
     */
   /* @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;*/

    @Override
        public List<AdiestramientosF> adiestramientosF(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT aF FROM AdiestramientosF aF ORDER BY aF.descripcion");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<AdiestramientosF> adiestramientosF = query.getResultList();
            return adiestramientosF;
        } catch (Exception e) {
           log.error("Persistencia.PersistenciaAdiestramientosF.adiestramientosF() e: " + e);
            return null;
        }
    }
}
    

