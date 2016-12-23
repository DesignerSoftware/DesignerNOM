package Administrar;

import Entidades.CambiosMasivos;
import Entidades.Causasausentismos;
import Entidades.Parametros;
import Entidades.ParametrosCambiosMasivos;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaParametrosInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import InterfaceAdministrar.AdministrarCambiosMasivosInterface;
import Entidades.CentrosCostos;
import Entidades.Clasesausentismos;
import Entidades.Conceptos;
import Entidades.Empleados;
import Entidades.Estructuras;
import Entidades.Formulas;
import Entidades.MotivosCambiosSueldos;
import Entidades.MotivosDefinitivas;
import Entidades.MotivosRetiros;
import Entidades.Papeles;
import Entidades.Periodicidades;
import Entidades.Terceros;
import Entidades.TercerosSucursales;
import Entidades.TiposEntidades;
import Entidades.TiposSueldos;
import Entidades.Tiposausentismos;
import Entidades.Unidades;
import InterfacePersistencia.PersistenciaCausasAusentismosInterface;
import InterfacePersistencia.PersistenciaCentrosCostosInterface;
import InterfacePersistencia.PersistenciaClasesAusentismosInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaFormulasInterface;
import InterfacePersistencia.PersistenciaIBCSInterface;
import InterfacePersistencia.PersistenciaMotivosCambiosSueldosInterface;
import InterfacePersistencia.PersistenciaMotivosDefinitivasInterface;
import InterfacePersistencia.PersistenciaMotivosRetirosInterface;
import InterfacePersistencia.PersistenciaPapelesInterface;
import InterfacePersistencia.PersistenciaPeriodicidadesInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaTercerosSucursalesInterface;
import InterfacePersistencia.PersistenciaTiposAusentismosInterface;
import InterfacePersistencia.PersistenciaTiposEntidadesInterface;
import InterfacePersistencia.PersistenciaTiposSueldosInterface;
import InterfacePersistencia.PersistenciaUnidadesInterface;

@Stateful
public class AdministrarCambiosMasivos implements AdministrarCambiosMasivosInterface {

   private EntityManager em;
   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaParametrosInterface persistenciaParametros;
   @EJB
   PersistenciaActualUsuarioInterface persistenciaActualUsuario;
   @EJB
   PersistenciaEstructurasInterface persistenciaEstructuras;
   @EJB
   PersistenciaMotivosRetirosInterface persistenciaMotivosRetiros;
   @EJB
   PersistenciaMotivosDefinitivasInterface persistenciaMotivosDefinitivas;
   @EJB
   PersistenciaTiposEntidadesInterface persistenciaTiposEntidades;
   @EJB
   PersistenciaTercerosSucursalesInterface persistenciaTercerosSucursales;
   @EJB
   PersistenciaCentrosCostosInterface persistenciaCentrosCostos;
   @EJB
   PersistenciaPeriodicidadesInterface persistenciaPeriodicidades;
   @EJB
   PersistenciaConceptosInterface persistenciaConceptos;
   @EJB
   PersistenciaFormulasInterface persistenciaFormulas;
   @EJB
   PersistenciaTercerosInterface persistenciaTerceros;
   @EJB
   PersistenciaMotivosCambiosSueldosInterface persistenciaMotivosCambiosSueldos;
   @EJB
   PersistenciaTiposSueldosInterface persistenciaTiposSueldos;
   @EJB
   PersistenciaTiposAusentismosInterface persistenciaTiposAusentismos;
   @EJB
   PersistenciaUnidadesInterface persistenciaUnidades;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleados;
   @EJB
   PersistenciaIBCSInterface persistenciaIBCS;
   @EJB
   PersistenciaPapelesInterface persistenciaPapeles;
   @EJB
   PersistenciaCausasAusentismosInterface persistenciaCausasAusentismos;
   @EJB
   PersistenciaClasesAusentismosInterface persistenciaClasesAusentismos;

//   private String usuarioBD = " ";
   /**
    *
    * @param idSesion
    */
   @Override
   public void obtenerConexion(String idSesion) {
      System.out.println("Administrar.AdministrarCambiosMasivos.obtenerConexion()");
      em = administrarSesiones.obtenerConexionSesion(idSesion);
   }
   /**
    *
    * @return
    */
   @Override
   public List<Parametros> consultarEmpleadosParametros() {
      System.out.println("Administrar.AdministrarCambiosMasivos.consultarEmpleadosParametros()");
      try {
         String usuarioBD = persistenciaActualUsuario.actualAliasBD(em);
         return persistenciaParametros.empleadosParametros(em, usuarioBD);
      } catch (Exception e) {
         System.out.println("ERROR Administrar.AdministrarCambiosMasivos.consultarEmpleadosParametros()");
         System.out.println("ERROR : " + e);
         return null;
      }
   }
   /**
    *
    * @return
    */
   @Override
   public List<CambiosMasivos> consultarUltimosCambiosMasivos() {
      System.out.println("Administrar.AdministrarCambiosMasivos.consultarUltimosCambiosMasivos()");
      try {
         return persistenciaParametros.listcambiosmasivos(em);
      } catch (Exception e) {
         System.out.println("ERROR Administrar.AdministrarCambiosMasivos.consultarUltimosCambiosMasivos()");
         System.out.println("ERROR : " + e);
         return null;
      }
   }
   /**
    *
    * @return
    */
   @Override
   public ParametrosCambiosMasivos consultarParametrosCambiosMasivos() {
      System.out.println("Administrar.AdministrarCambiosMasivos.consultarParametrosCambiosMasivos()");
      try {
         String usuarioBD = persistenciaActualUsuario.actualAliasBD(em);
         return persistenciaParametros.parametrosCambiosMasivos(em, usuarioBD);
      } catch (Exception e) {
         System.out.println("ERROR Administrar.AdministrarCambiosMasivos.consultarParametrosCambiosMasivos()");
         System.out.println("ERROR : " + e);
         return null;
      }
   }

   public boolean actualizarParametroCM(ParametrosCambiosMasivos parametro) {
      return persistenciaParametros.actualizarParametroCambioMasivo(em, parametro);
   }

   @Override
   public List<Estructuras> consultarLovCargos_Estructuras() {
      return persistenciaEstructuras.buscarEstructuras(em);
   }

   @Override
   public List<MotivosDefinitivas> consultarLovMotivosDefinitivas() {
      return persistenciaMotivosDefinitivas.buscarMotivosDefinitivas(em);
   }

   @Override
   public List<MotivosRetiros> consultarLovMotivosRetiros() {
      return persistenciaMotivosRetiros.consultarMotivosRetiros(em);
   }

   @Override
   public List<TiposEntidades> consultarLovTiposEntidades() {
      return persistenciaTiposEntidades.buscarTiposEntidades(em);
   }

   @Override
   public List<TercerosSucursales> consultarLovTercerosSucursales() {
      return persistenciaTercerosSucursales.buscarTercerosSucursales(em);
   }

   @Override
   public List<CentrosCostos> consultarLovCentrosCostos() {
      return persistenciaCentrosCostos.buscarCentrosCostosCM(em);
   }

   @Override
   public List<Periodicidades> consultarLovPeriodicidades() {
      return persistenciaPeriodicidades.consultarPeriodicidades(em);
   }

   @Override
   public List<Conceptos> consultarLovConceptos() {
      return persistenciaConceptos.buscarConceptos(em);
   }

   @Override
   public List<Formulas> consultarLovFormulas() {
      return persistenciaFormulas.buscarFormulas(em);
   }

   @Override
   public List<Terceros> consultarLovTerceros() {
      return persistenciaTerceros.buscarTerceros(em);
   }

   @Override
   public List<MotivosCambiosSueldos> consultarLovMotivosCambiosSueldos() {
      return persistenciaMotivosCambiosSueldos.buscarMotivosCambiosSueldos(em);
   }

   @Override
   public List<TiposSueldos> consultarLovTiposSueldos() {
      return persistenciaTiposSueldos.buscarTiposSueldos(em);
   }

   @Override
   public List<Tiposausentismos> consultarLovTiposausentismos() {
      return persistenciaTiposAusentismos.consultarTiposAusentismos(em);
   }

   @Override
   public List<Unidades> consultarLovUnidades() {
      return persistenciaUnidades.consultarUnidades(em);
   }

   @Override
   public List<Empleados> consultarLovEmpleados() {
      return persistenciaEmpleados.buscarEmpleados(em);
   }

   @Override
   public List<Papeles> consultarLovPapeles() {
      return persistenciaPapeles.consultarPapeles(em);
   }

   @Override
   public List<Causasausentismos> consultarLovCausasausentismos() {
      return persistenciaCausasAusentismos.buscarCausasAusentismos(em);
   }

   @Override
   public List<Clasesausentismos> consultarLovClasesausentismos() {
      return persistenciaClasesAusentismos.buscarClasesAusentismos(em);
   }
}
