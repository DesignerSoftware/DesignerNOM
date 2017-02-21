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

@Stateless
public class PersistenciaTiposCotizantes implements PersistenciaTiposCotizantesInterface {

    @Override
    public void crear(EntityManager em, TiposCotizantes tiposCotizantes) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposCotizantes);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposCotizantes.crear: " + e.getMessage());
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
            System.out.println("Error PersistenciaTiposCotizantes.editar: " + e.getMessage());
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
            System.out.println("Error PersistenciaTiposCotizantes.borrar: " + e.getMessage());
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
            System.out.println("Persistencia.PersistenciaTiposCotizantes.lovTiposCotizantes()" + e.getMessage());
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
            tx.commit();
            return secuenciaClonado;
        } catch (Exception e) {
            System.out.println("error en  persistenciaTiposCotizantes.clonarTipoCotizante " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
            return null;
        } 
    }
}
