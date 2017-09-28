/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import InterfacePersistencia.PersistenciaTiposFamiliaresInterface;
import Entidades.TiposFamiliares;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaTiposFamiliares implements PersistenciaTiposFamiliaresInterface {

   private static Logger log = Logger.getLogger(PersistenciaTiposFamiliares.class);

    public void crear(EntityManager em, TiposFamiliares tiposFamiliares) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposFamiliares);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposFamiliares.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void editar(EntityManager em, TiposFamiliares tiposFamiliares) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(tiposFamiliares);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposFamiliares.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public void borrar(EntityManager em, TiposFamiliares tiposFamiliares) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(tiposFamiliares));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaTiposFamiliares.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    public TiposFamiliares buscarTiposFamiliares(EntityManager em, BigInteger secuenciaTF) {
        try {
            em.clear();
            return em.find(TiposFamiliares.class, secuenciaTF);
        } catch (Exception e) {
            log.error("PersistenciaTiposFamiliares.buscarTiposFamiliares():  ", e);
            return null;
        }
    }

    public List<TiposFamiliares> buscarTiposFamiliares(EntityManager em) {
       try{
        em.clear();
        String sql="SELECT * FROM TIPOSFAMILIARES ORDER BY CODIGO ASC";
        Query query = em.createNativeQuery(sql, TiposFamiliares.class);
        List<TiposFamiliares> listTiposFamiliares = query.getResultList();
        return listTiposFamiliares;
       } catch(Exception e){
           log.error("error buscarTiposFamiliares.PersistenciaTiposFamiliares  ", e);
           return null;
       }

    }

    public BigInteger contadorHvReferencias(EntityManager em, BigInteger secuencia) {
        BigInteger retorno = new BigInteger("-1");
        try {
            em.clear();
            String sqlQuery = "SELECT COUNT(*)FROM  hvreferencias hvr WHERE parentesco =?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secuencia);
            retorno = new BigInteger(query.getSingleResult().toString());
            log.warn("Contador PERSISTENCIATIPOSFAMILIARES contadorHvReferencias  " + retorno);
            return retorno;
        } catch (Exception e) {
            log.error("PersistenciaTiposFamiliares.contadorHvReferencias():  ", e);
            return retorno;
        }
    }

}
