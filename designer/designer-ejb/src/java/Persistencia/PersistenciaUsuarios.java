/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Usuarios;
import InterfacePersistencia.PersistenciaUsuariosInterface;
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

@Stateless
public class PersistenciaUsuarios implements PersistenciaUsuariosInterface {

    @Override
    public List<Usuarios> buscarUsuarios(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT u FROM Usuarios u WHERE u.persona is not null");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Usuarios> usuarios = (List<Usuarios>) query.getResultList();
            return usuarios;
        } catch (Exception e) {
            System.out.println("Error buscarUsuarios PersistenciaUsuarios" + e.getMessage());
            return null;
        }
    }

    @Override
    public Usuarios buscarUsuario(EntityManager em, String alias) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT u FROM Usuarios u WHERE u.alias= :alias");
            query.setParameter("alias", alias);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            Usuarios usuarios = (Usuarios) query.getSingleResult();
            return usuarios;
        } catch (Exception e) {
            System.out.println("Persistencia.PersistenciaUsuarios.buscarUsuario()" + e.getMessage());
            return null;
        }
    }

    @Override
    public void crear(EntityManager em, Usuarios usuarios) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuarios);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaUsuarios.crear: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void crearUsuario(EntityManager em, String alias) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "call USUARIOS_PKG.CrearUsuario(?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, alias);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void crearUsuarioPerfil(EntityManager em, String alias, String perfil) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "call USUARIOS_PKG.AsignarPerfil(?,?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, alias);
            query.setParameter(2, perfil);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void editar(EntityManager em, Usuarios usuarios) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuarios);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaUsuarios.editar: " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void borrar(EntityManager em, Usuarios usuarios) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(usuarios));
            tx.commit();

        } catch (Exception e) {
            try {
                if (tx.isActive()) {
                    tx.rollback();
                }
            } catch (Exception ex) {
                System.out.println("Error PersistenciaUsuarios.borrar: " + e.getMessage());
            }
        }
    }

    @Override
    public void borrarUsuario(EntityManager em, String alias) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "call USUARIOS_PKG.EliminarUsuario(?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, alias);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaUsuarios.borrarUsuario. " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public Integer borrarUsuarioTotal(EntityManager em, String alias) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        Integer exeE2 = null;
        try {
            tx.begin();
            String sqlQuery = "call USUARIOS_PKG.EliminarRegistrosUsuario(?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, alias);
            exeE2 = query.executeUpdate();
            tx.commit();
            return exeE2;
        } catch (Exception e) {
            System.out.println("Error PersistenciaUsuarios.borrarUsuarioTotal. " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
            return null;
        }
    }

    @Override
    public void clonarUsuario(EntityManager em, BigInteger usuarioOrigen, BigInteger usuarioDestino) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "call USUARIOS_PKG.ClonarUsuario(?, ?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, usuarioOrigen);
            query.setParameter(2, usuarioDestino);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaUsuarios.clonarUsuario. " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void desbloquearUsuario(EntityManager em, String alias) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sentencia = "ALTER USER " + alias + " ACCOUNT UNLOCK";
            String sqlQuery = "call PERFILES_PKG.AsignaDDL(?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, sentencia);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaUsuarios.clonarUsuario. " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void restaurarUsuario(EntityManager em, String alias, String fecha) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String contra = alias + "_" + fecha;
            String sentencia = "ALTER USER " + alias + " IDENTIFIED BY " + contra;
            String sqlQuery = "call PERFILES_PKG.AsignaDDL(?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, sentencia);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            System.out.println("Error PersistenciaUsuarios.restaurarUsuario. " + e.getMessage());
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }
}
