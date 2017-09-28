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
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaUsuariosInfoReportes implements PersistenciaUsuariosInfoReportesInterface {

    private static Logger log = Logger.getLogger(PersistenciaUsuariosInfoReportes.class);

    @Override
    public void crear(EntityManager em, UsuariosInforeportes usuarioIR) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuarioIR);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaUsuariosInfoReportes.crear :  ", e);
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
            log.error("Error PersistenciaUsuariosInfoReportes.editar :  ", e);
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
            log.error("Error PersistenciaUsuariosInfoReportes.borrar :  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<UsuariosInforeportes> listaUsuariosIR(EntityManager em, BigInteger secUsuario) {
        try {
            em.clear();
            String sql = "SELECT * FROM USUARIOSINFOREPORTES WHERE USUARIO = ? ORDER BY INFOREPORTE";
            Query query = em.createNativeQuery(sql, UsuariosInforeportes.class);
            query.setParameter(1, secUsuario);
            List<UsuariosInforeportes> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            log.error("Error PersitenciaUsuariosInfoReportes.listaUsuariosIR() :  ", e);
            return null;
        }
    }

    @Override
    public List<Inforeportes> lovIR(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM INFOREPORTES ORDER BY CODIGO";
            Query query = em.createNativeQuery(sql, Inforeportes.class);
            List<Inforeportes> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            log.error("Error PersitenciaUsuariosInfoReportes.lovIR() :  ", e);
            return null;
        }
    }

    @Override
    public List<Usuarios> listaUsuarios(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT * FROM USUARIOS WHERE PERFIL IS NOT NULL ORDER BY ALIAS";
            Query query = em.createNativeQuery(sql, Usuarios.class);
            List<Usuarios> lista = query.getResultList();
            return lista;
        } catch (Exception e) {
            log.error("Error Persitencia Usuarios InfoReportes.listaUsuarios() :  ", e);
            return null;
        }
    }

    @Override
    public Long getTotalRegistros(EntityManager em, BigInteger secUsuario) {
        Long count;
        try {
            em.clear();
            Query query = em.createQuery("select count(uir) from UsuariosInforeportes uir where uir.usuario.secuencia=:secusuario");
            query.setParameter("secusuario", secUsuario);
            List lista = query.getResultList();
            count = (Long) lista.get(0);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return count;
        } catch (Exception e) {
            log.error("Error en getTotalRegistros :  ", e);
            count = Long.valueOf(0);
            return count;
        }
    }

    @Override
    public List<UsuariosInforeportes> getFind(EntityManager em, int firstRow, int max, BigInteger secUsuario) {
        try {
            em.clear();
            Query query = em.createQuery("select uir from UsuariosInforeportes uir where uir.usuario.secuencia=:secUsuario order by uir.inforeporte.secuencia").setFirstResult(firstRow).setMaxResults(max);
            query.setParameter("secUsuario", secUsuario);
            List<UsuariosInforeportes> lista = query.getResultList();
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return lista;
        } catch (Exception e) {
            log.error("error en getFind Persistencia :   ", e);
            return null;
        }
    }

    @Override
    public List<UsuariosInforeportes> getBuscarUIR(EntityManager em, int firstRow, int max, BigInteger secUsuarioIR) {
        try {
            em.clear();
            Query query = em.createQuery("select uir from UsuariosInforeportes uir where uir.secuencia=:secUsuarioIR", UsuariosInforeportes.class).setFirstResult(firstRow).setMaxResults(max);
            query.setParameter("secUsuarioIR", secUsuarioIR);
            List<UsuariosInforeportes> lista = query.getResultList();
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return lista;
        } catch (Exception e) {
            log.error("error en getBuscarUIR Persistencia :   ", e);
            return null;
        }
    }

    @Override
    public Long getTotalRegistrosBuscar(EntityManager em, BigInteger secUsuarioIR) {
        Long count;
        try {
            em.clear();
            Query query = em.createQuery("select count(uir) from UsuariosInforeportes uir where uir.secuencia=:secusuario");
            query.setParameter("secusuario", secUsuarioIR);
            List lista = query.getResultList();
            count = (Long) lista.get(0);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            return count;
        } catch (Exception e) {
            log.error("Error en getTotalRegistrosBuscar :  ", e);
            count = Long.valueOf(0);
            return count;
        }
    }

}
