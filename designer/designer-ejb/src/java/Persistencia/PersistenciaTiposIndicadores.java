/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.TiposIndicadores;
import InterfacePersistencia.PersistenciaTiposIndicadoresInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'TiposIndicadores' de
 * la base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaTiposIndicadores implements PersistenciaTiposIndicadoresInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
/*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
    private EntityManager em;
*/

    @Override
    public void crear(EntityManager em, TiposIndicadores tiposIndicadores) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposIndicadores);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposIndicadores.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, TiposIndicadores tiposIndicadores) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposIndicadores);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposIndicadores.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TiposIndicadores tiposIndicadores) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposIndicadores));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposIndicadores.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<TiposIndicadores> buscarTiposIndicadores(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT g FROM TiposIndicadores g ORDER BY g.codigo ASC ");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List< TiposIndicadores> listMotivosDemandas = query.getResultList();
            return listMotivosDemandas;

        } catch (Exception e) {
            System.out.println("Error buscarTiposIndicadores PersistenciaTiposIndicadores : " + e.toString());
            return null;
        }
    }

    @Override
    public TiposIndicadores buscarTiposIndicadoresSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT te FROM TiposIndicadores te WHERE te.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            TiposIndicadores tiposIndicadores = (TiposIndicadores) query.getSingleResult();
            return tiposIndicadores;
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposIndicadores buscarTiposIndicadoresSecuencia : " + e.toString());
            TiposIndicadores tiposEntidades = null;
            return tiposEntidades;
        }
    }

    @Override
    public BigInteger contadorVigenciasIndicadores(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*) FROM vigenciasindicadores WHERE tipoindicador= ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            System.err.println("Contador TiposIndicadores contadorVigenciasIndicadores persistencia " + retorno);
            return retorno;
        } catch (Exception e) {
            System.out.println("Error TiposIndicadores contadorVigenciasIndicadores. " + e);
            return retorno;
        }
    }
}
