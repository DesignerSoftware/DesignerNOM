/**
 * Documentación a cargo de AndresPineda
 */
package InterfaceAdministrar;

import Entidades.CentrosCostos;
import Entidades.Empresas;
import Entidades.Estructuras;
import Entidades.Organigramas;
import java.math.BigInteger;
import java.util.List;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la
 * pantalla 'EstructuraPlanta'.
 *
 * @author AndresPineda
 */
public interface AdministrarEstructurasPlantasInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    /**
     * Método encargado de recuperar todos los Organigramas.
     *
     * @return Retorna una lista de Organigramas.
     */
    public List<Organigramas> listaOrganigramas();

    /**
     * Método encargado de editar Organigramas.
     *
     * @param listaO Lista de los Organigramas que se van a modificar.
     */
    public void modificarOrganigramas(List<Organigramas> listaO);

    /**
     * Método encargado de recuperar todas las Estructuras que se encuentran
     * referenciados a una secuencia de un Organigrama
     *
     * @param secOrganigrama Secuencia Organigrama
     * @return Retorna una lista de Estructuras.
     */
    public List<Estructuras> listaEstructurasPorSecuenciaOrganigrama(BigInteger secOrganigrama);

    /**
     * Método encargado de crear Estructuras.
     *
     * @param listaE Lista de los Estructuras que se van a crear.
     */
    public void crearEstructura(List<Estructuras> listaE);

    /**
     * Método encargado de editar Estructuras.
     *
     * @param listaE Lista de los Estructuras que se van a modificar.
     */
    public void editarEstructura(List<Estructuras> listaE);

    /**
     * Método encargado de borrar Estructuras.
     *
     * @param listaE Lista de los Estructuras que se van a eliminar.
     */
    public void borrarEstructura(List<Estructuras> listaE);

    /**
     * Método encargado de recuperar las Estructuras que se encuentran
     * relacionadas con un Organigrama y que no sean iguales a la Estructura
     * dada por parametro
     *
     * @param secOrganigrama Secuencia Organigrama
     * @param secEstructura Secuencia Estructura
     * @return Retorna una lista de Empresas.
     */
    public List<Estructuras> lovEstructurasPadres(BigInteger secOrganigrama, BigInteger secEstructura);

    /**
     * Método encargado de recuperar todas los CentrosCostos que se encuentran
     * relacionados con una Empresa especifica
     *
     * @param secEmpresa Secuencia Empresa
     * @return Retorna una lista de CentrosCostos.
     */
    public List<CentrosCostos> lovCentrosCostos(BigInteger secEmpresa);

    /**
     * Método encargado de obtener el valor de cargos a controlar de una
     * Estructura
     *
     * @param secEstructura Secuencia Estructura
     * @return Valor de la cantidad de cargos a controlar
     */
    public String cantidadCargosAControlar(BigInteger secEstructura);

    /**
     * Método encargado de obtener el valor de cargos por empleados activos de
     * una Estructura
     *
     * @param secEstructura Secuencia Estructura
     * @return Valor de la cantidad de cargos a controlarpor empleados activos
     */
    public String cantidadCargosEmplActivos(BigInteger secEstructura);

    /**
     * Método encargado de recuperar todas las Estructuras
     *
     * @return Retorna una lista de Estructuras.
     */
    public List<Estructuras> lovEstructuras();

    public List<Empresas> consultarEmpresas();

    public List<Organigramas> listaTodosOrganigramas();

    public void modificarOrganigrama(List<Organigramas> listOrganigramasModificados);

    public void borrarOrganigrama(Organigramas organigrama);

    public void crearOrganigrama(Organigramas organigrama);
}
