/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Sucursales;
import InterfacePersistencia.PersistenciaSucursalesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;


@Stateless
public class PersistenciaSucursales implements PersistenciaSucursalesInterface {

   private static Logger log = Logger.getLogger(PersistenciaSucursales.class);

    @Override
    public void crear(EntityManager em, Sucursales sucursales) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(sucursales);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaSucursales.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Sucursales sucursales) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(sucursales);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaSucursales.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Sucursales sucursales) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(sucursales));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaSucursales.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public Sucursales buscarSucursal(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            return em.find(Sucursales.class, secuencia);
        } catch (Exception e) {
            log.error("Persistencia.PersistenciaSucursales.buscarSucursal()" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Sucursales> consultarSucursales(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM SUCURSALES ORDER BY CODIGO";
            Query query = em.createNativeQuery(sql, Sucursales.class);
            List<Sucursales> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            log.error("error consultarSucursales :  " + e.toString());
            return null;
        }
    }

    public BigInteger contarVigenciasFormasPagosSucursal(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM vigenciasformaspagos WHERE sucursal = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            log.warn("Contador PersistenciaSubCategorias contarVigenciasFormasPagosSucursal persistencia " + retorno);
            return retorno;
        } catch (Exception e) {
            log.error("Error PERSISTENCIASUCURSALES contarVigenciasFormasPagosSucursal : " + e.getMessage());
            return retorno;
        }
    }
}
