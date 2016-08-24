/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfaceAdministrar;

import Entidades.Contratos;
import Entidades.TiposCotizantes;
import java.util.List;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la
 * pantalla 'Contratos'.
 *
 * @author betelgeuse.
 */
public interface AdministrarContratosInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    /**
     * Método encargado de recuperar todos los Contratos.
     *
     * @return Retorna una lista de Contratos.
     */
    public List<Contratos> consultarContratos();

    /**
     * Método encargado de recuperar los TiposCotizantes necesarios para la
     * lista de valores.
     *
     * @return Retorna una lista de TiposCotizantes.
     */
    public List<TiposCotizantes> consultaLOVTiposCotizantes();

    /**
     * Método encargado de crear Contratos.
     *
     * @param listaContratos Lista de los Contratos que se van a crear.
     */
    public void crearConceptos(List<Contratos> listaContratos);

    /**
     * Método encargado de editar Contratos.
     *
     * @param listaContratos Lista de los Contratos que se van a modificar.
     */
    public void modificarConceptos(List<Contratos> listaContratos);

    /**
     * Método encargado de borrar Contratos.
     *
     * @param listaContratos Lista de los Contratos que se van a eliminar.
     */
    public void borrarConceptos(List<Contratos> listaContratos);

    /**
     * Método encagado de copiar la configuración de un contrato a otro.
     *
     * @param codigoOrigen Código del contrato dueño de la configuración
     * deseada.
     * @param codigoDestino Código del contrato al que se le va a imponer la
     * configuración.
     */
    public void reproducirContrato(Short codigoOrigen, Short codigoDestino);
}
