/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.SoausentismosProrrogas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la entidad 'SoausentismosProrrogas',
 * la cual no es un mapeo de la base de datos sino una Entidad para albergar un resultado. 
 */
public interface PersistenciaSoausentismosProrrogasInterface {
    /**
     * Método encargado de llenar la lista de valores de las prorrogas referentes a los ausentismos teniendo en cuenta el empleado,
     * la causa y el ausentismo.
     * @param secEmpleado Secuencia del Empleado.
     * @param secuenciaCausa Secuencia de la Causa.
     * @param secuenciaAusentismo Secuencia del Ausentismo.
     * @return Retorna una lista de SoausentismosProrrogas con las condiciones dadas por parámetros.
     */
    public List<SoausentismosProrrogas> prorrogas(EntityManager em, BigInteger secEmpleado, BigInteger secuenciaCausa, BigInteger secuenciaAusentismo);    
}
