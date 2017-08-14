/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.HVHojasDeVida;
import InterfacePersistencia.PersistenciaHVHojasDeVidaInterface;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'HVHojasDeVida' de la
 * base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaHVHojasDeVida implements PersistenciaHVHojasDeVidaInterface {

   private static Logger log = Logger.getLogger(PersistenciaHVHojasDeVida.class);

    /*@PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;*/
    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     *
     * @param em
     * @param hVHojasDeVida
     */
    @Override
    public void editar(EntityManager em, HVHojasDeVida hVHojasDeVida) {
        log.warn(this.getClass().getName() + ".hvHoaDeVidaPersona()");
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(hVHojasDeVida);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            log.error("error en editar");
            e.printStackTrace();
        }
    }

    @Override
    public HVHojasDeVida hvHojaDeVidaPersona(EntityManager em, BigInteger secuenciaPersona) {
        log.warn(this.getClass().getName() + ".hvHojaDeVidaPersona()");
        HVHojasDeVida hVHojasDeVida = null;
        try {
            em.clear();
            String sql = "SELECT * FROM HVHOJASDEVIDA WHERE PERSONA = ?";
            Query query = em.createNativeQuery(sql, HVHojasDeVida.class);
            query.setParameter(1, secuenciaPersona);
//            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            hVHojasDeVida = (HVHojasDeVida) query.getSingleResult();
            return hVHojasDeVida;
        } catch (Exception e) {
            log.error("error en hvHojaDeVidaPersona Mensaje de excepcion: "+e.getMessage());
            return hVHojasDeVida;
        }
    }
}
