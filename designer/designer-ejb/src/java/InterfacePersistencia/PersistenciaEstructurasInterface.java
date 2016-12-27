/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Estructuras;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'Estructuras' de la base de datos.
 *
 * @author Hugo David Sin Gutiérrez
 */
public interface PersistenciaEstructurasInterface {

    /**
     * Método encargado de insertar una Estructura en la base de datos.
     *
     * @param estructuras Estructura que se quiere crear.
     */
    public void crear(EntityManager em, Estructuras estructuras);

    /**
     * Método encargado de modificar una Estructura de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param estructuras Estructura con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, Estructuras estructuras);

    /**
     * Método encargado de eliminar de la base de datos el Estructura que entra
     * por parámetro.
     *
     * @param estructuras Estructura que se quiere eliminar.
     */
    public void borrar(EntityManager em, Estructuras estructuras);

    /**
     * Método encargado de buscar todas las Estructuras existentes, ordenadas
     * por nombre, en la base de datos.
     *
     * @return Retorna una lista de Estructuras.
     */
    public List<Estructuras> estructuras(EntityManager em);

    /**
     * Método encargado de buscar la Estructura con el secuencia dado por
     * parámetro.
     *
     * @param secuencia Identificador único de la Estructura que se quiere
     * encontrar.
     * @return Retorna la Estructura identificada con el secuencia dado por
     * parámetro.
     */
    public Estructuras buscarEstructura(EntityManager em, BigInteger secuencia);

    /**
     * Método encargado de buscar las Estructuras de un organigrama.
     *
     * @param secOrganigrama Secuencia del organigrama.
     * @return Retorna una lista de Estructuras.
     */
    public List<Estructuras> buscarEstructurasPorOrganigrama(EntityManager em, BigInteger secOrganigrama);

    /**
     * Método encargado de recuperar las estructuras que se mostraran en la
     * lista de valores (LOV). La lista de valores muestra la Estructuras
     * vigentes en una fecha específica.
     *
     * @param fechaVigencia Fecha base para recuperar la información de
     * estructuras para la LOV.
     * @return Retorna una lista de Estructuras habilitadas para la empresa a la
     * fecha dada por parámetro.
     */
    public List<Estructuras> buscarlistaValores(EntityManager em, String fechaVigencia);

    /**
     * Método encargado de traer todas las estructuras padre (no tienen padre)
     * asociadas a un organigrama.
     *
     * @param secOrg Secuencia del organigrama al que se le quiere averiguar las
     * estructuras padre.
     * @return Retorna una lista de Estructuras asociadas a un organigrama y con
     * el atributo estructurapadre = IS NULL.
     */
    public List<Estructuras> estructuraPadre(EntityManager em, BigInteger secOrg);

    /**
     * Método encargado de recuperar las estructuras hijas de una estructura
     * padre especifica. Para realizar la consulta se necesita saber a qué
     * empresa pertenece la estructura padre.
     *
     * @param secEstructuraPadre Secuencia de la Estructura padre.
     * @param codigoEmpresa Código de la empresa.
     * @return Retorna una lista de Estructuras.
     */
    public List<Estructuras> estructurasHijas(EntityManager em, BigInteger secEstructuraPadre, Short codigoEmpresa);

    /**
     * Método encargado de buscar todas las Estructuras existentes en la base de
     * datos.
     *
     * @return Retorna una lista de Contratos.
     */
    public List<Estructuras> buscarEstructuras(EntityManager em);

    /**
     * Método encargado de buscar la Estructura con la secuencia dada por
     * parámetro.
     *
     * @param secuencia Secuencia de la Estructura que se quiere encontrar.
     * @return Retorna la Estructura identificada con la secuencia dada por
     * parámetro.
     */
    public Estructuras buscarEstructuraSecuencia(EntityManager em, BigInteger secuencia);

    /**
     * Método encargado de recuperar las Estructuras que se encuentran
     * relacionadas con un Organigrama y que no sean iguales a la Estructura
     * dada por parametro
     *
     * @param secOrganigrama Secuencia Organigrama
     * @param secEstructura Secuencia Estructura
     * @return Retorna una lista de Estructuras.
     */
    public List<Estructuras> buscarEstructurasPadres(EntityManager em, BigInteger secOrganigrama, BigInteger secEstructura);

    /**
     * Método encargado de obtener la lista de Estructuras que corresponden a la
     * secuencia de un Organigrama
     *
     * @param secOrganigrama
     * @return Retorna una lista de Estructuras.
     */
    public List<Estructuras> buscarEstructurasPorSecuenciaOrganigrama(EntityManager em, BigInteger secOrganigrama);

    public List<Estructuras> buscarEstructurasPorEmpresaFechaIngreso(EntityManager em, BigInteger secEmpresa, Date fechaIngreso);

    public List<Estructuras> buscarEstructurasPorEmpresa(EntityManager em, BigInteger secEmpresa);

    public List<Estructuras> consultarEstructurasReingreso(EntityManager em);

    public List<Estructuras> consultarEstructurasTurnoEmpleado(EntityManager em);
    
    public List<Estructuras> consultarEstructurasEersCabeceras(EntityManager em, BigInteger secuencia);
    
    public void adicionaEstructuraCambiosMasivos(EntityManager em, BigInteger secEstructura, Date fechaCambio);
    
    public void undoAdicionaEstructuraCambiosMasivos(EntityManager em, BigInteger secEstructura, Date fechaCambio);
    
    public void adicionaLocalizacionCambiosMasivos(EntityManager em, BigInteger secEstructura, Date fechaCambio);

    public void undoAdicionaLocalizacionCambiosMasivos(EntityManager em, BigInteger secEstructura, Date fechaCambio);
}
