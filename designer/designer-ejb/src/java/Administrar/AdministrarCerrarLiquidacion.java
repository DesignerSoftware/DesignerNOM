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

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'CerrarLiquidacion'.
 *
 * @author betelgeuse.
 */
@Stateful
public class AdministrarCerrarLiquidacion implements AdministrarCerrarLiquidacionInterface {

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

   private EntityManager em;
   //--------------------------------------------------------------------------
   //MÉTODOS
   //--------------------------------------------------------------------------

   @Override
   public void obtenerConexion(String idSesion) {
      em = administrarSesiones.obtenerConexionSesion(idSesion);
   }

   @Override
   public Integer contarEmpleadosParaLiquidar() {
      return persistenciaParametrosEstados.empleadosParaLiquidar(em, consultarAliasUsuarioBD());
   }

   @Override
   public boolean verificarPermisosLiquidar(String usuarioBD) {
      return persistenciaCandados.permisoLiquidar(em, usuarioBD);
   }

   @Override
   public String consultarAliasUsuarioBD() {
      return persistenciaActualUsuario.actualAliasBD(em);
   }

   @Override
   public ParametrosEstructuras consultarParametrosLiquidacion() {
      return persistenciaParametrosEstructuras.buscarParametro(em, consultarAliasUsuarioBD());
   }

   @Override
   public List<Parametros> consultarEmpleadosCerrarLiquidacion(String usuarioBD) {
      return persistenciaParametros.parametrosComprobantes(em, usuarioBD);
   }

   @Override
   public void cerrarLiquidacionAutomatico() {
      persistenciaCandados.cerrarLiquidacionAutomatico(em);
   }

   @Override
   public void cerrarLiquidacionNoAutomatico() {
      persistenciaCandados.cerrarLiquidacionNoAutomatico(em);
   }

   @Override
   public Integer consultarConteoProcesoSN(BigInteger secProceso) {
      return persistenciaSolucionesNodos.ContarProcesosSN(em, secProceso);
   }

   @Override
   public Integer contarLiquidacionesCerradas(BigInteger secProceso, String fechaDesde, String fechaHasta) {
      return persistenciaCortesProcesos.contarLiquidacionesCerradas(em, secProceso, fechaDesde, fechaHasta);
   }

   @Override
   public String abrirLiquidacion(Short codigoProceso, String fechaDesde, String fechaHasta) {
      return persistenciaCortesProcesos.eliminarComprobante(em, codigoProceso, fechaDesde, fechaHasta);
   }
}
