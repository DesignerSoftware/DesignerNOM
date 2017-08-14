/**
 * Documentación a cargo de AndresPineda
 */
package Administrar;

import Entidades.Cargos;
import Entidades.CentrosCostos;
import Entidades.Empleados;
import Entidades.Estructuras;
import Entidades.MotivosCambiosCargos;
import Entidades.Papeles;
import InterfaceAdministrar.AdministrarVigenciasCargosBusquedaAvanzadaInterface;
import InterfacePersistencia.PersistenciaCargosInterface;
import InterfacePersistencia.PersistenciaCentrosCostosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaMotivosCambiosCargosInterface;
import InterfacePersistencia.PersistenciaPapelesInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'BusquedaAvanzada'.
 *
 * @author Andres Pineda.
 */
@Stateful
public class AdministrarVigenciasCargosBusquedaAvanzada implements AdministrarVigenciasCargosBusquedaAvanzadaInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciasCargosBusquedaAvanzada.class);
   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaEstructuras'.
    */
   @EJB
   PersistenciaEstructurasInterface persistenciaEstructuras;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaMotivosCambiosCargos'.
    */
   @EJB
   PersistenciaMotivosCambiosCargosInterface persistenciaMotivosCambiosCargos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaCentrosCostos'.
    */
   @EJB
   PersistenciaCentrosCostosInterface persistenciaCentrosCostos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaPapeles'.
    */
   @EJB
   PersistenciaPapelesInterface persistenciaPapeles;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaCargos'.
    */
   @EJB
   PersistenciaCargosInterface persistenciaCargos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaEmpleado'.
    */
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

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
   public List<Estructuras> lovEstructura() {
      try {
         return persistenciaEstructuras.buscarEstructuras(getEm());
      } catch (Exception e) {
         log.warn("Error lovEstructura Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<MotivosCambiosCargos> lovMotivosCambiosCargos() {
      try {
         return persistenciaMotivosCambiosCargos.buscarMotivosCambiosCargos(getEm());
      } catch (Exception e) {
         log.warn("Error lovMotivosCambiosCargos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<CentrosCostos> lovCentrosCostos() {
      try {
         return persistenciaCentrosCostos.buscarCentrosCostos(getEm());
      } catch (Exception e) {
         log.warn("Error lovCentrosCostos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Papeles> lovPapeles() {
      try {
         return persistenciaPapeles.consultarPapeles(getEm());
      } catch (Exception e) {
         log.warn("Error lovPapeles Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Cargos> lovCargos() {
      try {
         return persistenciaCargos.consultarCargos(getEm());
      } catch (Exception e) {
         log.warn("Error lovCargos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Empleados> lovEmpleados() {
      try {
         return persistenciaEmpleado.buscarEmpleados(getEm());
      } catch (Exception e) {
         log.warn("Error lovEmpleados Admi : " + e.toString());
         return null;
      }
   }
}
