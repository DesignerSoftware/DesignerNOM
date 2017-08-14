package InterfaceAdministrar;

import Entidades.Empleados;
import Entidades.JornadasLaborales;
import Entidades.TiposDescansos;
import Entidades.VigenciasCompensaciones;
import Entidades.VigenciasJornadas;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author AndresPineda
 */

public interface AdministrarVigenciasJornadasInterface {
    
    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Obtiene la lista de VigenciaJornada de un empleado
     * @param secEmpleado Secuencia Empleado
     * @return Lista de VigenciaJornada del empleado dado
     */
    public List<VigenciasJornadas> VigenciasJornadasEmpleado(BigInteger secEmpleado);
    /**
     * Modifica la lista de VigenciaJornada
     * @param listVJModificadas Lista de VigenciaJornada a modificar
     */
    public void modificarVJ(List<VigenciasJornadas> listVJModificadas);
    /**
     * Borra una VigenciaJornada
     * @param vigenciasJornadas Objeto a borrar
     */
    public void borrarVJ(VigenciasJornadas vigenciasJornadas);
    /**
     * Crea una VigenciaJornada
     * @param vigenciasJornadas Objeto a crear
     */
    public void crearVJ(VigenciasJornadas vigenciasJornadas);
    /**
     * Obtiene la lista de VigenciaCompensacion que cumple con la vigencia y tipo compensacion deseado
     * @param tipoC Tipo Compensacion
     * @param secVigencia Secuencia VigenciaJornada
     * @return Lista de VigenciaCompensacion que cumple con la secuencia de la vigencia y el tipo compensacion
     */
    public List<VigenciasCompensaciones> VigenciasCompensacionesSecVigenciaTipoComp(String tipoC, BigInteger secVigencia);
    /**
     * Modifica la lista de VigenciaCompensacion
     * @param listVCModificadas Lista a modificar
     */
    public void modificarVC(List<VigenciasCompensaciones> listVCModificadas);
    /**
     * Borra una VigenciaCompensacion
     * @param vigenciasCompensaciones Objeto a borrar
     */
    public void borrarVC(VigenciasCompensaciones vigenciasCompensaciones);
    /**
     * Crea una VigenciaCompensacion
     * @param vigenciasCompensaciones Objeto a crear
     */
    public void crearVC(VigenciasCompensaciones vigenciasCompensaciones);
    /**
     * Obtiene la lista total de TiposDescansos
     * @return Lista de TiposDescansos
     */
    public List<TiposDescansos> tiposDescansos();
    /**
     * Obtiene la lista total de JornadasLaborales
     * @return Lista de JornadasLaborales
     */
    public List<JornadasLaborales> jornadasLaborales();
    /**
     * Obtiene a el empleado usado
     * @param secuencia Secuencia empleado
     * @return Empleado que cumple con la secuencia
     */
    public Empleados buscarEmpleado(BigInteger secuencia);
    /**
     * Busca las viegncias compensaciones de una vigencia jornada
     * @param secVigencia Secuencia Vigencia
     * @return Lista de vigencias compensaciones de la vigencia jornada
     */
    public List<VigenciasCompensaciones> VigenciasCompensacionesSecVigencia (BigInteger secVigencia);
    
}
