/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.TiposTrabajadores;
import InterfacePersistencia.PersistenciaTiposTrabajadoresInterface;
//import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
//import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class PersistenciaTiposTrabajadores implements PersistenciaTiposTrabajadoresInterface {

    @Override
    public void crear(EntityManager em, TiposTrabajadores tiposTrabajadores) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposTrabajadores);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposTrabajadores.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, TiposTrabajadores tiposTrabajadores) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposTrabajadores);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposTrabajadores.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, TiposTrabajadores tiposTrabajadores) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposTrabajadores));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaTiposTrabajadores.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<TiposTrabajadores> buscarTiposTrabajadores(EntityManager em) {
        try {
            em.clear();
            String sql = "select * from TiposTrabajadores";
            Query query = em.createNativeQuery(sql, TiposTrabajadores.class);
            List<TiposTrabajadores> tipoTLista = query.getResultList();
            return tipoTLista;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaTiposTrabajadores.buscarTiposTrabajadores()" + e.getMessage());
            return null;
        }
    }

    @Override
    public TiposTrabajadores buscarTipoTrabajadorSecuencia(EntityManager em, BigInteger secuencia) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT tt FROM TiposTrabajadores e WHERE tt.secuencia = :secuencia");
            query.setParameter("secuencia", secuencia);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            TiposTrabajadores tipoT = (TiposTrabajadores) query.getSingleResult();
            return tipoT;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaTiposTrabajadores.buscarTipoTrabajadorSecuencia()" + e.getMessage());
            return null;
        }
    }

    @Override
    public String plantillaValidarTipoTrabajadorReformaLaboral(EntityManager em, BigInteger tipoTrabajador, BigInteger reformaLaboral) {
        try {
            em.clear();
            String sqlQuery = "SELECT tipostrabajadores_PKG.plantillavalidarl(?,?) FROM dual";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, tipoTrabajador);
            query.setParameter(2, reformaLaboral);
            String retorno = (String) query.getSingleResult();
            return retorno;
        } catch (Exception e) {
            System.out.println("Error plantillaValidarTipoTrabajadorReformaLaboral PersistenciaTiposTrabajadores : " + e.getMessage());
            return null;
        }
    }

    @Override
    public String plantillaValidarTipoTrabajadorTipoSueldo(EntityManager em, BigInteger tipoTrabajador, BigInteger tipoSueldo) {
        try {
            em.clear();
            String sqlQuery = "SELECT tipostrabajadores_PKG.plantillavalidats(?,?) FROM dual";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, tipoTrabajador);
            query.setParameter(2, tipoSueldo);
            String retorno = (String) query.getSingleResult();
            return retorno;
        } catch (Exception e) {
            System.out.println("Error plantillaValidarTipoTrabajadorTipoSueldo PersistenciaTiposTrabajadores : " + e.getMessage());
            return null;
        }
    }

    @Override
    public String plantillaValidarTipoTrabajadorTipoContrato(EntityManager em, BigInteger tipoTrabajador, BigInteger tipoContrato) {
        try {
            em.clear();
            String sqlQuery = "SELECT tipostrabajadores_PKG.plantillavalidatc(?,?) FROM dual";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, tipoTrabajador);
            query.setParameter(2, tipoContrato);
            String retorno = (String) query.getSingleResult();
            return retorno;
        } catch (Exception e) {
            System.out.println("Error plantillaValidarTipoTrabajadorTipoContrato PersistenciaTiposTrabajadores : " + e.getMessage());
            return null;
        }
    }

    @Override
    public String plantillaValidarTipoTrabajadorContrato(EntityManager em, BigInteger tipoTrabajador, BigInteger contrato) {
        try {
            em.clear();
            String sqlQuery = "SELECT tipostrabajadores_PKG.plantillavalidall(?,?) FROM dual";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, tipoTrabajador);
            query.setParameter(2, contrato);
            String retorno = (String) query.getSingleResult();
            return retorno;
        } catch (Exception e) {
            System.out.println("Error plantillaValidarTipoTrabajadorContrato PersistenciaTiposTrabajadores : " + e.getMessage());
            return null;
        }
    }

    @Override
    public String plantillaValidarTipoTrabajadorNormaLaboral(EntityManager em, BigInteger tipoTrabajador, BigInteger normaLaboral) {
        try {
            em.clear();
            String sqlQuery = "SELECT tipostrabajadores_PKG.plantillavalidanl(?,?) FROM dual";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, tipoTrabajador);
            query.setParameter(2, normaLaboral);
            String retorno = (String) query.getSingleResult();
            return retorno;
        } catch (Exception e) {
            System.out.println("Error plantillaValidarTipoTrabajadorNormaLaboral PersistenciaTiposTrabajadores : " + e.getMessage());
            return null;
        }
    }

    @Override
    public TiposTrabajadores buscarTipoTrabajadorCodigoTiposhort(EntityManager em, short codigo) {
        try {
            em.clear();
            String sql = "SELECT * FROM TiposTrabajadores WHERE codigo=?";
            Query query = em.createNativeQuery(sql, TiposTrabajadores.class);
            query.setParameter(1, codigo);
            TiposTrabajadores tipoTC = (TiposTrabajadores) query.getSingleResult();
            return tipoTC;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaTiposTrabajadores.buscarTipoTrabajadorCodigoTiposhort()" + e.getMessage());
            return null;
        }
    }

    @Override
    public String clonarTipoT(EntityManager em, String nombreNuevo, Short codigoNuevo, BigInteger secTTClonado) {
        try {
            em.clear();
            String sqlQuery = "call XXXXXX_PKG.CLONARTIPOTRABAJADOR(?, ?, ?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secTTClonado);
            query.setParameter(2, codigoNuevo);
            query.setParameter(3, nombreNuevo);
            query.executeUpdate();
            return "S";
        } catch (Exception e) {
            return ("ERROR PersistenciaTiposTrabajadores.clonarTipoT" + e.getMessage());
        }
    }
}
