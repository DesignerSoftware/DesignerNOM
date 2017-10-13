/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import Entidades.Usuarios;
import InterfacePersistencia.PersistenciaUsuariosInterface;
import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import org.eclipse.persistence.exceptions.DatabaseException;

@Stateless
public class PersistenciaUsuarios implements PersistenciaUsuariosInterface {

    private static Logger log = Logger.getLogger(PersistenciaUsuarios.class);

    @Override
    public List<Usuarios> buscarUsuarios(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT u FROM Usuarios u WHERE u.persona is not null");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Usuarios> usuarios = (List<Usuarios>) query.getResultList();
            return usuarios;
        } catch (Exception e) {
            log.error("Error buscarUsuarios PersistenciaUsuarios ", e);
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
            log.error("PersistenciaUsuarios.buscarUsuario():  ", e);
            return null;
        }
    }

    @Override
    public String crear(EntityManager em, Usuarios usuarios) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuarios);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaUsuarios.crear:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Editar la novedad ";
            }
        }
    }

    @Override
    public String crearUsuario(EntityManager em, String alias) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "call USUARIOS_PKG.CrearUsuario(?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, alias);
            query.executeUpdate();
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLException || e instanceof SQLSyntaxErrorException) {
                return e.toString();
            } else {
                return "Ha ocurrido un error al crear al usuario";
            }
        }
    }

    @Override
    public String crearUsuarioPerfil(EntityManager em, String alias, String perfil) {
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
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLException || e instanceof SQLSyntaxErrorException) {
                return e.toString();
            } else {
                return "Ha ocurrido un error al Asignar el perfil al usuario";
            }
        }
    }

    @Override
    public String editar(EntityManager em, Usuarios usuarios) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuarios);
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaUsuarios.editar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Editar la novedad ";
            }
        }
    }

    @Override
    public String borrar(EntityManager em, Usuarios usuarios) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(usuarios));
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLIntegrityConstraintViolationException || e instanceof DatabaseException) {
                log.error("Error PersistenciaUsuarios.borrar:  ", e);
                return e.toString();
            } else {
                return "Ha ocurrido un error al Editar la novedad ";
            }
        }
    }

    @Override
    public String borrarUsuario(EntityManager em, String alias) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "call USUARIOS_PKG.EliminarUsuario(?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, alias);
            query.executeUpdate();
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            log.error("Error PersistenciaUsuarios.borrarUsuario.  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLException || e instanceof SQLSyntaxErrorException) {
                return e.toString();
            } else {
                return "Ha ocurrido un error al borrar el usuario";
            }
        }
    }

    @Override
    public String borrarUsuarioTotal(EntityManager em, String alias) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "call USUARIOS_PKG.EliminarRegistrosUsuario(?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, alias);
            query.executeUpdate();
            tx.commit();
            return "EXITO";
        } catch (Exception e) {
            log.error("Error PersistenciaUsuarios.borrarUsuarioTotal.  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLException || e instanceof SQLSyntaxErrorException) {
                return e.toString();
            } else {
                return "Ha ocurrido un error al borrar los registros del usuario";
            }
        }
    }

    @Override
    public String clonarUsuario(EntityManager em, BigInteger usuarioOrigen, BigInteger usuarioDestino) {
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
            return "EXITO";
        } catch (Exception e) {
            log.error("Error PersistenciaUsuarios.clonarUsuario.  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLException || e instanceof SQLSyntaxErrorException) {
                return e.toString();
            } else {
                return "Ha ocurrido un error al clonar el usuario";
            }
        }
    }

    @Override
    public String desbloquearUsuario(EntityManager em, String alias) {
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
            return "EXITO";
        } catch (Exception e) {
            log.error("Error desbloquearUsuario.  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLException || e instanceof SQLSyntaxErrorException) {
                return e.toString();
            } else {
                return "Ha ocurrido un error al desbloquear al usuario";
            }
        }
    }

    @Override
    public String restaurarUsuario(EntityManager em, String alias, String fecha) {
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
            return "EXITO";
        } catch (Exception e) {
            log.error("Error PersistenciaUsuarios.restaurarUsuario.  ", e);
            if (tx.isActive()) {
                tx.rollback();
            }
            if (e instanceof PersistenceException || e instanceof SQLException || e instanceof SQLSyntaxErrorException) {
                return e.toString();
            } else {
                return "Ha ocurrido un error al Asignar el reiniciar el usuario";
            }
        }
    }

    @Override
    public List<Usuarios> buscarUsuariosXSecuencia(EntityManager em, BigInteger secUsuario) {
        try {
            em.clear();
            String sql = "SELECT * FROM USUARIOS WHERE SECUENCIA = ? ";
            Query query = em.createNativeQuery(sql, Usuarios.class);
            query.setParameter(1, secUsuario);
            List<Usuarios> usuarios = (List<Usuarios>) query.getResultList();
            return usuarios;
        } catch (Exception e) {
            log.error("Error buscarUsuarios PersistenciaUsuarios ", e);
            return null;
        }
    }
}
