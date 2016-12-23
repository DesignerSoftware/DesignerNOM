/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.TiposCotizantes;
import InterfacePersistencia.PersistenciaTiposCotizantesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'TiposCotizantes' de
 * la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaTiposCotizantes implements PersistenciaTiposCotizantesInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
     */

    @Override
    public void crear(EntityManager em, TiposCotizantes tiposCotizantes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposCotizantes);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposCotizantes.crear: " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, TiposCotizantes tiposCotizantes) {
        System.out.println("Persistencia.PersistenciaTiposCotizantes.editar()");
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposCotizantes);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposCotizantes.editar: " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TiposCotizantes tiposCotizantes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposCotizantes));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposCotizantes.borrar: " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<TiposCotizantes> lovTiposCotizantes(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT tc FROM TiposCotizantes tc ORDER BY tc.codigo ASC");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<TiposCotizantes> listaTiposCotizantes = query.getResultList();
            return listaTiposCotizantes;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public BigInteger clonarTipoCotizante(EntityManager em, BigInteger codOrigen, BigInteger codDestino, String descripcion , BigInteger secClonado) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            StoredProcedureQuery query = em.createStoredProcedureQuery("TIPOSCOTIZANTES_PKG.clonarTipoCotizante");
            query.registerStoredProcedureParameter(1, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(2, BigInteger.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(3, String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter(4, BigInteger.class, ParameterMode.OUT);

            query.setParameter(1, codOrigen);
            query.setParameter(2, codDestino);
            query.setParameter(3, descripcion);
            query.setParameter(4, secClonado);
            
            query.execute();
            query.hasMoreResults();
            Long aux = (Long) query.getOutputParameterValue(4);
            BigInteger secuenciaClonado = BigInteger.valueOf(aux);
            System.out.println("secuenciaClonado : " + secuenciaClonado);
            tx.commit();
            return secuenciaClonado;
        } catch (Exception e) {
            System.out.println("error en  persistenciaTiposCotizantes.clonarTipoCotizante " + e.toString());
            if (tx.isActive()) {
                tx.rollback();
            }
            return null;
        } 
    }
}
