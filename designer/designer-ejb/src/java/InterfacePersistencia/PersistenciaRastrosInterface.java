/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Rastros;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Rastros' 
 * de la base de datos.
 * @author betelgeuse
 */
public interface PersistenciaRastrosInterface {
    /**
     * Método encargado de buscar los Rastros de un registro que han sido Insertado o modificado en una tabla especifica,
     * organizados por la fechaRastro de forma descendiente.
     * @param secRegistro Secuencia del registro que se desea buscar.
     * @param nombreTabla Nombre de la tabla a la que pertenece el registro.
     * @return Retorna una lista de los todos los Rastros de un registro en una tabla especifica.
     */
    public List<Rastros> rastrosTabla(EntityManager em, BigInteger secRegistro, String nombreTabla);
    /**
     * Método encargado de buscar los Rastros de los registros que han sido Insertado o modificado en una tabla especifica,
     * organizados por la fechaRastro de forma descendiente.
     * @param nombreTabla Nombre de la tabla donde se inserta o modifica el registro.
     * @return Retorna una lista con los Rastros de una tabla.
     */
    public List<Rastros> rastrosTablaHistoricos(EntityManager em, String nombreTabla);
    /**
     * Método encargado de buscar los Rastros de los registros que han sido Eliminados de una tabla especifica,
     * organizados por la fechaRastro de forma descendiente.
     * @param nombreTabla Nombre de la tabla donde se elimina el registro.
     * @return Retorna una lista con los Rastros de una tabla.
     */
    public List<Rastros> rastrosTablaHistoricosEliminados(EntityManager em, String nombreTabla);
    /**
     * Método encargado de buscar los Rastros de un registro que ha sido Eliminado si y solo si a una tabla específica
     * se le ha modificado el valor de la columna ´EMPLEADO´ y este tenía un valor anteriormente. Entrega la busqueda por fechaRastro y 
     * ordenada descendentemente
     * @param nombreTabla Nombre de la tabla donde se elimina el registro
     * @return Retorna una lista de Rastros de una tabla.
     */
    public List<Rastros> rastrosTablaHistoricosEliminadosEmpleados(EntityManager em, String nombreTabla);
    /**
     * Método encargado de buscar los rastros de una tabla específica, realizados en una fecha dada por parámetro y 
     * que los registros hallan sido insertados o modificados.
     * @param fechaRegistro Fecha en la que se realizó el rastro y por la cual se quiere filtrar la busqueda.
     * @param nombreTabla Nombre de la tabla a la que pertenecen los registros insertados o modificados.
     * @return Retorna una lista de Rastros.
     */
    public List<Rastros> rastrosTablaFecha(EntityManager em, Date fechaRegistro, String nombreTabla);
    /**
     * Método encargado de verificar si el registro de una tabla a sido modificado o insertado y tiene un Rastro asociado.
     * @param secRegistro Secuencia del registro que se quiere verificar.
     * @param nombreTabla Nombre de la tabla a la que pertenece el registro.
     * @return Retorna true si existe el rastro asociado a ese registro y el registro ha sido insertado o modificado.
     */
    public boolean verificarRastroRegistroTabla(EntityManager em, BigInteger secRegistro, String nombreTabla);
    /**
     * Método encargado de verificar si una tabla tiene algún Rastro asociado de un registro insertado o modificado. 
     * @param nombreTabla Nombre de la tabla que se quiere verificar.
     * @return Retorna true si la tabla tiene asociado algún rastro de un registro insertado o modificado. 
     */
    public boolean verificarRastroHistoricoTabla(EntityManager em, String nombreTabla);
    /**
     * Método encargado de verificar si una tabla específica se le ha eliminado el valor de la columna ´EMPLEADO´ y esta tenía un valor anteriormente.
     * @param nombreTabla Nombre de la tabla a la que se le quiere realizar la verificación.
     * @return Retorna true si se cumplen las condiciones mencionadas.
     */
    public boolean verificarEmpleadoTabla(EntityManager em, String nombreTabla);
}
