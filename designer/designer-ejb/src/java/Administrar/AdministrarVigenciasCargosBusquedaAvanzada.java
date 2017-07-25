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

   private EntityManager em;

   //--------------------------------------------------------------------------
   //MÉTODOS
   //--------------------------------------------------------------------------
   @Override
   public void obtenerConexion(String idSesion) {
      em = administrarSesiones.obtenerConexionSesion(idSesion);
   }

   @Override
   public List<Estructuras> lovEstructura() {
      try {
         List<Estructuras> lista = persistenciaEstructuras.buscarEstructuras(em);
         return lista;
      } catch (Exception e) {
         log.warn("Error lovEstructura Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<MotivosCambiosCargos> lovMotivosCambiosCargos() {
      try {
         List<MotivosCambiosCargos> lista = persistenciaMotivosCambiosCargos.buscarMotivosCambiosCargos(em);
         return lista;
      } catch (Exception e) {
         log.warn("Error lovMotivosCambiosCargos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<CentrosCostos> lovCentrosCostos() {
      try {
         List<CentrosCostos> lista = persistenciaCentrosCostos.buscarCentrosCostos(em);
         return lista;
      } catch (Exception e) {
         log.warn("Error lovCentrosCostos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Papeles> lovPapeles() {
      try {
         List<Papeles> lista = persistenciaPapeles.consultarPapeles(em);
         return lista;
      } catch (Exception e) {
         log.warn("Error lovPapeles Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Cargos> lovCargos() {
      try {
         List<Cargos> lista = persistenciaCargos.consultarCargos(em);
         return lista;
      } catch (Exception e) {
         log.warn("Error lovCargos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Empleados> lovEmpleados() {
      try {
         List<Empleados> lista = persistenciaEmpleado.buscarEmpleados(em);
         return lista;
      } catch (Exception e) {
         log.warn("Error lovEmpleados Admi : " + e.toString());
         return null;
      }
   }
}
