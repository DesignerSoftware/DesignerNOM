/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfaceAdministrar;

import Entidades.Cargos;
import Entidades.Competenciascargos;
import Entidades.DetallesCargos;
import Entidades.Empresas;
import Entidades.Enfoques;
import Entidades.EvalCompetencias;
import Entidades.GruposSalariales;
import Entidades.GruposViaticos;
import Entidades.ProcesosProductivos;
import Entidades.SueldosMercados;
import Entidades.TiposDetalles;
import Entidades.TiposEmpresas;
import java.math.BigInteger;
import java.util.List;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la
 * pantalla 'Cargos'.
 *
 * @author betelgeuse
 */
public interface AdministrarCargosInterface {

    /**
     * Método encargado de recuperar todos los Cargos.
     *
     * @return Retorna una lista de Cargos.
     */
    public List<Cargos> consultarCargos();

    /**
     * Método encargado de recuperar todas las Empresas.
     *
     * @return Retorna una lista de Empresas.
     */
    public List<Empresas> listaEmpresas();

    /**
     * Método encargado de recuperar los Cargos de una empresa específica.
     *
     * @param secEmpresa Seciencia de la empresa.
     * @return Retorna la lista de ConceptosJuridicos que estan asociados a una
     * empresa.
     */
    public List<Cargos> listaCargosParaEmpresa(BigInteger secEmpresa);

    /**
     * Método encargado de crear Cargos.
     *
     * @param listaC Lista de los Cargos que se van a crear.
     */
    public void crearCargos(List<Cargos> listaC);

    /**
     * Método encargado de editar Cargos.
     *
     * @param listaC Lista de los Cargos que se van a modificar.
     */
    public void editarCargos(List<Cargos> listaC);

    /**
     * Método encargado de borrar Cargos.
     *
     * @param listaC Lista de los Cargos que se van a eliminar.
     */
    public void borrarCargos(List<Cargos> listaC);

    /**
     * Método encargado de recuperar los SueldosMercados de un Cargo específico.
     *
     * @param secCargo Secuiencia del Cargo.
     * @return Retorna la lista de SueldosMercados que estan asociados a un
     * Cargo
     */
    public List<SueldosMercados> listaSueldosMercadosParaCargo(BigInteger secCargo);

    /**
     * Método encargado de crear SueldosMercados.
     *
     * @param listaSM Lista de los SueldosMercados que se van a crear.
     */
    public void crearSueldosMercados(List<SueldosMercados> listaSM);

    /**
     * Método encargado de editar SueldosMercados.
     *
     * @param listaSM Lista de los SueldosMercados que se van a modificar.
     */
    public void editarSueldosMercados(List<SueldosMercados> listaSM);

    /**
     * Método encargado de borrar SueldosMercados.
     *
     * @param listaSM Lista de los SueldosMercados que se van a eliminar.
     */
    public void borrarSueldosMercados(List<SueldosMercados> listaSM);

    /**
     * Método encargado de recuperar las Competenciascargos de una empresa
     * específica.
     *
     * @param secCargo Secuiencia del Cargo.
     * @return Retorna la lista de Competenciascargos que estan asociados a un
     * Cargo.
     */
    public List<Competenciascargos> listaCompetenciasCargosParaCargo(BigInteger secCargo);

    /**
     * Método encargado de crear Competenciascargos.
     *
     * @param listaCC Lista de los Competenciascargos que se van a crear.
     */
    public void crearCompetenciasCargos(List<Competenciascargos> listaCC);

    /**
     * Método encargado de editar Competenciascargos.
     *
     * @param listaCC Lista de los Competenciascargos que se van a modificar.
     */
    public void editarCompetenciasCargos(List<Competenciascargos> listaCC);

    /**
     * Método encargado de borrar Competenciascargos.
     *
     * @param listaCC Lista de los Competenciascargos que se van a eliminar.
     */
    public void borrarCompetenciasCargos(List<Competenciascargos> listaCC);

    /**
     * Método encargado de recuperar todos los TiposDetalles
     *
     * @return Retorna la lista de TiposDetalles.
     */
    public List<TiposDetalles> listaTiposDetalles();

    /**
     * Método encargado de crear TiposDetalles.
     *
     * @param listaTD Lista de los TiposDetalles que se van a crear.
     */
    public void crearTiposDetalles(List<TiposDetalles> listaTD);

    /**
     * Método encargado de editar TiposDetalles.
     *
     * @param listaTD Lista de los TiposDetalles que se van a modificar.
     */
    public void editarTiposDetalles(List<TiposDetalles> listaTD);

    /**
     * Método encargado de borrar TiposDetalles.
     *
     * @param listaTD Lista de los TiposDetalles que se van a eliminar.
     */
    public void borrarTiposDetalles(List<TiposDetalles> listaTD);

    /**
     * Método encargado de recuperar todas las Empresas.
     *
     * @return Retorna una lista de Empresas.
     */
    public List<GruposSalariales> lovGruposSalariales();

    /**
     * Método encargado de recuperar todas los GruposViaticos.
     *
     * @return Retorna una lista de GruposViaticos.
     */
    public List<GruposViaticos> lovGruposViaticos();

    /**
     * Método encargado de recuperar todas los ProcesosProductivos.
     *
     * @return Retorna una lista de ProcesosProductivos.
     */
    public List<ProcesosProductivos> lovProcesosProductivos();

    /**
     * Método encargado de recuperar todas los TiposEmpresas.
     *
     * @return Retorna una lista de TiposEmpresas.
     */
    public List<TiposEmpresas> lovTiposEmpresas();

    /**
     * Método encargado de recuperar todas las EvalCompetencias.
     *
     * @return Retorna una lista de EvalCompetencias.
     */
    public List<EvalCompetencias> lovEvalCompetencias();

    /**
     * Método encargado de recuperar todas los Enfoques.
     *
     * @return Retorna una lista de Enfoques.
     */
    public List<Enfoques> lovEnfoques();

    /**
     * Método encargado de recuperar un Detalle Cargo asociado a un Cargo y a un
     * TipoDetalle.
     *
     * @param secTipoDetalle Secuencia TipoDetalle
     * @param secCargo Secuencia Cargo
     * @return Retorna un DetalleCargo
     */
    public DetallesCargos detalleDelCargo(BigInteger secTipoDetalle, BigInteger secCargo);

    /**
     * Método encargado de crear DetallesCargos.
     *
     * @param detalleCargo DetallesCargos que se va a crear.
     */
    public void crearDetalleCargo(DetallesCargos detalleCargo);

    /**
     * Método encargado de editar DetallesCargos.
     *
     * @param detalleCargo DetallesCargos que se va a modificar.
     */
    public void editarDetalleCargo(DetallesCargos detalleCargo);

    /**
     * Método encargado de borrar DetallesCargos.
     *
     * @param detalleCargo DetallesCargos que se va a eliminar.
     */
    public void borrarDetalleCargo(DetallesCargos detalleCargo);

    /**
     * Método encargado de encontrar el numero de registros de DetallesCargos
     * asociados a la secuencia de un Cargo
     *
     * @param secCargo Secuencia del Cargo
     * @return Numero de registros de DetallesCargos asociados al Cargo
     */
    public int validarExistenciaCargoDetalleCargos(BigInteger secCargo);

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
}
