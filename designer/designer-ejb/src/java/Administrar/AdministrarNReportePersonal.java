package Administrar;

import Entidades.Aficiones;
import Entidades.Ciudades;
import Entidades.Deportes;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.EstadosCiviles;
import Entidades.Estructuras;
import Entidades.Idiomas;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import Entidades.TiposTelefonos;
import Entidades.TiposTrabajadores;
import InterfaceAdministrar.AdministrarNReportePersonalInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaAficionesInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaDeportesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaEstadosCivilesInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaIdiomasInterface;
import InterfacePersistencia.PersistenciaInforeportesInterface;
import InterfacePersistencia.PersistenciaParametrosReportesInterface;
import InterfacePersistencia.PersistenciaTiposTelefonosInterface;
import InterfacePersistencia.PersistenciaTiposTrabajadoresInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author AndresPineda
 */
@Stateless
public class AdministrarNReportePersonal implements AdministrarNReportePersonalInterface {

   private static Logger log = Logger.getLogger(AdministrarNReportePersonal.class);

    @EJB
    PersistenciaInforeportesInterface persistenciaInforeportes;
    @EJB
    PersistenciaParametrosReportesInterface persistenciaParametrosReportes;
    @EJB
    PersistenciaActualUsuarioInterface persistenciaActualUsuario;
    ///
    @EJB
    PersistenciaEmpresasInterface persistenciaEmpresas;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    @EJB
    PersistenciaEstructurasInterface persistenciaEstructuras;
    @EJB
    PersistenciaTiposTrabajadoresInterface persistenciaTiposTrabajadores;
    @EJB
    PersistenciaEstadosCivilesInterface persistenciaEstadosCiviles;
    @EJB
    PersistenciaTiposTelefonosInterface persistenciaTiposTelefonos;
    @EJB
    PersistenciaCiudadesInterface persistenciaCiudades;
    @EJB
    PersistenciaDeportesInterface persistenciaDeportes;
    @EJB
    PersistenciaAficionesInterface persistenciaAficiones;
    @EJB
    PersistenciaIdiomasInterface persistenciaIdiomas;

    //
    List<Inforeportes> listInforeportes;
    ParametrosReportes parametroReporte;
    String usuarioActual;
    //
    List<EstadosCiviles> listEstadosCiviles;
    List<TiposTelefonos> listTiposTelefonos;
    List<Empleados> listEmpleados;
    List<Estructuras> listEstructuras;
    List<TiposTrabajadores> listTiposTrabajadores;
    List<Ciudades> listCiudades;
    List<Deportes> listDeportes;
    List<Aficiones> listAficiones;
    List<Idiomas> listIdiomas;
    List<Empresas> listEmpresas;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public ParametrosReportes parametrosDeReporte() {
        try {
            usuarioActual = persistenciaActualUsuario.actualAliasBD(em);
            parametroReporte = persistenciaParametrosReportes.buscarParametroInformeUsuario(em, usuarioActual);
            return parametroReporte;
        } catch (Exception e) {
            log.warn("Error parametrosDeReporte Administrar" + e);
            return null;
        }
    }

    @Override
    public List<Inforeportes> listInforeportesUsuario() {
        try {
            listInforeportes = persistenciaInforeportes.buscarInforeportesUsuarioPersonal(em);
            return listInforeportes;
        } catch (Exception e) {
            log.warn("Error listInforeportesUsuario " + e);
            return null;
        }
    }

    @Override
    public void modificarParametrosReportes(ParametrosReportes parametroInforme) {
        try {
            persistenciaParametrosReportes.editar(em, parametroInforme);
        } catch (Exception e) {
            log.warn("Error modificarParametrosReportes : " + e.toString());
        }
    }

    @Override
    public List<Empresas> listEmpresas() {
        try {
            listEmpresas = persistenciaEmpresas.consultarEmpresas(em);
            return listEmpresas;
        } catch (Exception e) {
            log.warn("Error listEmpresas Administrar : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Idiomas> listIdiomas() {
        try {
            listIdiomas = persistenciaIdiomas.buscarIdiomas(em);
            return listIdiomas;
        } catch (Exception e) {
            log.warn("Error listIdiomas Administrar : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Aficiones> listAficiones() {
        try {
            listAficiones = persistenciaAficiones.buscarAficiones(em);
            return listAficiones;
        } catch (Exception e) {
            log.warn("Error listAficiones Administrar : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Deportes> listDeportes() {
        try {
            listDeportes = persistenciaDeportes.buscarDeportes(em);
            return listDeportes;
        } catch (Exception e) {
            log.warn("Error listDeportes Administrar : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Ciudades> listCiudades() {
        try {
            listCiudades = persistenciaCiudades.consultarCiudades(em);
            return listCiudades;
        } catch (Exception e) {
            log.warn("Error listCiudades Administrar : " + e.toString());
            return null;
        }
    }

    @Override
    public List<TiposTrabajadores> listTiposTrabajadores() {
        try {
            listTiposTrabajadores = persistenciaTiposTrabajadores.buscarTiposTrabajadores(em);
            return listTiposTrabajadores;
        } catch (Exception e) {
            log.warn("Error listTiposTrabajadores Administrar : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Estructuras> listEstructuras() {
        try {
            listEstructuras = persistenciaEstructuras.buscarEstructuras(em);
            return listEstructuras;
        } catch (Exception e) {
            log.warn("Error listEstructuras Administrar : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Empleados> listEmpleados() {
        try {
            listEmpleados = persistenciaEmpleado.buscarEmpleados(em);
            return listEmpleados;
        } catch (Exception e) {
            log.warn("Error listEmpleados Administrar : " + e.toString());
            return null;
        }
    }

    @Override
    public List<TiposTelefonos> listTiposTelefonos() {
        try {
            listTiposTelefonos = persistenciaTiposTelefonos.tiposTelefonos(em);
            return listTiposTelefonos;
        } catch (Exception e) {
            log.warn("Error listTiposTelefonos Administrar : " + e.toString());
            return null;
        }
    }

    @Override
    public List<EstadosCiviles> listEstadosCiviles() {
        try {
            listEstadosCiviles = persistenciaEstadosCiviles.consultarEstadosCiviles(em);
            return listEstadosCiviles;
        } catch (Exception e) {
            log.warn("Error listEstadosCiviles Administrar : " + e.toString());
            return null;
        }
    }

    @Override
    public void guardarCambiosInfoReportes(List<Inforeportes> listaIR) {
        log.warn("Administrar.AdministrarNReportePersonal.guardarCambiosInfoReportes().listaIR: " + listaIR);
        try {
            for (int i = 0; i < listaIR.size(); i++) {
                log.warn("AdministrarNReportePersonal.Tipo Reporte: " + listaIR.get(i).getTipo());
                persistenciaInforeportes.editar(em, listaIR.get(i));
            }
            log.warn("Sali try AdministrarNReportePersonal.guardarCambiosInfoReportes()");
        } catch (Exception e) {
            log.warn("Error guardarCambiosInfoReportes Admi : " + e.toString());
        }
    }
}
