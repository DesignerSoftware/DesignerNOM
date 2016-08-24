/**
 * Documentación a cargo de Andres Pineda
 */
package InterfaceAdministrar;

import Entidades.Conceptos;
import Entidades.Formulas;
import Entidades.Grupostiposentidades;
import Entidades.TEFormulasConceptos;
import Entidades.TSFormulasConceptos;
import Entidades.TSGruposTiposEntidades;
import Entidades.TiposEntidades;
import Entidades.TiposSueldos;
import java.math.BigInteger;
import java.util.List;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la
 * pantalla 'TipoSueldo'.
 *
 * @author AndresPineda
 */
public interface AdministrarTiposSueldosInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Método encargado de recuperar todos los TiposSueldos.
     *
     * @return Retorna una lista de TiposSueldos.
     */
    public List<TiposSueldos> listaTiposSueldos();

    /**
     * Método encargado de crear un TipoSueldo.
     *
     * @param listaTS Lista de los TipoSueldo que se van a crear.
     */
    public void crearTiposSueldos(List<TiposSueldos> listaTS);

    /**
     * Método encargado de editar un TipoSueldo.
     *
     * @param listaTS Lista de los TipoSueldo que se van a modificar.
     */
    public void editarTiposSueldos(List<TiposSueldos> listaTS);

    /**
     * Método encargado de borrar un TipoSueldo.
     *
     * @param listaTS Lista de los TipoSueldo que se van a eliminar.
     */
    public void borrarTiposSueldos(List<TiposSueldos> listaTS);

    /**
     * Método encargado de recuperar todos los TSFormulasConceptos referenciado
     * para un TipoSueldo
     *
     * @param secTipoSueldo Secuencia del TipoSueldo
     * @return Retorna la lista de TSFormulasConceptos para el TipoSueldo dado
     */
    public List<TSFormulasConceptos> listaTSFormulasConceptosParaTipoSueldoSecuencia(BigInteger secTipoSueldo);

    /**
     * Método encargado de crear un TSFormulaConcepto.
     *
     * @param listaTS Lista de los TSFormulaConcepto que se van a crear.
     */
    public void crearTSFormulasConceptos(List<TSFormulasConceptos> listaTS);

    /**
     * Método encargado de editar un TSFormulaConcepto..
     *
     * @param listaTS Lista de los TSFormulaConcepto que se van a modificar.
     */
    public void editarTSFormulasConceptos(List<TSFormulasConceptos> listaTS);

    /**
     * Método encargado de borrar un TSFormulaConcepto..
     *
     * @param listaTS Lista de los TSFormulaConcepto que se van a eliminar.
     */
    public void borrarTSFormulasConceptos(List<TSFormulasConceptos> listaTS);

    /**
     * Método encargado de recuperar todos los TSGruposTiposEntidades
     * referenciado para un TipoSueldo
     *
     * @param secTipoSueldo Secuencia del TipoSueldo
     * @return Retorna la lista de TSGruposTiposEntidades para el TipoSueldo
     * dado
     */
    public List<TSGruposTiposEntidades> listaTSGruposTiposEntidadesParaTipoSueldoSecuencia(BigInteger secTipoSueldo);

    /**
     * Método encargado de crear un TSGrupoTipoEntidade.
     *
     * @param listaTS Lista de los TSGrupoTipoEntidade que se van a crear.
     */
    public void crearTSGruposTiposEntidades(List<TSGruposTiposEntidades> listaTS);

    /**
     * Método encargado de editar un TSGrupoTipoEntidade..
     *
     * @param listaTS Lista de los TSGrupoTipoEntidade que se van a modificar.
     */
    public void editarTSGruposTiposEntidades(List<TSGruposTiposEntidades> listaTS);

    /**
     * Método encargado de borrar un TSGrupoTipoEntidade..
     *
     * @param listaTS Lista de los TSGrupoTipoEntidade que se van a eliminar.
     */
    public void borrarTSGruposTiposEntidades(List<TSGruposTiposEntidades> listaTS);

    /**
     * Método encargado de recuperar todos los TEFormulasConceptos referenciado
     * para un TSGrupoTipoEntidad
     *
     * @param secTSGrupo Secuencia del TSGrupoTipoEntidad
     * @return Retorna la lista de TEFormulasConceptos para el
     * TSGrupoTipoEntidad dado
     */
    public List<TEFormulasConceptos> listaTEFormulasConceptosParaTSGrupoTipoEntidadSecuencia(BigInteger secTSGrupo);

    /**
     * Método encargado de crear un TEFormulaConcepto.
     *
     * @param listaTE Lista de los TEFormulaConcepto que se van a crear.
     */
    public void crearTEFormulasConceptos(List<TEFormulasConceptos> listaTE);

    /**
     * Método encargado de editar un TEFormulaConcepto..
     *
     * @param listaTE Lista de los TEFormulaConcepto que se van a modificar.
     */
    public void editarTEFormulasConceptos(List<TEFormulasConceptos> listaTE);

    /**
     * Método encargado de borrar un TEFormulaConcepto..
     *
     * @param listaTE Lista de los TEFormulaConcepto que se van a eliminar.
     */
    public void borrarTEFormulasConceptos(List<TEFormulasConceptos> listaTE);

    /**
     * Método encargado de recuperar todos las Formulas.
     *
     * @return Retorna una lista de Formulas.
     */
    public List<Formulas> lovFormulas();

    /**
     * Método encargado de recuperar todos los Conceptos.
     *
     * @return Retorna una lista de Conceptos.
     */
    public List<Conceptos> lovConceptos();

    /**
     * Método encargado de recuperar todos los Grupostiposentidades.
     *
     * @return Retorna una lista de Grupostiposentidades.
     */
    public List<Grupostiposentidades> lovGruposTiposEntidades();

    /**
     * Método encargado los TiposEntidades para un GrupoTipoEntidad dado por
     * parametro.
     *
     * @param secGrupo Secuencia del GrupoTipoEntidad
     * @return Retorna una lista de TiposEntidades.
     */
    public List<TiposEntidades> lovTiposEntidades(BigInteger secGrupo);
    /**
     * Método encargado de recuperar todos los TEFormulasConceptos.
     *
     * @return Retorna una lista de TEFormulasConceptos.
     */
    public List<TEFormulasConceptos> listaTEFormulasConceptos();
}
