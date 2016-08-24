/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.SoActosInseguros;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'SoActoInseguros' 
 * de la base de datos.
 * @author John Pineda.
 */
@Local
public interface PersistenciaSoActosInsegurosInterface {
    /**
     * Método encargado de insertar un SoActoInseguro en la base de datos.
     * @param soActosInseguros SoActoInseguro que se quiere crear.
     */
    public void crear(EntityManager em, SoActosInseguros soActosInseguros);
    /**
     * Método encargado de modificar un SoActoInseguro de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param soActosInseguros SoActoInseguro con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, SoActosInseguros soActosInseguros);
    /**
     * Método encargado de eliminar de la base de datos el SoActoInseguro que entra por parámetro.
     * @param soActosInseguros SoActoInseguro con los cambios que se van a realizar.
     */
    public void borrar(EntityManager em, SoActosInseguros soActosInseguros);
    /**
     * Método encargado de buscar el SoActoInseguro con la secuencia dada por parámetro.
     * @param secuencia Secuencia del SoActoInseguro que se quiere encontrar.
     * @return Retorna el SoActoInseguro identificado con la secuencia dada por parámetro.
     */
    public SoActosInseguros buscarSoActoInseguro(EntityManager em, BigInteger secuencia);
    /**
     * Método encargado de buscar todos los SoActosInseguros existentes en la base de datos, ordenados por código.
     * @return Retorna una lista de SoActosInseguros.
     */
    public List<SoActosInseguros> buscarSoActosInseguros(EntityManager em);
    /**
     * Método encargado de recuperar cuantos SoAccidentesMedicos están asociados a un SoActoInseguro específico.
     * @param secuencia Secuencia de la SoActoInseguro.
     * @return Retorna el número de SoAccidentesMedicos cuyo atributo 'ActoInseguro' tiene como secuencia el 
     * valor dado por parámetro.
     */
    public BigInteger contadorSoAccidentesMedicos(EntityManager em, BigInteger secuencia);
}
