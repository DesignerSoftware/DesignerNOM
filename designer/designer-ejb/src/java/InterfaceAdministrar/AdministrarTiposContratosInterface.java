/**
 * Documentación a cargo de Andres Pineda
 */
package InterfaceAdministrar;

import Entidades.DiasLaborables;
import Entidades.TiposContratos;
import Entidades.TiposDias;
import java.math.BigInteger;
import java.util.List;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la
 * pantalla 'TipoContrato'.
 *
 * @author AndresPineda
 */
public interface AdministrarTiposContratosInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    /**
     * Método encargado de recuperar todos los TiposContratos.
     *
     * @return Retorna una lista de TiposContratos.
     */
    public List<TiposContratos> listaTiposContratos();

    /**
     * Método encargado de crear un TipoContrato.
     *
     * @param listaTC Lista de los TiposContratos que se van a crear.
     */
    public void crearTiposContratos(List<TiposContratos> listaTC);

    /**
     * Método encargado de editar un TipoContrato.
     *
     * @param listaTC Lista de los TiposContratos que se van a modificar.
     */
    public void editarTiposContratos(List<TiposContratos> listaTC);

    /**
     * Método encargado de borrar un TipoContrato.
     *
     * @param listaTC Lista de los TiposContratos que se van a eliminar.
     */
    public void borrarTiposContratos(List<TiposContratos> listaTC);

    /**
     * Método encargado de recuperar todos los DiasLaborables referenciado para
     * un TipoContrato
     *
     * @param secTipoContrato Secuencia del TipoContrato
     * @return Retorna la lista de DiasLaborables para el TipoContrato dado
     */
    public List<DiasLaborables> listaDiasLaborablesParaTipoContrato(BigInteger secTipoContrato);

    /**
     * Método encargado de crear un DiaLaborable.
     *
     * @param listaDL Lista de los DiasLaborables que se van a crear.
     */
    public void crearDiasLaborables(List<DiasLaborables> listaDL);

    /**
     * Método encargado de editar un DiaLaborable..
     *
     * @param listaDL Lista de los DiasLaborables que se van a modificar.
     */
    public void editarDiasLaborables(List<DiasLaborables> listaDL);

    /**
     * Método encargado de borrar un DiaLaborable..
     *
     * @param listaDL Lista de los DiasLaborables que se van a eliminar.
     */
    public void borrarDiasLaborables(List<DiasLaborables> listaDL);

    /**
     * Método encargado de recuperar todos los TiposDias.
     *
     * @return Retorna una lista de TiposDias.
     */
    public List<TiposDias> lovTiposDias();

    public void clonarTC(BigInteger secuenciaClonado, String nuevoNombre, Short nuevoCodigo);
}
