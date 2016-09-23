/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfaceAdministrar;

import Entidades.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la pestaña 'Personal'.
 * @author betelgeuse
 */
public interface AdministrarCarpetaPersonalInterface {
    /**
     * Método encargado de obtener el Entity Manager el cual tiene
     * asociado la sesion del usuario que utiliza el aplicativo.
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);
    /**
     * Método encargado de recuperar el cargo actual de un empleado específico.
     * @param secEmpleado Secuencia del Empleado.
     * @return Retorna un VWActualesCargos con la información requerida.
     */
    public VWActualesCargos consultarActualCargoEmpleado(BigInteger secEmpleado);
    /**
     * Método encargado de recuperar la actual fecha hasta.
     * @return Retorna un Date con la información requerida.
     */
    public Date consultarActualesFechas();
    /**
     * Método encargado de recuperar el cargo actual de un empleado específico.
     * @param secEstructura Secuencia de la estructura.
     * @param secCargo Secuencia del cargo.
     * @param fechaHasta Fechas hasta de VWActualesFechas.
     * @return Retorna un VWActualesCargos con la información requerida.
     */
    public String consultarActualARP(BigInteger secEstructura, BigInteger secCargo, Date fechaHasta);
    /**
     * Método encargado de recuperar el Tipo Contrato actual de un empleado específico.
     * @param secEmpleado Secuencia del Empleado.
     * @return Retorna un VWActualesTiposContratos con la información requerida.
     */
    public VWActualesTiposContratos consultarActualTipoContratoEmpleado(BigInteger secEmpleado);
    /**
     * Método encargado de recuperar la Norma actual de un empleado específico.
     * @param secEmpleado Secuencia del Empleado.
     * @return Retorna un VWActualesNormasEmpleados con la información requerida.
     */
    public VWActualesNormasEmpleados consultarActualNormaLaboralEmpleado(BigInteger secEmpleado);
    /**
     * Método encargado de recuperar la afiliación a salud actual de un empleado específico.
     * @param secEmpleado Secuencia del Empleado.
     * @return Retorna un VWActualesAfiliacionesSalud con la información requerida.
     */
    public VWActualesAfiliacionesSalud consultarActualAfiliacionSaludEmpleado(BigInteger secEmpleado);
    /**
     * Método encargado de recuperar la afiliación a pensión actual de un empleado específico.
     * @param secEmpleado Secuencia del Empleado.
     * @return Retorna un VWActualesAfiliacionesPension con la información requerida.
     */
    public VWActualesAfiliacionesPension consultarActualAfiliacionPensionEmpleado(BigInteger secEmpleado);
    /**
     * Método encargado de recuperar la localización actual de un empleado específico.
     * @param secEmpleado Secuencia del Empleado.
     * @return Retorna un VWActualesLocalizaciones con la información requerida.
     */
    public VWActualesLocalizaciones consultarActualLocalizacionEmpleado(BigInteger secEmpleado);
    /**
     * Método encargado de recuperar el tipo de trabajador actual de un empleado específico.
     * @param secEmpleado Secuencia del Empleado.
     * @return Retorna un VWActualesTiposTrabajadores con la información requerida.
     */
    public VWActualesTiposTrabajadores consultarActualTipoTrabajadorEmpleado(BigInteger secEmpleado);
    /**
     * Método encargado de recuperar el contrato actual de un empleado específico.
     * @param secEmpleado Secuencia del Empleado.
     * @return Retorna un VWActualesContratos con la información requerida.
     */
    public VWActualesContratos consultarActualContratoEmpleado(BigInteger secEmpleado);
    /**
     * Método encargado de recuperar la jornada actual de un empleado específico.
     * @param secEmpleado Secuencia del Empleado.
     * @return Retorna un VWActualesJornadas con la información requerida.
     */
    public VWActualesJornadas consultarActualJornadaEmpleado(BigInteger secEmpleado);
    /**
     * Método encargado de recuperar el sueldo actual de un empleado específico.
     * @param secEmpleado Secuencia del Empleado.
     * @return Retorna un BigDecimal con la información requerida.
     */
    public BigDecimal consultarActualSueldoEmpleado(BigInteger secEmpleado);
    /**
     * Método encargado de recuperar la reforma laboral actual aplicada a un empleado específico.
     * @param secEmpleado Secuencia del Empleado.
     * @return Retorna un VWActualesReformasLaborales con la información requerida.
     */
    public VWActualesReformasLaborales consultarActualReformaLaboralEmpleado(BigInteger secEmpleado);
    /**
     * Método encargado de recuperar la Ubicación actual de un empleado específico.
     * @param secEmpleado Secuencia del Empleado.
     * @return Retorna un VWActualesUbicaciones con la información requerida.
     */
    public VWActualesUbicaciones consultarActualUbicacionEmpleado(BigInteger secEmpleado);
    /**
     * Método encargado de recuperar la forma de pago actual para un empleado específico.
     * @param secEmpleado Secuencia del Empleado.
     * @return Retorna un VWActualesFormasPagos con la información requerida.
     */
    public VWActualesFormasPagos consultarActualFormaPagoEmpleado(BigInteger secEmpleado);
    /**
     * Método encargado de recuperar el tipo viajero actual de un empleado específico.
     * @param secEmpleado Secuencia del Empleado.
     * @return Retorna un VWActualesUbicaciones con la información requerida.
     */
    public VWActualesVigenciasViajeros consultarActualTipoViajeroEmpleado(BigInteger secEmpleado);
    /**
     * Método encargado de recuperar los empleados asociados con un tipoTrabajador específico.
     * @param tipo Tipo del trabajador por el cual se va a filtrar.
     * @return Retorna una lista de VWActualesTiposTrabajadores con la informacion requerida.
     */
    public List<VwTiposEmpleados> consultarEmpleadosTipoTrabajador(String tipo);
    /**
     * Método encargado de recuperar la información de la empresa a la cual pertenece el Usuario conectado.
     * @return Retorna el DetalleEmpresa con la información de la empresa.
     */
    public DetallesEmpresas consultarDetalleEmpresaUsuario();
    
    public Empresas obtenerEmpresa(BigInteger secEmpresa);
    /**
     * Método encargado de recuperar el Usuario cuyo alias coincida con el dado por parámetro.
     * @param alias Alias del Usuario que se quiere encontrar.
     * @return Retorna el Usuario identificado con el alias dado por parámetro.
     */
    public Usuarios consultarUsuario(String alias);
    /**
     * Método encargado de recuperar los Parametros Estructuras del Usuario conectado.
     * @return Retorna un ParametroEstructura, el cual esta asociado con el usuario conectado.
     */
    public ParametrosEstructuras consultarParametrosUsuario();
    /**
     * Método encargado de recuperar las VigenciasCargos de un empleado específico. 
     * @param secEmpleado Secuencia del Empleado.
     * @return Retorna una lista de VigenciasCargos asociadas al empleado cuya secuencia coincide con el 
     * valor del parámetro.
     */
    public List<VigenciasCargos> consultarVigenciasCargosEmpleado(BigInteger secEmpleado);
    /**
     * Método encargado de recuperar los VWActualesTiposTrabajadores, para obtener todos los
     * empleados sin importar el tipo.
     * @return Retorna una lista con todos los VWActualesTiposTrabajadores de la vista.
     */
    public List<VwTiposEmpleados> consultarRapidaEmpleados();
    /**
     * Método encargado de recuperar una Persona según su identificación.
     * @param identificacion Identificación de la Persona.
     * @return Retorna la persona cuya identificación coincide con el valor del parámetro con el fin de
     * recuperar la foto.
     */
    public Personas consultarFotoPersona(BigInteger identificacion);
    /**
     * Método encargado de actualizar la foto de una persona específica.
     * @param identificacion Identificación de la persona a la cual se le va a actualizar la foto.
     */
    public void actualizarFotoPersona(BigInteger identificacion);
    /**
     * Método encargado de recuperar el empleado con una secuencia dada.
     * @param secuencia Secuencia del Empleado que se quiere recuperar.
     * @return Retorna el empleado cuya secuencia coincida con el valor dado por parámetro.
     */
    public Empleados consultarEmpleado(BigInteger secuencia);
    /**
     * Método encargado de editar VigenciasCargos.
     * @param vigenciasCargos Lista de las VigenciasCargos que se van a modificar.
     */
    public void editarVigenciasCargos(List<VigenciasCargos> vigenciasCargos);
    /**
     * Método encargado de recuperar el Alias del usuario que está conectado en la sesión actual.
     * @return Retorna un String con el Alias del usuario que esta conectado en la sesion actual.
     */
    public String consultarAliasActualUsuario();
    /**
     * Método encargado de recuperar el estado actual de vacaciones de un empleado específico.
     * @param secEmpleado Secuencia del Empleado.
     * @return Retorna un String con la información requerida.
     */
    public String consultarActualEstadoVacaciones(BigInteger secEmpleado);
    /**
     * Método encargado de recuperar el estado actual MVR de un empleado específico.
     * @param secEmpleado Secuencia del Empleado.
     * @return Retorna un String con la información requerida.
     */
    public BigDecimal consultarActualMVR(BigInteger secEmpleado);
    /**
     * Método encargado de borrar la liquidacion automatica.
     */
    public void borrarLiquidacionAutomatico();
    /**
     * Método encargado de borrar la liquidacion no automatica.
     */
    public void borrarLiquidacionNoAutomatico();
    public String actualIBC(BigInteger secEmpleado, String RETENCIONYSEGSOCXPERSONA);
    public String consultarActualSet(BigInteger secEmpleado);
    public String consultarActualComprobante(BigInteger secEmpleado);
    public BigDecimal borrarActivo(BigInteger secuencia);
    public void borrarEmpleadoActivo(BigInteger secuenciaEmpleado, BigInteger secuenciaPersona);
    public VWActualesTiposTrabajadores consultarEmpleadosTipoTrabajadorPosicion(String tipo, int posicion);
    public int obtenerTotalRegistrosTipoTrabajador(String tipo);

    public List<Empresas> consultarEmpresas();
}
