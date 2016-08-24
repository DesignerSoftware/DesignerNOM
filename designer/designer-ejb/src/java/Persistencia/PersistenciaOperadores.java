/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Operadores;
import InterfacePersistencia.PersistenciaOperadoresInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase Stateless.<br> 
 * Clase encargada de realizar operaciones sobre la tabla 'MotivosEmbargos'
 * de la base de datos.
 * @author Andres Pineda.
 */
@Stateless
public class PersistenciaOperadores implements PersistenciaOperadoresInterface{
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos
//     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;

    @Override
    public List<Operadores> buscarOperadores(EntityManager em){
        try {
            em.clear();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Operadores.class));
            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            System.out.println("Error buscarOperadores PersistenciaOperadores : "+e.toString());
            return null;
        }
    }
}
