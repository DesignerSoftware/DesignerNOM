/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.UsuariosFiltros;
import InterfacePersistencia.PersistenciaUsuariosFiltrosInterface;
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
public class PersistenciaUsuariosFiltros implements PersistenciaUsuariosFiltrosInterface {

    @Override
    public void crear(EntityManager em, UsuariosFiltros usuarioF) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuarioF);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaUsuariosFiltros.crear()" + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, UsuariosFiltros usuarioF) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuarioF);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaUsuariosFiltros.editar()" + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, UsuariosFiltros usuarioF) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(usuarioF));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaUsuariosFiltros.borrar()" + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<UsuariosFiltros> consultarUsuariosFiltros(EntityManager em, BigInteger secUsuarioEstructura) {
        System.out.println("Persistencia.PersistenciaUsuariosFiltros.consultarUsuariosFiltros()" + secUsuarioEstructura);
        try{
            em.clear();
            String sql = "SELECT * FROM USUARIOSFILTROS WHERE USUARIOESTRUCTURA = ?";
            Query query = em.createNativeQuery(sql, UsuariosFiltros.class);
            query.setParameter(1, secUsuarioEstructura);
            List<UsuariosFiltros> lista = query.getResultList();
            System.out.println("lista " + lista);
            return lista;
        }catch(Exception e){
            System.out.println("error PersistenciaUsuariosFiltros.consultarUsuariosFiltros()" + e.getMessage());
            return null;
        }
    }

}
