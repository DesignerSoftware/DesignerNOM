/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import InterfacePersistencia.PersistenciaTiposTallasInterface;
import Entidades.TiposTallas;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

@Stateless
public class PersistenciaTiposTallas implements PersistenciaTiposTallasInterface {

   private static Logger log = Logger.getLogger(PersistenciaTiposTallas.class);

    @Override
    public void crear(EntityManager em, TiposTallas tiposTallas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposTallas);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposTallas.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, TiposTallas tiposTallas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposTallas);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposTallas.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TiposTallas tiposTallas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposTallas));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposTallas.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public TiposTallas buscarTipoTalla(EntityManager em, BigInteger secuenciaTT) {
        try {
            return em.find(TiposTallas.class, secuenciaTT);
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaTiposTallas.buscarTipoTalla()" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<TiposTallas> buscarTiposTallas(EntityManager em) {
        try{
        em.clear();
        Query query = em.createQuery("SELECT m FROM TiposTallas m ORDER BY m.codigo ASC ");
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        List<TiposTallas> listMotivosDemandas = query.getResultList();
        return listMotivosDemandas;
        }catch(Exception e){
            log.error("Persistencia.PersistenciaTiposTallas.buscarTiposTallas()" + e.getMessage());
            return null;
        }
    }

    @Override
    public BigInteger contadorElementos(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = " SELECT COUNT(*)FROM  elementos e WHERE e.tipotalla = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = (BigInteger) new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("Error contadorElementos. " + e.getMessage());
            return retorno;
        }
    }

    @Override
    public BigInteger contadorVigenciasTallas(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM  vigenciastallas vt WHERE vt.tipotalla = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = (BigInteger) new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            log.error("Error PersistenciaTiposTallas  contadorVigenciasTallas. " + e.getMessage());
            return retorno;
        }
    }
}
