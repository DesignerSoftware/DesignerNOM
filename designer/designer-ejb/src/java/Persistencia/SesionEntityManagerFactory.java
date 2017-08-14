/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Persistencia;

import InterfacePersistencia.SesionEntityManagerFactoryInterface;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Remove;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.log4j.Logger;

/**
 * Clase de tipo singleton. <br>
 * Esta clase tiene la responsabilidad de crear y controlar las conexiones a la
 * base de datos que se están usando. Al ser ‘singleton’ solo permite crear una
 * instancia a la base de datos.
 *
 * @author Felipe Triviño
 */
@Stateless
public class SesionEntityManagerFactory implements SesionEntityManagerFactoryInterface, Serializable {

   private static Logger log = Logger.getLogger(SesionEntityManagerFactory.class);

   /**
    * Atributo EntityManagerFactory.
    */
   private EntityManagerFactory emf;

   @Override
   public boolean crearFactoryInicial(String baseDatos) {
      try {
         emf = Persistence.createEntityManagerFactory(baseDatos);
         log.warn(this.getClass().getSimpleName() + " CONEXION : ENTRADA, BD: " + baseDatos);
         return true;
      } catch (Exception e) {
         log.fatal(this.getClass().getName() + " ERROR crearFactoryInicial" + e.getMessage());
         return false;
      }
   }

   @Override
   public boolean crearFactoryUsuario(String usuario, String contraseña, String baseDatos) {
      try {
         Map<String, String> properties = new HashMap<String, String>();
         properties.put("javax.persistence.jdbc.user", usuario);
         properties.put("javax.persistence.jdbc.password", contraseña);
         log.warn("crearFactoryUsuario() Usuario: " + usuario);
         log.warn("crearFactoryUsuario() Contraseña: " + contraseña);
         log.warn("crearFactoryUsuario() Base de Datos: " + baseDatos);
         emf = Persistence.createEntityManagerFactory(baseDatos, properties);
         log.warn(this.getClass().getSimpleName() + " CONEXION : " + usuario + " , BD: " + baseDatos);
         return true;
      } catch (Exception e) {
         log.fatal("Error crearFactoryUsuario: " + e.getMessage());
         return false;
      }
   }

   @Override
   public EntityManagerFactory getEmf() {
      return emf;
   }

   @Override
   public void setEmf(EntityManagerFactory emf) {
      this.emf = emf;
   }

   @Remove
   @Override
   public void adios() {
      log.fatal("Cerrando xD");
   }
}
