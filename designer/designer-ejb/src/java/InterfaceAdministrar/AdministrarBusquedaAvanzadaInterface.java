package InterfaceAdministrar;

import ClasesAyuda.ColumnasBusquedaAvanzada;
import ClasesAyuda.ParametrosQueryBusquedaAvanzada;
import Entidades.*;
import java.math.BigInteger;
import java.util.List;

public interface AdministrarBusquedaAvanzadaInterface {

   public void obtenerConexion(String idSesion);

   public List<Cargos> lovCargos();

   public List<Estructuras> lovEstructuras();

   public List<Empleados> lovJefe();

   public List<MotivosCambiosCargos> lovMotivosCargos();

   public List<Papeles> lovPapeles();

   public List<MotivosLocalizaciones> lovMotivosLocalizaciones();

   public List<TiposSueldos> lovTiposSueldos();

   public List<MotivosCambiosSueldos> lovMotivosSueldos();

   public List<TiposContratos> lovTiposContratos();

   public List<MotivosContratos> lovMotivosContratos();

   public List<TiposTrabajadores> lovTiposTrabajadores();

   public List<ReformasLaborales> lovReformasLaborales();

   public List<Contratos> lovContratos();

   public List<UbicacionesGeograficas> lovUbicaciones();

   public List<TercerosSucursales> lovTercerosSucursales();

   public List<TiposEntidades> lovTiposEntidades();

   public List<EstadosAfiliaciones> lovEstadosAfiliaciones();

   public List<Periodicidades> lovFormasPagos();

   public List<Sucursales> lovSucursales();

   public List<Motivosmvrs> lovMotivosMvrs();

   public List<NormasLaborales> lovNormasLaborales();

   public List<JornadasLaborales> lovJornadasLaborales();

   public List<MotivosRetiros> lovMotivosRetiros();

   public List<Ciudades> lovCiudades();

   public List<EstadosCiviles> lovEstadosCiviles();

   public List<Idiomas> lovIdiomas();

   public List<TiposIndicadores> lovTiposIndicadores();

   public List<Indicadores> lovIndicadores();

   public List<Profesiones> lovProfesiones();

   public List<Instituciones> lovInstitucioneses();

   public List<Cursos> lovCursos();

   public List<SectoresEconomicos> lovSectoresEconomicos();

   public List<Proyectos> lovProyectos();

   public List<PryRoles> lovRoles();

   public List<Empleados> ejecutarQueryBusquedaAvanzadaPorModulos(String query);

   public String armarQueryModulosBusquedaAvanzada(List<ParametrosQueryBusquedaAvanzada> listaParametro);

   public List<ColumnasEscenarios> buscarColumnasEscenarios();

   public List<BigInteger> ejecutarQueryBusquedaAvanzadaPorModulosCodigo(String query);

   public List<ResultadoBusquedaAvanzada> obtenerQVWEmpleadosCorteParaEmpleado(List<ResultadoBusquedaAvanzada> listaEmpleadosResultados, List<String> campos);

   public List<ResultadoBusquedaAvanzada> obtenerQVWEmpleadosCortePorEmpleadoCodigo(List<BigInteger> listaCodigosEmpleados);

   public List<Parametros> empleadosParametros();

   public void borrarParametros(BigInteger secParametroEstructura);

   public ParametrosEstructuras parametrosLiquidacion();

   public Usuarios usuarioActual();

   public void crearParametroEstructura(ParametrosEstructuras parametroEstructura);

   public void crearParametros(List<Parametros> listaParametros);
   
   public List<Empleados> consultarEmpleadosXCodigo(List<BigInteger> listaCodigos);
}
