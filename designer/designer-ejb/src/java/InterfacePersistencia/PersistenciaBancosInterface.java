/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Bancos;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Bancos' 
 * de la base de datos.
 * @author Andrés Pineda
 */
public interface PersistenciaBancosInterface {
    /**
     * Método encargado de insertar un Banco en la base de datos.
     * @param bancos Banco que se quiere crear.
     */
    public void crear(EntityManager em,Bancos bancos);
    /**
     * Método encargado de modificar un Banco de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param bancos Banco con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,Bancos bancos);
    /**
     * Método encargado de eliminar de la base de datos el banco que entra por parámetro.
     * @param bancos Banco que se quiere eliminar.
     */
    public void borrar(EntityManager em,Bancos bancos);
    /**
     * Método encargado de buscar todos los bancos existentes en la base de datos.
     * @return Retorna una lista de Bancos.
     */
    public List<Bancos> buscarBancos(EntityManager em);
    
}
