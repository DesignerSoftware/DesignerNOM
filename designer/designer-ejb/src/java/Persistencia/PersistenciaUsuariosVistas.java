/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.UsuariosVistas;
import InterfacePersistencia.PersistenciaUsuariosVistasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
//import org.primefaces.context.RequestContext;

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'UsuariosVistas' de la
 * base de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaUsuariosVistas implements PersistenciaUsuariosVistasInterface {

   private static Logger log = Logger.getLogger(PersistenciaUsuariosVistas.class);

    @Override
    public List<UsuariosVistas> buscarUsuariosVistas(EntityManager em) {
        try {
            em.clear();
            String sql = "SELECT UV.*\n"
                    + "FROM USUARIOSVISTAS UV, OBJETOSDB O\n"
                    + "WHERE UV.OBJETODB=O.SECUENCIA";
            Query query = em.createNativeQuery(sql,UsuariosVistas.class);
            List<UsuariosVistas> usuariosvistas = query.getResultList();
            return usuariosvistas;
        } catch (Exception e) {
            log.error("Error buscarUsuarios PersistenciaUsuariosVista ", e);
            return null;
        }
    }

    @Override
    public void crear(EntityManager em, UsuariosVistas usuariosVistas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuariosVistas);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaUsuariosVistas.crear:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, UsuariosVistas usuariosVistas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuariosVistas);
            tx.commit();
        } catch (Exception e) {
            log.error("Error PersistenciaUsuariosVistas.editar:  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, UsuariosVistas usuariosVistas) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(usuariosVistas));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                log.error("Error PersistenciaUsuariosVistas.borrar:  ", e);
            }
        }
    }

    @Override
    public Integer crearUsuarioVista(EntityManager em, BigInteger objeto) {
        Integer exe = null;
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "call USUARIOS_PKG.CrearVistaFiltro(?)";
            Query query2 = em.createNativeQuery(sqlQuery);
            query2.setParameter(1, objeto);
            query2.executeUpdate();
            tx.commit();
            return exe;
        } catch (Exception e) {
            log.error("Error PersistenciaUsuarios.crearUsuarioVista.  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            return null;
        }
    }

}
