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

/**
 * Clase Stateless.<br>
 * Clase encargada de realizar operaciones sobre la tabla 'Usuarios' de la base
 * de datos.
 *
 * @author betelgeuse
 */
@Stateless
public class PersistenciaUsuarios implements PersistenciaUsuariosInterface {

    /**
     * Atributo EntityManager. Representa la comunicación con la base de datos.
     */
    /*    @PersistenceContext(unitName = "DesignerRHN-ejbPU")
     private EntityManager em;
     */
    @Override
    public List<Usuarios> buscarUsuarios(EntityManager em) {
        try {
            em.clear();
            Query query = em.createQuery("SELECT u FROM Usuarios u WHERE u.persona is not null");
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            List<Usuarios> usuarios = (List<Usuarios>) query.getResultList();
            return usuarios;
        } catch (Exception e) {
            System.out.println("Error buscarUsuarios PersistenciaUsuarios");
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
            Usuarios usuarios = null;
            return usuarios;
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
            System.out.println("Error PersistenciaUsuarios.crear: " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public Integer crearUsuario(EntityManager em, String alias) {
        Integer exeC = null;
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "call USUARIOS_PKG.CrearUsuario(?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, alias);
            exeC = query.executeUpdate();
            tx.commit();
            return exeC;            
        } catch (Exception e) {
            System.out.println("Error PersistenciaUsuarios.crearUsuario. " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
            return null;            
        }
    }

    @Override
    public Integer crearUsuarioPerfil(EntityManager em, String alias, String perfil) {
        Integer exeC2 = null;
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "call USUARIOS_PKG.AsignarPerfil(?, ?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, alias);
            query.setParameter(2, perfil);
            exeC2 = query.executeUpdate();
            tx.commit();
            return exeC2;
        } catch (Exception e) {
            System.out.println("Error PersistenciaUsuarios.crearUsuarioPerfil. " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
            return null;
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
            System.out.println("Error PersistenciaUsuarios.editar: " + e);
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
                System.out.println("Error PersistenciaUsuarios.borrar: " + e);
            }
        }
    }
    
    @Override
    public Integer borrarUsuario(EntityManager em, String alias) {
        em.clear();
        EntityTransaction tx = em.getTransaction();
        Integer exeE = null;
        try {
            tx.begin();
            String sqlQuery = "call USUARIOS_PKG.EliminarUsuario(?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, alias);
            exeE = query.executeUpdate();
            tx.commit();
            return exeE;
           
        } catch (Exception e) {
            System.out.println("Error PersistenciaUsuarios.borrarUsuario. " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
            return null;
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
            System.out.println("Error PersistenciaUsuarios.borrarUsuarioTotal. " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
            return null;
        }
    }
    @Override
    public Integer clonarUsuario(EntityManager em, String alias, String aliasclonado, BigInteger secuencia) {
        Integer exeA = null;
        em.clear();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            String sqlQuery = "call USUARIOS_PKG.ClonarRegistrosUsuario(?, ?, ?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, alias);
            query.setParameter(2, aliasclonado);
            query.setParameter(3, secuencia);
            exeA = query.executeUpdate();
            tx.commit();
            System.out.println("clonando o algo parecido");
            return exeA;
        } catch (Exception e) {
            System.out.println("Error PersistenciaUsuarios.clonarUsuario. " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
            return null;
        }
    }
    @Override
    public Integer desbloquearUsuario(EntityManager em, String alias) {
        Integer exeD = null;
        em.clear();
        EntityTransaction tx = em.getTransaction(); 
        try {
            tx.begin();
            String sentencia = "ALTER USER "+alias+" ACCOUNT UNLOCK";
            String sqlQuery = "call PERFILES_PKG.AsignaDDL(?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, sentencia);
            exeD = query.executeUpdate();
            tx.commit();
            System.out.println("desbloqueando o algo parecido");
            return exeD;
           
        } catch (Exception e) {
            System.out.println("Error PersistenciaUsuarios.clonarUsuario. " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
            return null;
        }
    }
    @Override
    public Integer restaurarUsuario(EntityManager em, String alias, String fecha) {
        Integer exeR = null;
        em.clear();
        EntityTransaction tx = em.getTransaction(); 
        try {
            tx.begin();
            String contra = alias+"_"+fecha;
            String sentencia = "ALTER USER "+alias+" IDENTIFIED BY "+contra;
            String sqlQuery = "call PERFILES_PKG.AsignaDDL(?)";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, sentencia);
            exeR = query.executeUpdate();
            tx.commit();
            System.out.println("restaurando o algo parecido");
            return exeR;
        } catch (Exception e) {
            System.out.println("Error PersistenciaUsuarios.restaurarUsuario. " + e);
            if (tx.isActive()) {
                tx.rollback();
            }
            return null;
        }
    }
}
