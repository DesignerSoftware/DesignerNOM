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
import InterfacePersistencia.PersistenciaNovedadesSistemaInterface;
import InterfacePersistencia.PersistenciaPapelesInterface;
import InterfacePersistencia.PersistenciaPeriodicidadesInterface;
import InterfacePersistencia.PersistenciaRetiradosInterface;
import InterfacePersistencia.PersistenciaSoausentismosInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaTercerosSucursalesInterface;
import InterfacePersistencia.PersistenciaTiposAusentismosInterface;
import InterfacePersistencia.PersistenciaTiposEntidadesInterface;
import InterfacePersistencia.PersistenciaTiposSueldosInterface;
import InterfacePersistencia.PersistenciaUnidadesInterface;
import InterfacePersistencia.PersistenciaVacacionesInterface;
import InterfacePersistencia.PersistenciaVigenciasCargosInterface;
import InterfacePersistencia.PersistenciaVigenciasSueldosInterface;
import java.math.BigInteger;
import java.util.Date;

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
   @EJB
   PersistenciaVacacionesInterface persistenciaVacaciones;
   @EJB
   PersistenciaRetiradosInterface persistenciaRetirados;
   @EJB
   PersistenciaVigenciasSueldosInterface persistenciaVigenciasSueldos;
   @EJB
   PersistenciaNovedadesSistemaInterface persistenciaNovedadesSistema;
   @EJB
   PersistenciaVigenciasCargosInterface persistenciaVigenciasCargos;
   @EJB
   PersistenciaSoausentismosInterface persistenciaSoausentismos;

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

   public void adicionaEstructuraCM(BigInteger secEstructura, Date fechaCambio) {
      persistenciaEstructuras.adicionaEstructuraCambiosMasivos(em, secEstructura, fechaCambio);
   }

   public void undoAdicionaEstructuraCM(BigInteger secEstructura, Date fechaCambio) {
      persistenciaEstructuras.undoAdicionaEstructuraCambiosMasivos(em, secEstructura, fechaCambio);
   }

   public void adicionaVacacionCM(BigInteger ndias, Date fechaCambio, Date fechaPago) {
      persistenciaVacaciones.adicionaVacacionCambiosMasivos(em, ndias, fechaCambio, fechaPago);
   }

   public void undoAdicionaVacacionCM(BigInteger ndias, Date fechaCambio, Date fechaPago) {
      persistenciaVacaciones.undoAdicionaVacacionCambiosMasivos(em, ndias, fechaCambio, fechaPago);
   }

   public void adicionaRetiroCM(String indemniza, BigInteger secMotivoDefinitiva, BigInteger secMotivoRetiro, Date fechaCambio) {
      persistenciaRetirados.adicionaRetiroCambiosMasivos(em, indemniza, BigInteger.TEN, BigInteger.ZERO, fechaCambio);
   }

   public void undoAdicionaRetiroCM(String indemniza, BigInteger secMotivoDefinitiva, BigInteger secMotivoRetiro, Date fechaCambio) {
      persistenciaRetirados.undoAdicionaRetiroCambiosMasivos(em, indemniza, BigInteger.TEN, BigInteger.ZERO, fechaCambio);
   }

   public void adicionaAfiliacionCM(BigInteger secTipoEntidad, BigInteger secTerceroSuc, Date fechaCambio) {
      persistenciaTercerosSucursales.adicionaAfiliacionCambiosMasivos(em, secTipoEntidad, secTerceroSuc, fechaCambio);
   }

   public void undoAdicionaAfiliacionCM(BigInteger secTipoEntidad, BigInteger secTerceroSuc, Date fechaCambio) {
      persistenciaTercerosSucursales.undoAdicionaAfiliacionCambiosMasivos(em, secTipoEntidad, secTerceroSuc, fechaCambio);
   }

   public void adicionaLocalizacionCM(BigInteger secEstructura, Date fechaCambio) {
      persistenciaEstructuras.adicionaLocalizacionCambiosMasivos(em, secEstructura, fechaCambio);
   }

   public void undoAdicionaLocalizacionCM(BigInteger secEstructura, Date fechaCambio) {
      persistenciaEstructuras.undoAdicionaLocalizacionCambiosMasivos(em, secEstructura, fechaCambio);
   }

   public void adicionaSueldoCM(BigInteger secMotivoCS, BigInteger secTipoSueldo, BigInteger secUnidad, BigInteger valor, Date fechaCambio) {
      persistenciaVigenciasSueldos.adicionaSueldoCambiosMasivos(em, secMotivoCS, secTipoSueldo, secUnidad, valor, fechaCambio);
   }

   public void undoAdicionaSueldoCM(BigInteger secMotivoCS, BigInteger secTipoSueldo, BigInteger secUnidad, BigInteger valor, Date fechaCambio) {
      persistenciaVigenciasSueldos.undoAdicionaSueldoCambiosMasivos(em, secMotivoCS, secTipoSueldo, secUnidad, valor, fechaCambio);
   }

   public void adicionaNovedadCM(String tipo, BigInteger secConcepto, BigInteger secPeriodicidad,
           BigInteger secTercero, BigInteger secFormula, BigInteger valor,
           BigInteger saldo, Date fechaCambioInicial, Date fechaCambioFinal,
           BigInteger unidadParteEntera, BigInteger unidadParteFraccion) {
      persistenciaNovedadesSistema.adicionaNovedadCambiosMasivos(em, tipo, secConcepto, secPeriodicidad, secTercero, secFormula, valor, saldo, fechaCambioInicial, fechaCambioFinal, unidadParteEntera, unidadParteFraccion);
   }

   public void undoAdicionaNovedadCM(String tipo, BigInteger secConcepto, BigInteger secPeriodicidad,
           BigInteger secTercero, BigInteger secFormula, BigInteger valor,
           BigInteger saldo, Date fechaCambioInicial, Date fechaCambioFinal) {
      persistenciaNovedadesSistema.undoAdicionaNovedadCambiosMasivos(em, tipo, secConcepto, secPeriodicidad, secTercero, secFormula, valor, saldo, fechaCambioInicial, fechaCambioFinal);
   }

   public void adicionaReingresoCM(Date fechaIni, Date fechaFin) {
      persistenciaRetirados.adicionaReingresoCambiosMasivos(em, fechaIni, fechaFin);
   }

   public void adicionaEmplJefeCM(BigInteger secEmpleado, Date fechaCambio) {
      persistenciaVigenciasCargos.adicionaEmplJefeCambiosMasivos(em, secEmpleado, fechaCambio);
   }

   public void adicionaPapelCM(BigInteger secPapel, Date fechaCambio) {
      persistenciaPapeles.adicionaPapelCambiosMasivos(em, secPapel, fechaCambio);
   }

   public void adicionaAusentismoCM(BigInteger secTipo, BigInteger secClase, BigInteger secCausa, BigInteger dias,
           BigInteger horas, Date fechaIniAusen, Date fechaFinAusen, Date fechaExpedicion,
           Date fechaIpago, Date fechaPago, BigInteger porcent, BigInteger baseliq, String forma) {
      persistenciaSoausentismos.adicionaAusentismoCambiosMasivos(em, secTipo, secClase, secCausa, dias, horas, fechaIniAusen, fechaFinAusen, fechaExpedicion, fechaIpago, fechaPago, porcent, baseliq, forma);
   }

   public void undoAdicionaAusentismoCM(BigInteger secTipo, BigInteger secClase, BigInteger secCausa, BigInteger dias, Date fechaIniAusen, Date fechaFinAusen) {
      persistenciaSoausentismos.undoAdicionaAusentismoCambiosMasivos(em, secTipo, secClase, secCausa, dias, fechaIniAusen, fechaFinAusen);
   }

}
