/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import ClasesAyuda.ExtraeCausaExcepcion;
import Entidades.Perfiles;
import InterfacePersistencia.PersistenciaConexionInicialInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 * Clase Stateless. <br>
 * Clase encargada de realizar operaciones referentes al ingreso al aplicativo
 * de la base de datos
 *
 * @author Andrés Pineda
 */
@Stateless
public class PersistenciaConexionInicial implements PersistenciaConexionInicialInterface {

   private static Logger log = Logger.getLogger(PersistenciaConexionInicial.class);

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    */
   private void usuarioActual(EntityManager eManager) {
      try {
         eManager.clear();
         Query q = eManager.createNativeQuery("select user from dual");
         String s = (String) q.getSingleResult();
         log.warn("Persistencia.PersistenciaConexionInicial.usuarioActual() select user from dual: " + s);
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaConexionInicial.usuarioActual() ERROR: " + e);
      }
   }

   @Override
   public boolean validarUsuario(EntityManager eManager, String usuario) {
      try {
         eManager.getTransaction().begin();
         String sqlQuery = "SET ROLE ROLENTRADA";
         Query query = eManager.createNativeQuery(sqlQuery);
         query.executeUpdate();
         sqlQuery = "select COUNT(*) from usuarios where alias = ? AND activo = 'S'";
         query = eManager.createNativeQuery(sqlQuery);
         query.setParameter(1, usuario);
         BigDecimal retorno = (BigDecimal) query.getSingleResult();
         Integer instancia = retorno.intValueExact();
         eManager.getTransaction().commit();
         if (instancia > 0) {
            log.warn("El usuario es correcto y esta activo");
            return true;
         } else {
            log.warn("El usuario es incorrecto y/o esta inactivo");
            eManager.getEntityManagerFactory().close();
            return false;
         }
      } catch (Exception e) {
         log.fatal("Error validarUsuario: " + e);
         return false;
      }
   }

   @Override
   public boolean validarUsuarioConJPA(EntityManager eManager, String usuario) {
      try {
         String sqlQuery = "SET ROLE ROLENTRADA";
         Query query = eManager.createNativeQuery(sqlQuery);
         query.executeUpdate();
         sqlQuery = "select COUNT(*) from usuarios where alias = ? AND activo = 'S'";
         query = eManager.createNativeQuery(sqlQuery);
         query.setParameter(1, usuario);
         BigDecimal retorno = (BigDecimal) query.getSingleResult();
         Integer instancia = retorno.intValueExact();
         if (instancia > 0) {
            log.warn("El usuario es correcto y esta activo");
            return true;
         } else {
            log.warn("El usuario es incorrecto y/o esta inactivo");
            eManager.getEntityManagerFactory().close();
            return false;
         }
      } catch (Exception e) {
         log.fatal("Error validarUsuarioConJPA: " + e);
         return false;
      }
   }

   @Override
   public EntityManager validarConexionUsuario(EntityManagerFactory emf) {
      try {
         log.warn("EMFFFFFFFFFF: " + emf + "Esta abierto?: " + emf.isOpen());
         log.warn("Entro al metodo validarConexionUsuario");
         EntityManager em = emf.createEntityManager();
         log.warn("EM: " + em);
         log.warn("em.getProperties(): " + em.getProperties());
         if (em.isOpen()) {
            return em;
         }
      } catch (Exception e) {
         log.fatal("Error validarConexionUsuario PersistenciaConexionInicial : " + e.toString());
         emf.close();
      }
      return null;
   }

   @Override
   public void accesoDefault(EntityManager eManager) {
      try {
         eManager.getTransaction().begin();
         String sqlQuery = "SET ROLE ROLENTRADA";
         Query query = eManager.createNativeQuery(sqlQuery);
         query.executeUpdate();
         eManager.getTransaction().commit();
      } catch (Exception e) {
         log.fatal("Persistencia.PersistenciaConexionInicial.accesoDefault() e: " + e);
      }
   }

//    /**
//     * Metodo encargado de retornar el ultimo error que se capturo en los try -
//     * catch.
//     *
//     * @param e Exception
//     * @return Retorna el ultimo error capturado.
//     */
//    private Throwable getLastThrowable(Exception e) {
//        Throwable t;
//        for (t = e.getCause(); t.getCause() != null; t = t.getCause());
//        return t;
//    }
   @Override
   public Perfiles perfilUsuario(EntityManager eManager, BigInteger secPerfil) {
      log.warn("PersistenciaConexionInicial.perfilUsuario()");
      try {
         eManager.getTransaction().begin();
         Query query = eManager.createQuery("SELECT p FROM Perfiles p WHERE p.secuencia = :secPerfil ");
         query.setParameter("secPerfil", secPerfil);
         Perfiles perfil = (Perfiles) query.getSingleResult();
         eManager.getTransaction().commit();
         return perfil;
      } catch (Exception e) {
         log.fatal("Persistencia.PersistenciaConexionInicial.perfilUsuario() e: " + e);
         return null;
      }
   }

   @Override
   public Perfiles perfilUsuarioConJPA(EntityManager eManager, BigInteger secPerfil) {
      log.warn("PersistenciaConexionInicial.perfilUsuarioConJPA()");
      try {
         Query query = eManager.createQuery("SELECT p FROM Perfiles p WHERE p.secuencia = :secPerfil ");
         query.setParameter("secPerfil", secPerfil);
         Perfiles perfil = (Perfiles) query.getSingleResult();
         return perfil;
      } catch (Exception e) {
         log.fatal("Persistencia.PersistenciaConexionInicial.perfilUsuario() e: " + e);
         return null;
      }
   }

   @Override
   public BigInteger usuarioLogin(EntityManager eManager, String usuarioBD) {
      log.warn("PersistenciaConexionInicial.usuarioLogin()");
      try {
         eManager.getTransaction().begin();
         Query query = eManager.createQuery("SELECT u.perfil.secuencia FROM Usuarios u WHERE u.alias = :usuarioBD");
         query.setParameter("usuarioBD", usuarioBD);
         eManager.getTransaction().commit();
         BigInteger secPerfil = (BigInteger) query.getSingleResult();
         return secPerfil;
      } catch (Exception e) {
         log.fatal("Persistencia.PersistenciaConexionInicial.methodName() e: " + e);
         return null;
      }
   }

   @Override
   public BigInteger usuarioLoginConJPA(EntityManager eManager, String usuarioBD) {
      log.warn("PersistenciaConexionInicial.usuarioLoginConJPA()");
      try {
         Query query = eManager.createQuery("SELECT u.perfil.secuencia FROM Usuarios u WHERE u.alias = :usuarioBD");
         query.setParameter("usuarioBD", usuarioBD);
         BigInteger secPerfil = (BigInteger) query.getSingleResult();
         return secPerfil;
      } catch (Exception e) {
         log.fatal("Persistencia.PersistenciaConexionInicial.methodName() e: " + e);
         return null;
      }
   }

   @Override
   public void setearUsuario(EntityManager eManager, String rol, String pwd) {
      try {
         usuarioActual(eManager);
         log.warn(this.getClass() + "eManager.getProperties(): " + eManager.getProperties());
         String texto = "SET ROLE " + rol + " IDENTIFIED BY " + pwd;
//         em =eManager eManager;
         eManager.getTransaction().begin();
         String sqlQuery = texto;
         Query query = eManager.createNativeQuery(sqlQuery);
         query.setParameter(1, rol);
         query.setParameter(2, pwd);
         query.executeUpdate();
         eManager.getTransaction().commit();
      } catch (Exception e) {
         log.fatal("Persistencia.PersistenciaConexionInicial.setearUsuario() e: " + e);
      }
   }

   @Override
   public int cambiarClave(EntityManager eManager, String usuario, String nuevaClave) {
      try {
         eManager.getTransaction().begin();
         String sqlQuery = "ALTER USER " + usuario + " IDENTIFIED BY " + nuevaClave;
         Query query = eManager.createNativeQuery(sqlQuery);
         int resultado = query.executeUpdate();
         eManager.getTransaction().commit();
         return resultado;
      } catch (Exception e) {
         log.error("Persistencia.PersistenciaConexionInicial.cambiarClave() e: " + e);
         eManager.getTransaction().rollback();
         try {
            int codigo = ExtraeCausaExcepcion.obtenerCodigoSQLException(e);
            return codigo;
         } catch (Exception ex) {
            log.error("Persistencia.PersistenciaConexionInicial.cambiarClave() ex: " + ex);
            return ex.hashCode();
         }
      }
   }
}
