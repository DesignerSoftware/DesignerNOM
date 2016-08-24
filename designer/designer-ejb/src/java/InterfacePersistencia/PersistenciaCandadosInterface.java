/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'Candados' 
 * de la base de datos.
 * @author Administrator
 */
public interface PersistenciaCandadosInterface {
    /**
     * Método encargado de verificar si el usuario, que está conectado al aplicativo, tiene permiso para liquidar.
     * @param usuarioBD Alias del usuario que está usando el aplicativo.
     * @return Retorna true si el usuario tiene permisos.
     */
    public boolean permisoLiquidar(EntityManager em,String usuarioBD);
    /**
     * Método encargado de llamar un procedimiento para liquidar. 
     * El procedimiento está definido en la base de datos.
     */
    public void liquidar(EntityManager em);
    /**
     * Método encargado de revisar cual es el estado de liquidación del usuario que esta usando el aplicativo. 
     * El estado de la liquidación puede ser: 
     * 'SIN INICIAR','INICIADO','EN COLA','EJECUTANDO','FINALIZADO','CANCELAR','CANCELADO'
     * @param usuarioBD Alias del usuario que está usando el aplicativo.
     * @return Retorna un String con el estado de la liquidación
     */
    public String estadoLiquidacion(EntityManager em,String usuarioBD);
    /**
     * Método encargado de mostrar el porcentaje de empleados que han sido liquidados frente 
     * al total de empleados a liquidar.
     * @param totalEmpleadosALiquidar
     * @return Retorna un Integer que indica el porcentaje de empleados liquidados
     */
    public Integer progresoLiquidacion(EntityManager em,Integer totalEmpleadosALiquidar);
    /**
     * Método encargado de detener la liquidación que está realizando por un usuario determinado.
     * @param usuarioBD Alias del usuario que está utilizando el aplicativo.
     */
    public void cancelarLiquidacion(EntityManager em,String usuarioBD);
    /**
     * Método encargado de cerrar la liquidación de manera automatica.
     */
    public void cerrarLiquidacionAutomatico(EntityManager em);
    /**
     * Método encargado de cerrar la liquidación de manera no automatica.
     */
    public void cerrarLiquidacionNoAutomatico(EntityManager em);
    /**
     * Método encargado de borrar la liquidación de manera automatica.
     */
    public void borrarLiquidacionAutomatico(EntityManager em);
    /**
     * Método encargado de borrar la liquidación de manera no automatica.
     */
    public void borrarLiquidacionNoAutomatico(EntityManager em);
}
