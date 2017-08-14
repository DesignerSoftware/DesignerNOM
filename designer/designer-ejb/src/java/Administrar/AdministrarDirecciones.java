/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.Ciudades;
import Entidades.Direcciones;
import Entidades.Empleados;
import Entidades.Personas;
import InterfaceAdministrar.AdministrarDireccionesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaDireccionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'Direcciones'.
 *
 * @author betelgeuse
 */
@Stateful
public class AdministrarDirecciones implements AdministrarDireccionesInterface {

   private static Logger log = Logger.getLogger(AdministrarDirecciones.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaPersonas'.
    */
   @EJB
   PersistenciaPersonasInterface persistenciaPersonas;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'PersistenciaCiudades'.
    */
   @EJB
   PersistenciaCiudadesInterface PersistenciaCiudades;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaDirecciones'.
    */
   @EJB
   PersistenciaDireccionesInterface persistenciaDirecciones;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;

   private EntityManagerFactory emf;
   private EntityManager em;

   private EntityManager getEm() {
      try {
         if (this.em != null) {
            if (this.em.isOpen()) {
               this.em.close();
            }
         }
         this.em = emf.createEntityManager();
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " getEm() ERROR : " + e);
      }
      return this.em;
   }

   //--------------------------------------------------------------------------
   //MÉTODOS
   //--------------------------------------------------------------------------
   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<Direcciones> consultarDireccionesPersona(BigInteger secPersona) {
      try {
         return persistenciaDirecciones.direccionesPersona(getEm(), secPersona);
      } catch (Exception e) {
         log.error("Error AdministrarTelefonos.telefonosPersona " + e);
         return null;
      }
   }

   @Override
   public Personas consultarPersona(BigInteger secPersona) {
      try {
         return persistenciaPersonas.buscarPersonaSecuencia(getEm(), secPersona);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Ciudades> consultarLOVCiudades() {
      try {
         return PersistenciaCiudades.consultarCiudades(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void modificarDirecciones(List<Direcciones> listaDirecciones) {
      try {
         Direcciones d;
         for (int i = 0; i < listaDirecciones.size(); i++) {
            log.warn("Modificando...");
            if (listaDirecciones.get(i).getCiudad().getSecuencia() == null) {
               listaDirecciones.get(i).setCiudad(null);
               d = listaDirecciones.get(i);
            } else {
               d = listaDirecciones.get(i);
            }
            persistenciaDirecciones.editar(getEm(), d);
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarDirecciones(List<Direcciones> listaDirecciones) {
      try {
         for (int i = 0; i < listaDirecciones.size(); i++) {
            log.warn("Borrando...");
            if (listaDirecciones.get(i).getHipoteca() == null) {
               listaDirecciones.get(i).setHipoteca("N");
            }
            persistenciaDirecciones.borrar(getEm(), listaDirecciones.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearDirecciones(List<Direcciones> listaDirecciones) {
      try {
         for (int i = 0; i < listaDirecciones.size(); i++) {
            log.warn("Borrando...");
            if (listaDirecciones.get(i).getHipoteca() == null) {
               listaDirecciones.get(i).setHipoteca("N");
            }
            persistenciaDirecciones.crear(getEm(), listaDirecciones.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public Empleados empleadoActual(BigInteger secuenciaP) {
      try {
         return persistenciaEmpleado.buscarEmpleado(getEm(), secuenciaP);
      } catch (Exception e) {
         log.warn("Error empleadoActual Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Direcciones> consultarDireccionesBanco(BigInteger secBanco) {
      try {
         return persistenciaDirecciones.direccionesBanco(getEm(), secBanco);
      } catch (Exception e) {
         log.error("Error AdministrarDirecciones.direccionesBanco " + e);
         return null;
      }
   }

}
