/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.Indices;
import InterfacePersistencia.PersistenciaIndicesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaIndices implements PersistenciaIndicesInterface {

    public void crear(EntityManager em, Indices indices) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(indices);
            tx.commit();
        } catch (Exception e) {
            System.err.println("Error PersistenciaIndices.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void editar(EntityManager em, Indices indices) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(indices);
            tx.commit();
        } catch (Exception e) {
            System.err.println("Error PersistenciaIndices.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void borrar(EntityManager em, Indices indices) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(indices));
            tx.commit();
        } catch (Exception e) {
            System.err.println("Error PersistenciaIndices.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public List<Indices> consultarIndices(EntityManager em) {
        List<Indices> lista;
        try {
            em.clear();
            String sql = "select * from indices i where exists\n"
                    + " (select 'x' from  usuariosindices ui, usuarios u\n"
                    + "  where u.secuencia = ui.usuario and u.alias = user \n"
                    + "  and ui.indice=i.SECUENCIA)";
            Query query = em.createNativeQuery(sql, Indices.class);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            lista = query.getResultList();
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaIndices.consultarIndices()" + e.getMessage());
            lista = null;
        }
        return lista;
    }

    public BigInteger contarUsuariosIndicesIndice(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "select count(*)from usuariosindices where indice = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            System.err.println("Error PersistenciaIndices   contarUsuariosIndicesIndice. " + e.getMessage());
            return retorno;
        }
    }

    public BigInteger contarParametrosIndicesIndice(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "select count(*) from parametrosindices where indice = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            System.err.println("Error PersistenciaIndices   contarParametrosIndicesIndice. " + e.getMessage());
            return retorno;
        }
    }

    public BigInteger contarResultadosIndicesSoportesIndice(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "select count(*)from resultadosindicessoportes where indice =?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            System.err.println("Error PersistenciaIndices   contarResultadosIndicesSoportesIndice. " + e.getMessage());
            return retorno;
        }
    }

    public BigInteger contarResultadosIndicesDetallesIndice(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "select count(*) from resultadosindicesdetalles where resultadoindice = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            System.err.println("Error PersistenciaIndices   contarResultadosIndicesDetallesIndice. " + e.getMessage());
            return retorno;
        }
    }

    public BigInteger contarResultadosIndicesIndice(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "select count(*) from resultadosindices where indice = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            System.err.println("Error PersistenciaIndices   contarResultadosIndicesIndice. " + e.getMessage());
            return retorno;
        }
    }

    public BigInteger contarCodigosRepetidosIndices(EntityManager em, Short codigo) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "select count(*) from indices where codigo = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, codigo);
            retorno = new BigInteger(query.getSingleResult().toString());
            return retorno;
        } catch (Exception e) {
            System.err.println("Error PersistenciaIndices   contarCodigosRepetidosIndices. " + e.getMessage());
            return retorno;
        }
    }

}
