/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.CentrosCostos;
import Entidades.Comprobantes;
import Entidades.CortesProcesos;
import Entidades.Cuentas;
import Entidades.DetallesFormulas;
import Entidades.Empleados;
import Entidades.Procesos;
import Entidades.SolucionesNodos;
import Entidades.Terceros;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author Administrator
 */
public interface AdministrarEmplComprobantesInterface {

    /**
     * Método encargado de recuperar un Empleado dada su secuencia.
     *
     * @param secEmpleado Secuencia de la Empleado.
     * @return Retorna el Empleado cuya secuencia coincida con el valor del
     * parámetro.
     */
    public Empleados consultarEmpleado(BigInteger secEmpleado);

    /**
     * Método encargado de recuperar el máximo número entre los comprobantes.
     *
     * @return Retorna el número mayor entre los números de los comprobantes.
     */
    public BigInteger consultarMaximoNumeroComprobante();

    /**
     * Método encargado de recuperar los Comprobantes de un Empleado.
     *
     * @param secEmpleado Secuencia de la Empleado.
     * @return Retorna una lista de Comprobantes.
     */
    public List<Comprobantes> consultarComprobantesEmpleado(BigInteger secEmpleado);

    /**
     * Método encargado de recuperar los CortesProcesos según el Comprobante que
     * tengan asociado.
     *
     * @param secComprobante Secuencia de la Comprobante por la cual se filtrara
     * la búsqueda.
     * @return Retorna una lista de CortesProcesos.
     */
    public List<CortesProcesos> consultarCortesProcesosComprobante(BigInteger secComprobante);

    public List<SolucionesNodos> consultarSolucionesNodosEmpleado(BigInteger secCorteProceso, BigInteger secEmpleado);

    public List<SolucionesNodos> consultarSolucionesNodosEmpleador(BigInteger secCorteProceso, BigInteger secEmpleado);

    public List<Procesos> consultarLOVProcesos();

    public void modificarComprobantes(List<Comprobantes> listComprobantes);

    public void borrarComprobantes(Comprobantes comprobante);

    public void crearComprobante(Comprobantes comprobantes);

    public void modificarCortesProcesos(List<CortesProcesos> listaCortesProcesos);

    public void borrarCortesProcesos(CortesProcesos corteProceso);

    public void crearCorteProceso(CortesProcesos corteProceso);

    public List<Terceros> consultarLOVTerceros(BigInteger secEmpresa);

    public void modificarSolucionesNodosEmpleado(List<SolucionesNodos> listaSolucionesNodosEmpleado);

    public List<DetallesFormulas> consultarDetallesFormula(BigInteger secEmpleado, String fechaDesde, String fechaHasta, BigInteger secProceso, BigInteger secHistoriaFormula);

    public BigInteger consultarHistoriaFormula(BigInteger secFormula, String fechaDesde);

    /**
     * Método encargado de obtener el Entity Manager el cual tiene asociado la
     * sesion del usuario que utiliza el aplicativo.
     *
     * @param idSesion Identificador se la sesion.
     */
    public void obtenerConexion(String idSesion);

    public List<Cuentas> lovCuentas();

    public List<CentrosCostos> lovCentrosCostos();
}
