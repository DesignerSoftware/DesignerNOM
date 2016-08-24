/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfaceAdministrar;

import Entidades.Deportes;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la pantalla 'Deportes'.
 * @author betelgeuse
 */
@Local
public interface AdministrarDeportesInterface {
    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    /**
     * Método encargado de crear Deportes.
     * @param listaDeportes Lista de los Deportes que se van a crear.
     */
    public void crearDeportes(List<Deportes> listaDeportes);
    /**
     * Método encargado de editar Deportes.
     * @param listaDeportes Lista de los Deportes que se van a modificar.
     */
    public void modificarDeportes(List<Deportes> listaDeportes);
    /**
     * Método encargado de borrar Deportes.
     * @param listaDeportes Lista de los Deportes que se van a eliminar.
     */
    public void borrarDeportes(List<Deportes> listaDeportes);
    /**
     * Método encargado de recuperar todas las Deportes.
     * @return Retorna una lista de Deportes.
     */
    public List<Deportes> consultarDeportes();
    /**
     * Método encargado de recuperar un Deporte dada su secuencia.
     * @param secDeportes Secuencia del Deporte.
     * @return Retorna el Deporte cuya secuencia coincida con el valor del parámetro. 
     */
    public Deportes consultarDeporte(BigInteger secDeportes);
    /**
     * Método encargado de contar la cantidad de relaciones entre un deporte y la tabla VigenciasDeportes.
     * @param secDeporte Secuencia del deporte.
     * @return Retorna 0 si no hay relación entre las tablas o retorna el número de asociaciones con el
     * deporte cuya secuencia coincida con el valor del parámetro indicando.
     */
    public BigInteger contarVigenciasDeportesDeporte(BigInteger secDeporte);
    /**
     * Método encargado de contar la cantidad de personas que han practicado un deporte específico.
     * @param secDeporte Secuencia del deporte.
     * @return Retorna un número indicando la cantidad de deportes que ha practicado la persona cuya
     * secuencia coincide con el valor del parámetro.
     */
    public BigInteger contarPersonasDeporte(BigInteger secDeporte);
    /**
     * Método encargado de contar la cantidad de ParametrosInformes asociados a un deporte específico.
     * @param secDeporte Secuencia del Deporte.
     * @return Retorna un número indicando la cantidad de ParametrosInformes cuyo deporte tenga como 
     * secuencia el valor dado como parámetro.
     */
    public BigInteger contarParametrosInformesDeportes(BigInteger secDeporte);
}
