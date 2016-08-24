/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Contratos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Contratos' 
 * de la base de datos.
 * @author AndresPineda
 */
public interface PersistenciaContratosInterface {
    
    /**
     * Método encargado de insertar un contrato en la base de datos.
     * @param contratos Contrato que se quiere crear.
     */
    public void crear(EntityManager em,Contratos contratos);
    /**
     * Método encargado de modificar un Contrato de la base de datos.
     * Este método recibe la información del parámetro para hacer un 'merge' con la 
     * información de la base de datos.
     * @param contratos Contrato con los cambios que se van a realizar.
     */
    public void editar(EntityManager em,Contratos contratos);
    /**
     * Método encargado de eliminar de la base de datos el Contrato que entra por parámetro.
     * @param contratos Contrato que se quiere eliminar.
     */
    public void borrar(EntityManager em,Contratos contratos);    
    /**
     * Método encargado de buscar todos los Contratos existentes en la base de datos.
     * @return Retorna una lista de Contratos.
     */
    public List<Contratos> buscarContratos(EntityManager em);
    /**
     * Método encargado de buscar el Contrato con la secuencia dada por parámetro.
     * @param secuencia Secuencia del Contrato que se quiere encontrar.
     * @return Retorna el Contrato identificado con la secuencia dada por parámetro.
     */
    public Contratos buscarContratoSecuencia(EntityManager em,BigInteger secuencia);
   /**
    * Método encargado de buscar todos los Contratos existentes en la base de datos, ordenados por código.
    * @return Retorna una lista de Contratos ordenados por código.
    */
    public List<Contratos> lovContratos(EntityManager em);
    /**
     * Método encagado de copiar la configuración de un contrato a otro.
     * @param codigoOrigen Código del contrato dueño de la configuración deseada. 
     * @param codigoDestino Código del contrato al que se le va a imponer la configuración
     */
    public void reproducirContrato(EntityManager em,Short codigoOrigen, Short codigoDestino);
    
    public List<Contratos> buscarContratosPorUsuario(EntityManager em);
    
}
