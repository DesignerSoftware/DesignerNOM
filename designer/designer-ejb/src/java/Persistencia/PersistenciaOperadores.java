/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Operadores;
import InterfacePersistencia.PersistenciaOperadoresInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

/**
 * Clase Stateless.<br> 
 * Clase encargada de realizar operaciones sobre la tabla 'MotivosEmbargos'
 * de la base de datos.
 * @author Andres Pineda.
 */
@Stateless
public class PersistenciaOperadores implements PersistenciaOperadoresInterface {

   private static Logger log = Logger.getLogger(PersistenciaOperadores.class);
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
//     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

    @Override
    public List<Operadores> buscarOperadores(EntityManager em){
        try {
            em.clear();
            String sql="SELECT * FROM OPERADORES";
            Query query= em.createNativeQuery(sql, Operadores.class);
            List<Operadores> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            log.error("Error buscarOperadores PersistenciaOperadores : "+e.toString());
            return null;
        }
    }
}
