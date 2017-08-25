package Administrar;

import ClasesAyuda.ParametrosQueryBusquedaAvanzada;
import Entidades.*;
import InterfaceAdministrar.AdministrarBusquedaAvanzadaInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

@Stateful
@Local
public class AdministrarBusquedaAvanzada implements AdministrarBusquedaAvanzadaInterface {

   private static Logger log = Logger.getLogger(AdministrarBusquedaAvanzada.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   //NOMINA
   @EJB
   PersistenciaCargosInterface persistenciaCargos;
   @EJB
   PersistenciaEstructurasInterface persistenciaEstructuras;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleados;
   @EJB
   PersistenciaMotivosCambiosCargosInterface persistenciaMotivosCambiosCargos;
   @EJB
   PersistenciaPapelesInterface persistenciaPapeles;
   @EJB
   PersistenciaMotivosLocalizacionesInterface persistenciaMotivosLocalizaciones;
   @EJB
   PersistenciaTiposSueldosInterface persistenciaTiposSueldos;
   @EJB
   PersistenciaMotivosCambiosSueldosInterface persistenciaMotivosCambiosSueldos;
   @EJB
   PersistenciaTiposContratosInterface persistenciaTiposContratos;
   @EJB
   PersistenciaMotivosContratosInterface persistenciaMotivosContratos;
   @EJB
   PersistenciaTiposTrabajadoresInterface persistenciaTiposTrabajadores;
   @EJB
   PersistenciaReformasLaboralesInterface persistenciaReformasLaborales;
   @EJB
   PersistenciaContratosInterface persistenciaContratos;
   @EJB
   PersistenciaUbicacionesGeograficasInterface persistenciaUbicacionesGeograficas;
   @EJB
   PersistenciaTercerosSucursalesInterface persistenciaTercerosSucursales;
   @EJB
   PersistenciaTiposEntidadesInterface persistenciaTiposEntidades;
   @EJB
   PersistenciaEstadosAfiliacionesInterface persistenciaEstadosAfiliaciones;
   @EJB
   PersistenciaPeriodicidadesInterface persistenciaPeriodicidades;
   @EJB
   PersistenciaSucursalesInterface persistenciaSucursales;
   @EJB
   PersistenciaMotivosMvrsInterface persistenciaMotivosMvrs;
   @EJB
   PersistenciaNormasLaboralesInterface persistenciaNormasLaborales;
   @EJB
   PersistenciaJornadasLaboralesInterface persistenciaJornadasLaborales;
   @EJB
   PersistenciaMotivosRetirosInterface persistenciaMotivosRetiros;
   //PERSONAL
   @EJB
   PersistenciaCiudadesInterface persistenciaCiudades;
   @EJB
   PersistenciaEstadosCivilesInterface persistenciaEstadosCiviles;
   @EJB
   PersistenciaIdiomasInterface persistenciaIdiomas;
   @EJB
   PersistenciaTiposIndicadoresInterface persistenciaTiposIndicadores;
   @EJB
   PersistenciaIndicadoresInterface persistenciaIndicadores;
   @EJB
   PersistenciaProfesionesInterface persistenciaProfesiones;
   @EJB
   PersistenciaInstitucionesInterface persistenciaInstituciones;
   @EJB
   PersistenciaCursosInterface persistenciaCursos;
   @EJB
   PersistenciaSectoresEconomicosInterface persistenciaSectoresEconomicos;
   @EJB
   PersistenciaProyectosInterface persistenciaProyectos;
   @EJB
   PersistenciaPryRolesInterface persistenciaPryRoles;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   @EJB
   PersistenciaEmpresasInterface persistenciaEmpresas;
   @EJB
   PersistenciaColumnasEscenariosInterface persistenciaColumnasEscenarios;
   //Para Añadir a parametros
   @EJB
   PersistenciaActualUsuarioInterface persistenciaActualUsuario;
   @EJB
   PersistenciaParametrosInterface persistenciaParametros;
   @EJB
   PersistenciaParametrosEstructurasInterface persistenciaParametrosEstructuras;
   @EJB
   PersistenciaUsuariosInterface persistenciaUsuarios;

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
   private boolean usoWhere = false;

   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   //NOMINA
   @Override
   public List<Cargos> lovCargos() {
      try {
         return persistenciaCargos.consultarCargos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Estructuras> lovEstructuras() {
      try {
         return persistenciaEstructuras.buscarEstructuras(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Empleados> lovJefe() {
      try {
         return persistenciaEmpleados.buscarEmpleadosActivos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<MotivosCambiosCargos> lovMotivosCargos() {
      try {
         return persistenciaMotivosCambiosCargos.buscarMotivosCambiosCargos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Papeles> lovPapeles() {
      try {
         return persistenciaPapeles.consultarPapeles(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<MotivosLocalizaciones> lovMotivosLocalizaciones() {
      try {
         return persistenciaMotivosLocalizaciones.buscarMotivosLocalizaciones(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<TiposSueldos> lovTiposSueldos() {
      try {
         return persistenciaTiposSueldos.buscarTiposSueldos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<MotivosCambiosSueldos> lovMotivosSueldos() {
      try {
         return persistenciaMotivosCambiosSueldos.buscarMotivosCambiosSueldos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<TiposContratos> lovTiposContratos() {
      try {
         return persistenciaTiposContratos.tiposContratos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<MotivosContratos> lovMotivosContratos() {
      try {
         return persistenciaMotivosContratos.buscarMotivosContratos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<TiposTrabajadores> lovTiposTrabajadores() {
      try {
         return persistenciaTiposTrabajadores.buscarTiposTrabajadores(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<ReformasLaborales> lovReformasLaborales() {
      try {
         return persistenciaReformasLaborales.buscarReformasLaborales(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Contratos> lovContratos() {
      try {
         return persistenciaContratos.buscarContratos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<UbicacionesGeograficas> lovUbicaciones() {
      try {
         return persistenciaUbicacionesGeograficas.consultarUbicacionesGeograficas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<TercerosSucursales> lovTercerosSucursales() {
      try {
         return persistenciaTercerosSucursales.buscarTercerosSucursales(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<TiposEntidades> lovTiposEntidades() {
      try {
         return persistenciaTiposEntidades.buscarTiposEntidades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<EstadosAfiliaciones> lovEstadosAfiliaciones() {
      try {
         return persistenciaEstadosAfiliaciones.buscarEstadosAfiliaciones(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Periodicidades> lovFormasPagos() {
      try {
         return persistenciaPeriodicidades.consultarPeriodicidades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Sucursales> lovSucursales() {
      try {
         return persistenciaSucursales.consultarSucursales(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   //PERSONAL
   @Override
   public List<Motivosmvrs> lovMotivosMvrs() {
      try {
         return persistenciaMotivosMvrs.buscarMotivosMvrs(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<NormasLaborales> lovNormasLaborales() {
      try {
         return persistenciaNormasLaborales.consultarNormasLaborales(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<JornadasLaborales> lovJornadasLaborales() {
      try {
         return persistenciaJornadasLaborales.buscarJornadasLaborales(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<MotivosRetiros> lovMotivosRetiros() {
      try {
         return persistenciaMotivosRetiros.consultarMotivosRetiros(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Ciudades> lovCiudades() {
      try {
         return persistenciaCiudades.consultarCiudades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<EstadosCiviles> lovEstadosCiviles() {
      try {
         return persistenciaEstadosCiviles.consultarEstadosCiviles(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Idiomas> lovIdiomas() {
      try {
         return persistenciaIdiomas.buscarIdiomas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<TiposIndicadores> lovTiposIndicadores() {
      try {
         return persistenciaTiposIndicadores.buscarTiposIndicadores(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Indicadores> lovIndicadores() {
      try {
         return persistenciaIndicadores.buscarIndicadores(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Profesiones> lovProfesiones() {
      try {
         return persistenciaProfesiones.profesiones(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Instituciones> lovInstitucioneses() {
      try {
         return persistenciaInstituciones.instituciones(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Cursos> lovCursos() {
      try {
         return persistenciaCursos.cursos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<SectoresEconomicos> lovSectoresEconomicos() {
      try {
         return persistenciaSectoresEconomicos.buscarSectoresEconomicos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Proyectos> lovProyectos() {
      try {
         return persistenciaProyectos.proyectos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<PryRoles> lovRoles() {
      try {
         return persistenciaPryRoles.pryroles(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   //QUERY
   @Override
   public List<ColumnasEscenarios> buscarColumnasEscenarios() {
      try {
         return persistenciaColumnasEscenarios.buscarColumnasEscenarios(getEm());
      } catch (Exception e) {
         log.warn("Error buscarColumnasEscenarios Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Empleados> ejecutarQueryBusquedaAvanzadaPorModulos(String query) {
      try {
         return persistenciaEmpleado.buscarEmpleadosBusquedaAvanzada(getEm(), query);
      } catch (Exception e) {
         log.warn("Error ejecutarQueryBusquedaAvanzadaPorModulos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<BigInteger> ejecutarQueryBusquedaAvanzadaPorModulosCodigo(String query) {
      try {
         return persistenciaEmpleado.buscarEmpleadosBusquedaAvanzadaCodigo(getEm(), query);
      } catch (Exception e) {
         log.warn("Error ejecutarQueryBusquedaAvanzadaPorModulos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public String armarQueryModulosBusquedaAvanzada(List<ParametrosQueryBusquedaAvanzada> listaParametro) {
      try {
         usoWhere = false;
         String query = "";
         if (listaParametro.size() > 0) {
            int i = 0;
            List<String> listaPorModulo = listaParametrosPorModulos(listaParametro);
            while (i < listaParametro.size()) {
               String modulo = listaParametro.get(i).getModulo();
               List<ParametrosQueryBusquedaAvanzada> listaAuxiliar = new ArrayList<ParametrosQueryBusquedaAvanzada>();
               int indiceInicial = i;
               int indiceFinal = listaPorModulo.lastIndexOf(modulo);
               int indiceFinalAux = indiceFinal + 1;
               for (int j = indiceInicial; j < indiceFinalAux; j++) {
                  listaAuxiliar.add(listaParametro.get(j));
               }
               query = query + crearQueryPorModulo(listaAuxiliar);
               int aumentoi = indiceFinal - indiceInicial;
               if (aumentoi == 0) {
                  i++;
               } else {
                  i = i + aumentoi + 1;
               }
            }
         }
         return query;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   private List<String> listaParametrosPorModulos(List<ParametrosQueryBusquedaAvanzada> listaAuxiliar) {
      List<String> retorno = new ArrayList<String>();
      try {
         for (int i = 0; i < listaAuxiliar.size(); i++) {
            retorno.add(listaAuxiliar.get(i).getModulo());
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
      return retorno;
   }

   private String crearQueryPorModulo(List<ParametrosQueryBusquedaAvanzada> listaAuxiliar) {
      try {
         String queryAux = "";
         String modulo = listaAuxiliar.get(0).getModulo();
         if (modulo.equalsIgnoreCase("CARGO")) {
            if (usoWhere == false) {
               queryAux = " WHERE ";
               usoWhere = true;
            } else {
               queryAux = " AND ";
            }
            for (int i = 0; i < listaAuxiliar.size(); i++) {
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("BCARGO")) {
                  if (listaAuxiliar.get(i).getValorParametro().equalsIgnoreCase("A")) {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM VWACTUALESCARGOS V ";
                     queryAux = queryAux + "WHERE V.EMPLEADO = EM.SECUENCIA ";
                  } else {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM VIGENCIASCARGOS V ";
                     queryAux = queryAux + "WHERE V.EMPLEADO = EM.SECUENCIA ";
                  }
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("CARGODESDE")) {
                  queryAux = queryAux + " AND  V.FECHAVIGENCIA >= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("CARGOHASTA")) {
                  queryAux = queryAux + " AND  V.FECHAVIGENCIA <= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }

               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("CARGO")) {
                  queryAux = queryAux + " AND  V.CARGO = " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("ESTRUCTURA")) {
                  queryAux = queryAux + " AND  V.ESTRUCTURA = " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("CENTROCOSTO")) {
                  queryAux = queryAux + " AND  EXISTS (SELECT 1 FROM ESTRUCTURAS ES WHERE ES.SECUENCIA=V.ESTRUCTURA";
                  queryAux = queryAux + " AND ES.CENTROCOSTO=" + listaAuxiliar.get(i).getValorParametro() + ")";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("EMPLEADOJEFE")) {
                  queryAux = queryAux + " AND  V.EMPLEADOJEFE = " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("MOTIVOCAMBIOCARGO")) {
                  queryAux = queryAux + " AND  V.MOTIVOCAMBIOCARGO = " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("PAPEL")) {
                  queryAux = queryAux + " AND  V.PAPEL = " + listaAuxiliar.get(i).getValorParametro();
               }
            }
            queryAux = queryAux + ")";
         }
         if (modulo.equalsIgnoreCase("CENTROCOSTO")) {
            if (usoWhere == false) {
               queryAux = " WHERE ";
               usoWhere = true;
            } else {
               queryAux = " AND ";
            }
            for (int i = 0; i < listaAuxiliar.size(); i++) {
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("BCENTROCOSTO")) {
                  if (listaAuxiliar.get(i).getValorParametro().equalsIgnoreCase("A")) {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM VWACTUALESLOCALIZACIONES V ";
                     queryAux = queryAux + "WHERE V.EMPLEADO = EM.SECUENCIA ";
                  } else {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM VIGENCIASLOCALIZACIONES V ";
                     queryAux = queryAux + "WHERE V.EMPLEADO = EM.SECUENCIA ";
                  }
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("CENTROCOSTODESDE")) {
                  queryAux = queryAux + " AND  V.FECHAVIGENCIA >= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("CENTROCOSTOHASTA")) {
                  queryAux = queryAux + " AND  V.FECHAVIGENCIA <= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }

               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("LOCALIZACION")) {
                  queryAux = queryAux + " AND  V.LOCALIZACION = " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("MOTIVOLOCALIZACION")) {
                  queryAux = queryAux + " AND  V.MOTIVO = " + listaAuxiliar.get(i).getValorParametro();
               }
            }
            queryAux = queryAux + ")";
         }
         if (modulo.equalsIgnoreCase("SUELDO")) {
            if (usoWhere == false) {
               queryAux = " WHERE ";
               usoWhere = true;
            } else {
               queryAux = " AND ";
            }
            for (int i = 0; i < listaAuxiliar.size(); i++) {
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("BSUELDO")) {
                  if (listaAuxiliar.get(i).getValorParametro().equalsIgnoreCase("A")) {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM VWACTUALESSUELDOS V ";
                     queryAux = queryAux + "WHERE V.EMPLEADO = EM.SECUENCIA ";
                  } else {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM VIGENCIASSUELDOS V ";
                     queryAux = queryAux + "WHERE V.EMPLEADO = EM.SECUENCIA ";
                  }
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("SUELDODESDE")) {
                  queryAux = queryAux + " AND  V.FECHAVIGENCIA >= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("SUELDOHASTA")) {
                  queryAux = queryAux + " AND  V.FECHAVIGENCIA <= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }

               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("TIPOSUELDO")) {
                  queryAux = queryAux + " AND  V.TIPOSUELDO = " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("MOTIVOCAMBIOSUELDO")) {
                  queryAux = queryAux + " AND  V.MOTIVOCAMBIOSUELDO = " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("SUELDOMINIMO")) {
                  queryAux = queryAux + " AND  V.VALOR >= " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("SUELDOMAXIMO")) {
                  queryAux = queryAux + " AND  V.VALOR <= " + listaAuxiliar.get(i).getValorParametro();
               }
            }
            queryAux = queryAux + ")";
         }
         if (modulo.equalsIgnoreCase("FECHACONTRATO")) {
            if (usoWhere == false) {
               queryAux = " WHERE ";
               usoWhere = true;
            } else {
               queryAux = " AND ";
            }
            for (int i = 0; i < listaAuxiliar.size(); i++) {
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("BFECHACONTRATO")) {
                  if (listaAuxiliar.get(i).getValorParametro().equalsIgnoreCase("A")) {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM VWactualesTiposcontratos V ";
                     queryAux = queryAux + "WHERE V.EMPLEADO = EM.SECUENCIA ";
                  } else {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM VIGENCIASTIPOSCONTRATOS V ";
                     queryAux = queryAux + "WHERE V.EMPLEADO = EM.SECUENCIA ";
                  }
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("FECHACONTRATODESDE")) {
                  queryAux = queryAux + " AND  V.FECHAVIGENCIA >= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("FECHACONTRATOHASTA")) {
                  queryAux = queryAux + " AND  V.FECHAVIGENCIA <= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }

               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("TIPOCONTRATO")) {
                  queryAux = queryAux + " AND  V.TIPOCONTRATO = " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("MOTIVOCONTRATO")) {
                  queryAux = queryAux + " AND  V.MOTIVOCONTRATO = " + listaAuxiliar.get(i).getValorParametro();
               }
            }
            queryAux = queryAux + ")";
         }
         if (modulo.equalsIgnoreCase("TIPOTRABAJADOR")) {
            if (usoWhere == false) {
               queryAux = " WHERE ";
               usoWhere = true;
            } else {
               queryAux = " AND ";
            }
            for (int i = 0; i < listaAuxiliar.size(); i++) {
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("BTIPOTRABAJADOR")) {
                  if (listaAuxiliar.get(i).getValorParametro().equalsIgnoreCase("A")) {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM VWactualesTiposTrabajadores V ";
                     queryAux = queryAux + "WHERE V.EMPLEADO = EM.SECUENCIA ";
                  } else {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM VIGENCIASTIPOSTRABAJADORES V ";
                     queryAux = queryAux + "WHERE V.EMPLEADO = EM.SECUENCIA ";
                  }
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("TIPOTRABAJADORDESDE")) {
                  queryAux = queryAux + " AND  V.FECHAVIGENCIA >= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("TIPOTRABAJADORHASTA")) {
                  queryAux = queryAux + " AND  V.FECHAVIGENCIA <= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("TIPOTRABAJADOR")) {
                  queryAux = queryAux + " AND  V.TIPOTRABAJADOR = " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("TIPOTRABAJADORACTIVO")) {
                  queryAux = queryAux + " AND  V.TIPOTRABAJADOR IN (SELECT tt.secuencia FROM TiposTrabajadores tt WHERE tt.tipo='ACTIVO')";
               }
            }
            queryAux = queryAux + ")";
         }
         if (modulo.equalsIgnoreCase("TIPOSALARIO")) {
            if (usoWhere == false) {
               queryAux = " WHERE ";
               usoWhere = true;
            } else {
               queryAux = " AND ";
            }
            for (int i = 0; i < listaAuxiliar.size(); i++) {
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("BTIPOSALARIO")) {
                  if (listaAuxiliar.get(i).getValorParametro().equalsIgnoreCase("A")) {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM VWactualesReformasLaborales V ";
                     queryAux = queryAux + "WHERE V.EMPLEADO = EM.SECUENCIA ";
                  } else {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM VIGENCIASREFORMASLABORALES V ";
                     queryAux = queryAux + "WHERE V.EMPLEADO = EM.SECUENCIA ";
                  }
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("TIPOSALARIODESDE")) {
                  queryAux = queryAux + " AND  V.FECHAVIGENCIA >= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("TIPOSALARIOHASTA")) {
                  queryAux = queryAux + " AND  V.FECHAVIGENCIA <= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }

               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("REFORMA")) {
                  queryAux = queryAux + " AND  V.REFORMALABORAL = " + listaAuxiliar.get(i).getValorParametro();
               }
            }
            queryAux = queryAux + ")";
         }
         if (modulo.equalsIgnoreCase("NORMALABORAL")) {
            if (usoWhere == false) {
               queryAux = " WHERE ";
               usoWhere = true;
            } else {
               queryAux = " AND ";
            }
            for (int i = 0; i < listaAuxiliar.size(); i++) {
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("BNORMALABORAL")) {
                  if (listaAuxiliar.get(i).getValorParametro().equalsIgnoreCase("A")) {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM VWactualesNormasEmpleados V ";
                     queryAux = queryAux + "WHERE V.EMPLEADO = EM.SECUENCIA ";
                  } else {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM VIGENCIASNORMASEMPLEADOS V ";
                     queryAux = queryAux + "WHERE V.EMPLEADO = EM.SECUENCIA ";
                  }
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("NORMALABORALDESDE")) {
                  queryAux = queryAux + " AND  V.FECHAVIGENCIA >= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("NORMALABORALHASTA")) {
                  queryAux = queryAux + " AND  V.FECHAVIGENCIA <= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }

               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("NORMA")) {
                  queryAux = queryAux + " AND  V.NORMALABORAL = " + listaAuxiliar.get(i).getValorParametro();
               }
            }
            queryAux = queryAux + ")";
         }
         if (modulo.equalsIgnoreCase("LEGISLACIONLABORAL")) {
            if (usoWhere == false) {
               queryAux = " WHERE ";
               usoWhere = true;
            } else {
               queryAux = " AND ";
            }
            for (int i = 0; i < listaAuxiliar.size(); i++) {
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("BLEGISLACIONLABORAL")) {
                  if (listaAuxiliar.get(i).getValorParametro().equalsIgnoreCase("A")) {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM VWactualesContratos V ";
                     queryAux = queryAux + "WHERE V.EMPLEADO = EM.SECUENCIA ";
                  } else {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM VIGENCIASCONTRATOS V ";
                     queryAux = queryAux + "WHERE V.EMPLEADO = EM.SECUENCIA ";
                  }
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("LEGISLACIONLABORALDESDE")) {
                  queryAux = queryAux + " AND  V.FECHAINICIAL >= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("LEGISLACIONLABORALHASTA")) {
                  queryAux = queryAux + " AND  V.FECHAINICIAL <= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("LEGISLACIONLABORALDESDEF")) {
                  queryAux = queryAux + " AND  V.FECHAFINAL >= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("LEGISLACIONLABORALHASTAF")) {
                  queryAux = queryAux + " AND  V.FECHAFINAL <= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("CONTRATO")) {
                  queryAux = queryAux + " AND  V.CONTRATO = " + listaAuxiliar.get(i).getValorParametro();
               }
            }
            queryAux = queryAux + ")";
         }
         if (modulo.equalsIgnoreCase("UBICACION")) {
            if (usoWhere == false) {
               queryAux = " WHERE ";
               usoWhere = true;
            } else {
               queryAux = " AND ";
            }
            for (int i = 0; i < listaAuxiliar.size(); i++) {
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("BUBICACION")) {
                  if (listaAuxiliar.get(i).getValorParametro().equalsIgnoreCase("A")) {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM VWACTUALESUBICACIONES V ";
                     queryAux = queryAux + "WHERE V.EMPLEADO = EM.SECUENCIA ";
                  } else {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM VIGENCIASUBICACIONES V ";
                     queryAux = queryAux + "WHERE V.EMPLEADO = EM.SECUENCIA ";
                  }
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("UBICACIONDESDE")) {
                  queryAux = queryAux + " AND  V.FECHAVIGENCIA >= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("UBICACIONHASTA")) {
                  queryAux = queryAux + " AND  V.FECHAVIGENCIA <= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("UBICACION")) {
                  queryAux = queryAux + " AND  V.UBICACION = " + listaAuxiliar.get(i).getValorParametro();
               }
            }
            queryAux = queryAux + ")";
         }
         if (modulo.equalsIgnoreCase("AFILIACIONES")) {
            if (usoWhere == false) {
               queryAux = " WHERE ";
               usoWhere = true;
            } else {
               queryAux = " AND ";
            }
            for (int i = 0; i < listaAuxiliar.size(); i++) {
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("BAFILIACIONES")) {
                  String v_vcRETENCIONYSEGSOCXPERSONA = persistenciaEmpresas.consultarPrimeraEmpresa(getEm());
                  if (v_vcRETENCIONYSEGSOCXPERSONA.equalsIgnoreCase("N")) {
                     if (listaAuxiliar.get(i).getValorParametro().equalsIgnoreCase("A")) {
                        queryAux = queryAux + "EXISTS (SELECT 1 FROM VWACTUALESAFILIACIONES V ";
                        queryAux = queryAux + "WHERE V.EMPLEADO = EM.SECUENCIA ";
                     } else {
                        queryAux = queryAux + "EXISTS (SELECT 1 FROM VIGENCIASAFILIACIONES V ";
                        queryAux = queryAux + "WHERE V.EMPLEADO = EM.SECUENCIA ";

                        if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("ESTADO")) {
                           queryAux = queryAux + " AND  V.ESTADOAFILIACION = " + listaAuxiliar.get(i).getValorParametro();
                        }
                     }
                  } else if (listaAuxiliar.get(i).getValorParametro().equalsIgnoreCase("A")) {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM VWACTUALESAFILIACIONESPERSONA V ";
                     queryAux = queryAux + "WHERE V.PERSONA = EM.PERSONA ";
                  } else {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM VIGENCIASAFILIACIONESPERSONA V ";
                     queryAux = queryAux + "WHERE V.PERSONA = EM.PERSONA ";
                  }
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("AFILIACIONESDESDE")) {
                  queryAux = queryAux + " AND  V.FECHAINICIAL >= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("AFILIACIONESHASTA")) {
                  queryAux = queryAux + " AND  V.FECHAINICIAL <= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("TERCERO")) {
                  queryAux = queryAux + " AND  V.TERCERO = " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("TIPOENTIDAD")) {
                  queryAux = queryAux + " AND  V.TIPOENTIDAD = " + listaAuxiliar.get(i).getValorParametro();
               }
               //ESTADO AFILIACION NO EXITE EN VWACTUALESAFILIACIONES NI EN VWACTUALESAFILIACIONESPERSONA SOLO ESTA EN VIGENCIASAFILIACIONES
               /*
                 * if
                 * (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("ESTADO"))
                 * { queryAux = queryAux + " AND V.ESTADOAFILIACION = " +
                 * listaAuxiliar.get(i).getValorParametro();
                }
                */
            }
            queryAux = queryAux + ")";
         }
         if (modulo.equalsIgnoreCase("FORMAPAGO")) {
            if (usoWhere == false) {
               queryAux = " WHERE ";
               usoWhere = true;
            } else {
               queryAux = " AND ";
            }
            for (int i = 0; i < listaAuxiliar.size(); i++) {
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("BFORMAPAGO")) {
                  if (listaAuxiliar.get(i).getValorParametro().equalsIgnoreCase("A")) {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM VWACTUALESFormasPagos V ";
                     queryAux = queryAux + "WHERE V.EMPLEADO = EM.SECUENCIA ";
                  } else {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM VIGENCIASFORMASPAGOS V ";
                     queryAux = queryAux + "WHERE V.EMPLEADO = EM.SECUENCIA ";
                  }
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("FORMAPAGODESDE")) {
                  queryAux = queryAux + " AND  V.FECHAVIGENCIA >= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("FORMAPAGOHASTA")) {
                  queryAux = queryAux + " AND  V.FECHAVIGENCIA <= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("FORMAPAGO")) {
                  queryAux = queryAux + " AND  V.FORMAPAGO = " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("SUCURSAL")) {
                  queryAux = queryAux + " AND  V.SUCURSAL = " + listaAuxiliar.get(i).getValorParametro();
               }
            }
            queryAux = queryAux + ")";
         }
         if (modulo.equalsIgnoreCase("MVRS")) {
            if (usoWhere == false) {
               queryAux = " WHERE ";
               usoWhere = true;
            } else {
               queryAux = " AND ";
            }
            String v_vcRETENCIONYSEGSOCXPERSONA = persistenciaEmpresas.consultarPrimeraEmpresa(getEm());
            for (int i = 0; i < listaAuxiliar.size(); i++) {
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("BMVRS")) {
                  if (v_vcRETENCIONYSEGSOCXPERSONA.equalsIgnoreCase("N")) {
                     if (listaAuxiliar.get(i).getValorParametro().equalsIgnoreCase("A")) {
                        queryAux = queryAux + "EXISTS (SELECT 1 FROM VWACTUALESMvrs V ";
                        queryAux = queryAux + "WHERE V.EMPLEADO = EM.SECUENCIA ";
                     } else {
                        queryAux = queryAux + "EXISTS (SELECT 1 FROM MVRS V ";
                        queryAux = queryAux + "WHERE V.EMPLEADO = EM.SECUENCIA ";
                     }
                  } else if (listaAuxiliar.get(i).getValorParametro().equalsIgnoreCase("A")) {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM VWACTUALESMvrsPersona V ";
                     queryAux = queryAux + "WHERE V.PERSONA = EM.PERSONA ";
                  } else {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM MVRSPERSONA V ";
                     queryAux = queryAux + "WHERE V.PERSONA = EM.PERSONA ";
                  }
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("MVRSDESDE")) {
                  queryAux = queryAux + " AND  V.FECHAINICIAL >= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("MVRSHASTA")) {
                  queryAux = queryAux + " AND  V.FECHAINICIAL <= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("MOTIVO")) {
                  queryAux = queryAux + " AND  V.MOTIVO = " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("SUELDOMINIMO")) {
                  queryAux = queryAux + " AND  V.VALOR >= " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("SUELDOMAXIMO")) {
                  queryAux = queryAux + " AND  V.VALOR <= " + listaAuxiliar.get(i).getValorParametro();
               }
            }
            queryAux = queryAux + ")";
         }
         if (modulo.equalsIgnoreCase("SETS")) {
            if (usoWhere == false) {
               queryAux = " WHERE ";
               usoWhere = true;
            } else {
               queryAux = " AND ";
            }
            String v_vcRETENCIONYSEGSOCXPERSONA = persistenciaEmpresas.consultarPrimeraEmpresa(getEm());
            for (int i = 0; i < listaAuxiliar.size(); i++) {
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("BSETS")) {
                  if (v_vcRETENCIONYSEGSOCXPERSONA.equalsIgnoreCase("N")) {
                     if (listaAuxiliar.get(i).getValorParametro().equalsIgnoreCase("A")) {
                        queryAux = queryAux + "EXISTS (SELECT 1 FROM VWACTUALESsets V ";
                        queryAux = queryAux + "WHERE V.EMPLEADO = EM.SECUENCIA ";
                     } else {
                        queryAux = queryAux + "EXISTS (SELECT 1 FROM sets V ";
                        queryAux = queryAux + "WHERE V.EMPLEADO = EM.SECUENCIA ";
                     }
                  } else if (listaAuxiliar.get(i).getValorParametro().equalsIgnoreCase("A")) {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM VWACTUALESsetsPERSONA V ";
                     queryAux = queryAux + "WHERE V.PERSONA = EM.PERSONA ";
                  } else {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM setsPERSONA V ";
                     queryAux = queryAux + "WHERE V.PERSONA = EM.PERSONA ";
                  }
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("SETSDESDE")) {
                  queryAux = queryAux + " AND  V.FECHAINICIAL >= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("SETSHASTA")) {
                  queryAux = queryAux + " AND  V.FECHAINICIAL <= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("SETSDESDEF")) {
                  queryAux = queryAux + " AND  V.FECHAFINAL >= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("SETSHASTAF")) {
                  queryAux = queryAux + " AND  V.FECHAFINAL <= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("METODO")) {
                  queryAux = queryAux + " AND  V.TIPOSET = '" + listaAuxiliar.get(i).getValorParametro() + "'";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("PROMEDIOMINIMO")) {
                  queryAux = queryAux + " AND  V.PROMEDIO >= " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("PROMEDIOMAXIMO")) {
                  queryAux = queryAux + " AND V.PROMEDIO <= " + listaAuxiliar.get(i).getValorParametro();
               }
            }
            queryAux = queryAux + ")";
         }
         if (modulo.equalsIgnoreCase("VACACIONES")) {
            if (usoWhere == false) {
               queryAux = " WHERE ";
               usoWhere = true;
            } else {
               queryAux = " AND ";
            }
            queryAux = queryAux + "EXISTS (SELECT 1 FROM NOVEDADESSISTEMA N ";
            queryAux = queryAux + "WHERE N.EMPLEADO = EM.SECUENCIA  AND N.TIPO = 'VACACION' ";
            for (int i = 0; i < listaAuxiliar.size(); i++) {
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("FECHASALIDADESDE")) {
                  queryAux = queryAux + " AND  N.FECHAINICIALDISFRUTE >= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("FECHASALIDAHASTA")) {
                  queryAux = queryAux + " AND  N.FECHAINICIALDISFRUTE <= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("FECHAREGRESODESDE")) {
                  queryAux = queryAux + " AND  N.FECHASIGUIENTEFINVACA >= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("FECHAREGRESOHASTA")) {
                  queryAux = queryAux + " AND  N.FECHASIGUIENTEFINVACA <= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
            }
            queryAux = queryAux + ")";
         }
         if (modulo.equalsIgnoreCase("FECHARETIRO")) {
            if (usoWhere == false) {
               queryAux = " WHERE ";
               usoWhere = true;
            } else {
               queryAux = " AND ";
            }
            queryAux = queryAux + "EXISTS (SELECT 1 FROM RETIRADOS R,vIGENCIASTIPOSTRABAJADORES VI ";
            queryAux = queryAux + "WHERE R.vigenciatipotrabajador = VI.secuencia AND VI.EMPLEADO = EM.SECUENCIA ";
            for (int i = 0; i < listaAuxiliar.size(); i++) {
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("FECHARETIRODESDE")) {
                  queryAux = queryAux + " AND  R.FECHARETIRO >= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("FECHARETIROHASTA")) {
                  queryAux = queryAux + " AND  R.FECHARETIRO <= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("MOTIVO")) {
                  queryAux = queryAux + " AND  R.MOTIVORETIRO = " + listaAuxiliar.get(i).getValorParametro();
               }
            }
            queryAux = queryAux + ")";
         }
         if (modulo.equalsIgnoreCase("JORNADALABORAL")) {
            if (usoWhere == false) {
               queryAux = " WHERE ";
               usoWhere = true;
            } else {
               queryAux = " AND ";
            }
            queryAux = queryAux + "EXISTS (SELECT 1 FROM VWACTUALESJORNADAS V ";
            queryAux = queryAux + "WHERE V.EMPLEADO = EM.SECUENCIA ";
            for (int i = 0; i < listaAuxiliar.size(); i++) {
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("JORNADALABORALDESDE")) {
                  queryAux = queryAux + " AND  V.FECHAVIGENCIA >= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("JORNADALABORALHASTA")) {
                  queryAux = queryAux + " AND  V.FECHAVIGENCIA <= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("JORNADA")) {
                  queryAux = queryAux + " AND  V.JORNADATRABAJO = " + listaAuxiliar.get(i).getValorParametro();
               }
            }
            queryAux = queryAux + ")";
         }

         if (modulo.equalsIgnoreCase("DATOSPERSONALES")) {
            if (usoWhere == false) {
               queryAux = " WHERE ";
               usoWhere = true;
            } else {
               queryAux = " AND ";
            }
            queryAux = queryAux + "EXISTS (SELECT 1 FROM PERSONAS P ";
            queryAux = queryAux + "WHERE P.SECUENCIA = EM.PERSONA ";
            for (int i = 0; i < listaAuxiliar.size(); i++) {
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("NUMERODOCUMENTO")) {
                  queryAux = queryAux + " AND P.NUMERODOCUMENTO =  " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("CIUDADDOCUMENTO")) {
                  queryAux = queryAux + " AND   P.CIUDADDOCUMENTO = " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("SEXO")) {
                  queryAux = queryAux + " AND   P.SEXO = '" + listaAuxiliar.get(i).getValorParametro() + "'";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("CIUDADNACIMIENTO")) {
                  queryAux = queryAux + " AND  P.CIUDADNACIMIENTO =" + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("FECHANACIMIENTODESDE")) {
                  queryAux = queryAux + " AND   P.FECHANACIMIENTO  >=  TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("FECHANACIMIENTOHASTA")) {
                  queryAux = queryAux + " AND   P.FECHANACIMIENTO  <= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
            }
            queryAux = queryAux + ")";
         }

         if (modulo.equalsIgnoreCase("FACTORRH")) {
            if (usoWhere == false) {
               queryAux = " WHERE ";
               usoWhere = true;
            } else {
               queryAux = " AND ";
            }
            queryAux = queryAux + "EXISTS (SELECT 1 FROM PERSONAS P ";
            queryAux = queryAux + "WHERE P.SECUENCIA = EM.PERSONA ";
            for (int i = 0; i < listaAuxiliar.size(); i++) {
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("FACTORRH")) {
                  queryAux = queryAux + " AND  P.FACTORRH  = '" + listaAuxiliar.get(i).getValorParametro() + "'";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("GRUPOSANGUINEO")) {
                  queryAux = queryAux + " AND  P.GRUPOSANGUINEO  = '" + listaAuxiliar.get(i).getValorParametro() + "'";
               }
            }
            queryAux = queryAux + ")";
         }

         if (modulo.equalsIgnoreCase("ESTADOCIVIL")) {
            if (usoWhere == false) {
               queryAux = " WHERE ";
               usoWhere = true;
            } else {
               queryAux = " AND ";
            }
            for (int i = 0; i < listaAuxiliar.size(); i++) {
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("BESTADOCIVIL")) {
                  if (listaAuxiliar.get(i).getValorParametro().equalsIgnoreCase("A")) {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM PERSONAS P, VWACTUALESESTADOSCIVILES V ";
                     queryAux = queryAux + "WHERE P.SECUENCIA = EM.PERSONA ";
                     queryAux = queryAux + "AND  P.SECUENCIA = V.PERSONA ";
                  } else {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM PERSONAS P, VIGENCIASESTADOSCIVILES V ";
                     queryAux = queryAux + "WHERE P.SECUENCIA = EM.PERSONA ";
                     queryAux = queryAux + "AND  P.SECUENCIA = V.PERSONA ";
                  }
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("ESTADOCIVILDESDE")) {
                  queryAux = queryAux + " AND  V.FECHAVIGENCIA >= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("ESTADOCIVILHASTA")) {
                  queryAux = queryAux + " AND  V.FECHAVIGENCIA <= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("ESTADOCIVIL")) {
                  queryAux = queryAux + " AND  V.ESTADOCIVIL = " + listaAuxiliar.get(i).getValorParametro();
               }
            }
            queryAux = queryAux + ")";
         }

         if (modulo.equalsIgnoreCase("IDIOMA")) {
            if (usoWhere == false) {
               queryAux = " WHERE ";
               usoWhere = true;
            } else {
               queryAux = " AND ";
            }
            queryAux = queryAux + "EXISTS (SELECT 1 FROM IDIOMASPERSONAS I, PERSONAS P ";
            queryAux = queryAux + "WHERE EM.PERSONA = P.SECUENCIA ";
            queryAux = queryAux + "AND  I.PERSONA = P.SECUENCIA ";
            for (int i = 0; i < listaAuxiliar.size(); i++) {
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("IDIOMA")) {
                  queryAux = queryAux + " AND  I.IDIOMA = " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("CONVERSACIONDESDE")) {
                  queryAux = queryAux + " AND  I.HABLA >= " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("CONVERSACIONHASTA")) {
                  queryAux = queryAux + " AND  I.HABLA <= " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("LECTURADESDE")) {
                  queryAux = queryAux + " AND  I.LECTURA >= " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("LECTURAHASTA")) {
                  queryAux = queryAux + " AND  I.LECTURA <= " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("ESCRITURADESDE")) {
                  queryAux = queryAux + " AND  I.ESCRITURA >= " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("ESCRITURAHASTA")) {
                  queryAux = queryAux + " AND  I.ESCRITURA <= " + listaAuxiliar.get(i).getValorParametro();
               }
            }
            queryAux = queryAux + ")";
         }

         if (modulo.equalsIgnoreCase("CENSOS")) {
            if (usoWhere == false) {
               queryAux = " WHERE ";
               usoWhere = true;
            } else {
               queryAux = " AND ";
            }
            for (int i = 0; i < listaAuxiliar.size(); i++) {
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("BCENSOS")) {
                  if (listaAuxiliar.get(i).getValorParametro().equalsIgnoreCase("A")) {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM VWACTUALESVIGENCIASINDICADORES V ";
                     queryAux = queryAux + "WHERE EM.SECUENCIA = V.EMPLEADO ";
                  } else {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM VIGENCIASINDICADORES V ";
                     queryAux = queryAux + "WHERE EM.SECUENCIA = V.EMPLEADO ";
                  }
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("CENSOSDESDE")) {
                  queryAux = queryAux + " AND  V.FECHAINICIAL >=  TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("CENSOSHASTA")) {
                  queryAux = queryAux + " AND  V.FECHAINICIAL <= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("TIPOINDICADOR")) {
                  queryAux = queryAux + " AND  V.TIPOINDICADOR = " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("INDICADOR")) {
                  queryAux = queryAux + " AND  V.INDICADOR = " + listaAuxiliar.get(i).getValorParametro();
               }
            }
            queryAux = queryAux + ")";
         }

         if (modulo.equalsIgnoreCase("EDUCACIONNOFORMAL")) {
            if (usoWhere == false) {
               queryAux = " WHERE ";
               usoWhere = true;
            } else {
               queryAux = " AND ";
            }
            for (int i = 0; i < listaAuxiliar.size(); i++) {
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("BEDUCACIONNOFORMAL")) {
                  if (listaAuxiliar.get(i).getValorParametro().equalsIgnoreCase("A")) {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM vwactualesvigenciasnoformales V, PERSONAS P ";
                     queryAux = queryAux + "WHERE EM.PERSONA = P.SECUENCIA ";
                     queryAux = queryAux + "AND  V.PERSONA = P.SECUENCIA ";
                  } else {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM VIGENCIASNOFORMALES V, PERSONAS P ";
                     queryAux = queryAux + "WHERE EM.PERSONA = P.SECUENCIA ";
                     queryAux = queryAux + "AND  V.PERSONA = P.SECUENCIA ";
                  }
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("EDUCACIONNOFORMALDESDE")) {
                  queryAux = queryAux + " AND  V.FECHAVIGENCIA  >=  TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("EDUCACIONNOFORMALHASTA")) {
                  queryAux = queryAux + " V.FECHAVIGENCIA  <= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("INSTITUCION")) {
                  queryAux = queryAux + " AND  V.INSTITUCION = " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("CURSO")) {
                  queryAux = queryAux + " AND  V.CURSO = " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("REALIZADO")) {
                  queryAux = queryAux + " AND  V.ACARGO = '" + listaAuxiliar.get(i).getValorParametro() + "'";
               }
            }
            queryAux = queryAux + ")";
         }

         if (modulo.equalsIgnoreCase("EDUCACIONFORMAL")) {
            if (usoWhere == false) {
               queryAux = " WHERE ";
               usoWhere = true;
            } else {
               queryAux = " AND ";
            }
            for (int i = 0; i < listaAuxiliar.size(); i++) {
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("BEDUCACIONFORMAL")) {
                  if (listaAuxiliar.get(i).getValorParametro().equalsIgnoreCase("A")) {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM vwactualesvigenciasformales V, PERSONAS P ";
                     queryAux = queryAux + "WHERE EM.PERSONA = P.SECUENCIA ";
                     queryAux = queryAux + "AND  V.PERSONA = P.SECUENCIA ";
                  } else {
                     queryAux = queryAux + "EXISTS (SELECT 1 FROM VIGENCIASFORMALES V, PERSONAS P ";
                     queryAux = queryAux + "WHERE EM.PERSONA = P.SECUENCIA ";
                     queryAux = queryAux + "AND  V.PERSONA = P.SECUENCIA ";
                  }
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("EDUCACIONFORMALDESDE")) {
                  queryAux = queryAux + " AND  V.FECHAVIGENCIA >=  TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("EDUCACIONFORMALHASTA")) {
                  queryAux = queryAux + " AND  V.FECHAVIGENCIA <= TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("INSTITUCION")) {
                  queryAux = queryAux + " AND  V.INSTITUCION = " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("PROFESION")) {
                  queryAux = queryAux + " AND  V.PROFESION = " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("REALIZADO")) {
                  queryAux = queryAux + " AND  V.ACARGO = '" + listaAuxiliar.get(i).getValorParametro() + "'";
               }
            }
            queryAux = queryAux + ")";
         }

         if (modulo.equalsIgnoreCase("CARGOPOSTULARSE")) {
            if (usoWhere == false) {
               queryAux = " WHERE ";
               usoWhere = true;
            } else {
               queryAux = " AND ";
            }
            queryAux = queryAux + "EXISTS (SELECT 1 FROM hvhojasdevida H, PERSONAS P ";
            queryAux = queryAux + "WHERE H.PERSONA = P.SECUENCIA ";
            queryAux = queryAux + "AND  H.PERSONA = EM.PERSONA ";
            for (int i = 0; i < listaAuxiliar.size(); i++) {
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("CARGO")) {
                  queryAux = queryAux + " AND H.CARGO = " + listaAuxiliar.get(i).getValorParametro();
               }
            }
            queryAux = queryAux + ")";
         }

         if (modulo.equalsIgnoreCase("PROYECTO")) {
            if (usoWhere == false) {
               queryAux = " WHERE ";
               usoWhere = true;
            } else {
               queryAux = " AND ";
            }
            queryAux = queryAux + "EXISTS (SELECT 1 FROM VIGENCIASPROYECTOS V, PERSONAS P ";
            queryAux = queryAux + "WHERE V.EMPLEADO = EM.SECUENCIA ";
            queryAux = queryAux + "AND  EM.PERSONA = P.SECUENCIA ";
            for (int i = 0; i < listaAuxiliar.size(); i++) {
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("PROYECTODESDE")) {
                  queryAux = queryAux + " AND  V.FECHAINICIAL =  TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("PROYECTOHASTA")) {
                  queryAux = queryAux + " AND  V.FECHAFINAL = TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("PROYECTO")) {
                  queryAux = queryAux + " AND V.PROYECTO = " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("ROL")) {
                  queryAux = queryAux + " AND V.PRY_ROL = " + listaAuxiliar.get(i).getValorParametro();
               }
            }
            queryAux = queryAux + ")";
         }

         if (modulo.equalsIgnoreCase("EXPERIENCIALABORAL")) {
            if (usoWhere == false) {
               queryAux = " WHERE ";
               usoWhere = true;
            } else {
               queryAux = " AND ";
            }
            queryAux = queryAux + "EXISTS (SELECT 1 FROM HVEXPERIENCIASLABORALES V, HVHOJASDEVIDA H, PERSONAS P ";
            queryAux = queryAux + "WHERE V.HOJADEVIDA = H.SECUENCIA ";
            queryAux = queryAux + "AND  H.PERSONA = P.SECUENCIA ";
            for (int i = 0; i < listaAuxiliar.size(); i++) {
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("EXPERIENCIALABORALDESDE")) {
                  queryAux = queryAux + " AND  V.FECHADESDE =  TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("EXPERIENCIALABORALHASTA")) {
                  queryAux = queryAux + " AND  V.FECHAHASTA = TO_CHAR(TO_DATE('" + listaAuxiliar.get(i).getValorParametro() + "','DD/MM/YYYY'),'DD/MM/YYYY')";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("CARGO")) {
                  queryAux = queryAux + " AND V.CARGO = '" + listaAuxiliar.get(i).getValorParametro() + "'";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("EMPRESA")) {
                  queryAux = queryAux + " AND V.EMPRESA = '" + listaAuxiliar.get(i).getValorParametro() + "'";
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("SECTORECONOMICO")) {
                  queryAux = queryAux + " AND V.SECTORECONOMICO = " + listaAuxiliar.get(i).getValorParametro();
               }
               if (listaAuxiliar.get(i).getNombreParametro().equalsIgnoreCase("MOTIVORETIRO")) {
                  queryAux = queryAux + " AND V.MOTIVORETIRO = " + listaAuxiliar.get(i).getValorParametro();
               }
            }
            queryAux = queryAux + ")";
         }
         return queryAux;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<ResultadoBusquedaAvanzada> obtenerQVWEmpleadosCorteParaEmpleado(List<ResultadoBusquedaAvanzada> listaEmpleadosResultados, List<String> campos) {
      try {
         List<ResultadoBusquedaAvanzada> retorno = persistenciaColumnasEscenarios.buscarQVWEmpleadosCorteCodigoEmpleado(getEm(), listaEmpleadosResultados, campos);
         return retorno;
      } catch (Exception e) {
         log.warn("Error obtenerQVWEmpleadosCorteParaEmpleado Admi : " + e.toString());
         return null;
      }

   }

   @Override
   public List<ResultadoBusquedaAvanzada> obtenerQVWEmpleadosCortePorEmpleadoCodigo(List<BigInteger> listaCodigosEmpleados) {
      try {
         log.warn("Administrar.AdministrarBusquedaAvanzada.obtenerQVWEmpleadosCortePorEmpleadoCodigo()");
         List<ResultadoBusquedaAvanzada> retorno = persistenciaColumnasEscenarios.buscarQVWEmpleadosCortePorEmpleadoCodigo(getEm(), listaCodigosEmpleados);
         return retorno;
      } catch (Exception e) {
         log.warn("Error obtenerQVWEmpleadosCorteParaEmpleado Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<ResultadoBusquedaAvanzada> obtenerQVWEmpleadosCortePorEmpleadoCodigoCompletos(List<BigInteger> listaCodigosEmpleados, List<String> campos) {
      try {
         log.warn("Administrar.AdministrarBusquedaAvanzada.obtenerQVWEmpleadosCortePorEmpleadoCodigoCompletos()");
         List<ResultadoBusquedaAvanzada> retorno = persistenciaColumnasEscenarios.buscarQVWEmpleadosCortePorEmpleadoCodigoCompletos(getEm(), listaCodigosEmpleados, campos);
         return retorno;
      } catch (Exception e) {
         log.warn("Error obtenerQVWEmpleadosCortePorEmpleadoCodigoCompletos Administrar : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Parametros> empleadosParametros() {
      try {
         String usuarioBD = persistenciaActualUsuario.actualAliasBD(getEm());
         List<Parametros> lista = persistenciaParametros.empleadosParametros(getEm(), usuarioBD);
         log.warn("Administrar.AdministrarBusquedaAvanzada.empleadosParametros() Ya consulto");
         if (lista != null) {
            log.warn("lista.size() : " + lista.size());
         }
         return lista;
      } catch (Exception e) {
         log.warn("ERROR : Administrar.AdministrarBusquedaAvanzada.empleadosParametros(): " + e);
         return null;
      }
   }

   @Override
   public void borrarParametros(BigInteger secParametroEstructura) {
      try {
         persistenciaParametros.borrarParametros(getEm(), secParametroEstructura);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public ParametrosEstructuras parametrosLiquidacion() {
      try {
         String usuarioBD = persistenciaActualUsuario.actualAliasBD(getEm());
         return persistenciaParametrosEstructuras.buscarParametro(getEm(), usuarioBD);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Usuarios usuarioActual() {
      try {
         String usuarioBD = persistenciaActualUsuario.actualAliasBD(getEm());
         return persistenciaUsuarios.buscarUsuario(getEm(), usuarioBD);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void crearParametroEstructura(ParametrosEstructuras parametroEstructura) {
      try {
         persistenciaParametrosEstructuras.editar(getEm(), parametroEstructura);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void crearParametros(List<Parametros> listaParametros) {
      try {
         for (int i = 0; i < listaParametros.size(); i++) {
            persistenciaParametros.crear(getEm(), listaParametros.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public List<Empleados> consultarEmpleadosXCodigo(List<BigInteger> listaCodigos) {
      try {
         return persistenciaEmpleado.buscarEmpleadosPorCodigo(getEm(), listaCodigos);
      } catch (Exception e) {
         log.warn("ERROR Administrar.AdministrarBusquedaAvanzada.consultarEmpleadosXCodigo() e: " + e);
         return new ArrayList<Empleados>();
      }
   }
}
