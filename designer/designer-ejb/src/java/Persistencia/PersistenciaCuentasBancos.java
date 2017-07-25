/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.Bancos;
import Entidades.CuentasBancos;
import Entidades.Inforeportes;
import InterfacePersistencia.PersistenciaCuentasBancosInterface;
import java.util.List;
import javax.ejb.Local;
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
@Local
public class PersistenciaCuentasBancos implements PersistenciaCuentasBancosInterface {

   private static Logger log = Logger.getLogger(PersistenciaCuentasBancos.class);

    @Override
    public void crear(EntityManager em, CuentasBancos cuentabanco) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (cuentabanco.getInforeporte().getSecuencia() == null) {
                cuentabanco.setInforeporte(null);
            }
            if(cuentabanco.getBanco().getSecuencia() == null){
                cuentabanco.setInforeporte(null);
            }
            em.merge(cuentabanco);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaCuentasBancos.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, CuentasBancos cuentabanco) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (cuentabanco.getInforeporte().getSecuencia() == null) {
                cuentabanco.setInforeporte(null);
            }
            if(cuentabanco.getBanco().getSecuencia() == null){
                cuentabanco.setInforeporte(null);
            }
            em.merge(cuentabanco);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaCuentasBancos.editar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, CuentasBancos cuentabanco) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (cuentabanco.getInforeporte().getSecuencia() == null) {
                cuentabanco.setInforeporte(null);
            }
            if(cuentabanco.getBanco().getSecuencia() == null){
                cuentabanco.setInforeporte(null);
            }
            em.remove(em.merge(cuentabanco));
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaCuentasBancos.borrar: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<CuentasBancos> buscarCuentasBanco(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM CUENTASBANCOS";
            Query query = em.createNativeQuery(sql, CuentasBancos.class);
            List<CuentasBancos> lista = query.getResultList();
            if (lista != null) {
                if (!lista.isEmpty()) {
                    for (int i = 0; i < lista.size(); i++) {
                        if (lista.get(i).getInforeporte() == null) {
                            lista.get(i).setInforeporte(new Inforeportes());
                        }
                        
                        if(lista.get(i).getBanco() == null){
                            lista.get(i).setBanco(new Bancos());
                        }
                    }
                }
            }
            return lista;
        } catch (Exception e) {
            log.error("error en persistenciacuentabanco.buscarcuentabanco :" + e.toString());
            return null;
        }
    }

    @Override
    public List<Bancos> buscarBancos(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM BANCOS";
            Query query = em.createNativeQuery(sql, Bancos.class);
            List<Bancos> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            log.error("error en persistenciacuentabanco.buscarBancos :" + e.toString());
            return null;
        }
    }

    @Override
    public List<Inforeportes> buscarReportes(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM INFOREPORTES";
            Query query = em.createNativeQuery(sql, Inforeportes.class);
            List<Inforeportes> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            log.error("error en persistenciacuentabanco.buscarReportes :" + e.toString());
            return null;
        }
    }
}
