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
import java.math.BigDecimal;
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

    @Override
    public BigDecimal contarUsuariosEstructuras(EntityManager em, BigInteger secUsuario) {
        em.clear();
        try {
            String sqlQuery = "SELECT COUNT(*)  FROM USUARIOSESTRUCTURAS WHERE USUARIO = ? ";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secUsuario);
            BigDecimal count = (BigDecimal)query.getSingleResult();
            return count;
        } catch (Exception e) {
            System.out.println("Error PersistenciaUsuariosEstructuras.contarUsuariosEstructuras: " + e.getMessage());
            return null;
        }

    }

    @Override
    public void crearVistaUsuarioEstructura(EntityManager em, BigInteger secUsuarioEstructura, BigInteger secUsuario) {
         em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "call USUARIOS_PKG.CrearVistaUsuarioEstructura(?, ?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, secUsuarioEstructura);
            query.setParameter(2, secUsuario);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaUsuariosEstructuras.crearVistaUsuarioEstructura: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

}
