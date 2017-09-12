/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.Parametros;
import Entidades.ParametrosEstructuras;
import InterfaceAdministrar.AdministrarCerrarLiquidacionInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaCandadosInterface;
import InterfacePersistencia.PersistenciaCortesProcesosInterface;
import InterfacePersistencia.PersistenciaParametrosEstadosInterface;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import InterfacePersistencia.PersistenciaParametrosInterface;
import InterfacePersistencia.PersistenciaSolucionesNodosInterface;
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
 * 'CerrarLiquidacion'.
 *
 * @author betelgeuse.
 */


@Stateful
public class AdministrarCerrarLiquidacion implements AdministrarCerrarLiquidacionInterface {

   private static Logger log = Logger.getLogger(AdministrarCerrarLiquidacion.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaParametrosEstados'.
    */
   @EJB
   PersistenciaParametrosEstadosInterface persistenciaParametrosEstados;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaCandados'.
    */
   @EJB
   PersistenciaCandadosInterface persistenciaCandados;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaActualUsuario'.
    */
   @EJB
   PersistenciaActualUsuarioInterface persistenciaActualUsuario;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaParametrosEstructuras'.
    */
   @EJB
   PersistenciaParametrosEstructurasInterface persistenciaParametrosEstructuras;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaParametros'.
    */
   @EJB
   PersistenciaParametrosInterface persistenciaParametros;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaSolucionesNodos'.
    */
   @EJB
   PersistenciaSolucionesNodosInterface persistenciaSolucionesNodos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaCortesProcesos'.
    */
   @EJB
   PersistenciaCortesProcesosInterface persistenciaCortesProcesos;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

   private EntityManagerFactory emf;
   private EntityManager em; private String idSesionBck;

   private EntityManager getEm() {
      try {
         if (this.emf != null) { if (this.em != null) {
            if (this.em.isOpen()) {
               this.em.close();
            }
         }
         } else {
            this.emf = administrarSesiones.obtenerConexionSesionEMF(idSesionBck);
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
   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public Integer contarEmpleadosParaLiquidar() {
      try {
          String user = consultarAliasUsuarioBD();
         return persistenciaParametrosEstados.empleadosParaLiquidar(getEm(), user);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".contarEmpleadosParaLiquidar() ERROR: " + e);
         return null;
      }
   }

   @Override
   public boolean verificarPermisosLiquidar(String usuarioBD) {
      try {
         return persistenciaCandados.permisoLiquidar(getEm(), usuarioBD);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".verificarPermisosLiquidar() ERROR: " + e);
         return false;
      }
   }

   @Override
   public String consultarAliasUsuarioBD() {
      try {
         return persistenciaActualUsuario.actualAliasBD(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarAliasUsuarioBD() ERROR: " + e);
         return null;
      }
   }

   @Override
   public ParametrosEstructuras consultarParametrosLiquidacion() {
      try {
          String user = consultarAliasUsuarioBD();
         return persistenciaParametrosEstructuras.buscarParametro(getEm(), user);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarParametrosLiquidacion() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Parametros> consultarEmpleadosCerrarLiquidacion(String usuarioBD) {
      try {
         return persistenciaParametros.parametrosComprobantes(getEm(), usuarioBD);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarEmpleadosCerrarLiquidacion() ERROR: " + e);
         return null;
      }
   }

   @Override
   public void cerrarLiquidacionAutomatico() {
      try {
         persistenciaCandados.cerrarLiquidacionAutomatico(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".cerrarLiquidacionAutomatico() ERROR: " + e);
      }
   }

   @Override
   public void cerrarLiquidacionNoAutomatico() {
      try {
         persistenciaCandados.cerrarLiquidacionNoAutomatico(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".cerrarLiquidacionNoAutomatico() ERROR: " + e);
      }
   }

   @Override
   public Integer consultarConteoProcesoSN(BigInteger secProceso) {
      try {
         return persistenciaSolucionesNodos.ContarProcesosSN(getEm(), secProceso);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarConteoProcesoSN() ERROR: " + e);
         return null;
      }
   }

   @Override
   public Integer contarLiquidacionesCerradas(BigInteger secProceso, String fechaDesde, String fechaHasta) {
      try {
         return persistenciaCortesProcesos.contarLiquidacionesCerradas(getEm(), secProceso, fechaDesde, fechaHasta);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".contarLiquidacionesCerradas() ERROR: " + e);
         return null;
      }
   }

   @Override
   public String abrirLiquidacion(Short codigoProceso, String fechaDesde, String fechaHasta) {
      try {
         return persistenciaCortesProcesos.eliminarComprobante(getEm(), codigoProceso, fechaDesde, fechaHasta);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".abrirLiquidacion() ERROR: " + e);
         return null;
      }
   }
}
