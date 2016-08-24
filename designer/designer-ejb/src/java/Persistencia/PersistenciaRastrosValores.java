/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.RastrosValores;
import InterfacePersistencia.PersistenciaRastrosValoresInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
/**
 * Clase Stateless. <br> 
 * Clase encargada de realizar operaciones sobre la tabla 'RastrosValores'
 * de la base de datos.
 * @author betelgeuse
 */
@Stateless
public class PersistenciaRastrosValores implements PersistenciaRastrosValoresInterface{
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
//    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
//    private EntityManager em;
    
    @Override
    public List<RastrosValores> rastroValores(EntityManager em, BigInteger secRastro) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT rv FROM RastrosValores rv WHERE rv.rastro.secuencia = :secRastro");
            query.setParameter("secRastro", secRastro);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<RastrosValores> listaRastroV =query.getResultList();
            return listaRastroV;

        } catch (Exception e) {
            System.out.println("Exepcion en rastroValores " + e);
            return null;
        }
    }
}
