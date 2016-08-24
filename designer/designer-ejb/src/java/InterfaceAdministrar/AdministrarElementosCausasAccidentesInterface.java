/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfaceAdministrar;

import Entidades.ElementosCausasAccidentes;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la
 * pantalla 'ElementosCausasAccidentes'.
 *
 * @author betelgeuse
 */
@Local
public interface AdministrarElementosCausasAccidentesInterface {

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    /**
     * Método encargado de crear ElementosCausasAccidentes.
     *
     * @param listaElementosCausasAccidentes Lista de los
     * ElementosCausasAccidentes que se van a crear.
     */
    public void crearElementosCausasAccidentes(List<ElementosCausasAccidentes> listaElementosCausasAccidentes);

    /**
     * Método encargado de editar ElementosCausasAccidentes.
     *
     * @param listaElementosCausasAccidentes Lista de las
     * ElementosCausasAccidentes que se van a modificar.
     */
    public void modificarElementosCausasAccidentes(List<ElementosCausasAccidentes> listaElementosCausasAccidentes);

    /**
     * Método encargado de borrar ElementosCausasAccidentes.
     *
     * @param listaElementosCausasAccidentes Lista de los
     * ElementosCausasAccidentes que se van a eliminar.
     */
    public void borrarElementosCausasAccidentes(List<ElementosCausasAccidentes> listaElementosCausasAccidentes);

    /**
     * Método encargado de recuperar todos los ElementosCausasAccidentes.
     *
     * @return Retorna una lista de ElementosCausasAccidentes.
     */
    public List<ElementosCausasAccidentes> consultarElementosCausasAccidentes();

    /**
     * Método encargado de recuperar un ElementoCausaAccidente dada su
     * secuencia.
     *
     * @param secElementosCausasAccidentes Secuencia del ElementoCausaAccidente.
     * @return Retorna el ElementoCausaAccidente cuya secuencia coincida con el
     * valor del parámetro.
     */
    public ElementosCausasAccidentes consultarElementoCausaAccidente(BigInteger secElementosCausasAccidentes);

    /**
     * Método encargado de contar la cantidad de SoAccidentes que una Causa
     * tiene asociadas.
     *
     * @param secCausa Secuencia de la Causa.
     * @return Retorna un número indicando la cantidad de SoAccidentes asociadas
     * a la Causa cuya secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarSoAccidentesCausa(BigInteger secCausa);

    /**
     * Método encargado de contar la cantidad de SoAccidentesMedicos que un
     * ElementoCausaAccidente tiene asociadas.
     *
     * @param secElementoCausaAccidente Secuencia del ElementoCausaAccidente.
     * @return Retorna un número indicando la cantidad de SoAccidentesMedicos
     * asociadas a el ElementoCausaAccidente cuya secuencia coincide con el
     * valor del parámetro.
     */
    public BigInteger contarSoAccidentesMedicosElementoCausaAccidente(BigInteger secElementoCausaAccidente);

    /**
     * Método encargado de contar la cantidad de SoIndicadoresFr que una
     * CausaAccidente tiene asociados.
     *
     * @param secCausaAccidente Secuencia de la CausaAccidente.
     * @return Retorna un número indicando la cantidad de SoIndicadoresFr
     * asociados a la CausaAccidente cuya secuencia coincide con el valor del
     * parámetro.
     */
    public BigInteger contarSoIndicadoresFrElementoCausaAccidente(BigInteger secCausaAccidente);
}
