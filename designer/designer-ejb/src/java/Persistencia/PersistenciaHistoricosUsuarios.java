/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.HistoricosUsuarios;
import InterfacePersistencia.PersistenciaHistoricosUsuariosInterface;
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
public class PersistenciaHistoricosUsuarios implements PersistenciaHistoricosUsuariosInterface {

    @Override
    public List<HistoricosUsuarios> buscarHistoricosUsuarios(EntityManager em,BigInteger secUsuario) {
       try {
            em.clear();
            String sql = "SELECT * FROM HISTORICOSUSUARIOS WHERE USUARIO = ? ORDER BY FECHAINICIAL DESC";
            Query query = em.createNativeQuery(sql,HistoricosUsuarios.class);
            query.setParameter(1, secUsuario);
            List<HistoricosUsuarios> historicosusu = query.getResultList();
            return historicosusu;
        } catch (Exception e) {
            System.out.println("Error buscarUsuarios PersistenciaUsuariosVista" + e.getMessage());
            return null;
        }
    }

    @Override
    public void crear(EntityManager em, HistoricosUsuarios historicou) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(historicou);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaHistoricosUsuarios.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, HistoricosUsuarios historicou) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(historicou);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaHistoricosUsuarios.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, HistoricosUsuarios historicou) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(historicou));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaHistoricosUsuarios.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

}
