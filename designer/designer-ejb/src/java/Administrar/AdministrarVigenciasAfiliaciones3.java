package Administrar;

import Entidades.Empleados;
import Entidades.EstadosAfiliaciones;
import Entidades.Terceros;
import Entidades.TercerosSucursales;
import Entidades.TiposEntidades;
import Entidades.VigenciasAfiliaciones;
import InterfaceAdministrar.AdministrarVigenciasAfiliaciones3Interface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEstadosAfiliacionesInterface;
import InterfacePersistencia.PersistenciaSolucionesNodosInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaTercerosSucursalesInterface;
import InterfacePersistencia.PersistenciaTiposEntidadesInterface;
import InterfacePersistencia.PersistenciaVigenciasAfiliacionesInterface;
import InterfacePersistencia.PersistenciaVigenciasTiposContratosInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateless
public class AdministrarVigenciasAfiliaciones3 implements AdministrarVigenciasAfiliaciones3Interface {

    @EJB
    PersistenciaVigenciasAfiliacionesInterface persistenciaVigenciasAfilicaciones;
    @EJB
    PersistenciaTiposEntidadesInterface persistenciaTiposEntidades;
    @EJB
    PersistenciaTercerosInterface persistenciaTerceros;
    @EJB
    PersistenciaEstadosAfiliacionesInterface persistenciaEstadosAfiliaciones;
    @EJB
    PersistenciaTercerosSucursalesInterface persistenciaTercerosSucursales;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    @EJB
    PersistenciaSolucionesNodosInterface persistenciaSolucionesNodos;
    @EJB
    PersistenciaVigenciasTiposContratosInterface persistenciaVigenciasTiposContratos;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    //
    List<VigenciasAfiliaciones> listVigenciasAfiliaciones;
    List<Terceros> listTercetos;
    List<EstadosAfiliaciones> listEstadosAfiliaciones;
    List<TiposEntidades> listTiposEntidades;
    List<TercerosSucursales> listTercerosSucursales;
    Empleados empleado;
    Date fechaContratacion;
    private EntityManager em;
	
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void crearVigenciaAfiliacion(VigenciasAfiliaciones vigencia) {
        try {
            System.out.println("AdministrarVigenciasAfiliaciones3.crearVigenciaAfiliacion");
            System.out.println("Secuencia empleado: "+ vigencia.getEmpleado());
            persistenciaVigenciasAfilicaciones.crear(em, vigencia);
        } catch (Exception e) {
            System.out.println("Error crearVigenciaAfiliacion Admi : " + e.toString());
        }
    }

    @Override
    public void editarVigenciaAfiliacion(VigenciasAfiliaciones vigencia) {
        try {
            persistenciaVigenciasAfilicaciones.editar(em, vigencia);
        } catch (Exception e) {
            System.out.println("Error editarVigenciaAfiliacion Admi : " + e.toString());
        }
    }

    @Override
    public void borrarVigenciaAfiliacion(VigenciasAfiliaciones vigencia) {
        try {
            persistenciaVigenciasAfilicaciones.borrar(em, vigencia);
        } catch (Exception e) {
            System.out.println("Error borrarVigenciaAfiliacion Admi : " + e.toString());
        }
    }

    @Override
    public List<VigenciasAfiliaciones> listVigenciasAfiliacionesEmpleado(BigInteger secuencia) {
        try {
            listVigenciasAfiliaciones = persistenciaVigenciasAfilicaciones.buscarVigenciasAfiliacionesEmpleado(em, secuencia);
            return listVigenciasAfiliaciones;
        } catch (Exception e) {
            System.out.println("Error listVigenciasAfiliacionesEmpleado Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public VigenciasAfiliaciones vigenciaAfiliacionSecuencia(BigInteger secuencia) {
        try {
            VigenciasAfiliaciones retorno = persistenciaVigenciasAfilicaciones.buscarVigenciasAfiliacionesSecuencia(em, secuencia);
            return retorno;
        } catch (Exception e) {
            System.out.println("Error vigenciaAfiliacionSecuencia Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Terceros> listTerceros() {
        try {
            listTercetos = persistenciaTerceros.buscarTerceros(em);
            return listTercetos;
        } catch (Exception e) {
            System.out.println("Error listTerceros Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<EstadosAfiliaciones> listEstadosAfiliaciones() {
        try {
            listEstadosAfiliaciones = persistenciaEstadosAfiliaciones.buscarEstadosAfiliaciones(em);
            return listEstadosAfiliaciones;
        } catch (Exception e) {
            System.out.println("Error listEstadosAfiliaciones Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<TiposEntidades> listTiposEntidades() {
        try {
            listTiposEntidades = persistenciaTiposEntidades.buscarTiposEntidades(em);
            return listTiposEntidades;
        } catch (Exception e) {
            System.out.println("Error listTiposEntidades Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<TercerosSucursales> listTercerosSucursales() {
        try {
            listTercerosSucursales = persistenciaTercerosSucursales.buscarTercerosSucursales(em);
            return listTercerosSucursales;
        } catch (Exception e) {
            System.out.println("Error listTercerosSucursales Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Empleados obtenerEmpleado(BigInteger secuencia) {
        try {
            empleado = persistenciaEmpleado.buscarEmpleado(em, secuencia);
            System.out.println("AdministrarVigenciasAfiliaciones.obtenerEmpleado: " + empleado);
            return empleado;
        } catch (Exception e) {
            System.out.println("Error obtenerEmpleado Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Long validacionTercerosSurcursalesNuevaVigencia(BigInteger secuencia, Date fechaInicial, BigInteger secuenciaTE, BigInteger secuenciaTer) {
        try {
            Long result = persistenciaSolucionesNodos.validacionTercerosVigenciaAfiliacion(em, secuencia, fechaInicial, secuenciaTE, secuenciaTer);
            return result;
        } catch (Exception e) {
            System.out.println("Error validacionTercerosSurcursales Admi : " + e.toString());
            return null;
        }
    }
    
    
    @Override
    public Date fechaContratacion(Empleados empleado) {
        try {
            fechaContratacion = persistenciaVigenciasTiposContratos.fechaMaxContratacion(em, empleado);
            return fechaContratacion;
        } catch (Exception e) {
            System.out.println("Error fechaContratacion Admi : " + e.toString());
            return null;
        }
    }
}
