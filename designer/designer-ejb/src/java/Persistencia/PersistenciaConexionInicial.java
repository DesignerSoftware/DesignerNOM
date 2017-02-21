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

   /**
    * Atributo EntityManager. Representa la comunicación con la base de datos.
    */
   private EntityManager em;

   @Override
   public boolean validarUsuario(EntityManager eManager, String usuario) {
      try {
         em = eManager;
         em.getTransaction().begin();
         String sqlQuery = "SET ROLE ROLENTRADA";
         Query query = em.createNativeQuery(sqlQuery);
         query.executeUpdate();
         sqlQuery = "select COUNT(*) from usuarios where alias = ? AND activo = 'S'";
         query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, usuario);
         BigDecimal retorno = (BigDecimal) query.getSingleResult();
         Integer instancia = retorno.intValueExact();
         em.getTransaction().commit();
         if (instancia > 0) {
            System.out.println("El usuario es correcto y esta activo");
            return true;
         } else {
            System.out.println("El usuario es incorrecto y/o esta inactivo");
            em.getEntityManagerFactory().close();
            return false;
         }
      } catch (Exception e) {
         System.out.println("Error validarUsuario: " + e);
         return false;
      }
   }

   @Override
   public EntityManager validarConexionUsuario(EntityManagerFactory emf) {
      try {
         System.out.println("EMFFFFFFFFFF: " + emf + "Esta abierto?: " + emf.isOpen());
         System.out.println("Entro al metodo validarConexionUsuario");
         em = emf.createEntityManager();
         System.out.println("EM: " + em);
         if (em.isOpen()) {
            return em;
         }
      } catch (Exception e) {
         System.out.println("Error validarConexionUsuario PersistenciaConexionInicial : " + e.toString());
         emf.close();
      }
      return null;
   }

   @Override
   public void accesoDefault(EntityManager eManager) {
      try {
         em = eManager;
         em.getTransaction().begin();
         String sqlQuery = "SET ROLE ROLENTRADA";
         Query query = em.createNativeQuery(sqlQuery);
         query.executeUpdate();
         em.getTransaction().commit();
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaConexionInicial.accesoDefault() e: " + e);
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
      try {
         em = eManager;
         em.getTransaction().begin();
         Query query = em.createQuery("SELECT p FROM Perfiles p WHERE p.secuencia = :secPerfil ");
         query.setParameter("secPerfil", secPerfil);
         Perfiles perfil = (Perfiles) query.getSingleResult();
         em.getTransaction().commit();
         return perfil;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaConexionInicial.perfilUsuario() e: " + e);
         return null;
      }
   }

   @Override
   public BigInteger usuarioLogin(EntityManager eManager, String usuarioBD) {
      try {
         em = eManager;
         em.getTransaction().begin();
         Query query = em.createQuery("SELECT u.perfil.secuencia FROM Usuarios u WHERE u.alias = :usuarioBD");
         query.setParameter("usuarioBD", usuarioBD);
         em.getTransaction().commit();
         BigInteger secPerfil = (BigInteger) query.getSingleResult();
         return secPerfil;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaConexionInicial.methodName() e: " + e);
         return null;
      }
   }

   @Override
   public void setearUsuario(EntityManager eManager, String rol, String pwd) {
      try {
         String texto = "SET ROLE " + rol + " IDENTIFIED BY " + pwd;
         em = eManager;
         em.getTransaction().begin();
         String sqlQuery = texto;
         Query query = em.createNativeQuery(sqlQuery);
         query.setParameter(1, rol);
         query.setParameter(2, pwd);
         query.executeUpdate();
         em.getTransaction().commit();
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaConexionInicial.setearUsuario() e: " + e);
      }
   }

   @Override
   public int cambiarClave(EntityManager eManager, String usuario, String nuevaClave) {
      try {
         em = eManager;
         em.getTransaction().begin();
         String sqlQuery = "ALTER USER " + usuario + " IDENTIFIED BY " + nuevaClave;
         Query query = em.createNativeQuery(sqlQuery);
         int resultado = query.executeUpdate();
         em.getTransaction().commit();
         return resultado;
      } catch (Exception e) {
         System.out.println("Persistencia.PersistenciaConexionInicial.cambiarClave() e: " + e);
//            Throwable t = getLastThrowable(e);
//            SQLException exxx = (SQLException) t;
         em.getTransaction().rollback();
//            return exxx.getErrorCode();
         try {
            int codigo = ExtraeCausaExcepcion.obtenerCodigoSQLException(e);
            return codigo;
         } catch (Exception ex) {
            System.out.println("Persistencia.PersistenciaConexionInicial.cambiarClave() ex: " + ex);
            return ex.hashCode();
         }
      }
   }
}
