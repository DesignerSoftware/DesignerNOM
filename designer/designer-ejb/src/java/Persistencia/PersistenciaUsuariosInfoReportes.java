/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.Inforeportes;
import Entidades.Usuarios;
import Entidades.UsuariosInforeportes;
import InterfacePersistencia.PersistenciaUsuariosInfoReportesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaUsuariosInfoReportes implements PersistenciaUsuariosInfoReportesInterface {

    @Override
    public void crear(EntityManager em, UsuariosInforeportes usuarioIR) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuarioIR);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaUsuariosInfoReportes.crear : " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, UsuariosInforeportes usuarioIR) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuarioIR);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaUsuariosInfoReportes.editar : " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, UsuariosInforeportes usuarioIR) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(usuarioIR));
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaUsuariosInfoReportes.borrar : " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<UsuariosInforeportes> listaUsuariosIR(EntityManager em, BigInteger secUsuario) {
        try {
            em.clear();
            String sql = "SELECT * FROM USUARIOSINFOREPORTES WHERE USUARIO = ?";
            Query query = em.createNativeQuery(sql, UsuariosInforeportes.class);
            query.setParameter(1, secUsuario);
            List<UsuariosInforeportes> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.out.println("Error PersitenciaUsuariosInfoReportes.listaUsuariosIR() : " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Inforeportes> lovIR(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM INFOREPORTES";
            Query query = em.createNativeQuery(sql, Inforeportes.class);
            List<Inforeportes> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.out.println("Error PersitenciaUsuariosInfoReportes.lovIR() : " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Usuarios> listaUsuarios(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM USUARIOS WHERE PERFIL IS NOT NULL";
            Query query = em.createNativeQuery(sql, Usuarios.class);
            List<Usuarios> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            System.out.println("Error Persitencia Usuarios InfoReportes.listaUsuarios() : " + e.getMessage());
            return null;
        }
    }

}
