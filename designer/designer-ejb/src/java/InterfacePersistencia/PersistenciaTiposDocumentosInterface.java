/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.TiposDocumentos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'TiposDocumentos' de la base de datos.
 *
 * @author betelgeuse
 */
public interface PersistenciaTiposDocumentosInterface {

    /**
     * Método encargado de insertar un TipoDocumento en la base de datos.
     *
     * @param tiposDocumentos Moneda que se quiere crear.
     */
    public String crear(EntityManager em, TiposDocumentos tiposDocumentos);

    /**
     * Método encargado de modificar un TipoDocumento de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param tiposDocumentos TiposViajeros con los cambios que se van a
     * realizar.
     */
    public String editar(EntityManager em, TiposDocumentos tiposDocumentos);

    /**
     * Método encargado de eliminar de la base de datos un TipoDocumento que
     * entra por parámetro.
     *
     * @param tiposDocumentos TiposViajeros que se quiere eliminar.
     */
    public String borrar(EntityManager em, TiposDocumentos tiposDocumentos);

    /**
     * Método encargado de buscar todos los TiposDocumentos existentes en la
     * base de datos, ordenados por nombreCorto.
     *
     * @return Retorna una lista de TiposDocumentos ordenados por nombreCorto.
     */
    public List<TiposDocumentos> consultarTiposDocumentos(EntityManager em);

    /**
     * Método encargado de buscar el TipoDocumento con la secTiposDocumentos
     * dada por parámetro.
     *
     * @param secTiposDocumentos Secuencia del TipoDocumento que se quiere
     * encontrar.
     * @return Retorna el TipoDocumento identificado con la secTiposDocumentos
     * dada por parámetro.
     */
    public TiposDocumentos consultarTipoDocumento(EntityManager em, BigInteger secTiposDocumentos);

    /**
     * Método encargado de revisar si existe una relacion entre un TipoDocumento
     * específica y algúna Codeudores. Adémas de la revisión, cuenta cuantas
     * relaciones existen.
     *
     * @param secTiposDocumentos Secuencia del TipoDocumento.
     * @return Retorna el número de Codeudores relacionados con el TipoDocumento
     * cuya secTiposDocumentos coincide con el parámetro.
     */
    public BigInteger contarCodeudoresTipoDocumento(EntityManager em, BigInteger secTiposDocumentos);

    /**
     * Método encargado de revisar si existe una relacion entre un TipoDocumento
     * específica y algúna Personas. Adémas de la revisión, cuenta cuantas
     * relaciones existen.
     *
     * @param secTiposDocumentos Secuencia del TipoDocumento.
     * @return Retorna el número de Personas relacionados con el TipoDocumento
     * cuya secTiposDocumentos coincide con el parámetro.
     */
    public BigInteger contarPersonasTipoDocumento(EntityManager em, BigInteger secTiposDocumentos);

}
