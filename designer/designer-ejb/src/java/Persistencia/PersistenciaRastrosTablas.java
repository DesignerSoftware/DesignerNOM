/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import InterfacePersistencia.PersistenciaRastrosTablasInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;
/**
 * Clase Stateless. <br> 
 * Clase encargada de realizar operaciones sobre la tabla 'RastrosTablas'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaRastrosTablas implements PersistenciaRastrosTablasInterface {

   private static Logger log = Logger.getLogger(PersistenciaRastrosTablas.class);
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
//     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    
    @Override
    public boolean verificarRastroTabla(EntityManager em, BigInteger secObjetoTabla) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT COUNT(rt) FROM RastrosTablas rt WHERE rt.objeto.secuencia = :secObjetoTabla");
            query.setParameter("secObjetoTabla", secObjetoTabla);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Long resultado = (Long) query.getSingleResult();
            if (resultado > 0) {
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            log.error("PersistenciaRastrosTablas.verificarRastroTabla():  ", e);
            return false;
        }
    }
}
