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
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'TiposTallas' de la
 * base de datos.
 *
 * @author John Pineda.
 */
@Stateless
public class PersistenciaTiposTallas implements PersistenciaTiposTallasInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
     */
    @Override
    public void crear(EntityManager em, TiposTallas tiposTallas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposTallas);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposTallas.crear: " + e);
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
            System.out.println("Error PersistenciaTiposTallas.editar: " + e);
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
            System.out.println("Error PersistenciaTiposTallas.borrar: " + e);
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
            return null;
        }
    }

    @Override
    public List<TiposTallas> buscarTiposTallas(EntityManager em) {
        em.clear();
        Query query = em.createQuery("SELECT m FROM TiposTallas m ORDER BY m.codigo ASC ");
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        List<TiposTallas> listMotivosDemandas = query.getResultList();
        return listMotivosDemandas;
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
            System.err.println("Contador contadorElementos persistencia " + retorno);
            return retorno;
        } catch (Exception e) {
            System.out.println("Error contadorElementos. " + e);
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
            System.err.println("Contador PersistenciaTiposTallas contadorVigenciasTallas persistencia " + retorno);
            return retorno;
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposTallas  contadorVigenciasTallas. " + e);
            return retorno;
        }
    }
}
