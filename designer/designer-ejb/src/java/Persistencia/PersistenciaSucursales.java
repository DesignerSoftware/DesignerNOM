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
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;


@Stateless
public class PersistenciaSucursales implements PersistenciaSucursalesInterface {

    @Override
    public void crear(EntityManager em, Sucursales sucursales) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(sucursales);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaSucursales.crear: " + e.getMessage());
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
            System.out.println("Error PersistenciaSucursales.editar: " + e.getMessage());
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
            System.out.println("Error PersistenciaSucursales.borrar: " + e.getMessage());
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
            System.out.println("Persistencia.PersistenciaSucursales.buscarSucursal()" + e.getMessage());
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
            System.out.println("error consultarSucursales :  " + e.toString());
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
            System.out.println("Contador PersistenciaSubCategorias contarVigenciasFormasPagosSucursal persistencia " + retorno);
            return retorno;
        } catch (Exception e) {
            System.err.println("Error PERSISTENCIASUCURSALES contarVigenciasFormasPagosSucursal : " + e.getMessage());
            return retorno;
        }
    }
}
