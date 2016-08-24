/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfaceAdministrar;

import Entidades.ClasesAccidentes;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la
 * pantalla 'ClasesAccidentes'.
 *
 * @author betelgeuse
 */
@Local
public interface AdministrarClasesAccidentesInterface {

    /**
     * Método encargado de crear ClasesAccidentes.
     *
     * @param listaClasesAccidentes Lista de los ClasesAccidentes que se van a
     * crear.
     */
    public void crearClasesAccidentes(List<ClasesAccidentes> listaClasesAccidentes);

    /**
     * Método encargado de editar ClasesAccidentes.
     *
     * @param listaClasesAccidentes Lista de los ClasesAccidentes que se van a
     * modificar.
     */
    public void modificarClasesAccidentes(List<ClasesAccidentes> listaClasesAccidentes);

    /**
     * Método encargado de borrar ClasesAccidentes.
     *
     * @param listaClasesAccidentes Lista de los ClasesAccidentes que se van a
     * borrar.
     */
    public void borrarClasesAccidentes(List<ClasesAccidentes> listaClasesAccidentes);

    /**
     * Método encargado de recuperar una ClaseAccidente dada su secuencia.
     *
     * @param secClasesAccidentes Secuencia de la ClaseAccidente.
     * @return Retorna la ClaseAccidente cuya secuencia coincida con el valor
     * del parámetro.
     */
    public ClasesAccidentes consultarClaseAccidente(BigInteger secClasesAccidentes);

    /**
     * Método encargado de recuperar todos los ClasesAccidentes.
     *
     * @return Retorna una lista de ClasesAccidentes.
     */
    public List<ClasesAccidentes> consultarClasesAccidentes();

    /**
     * Método encargado de validar si existe una relación entre un
     * ClaseAccidente específico y algún AccidenteMedico. Además de la revisión,
     * cuenta cuantas relaciones existen.
     *
     * @param secClasesAccidentes Secuencia de un ClaseAccidente.
     * @return Retorna el número de proyectos relacionados con un ClaseAccidente
     * cuya secuencia coincide con el parámetro.
     */
    public BigInteger verificarSoAccidentesMedicosClaseAccidente(BigInteger secClasesAccidentes);
    
    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
}
