/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.CentrosCostos;
import Entidades.Empleados;
import Entidades.Estructuras;
import Entidades.MotivosLocalizaciones;
import Entidades.Proyectos;
import Entidades.VigenciasLocalizaciones;
import Entidades.VigenciasProrrateos;
import Entidades.VigenciasProrrateosProyectos;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author AndresPineda
 */
public interface AdministrarVigenciaLocalizacionInterface {
    
    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    
    /**
     * Obtiene la lista de Vigencias Localizaciones de un Empleado
     * @param secEmpleado Secuencia Empleado
     * @return listVLE Lista de VigenciasLocalizaciones de un Empleado
     */
    public List<VigenciasLocalizaciones> VigenciasLocalizacionesEmpleado(BigInteger secEmpleado);
    /**
     * Modifica las VigenciasLocalizaiones enviadas en la lista
     * @param listVLModificadas Lista de VigenciasLocalizaciones a modificar 
     */
    public void modificarVL(List<VigenciasLocalizaciones> listVLModificadas);
    /**
     * Borra las VigenciasLocalizaciones enviadas en la lista
     * @param vigenciasLocalizaciones Lista de VigenciasLocalizaciones a borrar
     */
    public void borrarVL(VigenciasLocalizaciones vigenciasLocalizaciones);
    /**
     * Crea una VigenciaLocalizacion
     * @param vigenciasLocalizaciones Objeto a crear 
     */
    public void crearVL(VigenciasLocalizaciones vigenciasLocalizaciones);
    /**
     * Busca un empleado por medio de la secuencia
     * @param secuencia Secuencia Empleado
     * @return empl Empleado que responde por la secuencia enviada
     */
    public Empleados buscarEmpleado(BigInteger secuencia);
    /**
     * Obtiene la lista de motivos localizaciones totales
     * @return listML Lista de MotivosLocalizaciones
     */
    public List<MotivosLocalizaciones> motivosLocalizaciones();
    /**
     * Obtiene la lista de estructuras totales
     * @return listE Lista de Estructuras
     */
    public List<Estructuras> estructuras();
    /**
     * Obtiene la lista de proyectos totales
     * @return listP Lista de Proyectos
     */
    public List<Proyectos> proyectos();
    /**
     * Limpia el VigenciaLocalizaion, cierra la sesion
     */
    public void salir();
    /**
     * Obtiene la lista de VigenciasProrrateos de una VigenciaLocalizacion
     * @param secVigencia Secuencia VigenciaLocalizacion
     * @return listVPVL Lista de Vigencias Prorrateos de una Vigencia Localizacion
     */
    public List<VigenciasProrrateos> VigenciasProrrateosVigencia(BigInteger secVigencia);
    /**
     * Modifica la lista de VigenciasProrrateos modificas en la pagina
     * @param listVPModificadas Lista VigenciasProrrateos a modificar
     */
    public void modificarVP(List<VigenciasProrrateos> listVPModificadas);
    /**
     * Borra un elemento VigenciaProrrateo
     * @param vigenciasProrrateos Objeto a borrar
     */
    public void borrarVP(VigenciasProrrateos vigenciasProrrateos);
    /**
     * Crea una nueva VigenciaProrrateo
     * @param vigenciasProrrateos Objeto a crear
     */
    public void crearVP(VigenciasProrrateos vigenciasProrrateos);
    /**
     * Obtiene la lista de CentroCostos total
     * @return listCC Lista de Centro Costos
     */
    public  List<CentrosCostos> centrosCostos();
    /**
     * Obtiene de VigenciasProrrateosProyectos la lista de VigenciaLocalizacion por medio de la secuencia
     * @param secVigencia Secuencia VigenciaLocalizacion
     * @return listVPPVL Lista de VigenciasProrrateosProyectos de una VigenciaLocalizacion
     */
    public List<VigenciasProrrateosProyectos> VigenciasProrrateosProyectosVigencia(BigInteger secVigencia);
    /**
     * Modifica la lista de VigenciasProrrateosProyectos modificas en la pagina
     * @param listVPPModificadas Lista de VigenciasProrrateosProyectos a ser modificadas
     */
    public void modificarVPP(List<VigenciasProrrateosProyectos> listVPPModificadas);
    /**
     * Borra una VigenciasProrrateosProyectos
     * @param vigenciasProrrateosProyectos Objeto a ser borrado
     */
    public void borrarVPP(VigenciasProrrateosProyectos vigenciasProrrateosProyectos);
    /**
     * Crea una nueva VigenciasProrrateosProyectos
     * @param vigenciasProrrateosProyectos Objeto a ser creado
     */
    public void crearVPP(VigenciasProrrateosProyectos vigenciasProrrateosProyectos);
    
}
