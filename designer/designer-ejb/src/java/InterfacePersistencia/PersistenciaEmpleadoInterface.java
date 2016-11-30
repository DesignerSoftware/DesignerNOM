/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Empleados;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import Entidades.NovedadesSistema;
import java.math.BigDecimal;

public interface PersistenciaEmpleadoInterface {

    /**
     * Método encargado de insertar un Empleado en la base de datos.
     *
     * @param em
     * @param empleados Empleado que se quiere crear.
     */
    public void crear(EntityManager em, Empleados empleados);

    public BigInteger crearConVCargo(EntityManager em, BigDecimal codigoEmpleado, BigInteger secPersona, BigInteger secEmpresa,
            BigInteger secCargo, BigInteger secEstructura, Date fechaIngreso, BigInteger motivoCargo);

    /**
     * Método encargado de modificar un Empleado de la base de datos. Este
     * método recibe la información del parámetro para hacer un 'merge' con la
     * información de la base de datos.
     *
     * @param em
     * @param empleados Empleado con los cambios que se van a realizar.
     */
    public void editar(EntityManager em, Empleados empleados);

    /**
     * Método encargado de eliminar de la base de datos el empleado que entra
     * por parámetro.
     *
     * @param empleados Empleado que se quiere eliminar.
     */
    public void borrar(EntityManager em, Empleados empleados);

    /**
     * Método encargado de buscar el Empleado con la secuencia dada por
     * parámetro.
     *
     * @param secuencia Secuencia del Empleado que se quiere encontrar.
     * @return Retorna el Empleado identificado con la secuencia dada por
     * parámetro.
     */
    public Empleados buscarEmpleado(EntityManager em, BigInteger secuencia);

    /**
     * Método encargado de buscar todos los Empleados existentes en la base de
     * datos.
     *
     * @return Retorna una lista de Contratos.
     */
    public List<Empleados> buscarEmpleados(EntityManager em);

    /**
     * Método encargado de buscar el Empleado con la secuencia dada por
     * parámetro.
     *
     * @param em
     * @param secuencia Secuencia del Empleado que se quiere encontrar.
     * @return Retorna el Empleado identificado con la secuencia dada por
     * parámetro.
     */
    public Empleados buscarEmpleadoSecuencia(EntityManager em, BigInteger secuencia);

    /**
     * Método encargado de verificar si un empleado existe para una empresa
     * especifica.
     *
     * @param codigoEmpleado Códígo del empleado.
     * @param secEmpresa Secuencia de la empresa a la que se quiere verificar si
     * el usuario pertenece.
     * @return Retorna True si el empleado existe para esa empresa
     */
    public boolean verificarCodigoEmpleado_Empresa(EntityManager em, BigInteger codigoEmpleado, BigInteger secEmpresa);

    /**
     * Método encargado de buscar un empleado de una empresa específica.
     *
     * @param codigoEmpleado Códígo del empleado.
     * @param secEmpresa Secuencia de la empresa a la que el usuario
     * pertenecería.
     * @return Retorna el empleado que cumple las características dadas por los
     * parámetros.
     */
    public Empleados buscarEmpleadoCodigo_Empresa(EntityManager em, BigInteger codigoEmpleado, BigInteger secEmpresa);

    /**
     * Método encargado de buscar el Empleado con la secuencia dada por
     * parámetro.
     *
     * @param codigoEmpleado Código del Empleado que se quiere encontrar.
     * @return Retorna el Empleado identificado con el código dado por
     * parámetro.
     */
    public Empleados buscarEmpleadoCodigo(EntityManager em, BigInteger codigoEmpleado);

    /**
     * Método encargado de buscar los empleados que fueron parametrizados y se
     * quiere consultar el comprobante de pago.
     *
     * @param usuarioBD Alias del Usuario en la base de datos.
     * @return Retorna una lista de empleados
     */
    public List<Empleados> empleadosComprobantes(EntityManager em, String usuarioBD);

    /**
     * Método encargado de buscar un empleado por su código
     *
     * @param codigoEmpleado
     * @return
     */
    public Empleados buscarEmpleadoTipo(EntityManager em, BigInteger codigoEmpleado);

    /**
     * Método encargado de buscar los empleado que sean 'ACTIVOS','RETIRADOS' O
     * 'PENSIONADOS' y que se encuentren en la vista vwactualestipostrabajadores
     * de la base de datos.
     *
     * @return Retorna una lista de empleados.
     */
    public List<Empleados> empleadosNovedad(EntityManager em);

    public List<Empleados> empleadosNovedadSoloAlgunos(EntityManager em);

    public int contarEmpleadosNovedad(EntityManager em);

    /**
     * Método encargado de buscar todos los Empleados existentes en la base de
     * datos, ordenados por código.
     *
     * @return Retorna una lista de Empleados ordenados por código.
     */
    public List<Empleados> todosEmpleados(EntityManager em);

    /**
     * Método encargado de buscar los empleado que sean 'ACTIVOS' y que se
     * encuentren en la vista vwactualestipostrabajadores de la base de datos.
     *
     * @return
     */
    public List<Empleados> empleadosVacaciones(EntityManager em);

    /**
     * Método encargado de buscar los empleados en estado 'ACTIVO' y
     * 'PENSIONADO' para que sean adicionados a los parametors de liquidación.
     *
     * @return
     */
    public List<Empleados> lovEmpleadosParametros(EntityManager em);

    public List<Empleados> empleadosAuxilios(EntityManager em);

    /**
     * Método encargado de buscar los empleado que sean 'ACTIVOS' y que se
     * encuentren en la vista vwactualestipostrabajadores de la base de datos.
     *
     * @return Retorna una lista de empleados.
     */
    public List<Empleados> empleadosNovedadEmbargo(EntityManager em);

    public List<Empleados> buscarEmpleadosBusquedaAvanzada(EntityManager em, String queryBusquedaAvanzada);

    public List<BigInteger> buscarEmpleadosBusquedaAvanzadaCodigo(EntityManager em, String queryBusquedaAvanzada);

    public Empleados buscarEmpleadoPorCodigoyEmpresa(EntityManager em, BigDecimal codigo, BigInteger empresa);

    public Empleados obtenerUltimoEmpleadoAlmacenado(EntityManager em, BigInteger secuenciaEmpresa, BigDecimal codigoEmpleado);

    public Empleados buscarEmpleadoSecuenciaPersona(EntityManager em, BigInteger secuencia);

    public List<Empleados> consultarEmpleadosLiquidacionesLog(EntityManager em);

    public List<Empleados> consultarEmpleadosParametroAutoliq(EntityManager em);

    public List<Empleados> consultarEmpleadosParaProyecciones(EntityManager em);

    public void eliminarEmpleadoNominaF(EntityManager em, BigInteger secuenciaEmpleado, BigInteger secuenciaPersona);

    public void reingresarEmpleado(EntityManager em, BigInteger codigoEmpleado, BigInteger centroCosto, Date fechaReingreso, BigInteger empresa, Date fechaFinal);

    public List<Empleados> consultarEmpleadosReingreso(EntityManager em);

    public Date verificarFecha(EntityManager em, BigInteger secuenciaEmpleado);

    public void cambiarFechaIngreso(EntityManager em, BigInteger secuenciaEmpleado, Date fechaAntigua, Date fechaNueva);

    public List<Empleados> consultarEmpleadosCuadrillas(EntityManager em);

    public List<Empleados> buscarEmpleadosATHoraExtra(EntityManager em);

    public List<Empleados> consultarEmpleadosParaAprobarHorasExtras(EntityManager em);

    public List<Empleados> empleadosCesantias(EntityManager em);

    public List<Empleados> consultarCesantiasnoLiquidadas(EntityManager em);

    public List<NovedadesSistema> novedadescesantiasnoliquidadas(EntityManager em, BigInteger secuenciaEmpleado);

    public List<Empleados> empleadosAusentismos(EntityManager em);

    public List<Empleados> empleadosDefinitiva(EntityManager em);
}
