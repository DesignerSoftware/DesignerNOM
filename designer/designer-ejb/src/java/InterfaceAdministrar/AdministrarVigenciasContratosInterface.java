
package InterfaceAdministrar;

import Entidades.Contratos;
import Entidades.Empleados;
import Entidades.TiposContratos;
import Entidades.VigenciasContratos;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author AndresPineda
 */
public interface AdministrarVigenciasContratosInterface {
    
    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Metodo que obtiene las VigenciasContrados por la secuencia de un Emplado
     * @param secEmpleado Secuencia del Empleado
     * @return listVC Lista de Vigencias Contratos por Empleado
     */
    public List<VigenciasContratos> VigenciasContratosEmpleado(BigInteger secEmpleado);
    /**
     * Metodo que modifica las VigenciasContratos dentro de la tabla
     * @param listVCModificadas Lista de VigenciasContratos que se modificaran 
     */
    public void modificarVC(List<VigenciasContratos> listVCModificadas);
    /**
     * Metodo que borra las VigenciasContratos dentro de la tabla
     * @param vigenciasContratos Lista de VigenciasContratos que se borraran
     */
    public void borrarVC(VigenciasContratos vigenciasContratos);
    /**
     * Metodo que crea las VigenciasContratos dentro de la tabla
     * @param vigenciasContratos Lista de VigenciasContratos que se crearan
     */
    public void crearVC(VigenciasContratos vigenciasContratos);
    /**
     * Metodo que busca un Empleado por la secuencia del mismo
     * @param secuencia Secuencia del Empleado
     * @return emlp Empleado que responder por la secuencia dada
     */
    public Empleados buscarEmpleado(BigInteger secuencia);
    /**
     * Metodo que obtiene la lista total de los Contratos almacenados
     * @return Lista de todos los Contratos
     */
    public List<Contratos> contratos();
    /**
     * Metodo que obtiene la lista de todos los TiposContratos almacenados
     * @return listTC Lista de todos los TiposContratos
     */
    public List<TiposContratos> tiposContratos();
    /**
     * Metodo que cierra la sesion
     */
    public void salir();
    
}
