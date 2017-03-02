/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.UsuariosEstructuras;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaUsuariosEstructurasInterface;
import InterfacePersistencia.PersistenciaUsuariosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaUsuariosEstructuras implements PersistenciaUsuariosEstructurasInterface {

    @Override
    public void crear(EntityManager em, UsuariosEstructuras usuarioEstructura) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuarioEstructura);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaUsuariosEstructuras.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, UsuariosEstructuras usuarioEstructura) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuarioEstructura);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaUsuariosEstructuras.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, UsuariosEstructuras usuarioEstructura) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(usuarioEstructura));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaUsuariosEstructuras.borrar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<UsuariosEstructuras> consultarUsuariosEstructuras(EntityManager em, BigInteger secUsuario) {
        em.clear();
        try {
            String sqlQuery = "SELECT *  FROM USUARIOSESTRUCTURAS WHERE USUARIO = ? ";
            Query query = em.createNativeQuery(sqlQuery, UsuariosEstructuras.class);
            query.setParameter(1, secUsuario);
            List<UsuariosEstructuras> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.out.println("Error PersistenciaUsuariosEstructuras.consultarUsuariosEstructuras: " + e.getMessage());
            return null;
        }

    }

}
