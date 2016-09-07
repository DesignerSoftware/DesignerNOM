/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.CentrosCostos;
import Entidades.Comprobantes;
import Entidades.CortesProcesos;
import Entidades.Cuentas;
import Entidades.DetallesFormulas;
import Entidades.Empleados;
import Entidades.Procesos;
import Entidades.SolucionesNodos;
import Entidades.Terceros;
import InterfaceAdministrar.AdministrarEmplComprobantesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCentrosCostosInterface;
import InterfacePersistencia.PersistenciaComprobantesInterface;
import InterfacePersistencia.PersistenciaCortesProcesosInterface;
import InterfacePersistencia.PersistenciaCuentasInterface;
import InterfacePersistencia.PersistenciaDetallesFormulasInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaHistoriasformulasInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import InterfacePersistencia.PersistenciaSolucionesNodosInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'EmplComprobantes'.
 *
 * @author betelgeuse
 */
@Stateful
public class AdministrarEmplComprobantes implements AdministrarEmplComprobantesInterface {

    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaComprobantes'.
     */
    @EJB
    PersistenciaComprobantesInterface persistenciaComprobantes;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaCortesProcesos'.
     */
    @EJB
    PersistenciaCortesProcesosInterface persistenciaCortesProcesos;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaSolucionesNodos'.
     */
    @EJB
    PersistenciaSolucionesNodosInterface persistenciaSolucionesNodos;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaEmpleado'.
     */
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaProcesos'.
     */
    @EJB
    PersistenciaProcesosInterface persistenciaProcesos;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaTerceros'.
     */
    @EJB
    PersistenciaTercerosInterface persistenciaTerceros;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaDetallesFormulas'.
     */
    @EJB
    PersistenciaDetallesFormulasInterface persistenciaDetallesFormulas;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaHistoriasformulas'.
     */
    @EJB
    PersistenciaHistoriasformulasInterface persistenciaHistoriasformulas;
    @EJB
    PersistenciaCuentasInterface persistenciaCuentas;
    @EJB
    PersistenciaCentrosCostosInterface persistenciaCentrosCostos;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    //--------------------------------------------------------------------------
    //MÉTODOS
    //--------------------------------------------------------------------------
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public Empleados consultarEmpleado(BigInteger secuencia) {
        Empleados empleado;
        try {
            empleado = persistenciaEmpleado.buscarEmpleadoSecuencia(em, secuencia);
            return empleado;
        } catch (Exception e) {
            empleado = null;
            return empleado;
        }
    }

    @Override
    public BigInteger consultarMaximoNumeroComprobante() {
        return persistenciaComprobantes.numeroMaximoComprobante(em);
    }

    @Override
    public List<Comprobantes> consultarComprobantesEmpleado(BigInteger secuenciaEmpleado) {
        return persistenciaComprobantes.comprobantesEmpleado(em, secuenciaEmpleado);
    }

    @Override
    public List<CortesProcesos> consultarCortesProcesosComprobante(BigInteger secuenciaComprobante) {
        return persistenciaCortesProcesos.cortesProcesosComprobante(em, secuenciaComprobante);
    }

    @Override
    public List<SolucionesNodos> consultarSolucionesNodosEmpleado(BigInteger secuenciaCorteProceso, BigInteger secuenciaEmpleado) {
        return persistenciaSolucionesNodos.solucionNodoCorteProcesoEmpleado(em, secuenciaCorteProceso, secuenciaEmpleado);
    }

    @Override
    public List<SolucionesNodos> consultarSolucionesNodosEmpleador(BigInteger secuenciaCorteProceso, BigInteger secuenciaEmpleado) {
        return persistenciaSolucionesNodos.solucionNodoCorteProcesoEmpleador(em, secuenciaCorteProceso, secuenciaEmpleado);
    }

    @Override
    public List<Procesos> consultarLOVProcesos() {
        return persistenciaProcesos.lovProcesos(em);
    }

    @Override
    public List<Terceros> consultarLOVTerceros(BigInteger secEmpresa) {
        return persistenciaTerceros.lovTerceros(em, secEmpresa);
    }

    @Override
    public void modificarComprobantes(List<Comprobantes> listComprobantes) {
        for (int i = 0; i < listComprobantes.size(); i++) {
            System.out.println("Modificando Comprobantes...");
            persistenciaComprobantes.editar(em, listComprobantes.get(i));
        }

    }

    @Override
    public void borrarComprobantes(Comprobantes comprobante) {
        try {
            persistenciaComprobantes.borrar(em, comprobante);
        } catch (Exception e) {
            System.out.println("Error borrarComprobantes" + e);
        }
    }

    @Override
    public void crearComprobante(Comprobantes comprobantes) {
        persistenciaComprobantes.crear(em, comprobantes);
    }

    @Override
    public void modificarCortesProcesos(List<CortesProcesos> listaCortesProcesos) {
        for (int i = 0; i < listaCortesProcesos.size(); i++) {
            System.out.println("Modificando Cortes procesos...");
            if (listaCortesProcesos.get(i).getProceso().getSecuencia() == null) {
                listaCortesProcesos.get(i).setProceso(null);
                persistenciaCortesProcesos.editar(em, listaCortesProcesos.get(i));
            } else {
                persistenciaCortesProcesos.editar(em, listaCortesProcesos.get(i));
            }
        }
    }

    @Override
    public void borrarCortesProcesos(CortesProcesos corteProceso) {
        try {
            persistenciaCortesProcesos.borrar(em, corteProceso);
        } catch (Exception e) {
            System.out.println("Error borrarCortesProcesos" + e);
        }
    }

    @Override
    public void crearCorteProceso(CortesProcesos corteProceso) {
        persistenciaCortesProcesos.crear(em, corteProceso);
    }

    @Override
    public void modificarSolucionesNodosEmpleado(List<SolucionesNodos> listaSolucionesNodosEmpleado) {
        for (int i = 0; i < listaSolucionesNodosEmpleado.size(); i++) {
            System.out.println("Modificando Soluciones Nodo Empleado...");
            if (listaSolucionesNodosEmpleado.get(i).getNittercero() == null) {
                listaSolucionesNodosEmpleado.get(i).setNittercero(null);
                persistenciaSolucionesNodos.editar(em, listaSolucionesNodosEmpleado.get(i));
            } else {
                persistenciaSolucionesNodos.editar(em, listaSolucionesNodosEmpleado.get(i));
            }
        }
    }

    @Override
    public List<DetallesFormulas> consultarDetallesFormula(BigInteger secEmpleado, String fechaDesde, String fechaHasta, BigInteger secProceso, BigInteger secHistoriaFormula) {
        return persistenciaDetallesFormulas.detallesFormula(em, secEmpleado, fechaDesde, fechaHasta, secProceso, secHistoriaFormula);
    }

    @Override
    public BigInteger consultarHistoriaFormula(BigInteger secFormula, String fechaDesde) {
        return persistenciaHistoriasformulas.obtenerSecuenciaHistoriaFormula(em, secFormula, fechaDesde);
    }

    @Override
    public List<Cuentas> lovCuentas() {
        return persistenciaCuentas.buscarCuentas(em);
    }

    @Override
    public List<CentrosCostos> lovCentrosCostos() {
        return persistenciaCentrosCostos.buscarCentrosCostos(em);
    }
    
    @Override
    public void eliminarCPconUndoCierre(BigInteger secProceso, BigInteger secEmpleado, Date fechaCorte){
        try {
            System.out.println("Entro en eliminarCPconUndoCierre()");
            persistenciaCortesProcesos.eliminarCPconUndoCierre(em, secProceso, secEmpleado, fechaCorte);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + " Error eliminarCPconUndoCierre : " + e);
        }
    }
}
