/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.ConsultasLiquidaciones;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la entidad 'ConsultasLiquidaciones',
 * la cual no es un mapeo de la base de datos sino una Entidad para albergar un resultado. 
 * @author betelgeuse
 */
public interface PersistenciaConsultasLiquidacionesInterface {
    /**
     * Método encargado de consultar las liquidaciones que han sido cerradas en un rango de fechas especifico.
     * @param fechaInicial Fecha inicial del rango deseado. 
     * @param fechaFinal Fecha final del rango deseado.
     * @return Retorna la una lista con consultasLiquidaciones que estén cerradas y realizadas en un rango definido de fechas.
     */
    public List<ConsultasLiquidaciones> liquidacionesCerradas(EntityManager em,String fechaInicial, String fechaFinal);
    /**
     * Método encargado de consultar las liquidaciones no cerradas.
     * @return Retorna todas las consultasLiquidaciones realizadas para preNomina
     */
    public List<ConsultasLiquidaciones> preNomina(EntityManager em);
}
