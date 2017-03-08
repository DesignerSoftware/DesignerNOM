/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.UsuariosVistas;
import InterfacePersistencia.PersistenciaUsuariosVistasInterface;
import static com.sun.faces.facelets.util.Path.context;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
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
            System.out.println("Error buscarUsuarios PersistenciaUsuariosVista" + e.getMessage());
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
            System.out.println("Error PersistenciaUsuariosVistas.crear: " + e.getMessage());
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
            System.out.println("Error PersistenciaUsuariosVistas.editar: " + e.getMessage());
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
                System.out.println("Error PersistenciaUsuariosVistas.borrar: " + e.getMessage());
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
            System.out.println("Error PersistenciaUsuarios.crearUsuarioVista. " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
            return null;
        }
    }

}
