/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.CentrosCostos;
import Entidades.Empresas;
import Entidades.TiposCentrosCostos;
import InterfaceAdministrar.AdministrarCentroCostosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCentrosCostosInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaTiposCentrosCostosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'CentroCostos'.
 *
 * @author betelgeuse
 */
@Stateful
@LocalBean
public class AdministrarCentroCostos implements AdministrarCentroCostosInterface {

    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaCentrosCostos'.
     */
    @EJB
    PersistenciaCentrosCostosInterface persistenciaCentrosCostos;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaTiposCentrosCostos'.
     */
    @EJB
    PersistenciaTiposCentrosCostosInterface persistenciaTiposCentrosCostos;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaEmpresas'.
     */
    @EJB
    PersistenciaEmpresasInterface persistenciaEmpresas;
    
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;
	

    //-------------------------------------------------------------------------------------
    
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }
    @Override
    public List<Empresas> buscarEmpresas() {
        try {
            List<Empresas> listaEmpresas = persistenciaEmpresas.consultarEmpresas(em);
            return listaEmpresas;
        } catch (Exception e) {
            System.out.println("AdministrarCentroCostos: Falló al buscar las empresas /n" + e.getMessage());
            return null;
        }
    }

    @Override
    public void modificarCentroCostos(List<CentrosCostos> listaCentrosCostos) {
        try {
            for (int i = 0; i < listaCentrosCostos.size(); i++) {
                System.out.println("Modificando...");
                persistenciaCentrosCostos.editar(em,listaCentrosCostos.get(i));
            }
        } catch (Exception e) {
            System.err.println("AdministrarCentrosCostos: Falló al editar el CentroCosto /n" + e.getMessage());
        }
    }

    @Override
    public void borrarCentroCostos(List<CentrosCostos> listaCentrosCostos) {
        System.out.println("ENTRE A AdministrarCentroCostos.borrarCentroCostos ");
        try {
            for (int i = 0; i < listaCentrosCostos.size(); i++) {
                System.out.println("Borrando...");
                persistenciaCentrosCostos.borrar(em,listaCentrosCostos.get(i));
            }
        } catch (Exception e) {
            System.err.println("ERROR AdministrarCentroCostos.borrarCentroCostos ERROR=====" + e.getMessage());
        }
    }

    @Override
    public void crearCentroCostos(List<CentrosCostos> listaCentrosCostos) {
        System.out.println("ENTRE A AdministrarCentroCostos.crearCentroCostos ");
        try {
            for (int i = 0; i < listaCentrosCostos.size(); i++) {
                System.out.println("Creando...");
                persistenciaCentrosCostos.crear(em,listaCentrosCostos.get(i));
            }
        } catch (Exception e) {
            System.err.println("ERROR AdministrarCentroCostos.crearCentroCostos ERROR======" + e.getMessage());
        }
    }

    @Override
    public List<CentrosCostos> consultarCentrosCostosPorEmpresa(BigInteger secEmpresa) {
        try {
            System.out.println("ENTRE A AdministrarCentroCostos.buscarCentrosCostosPorEmpresa ");
            List<CentrosCostos> listaCentrosCostos = persistenciaCentrosCostos.buscarCentrosCostosEmpr(em,secEmpresa);
            return listaCentrosCostos;
        } catch (Exception e) {
            System.out.println("Error en Administrar CentrosCostos (centrosCostosPorEmpresa)");
            return null;
        }
    }

    @Override
    public List<TiposCentrosCostos> lovTiposCentrosCostos() {
        try {
            List<TiposCentrosCostos> listaTiposCentrosCostos = persistenciaTiposCentrosCostos.buscarTiposCentrosCostos(em);
            return listaTiposCentrosCostos;
        } catch (Exception e) {
            System.out.println("\n AdministrarCentroCostos error en buscarTiposCentroCostos \n" + e.getMessage());
            return null;
        }
    }

    @Override
    public BigInteger contadorComprobantesContables(BigInteger secCentroCosto) {
        BigInteger contadorComprobantesContables;
        try {
            contadorComprobantesContables = persistenciaCentrosCostos.contadorComprobantesContables(em,secCentroCosto);
            return contadorComprobantesContables;
        } catch (Exception e) {
            System.out.println("ERROR administrarCentrosCostos.contadorComprobantesContables ERROR===" + e.getMessage());
            return null;
        }
    }

    @Override
    public BigInteger contadorDetallesCCConsolidador(BigInteger secCentroCosto) {

        try {
            BigInteger contadorDetallesCCConsolidador = persistenciaCentrosCostos.contadorDetallesCCConsolidador(em,secCentroCosto);
            return contadorDetallesCCConsolidador;
        } catch (Exception e) {
            System.out.println("ERROR administrarCentrosCostos.contadorDetallesCCConsolidador ERROR===" + e.getMessage());
            return null;
        }
    }

    @Override
    public BigInteger contadorDetalleContable(BigInteger secCentroCosto) {

        try {
            BigInteger contadorDetallesCCDetalle = persistenciaCentrosCostos.contadorDetallesCCDetalle(em,secCentroCosto);
            return contadorDetallesCCDetalle;

        } catch (Exception e) {
            System.out.println("ERROR administrarCentrosCostos.contadorDetalleContable ERROR===" + e.getMessage());

            return null;
        }
    }

    @Override
    public BigInteger contadorEmpresas(BigInteger secCentroCosto) {

        try {
            BigInteger contadorEmpresasV;
            return contadorEmpresasV = persistenciaCentrosCostos.contadorEmpresas(em,secCentroCosto);
        } catch (Exception e) {
            System.out.println("ERROR administrarCentrosCostos.contadorEmpresas ERROR===" + e.getMessage());

            return null;
        }
    }

    @Override
    public BigInteger contadorEstructuras(BigInteger secCentroCosto) {

        try {
            BigInteger contadorEstructuras;
            return contadorEstructuras = persistenciaCentrosCostos.contadorEstructuras(em,secCentroCosto);
        } catch (Exception e) {
            System.out.println("ERROR administrarCentrosCostos.contadorEstructuras ERROR===" + e.getMessage());

            return null;
        }
    }

    @Override
    public BigInteger contadorInterConCondor(BigInteger secCentroCosto) {

        try {
            BigInteger contadorInterconCondor;
            return contadorInterconCondor = persistenciaCentrosCostos.contadorInterconCondor(em,secCentroCosto);
        } catch (Exception e) {
            System.out.println("ERROR administrarCentrosCostos.contadorInterconCondor ERROR===" + e.getMessage());
            return null;
        }
    }

    @Override
    public BigInteger contadorInterConDynamics(BigInteger secCentroCosto) {

        try {
            BigInteger contadorInterconDynamics;
            return contadorInterconDynamics = persistenciaCentrosCostos.contadorInterconDynamics(em,secCentroCosto);
        } catch (Exception e) {
            System.out.println("ERROR administrarCentrosCostos.contadorInterconDynamics ERROR===" + e.getMessage());

            return null;
        }
    }

    @Override
    public BigInteger contadorInterConGeneral(BigInteger secCentroCosto) {

        try {
            BigInteger contadorInterconGeneral;

            return contadorInterconGeneral = persistenciaCentrosCostos.contadorInterconGeneral(em,secCentroCosto);
        } catch (Exception e) {
            System.out.println("ERROR administrarCentrosCostos.contadorInterconGeneral ERROR===" + e.getMessage());

            return null;
        }
    }

    @Override
    public BigInteger contadorInterConHelisa(BigInteger secCentroCosto) {

        try {
            BigInteger contadorInterconHelisa;
            return contadorInterconHelisa = persistenciaCentrosCostos.contadorInterconHelisa(em,secCentroCosto);
        } catch (Exception e) {
            System.out.println("ERROR administrarCentrosCostos.contadorInterconHelisa ERROR===" + e.getMessage());

            return null;
        }
    }

    @Override
    public BigInteger contadorInterConSapbo(BigInteger secCentroCosto) {

        try {
            BigInteger contadorInterconSapbo;
            return contadorInterconSapbo = persistenciaCentrosCostos.contadorInterconSapbo(em,secCentroCosto);
        } catch (Exception e) {
            System.out.println("ERROR administrarCentrosCostos.contadorInterconSapbo ERROR===" + e.getMessage());
            return null;
        }
    }

    @Override
    public BigInteger contadorInterConSiigo(BigInteger secCentroCosto) {

        try {
            BigInteger contadorInterconSiigo;

            return contadorInterconSiigo = persistenciaCentrosCostos.contadorInterconSiigo(em,secCentroCosto);
        } catch (Exception e) {
            System.out.println("ERROR administrarCentrosCostos.contadorInterconSiigo ERROR===" + e.getMessage());
            return null;
        }
    }

    @Override
    public BigInteger contadorInterConTotal(BigInteger secCentroCosto) {

        try {
            BigInteger contadorInterconTotal;

            return contadorInterconTotal = persistenciaCentrosCostos.contadorInterconTotal(em,secCentroCosto);
        } catch (Exception e) {
            System.out.println("ERROR administrarCentrosCostos.contadorInterconTotal ERROR===" + e.getMessage());
            return null;
        }
    }

    @Override
    public BigInteger contadorNovedadesD(BigInteger secCentroCosto) {

        try {
            BigInteger contadorNovedadesD;

            return contadorNovedadesD = persistenciaCentrosCostos.contadorNovedadesD(em,secCentroCosto);
        } catch (Exception e) {
            System.out.println("ERROR administrarCentrosCostos.contadorNovedadesD ERROR===" + e.getMessage());
            return null;
        }
    }

    @Override
    public BigInteger contadorNovedadesC(BigInteger secCentroCosto) {

        try {
            BigInteger contadorNovedadesC;

            return contadorNovedadesC = persistenciaCentrosCostos.contadorNovedadesC(em,secCentroCosto);
        } catch (Exception e) {
            System.out.println("ERROR administrarCentrosCostos.contadorNovedadesC ERROR===" + e.getMessage());
            return null;
        }
    }

    @Override
    public BigInteger contadorProcesosProductivos(BigInteger secCentroCosto) {

        try {
            BigInteger contadorProcesosProductivos;
            return contadorProcesosProductivos = persistenciaCentrosCostos.contadorProcesosProductivos(em,secCentroCosto);
        } catch (Exception e) {
            System.out.println("ERROR administrarCentrosCostos.contadorProcesosProductivos ERROR===" + e.getMessage());
            return null;
        }
    }

    @Override
    public BigInteger contadorProyecciones(BigInteger secCentroCosto) {

        try {
            BigInteger contadorProyecciones;
            return contadorProyecciones = persistenciaCentrosCostos.contadorProyecciones(em,secCentroCosto);
        } catch (Exception e) {
            System.out.println("ERROR administrarCentrosCostos.contadorProyecciones ERROR===" + e.getMessage());
            return null;
        }
    }

    @Override
    public BigInteger contadorSolucionesNodosC(BigInteger secCentroCosto) {

        try {
            BigInteger contadorSolucionesNodosC;
            return contadorSolucionesNodosC = persistenciaCentrosCostos.contadorSolucionesNodosC(em,secCentroCosto);
        } catch (Exception e) {
            System.out.println("ERROR administrarCentrosCostos.contadorSolucionesNodosC ERROR===" + e.getMessage());

            return null;
        }
    }

    @Override
    public BigInteger contadorSolucionesNodosD(BigInteger secCentroCosto) {

        try {
            BigInteger contadorSolucionesNodosD;
            return contadorSolucionesNodosD = persistenciaCentrosCostos.contadorSolucionesNodosD(em,secCentroCosto);
        } catch (Exception e) {
            System.out.println("ERROR administrarCentrosCostos.contadorSolucionesNodosD ERROR===" + e.getMessage());
            return null;
        }
    }

    @Override
    public BigInteger contadorSoPanoramas(BigInteger secCentroCosto) {

        try {
            BigInteger contadorSoPanoramas;
            return contadorSoPanoramas = persistenciaCentrosCostos.contadorSoPanoramas(em,secCentroCosto);
        } catch (Exception e) {
            System.out.println("ERROR administrarCentrosCostos.contadorSoPanoramas ERROR===" + e.getMessage());
            return null;
        }
    }

    @Override
    public BigInteger contadorTerceros(BigInteger secCentroCosto) {

        try {
            BigInteger contadorTerceros;
            return contadorTerceros = persistenciaCentrosCostos.contadorTerceros(em,secCentroCosto);
        } catch (Exception e) {
            System.out.println("ERROR administrarCentrosCostos.contadorTerceros ERROR===" + e.getMessage());
            return null;
        }
    }

    @Override
    public BigInteger contadorUnidadesRegistradas(BigInteger secCentroCosto) {

        try {
            BigInteger contadorUnidadesRegistradas;
            return contadorUnidadesRegistradas = persistenciaCentrosCostos.contadorUnidadesRegistradas(em,secCentroCosto);
        } catch (Exception e) {
            System.out.println("ERROR administrarCentrosCostos.contadorUnidadesRegistradas ERROR===" + e.getMessage());
            return null;
        }
    }

    @Override
    public BigInteger contadorVigenciasCuentasC(BigInteger secCentroCosto) {

        try {
            BigInteger contadorVigenciasCuentasC;
            return contadorVigenciasCuentasC = persistenciaCentrosCostos.contadorVigenciasCuentasC(em,secCentroCosto);
        } catch (Exception e) {
            System.out.println("ERROR administrarCentrosCostos.contadorVigenciasCuentasC ERROR===" + e.getMessage());
            return null;
        }
    }

    @Override
    public BigInteger contadorVigenciasCuentasD(BigInteger secCentroCosto) {

        try {
            BigInteger contadorVigenciasCuentasD;
            return contadorVigenciasCuentasD = persistenciaCentrosCostos.contadorVigenciasCuentasD(em,secCentroCosto);
        } catch (Exception e) {
            System.out.println("ERROR administrarCentrosCostos.contadorVigenciasCuentasD ERROR===" + e.getMessage());
            return null;
        }
    }

    @Override
    public BigInteger contadorVigenciasProrrateos(BigInteger secCentroCosto) {

        try {
            BigInteger contadorVigenciasProrrateos;
            return contadorVigenciasProrrateos = persistenciaCentrosCostos.contadorVigenciasProrrateos(em,secCentroCosto);
        } catch (Exception e) {
            System.out.println("ERROR administrarCentrosCostos.contadorVigenciasProrrateos ERROR===" + e.getMessage());
            return null;
        }
    }



}
