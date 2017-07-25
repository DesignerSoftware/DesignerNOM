/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Estructuras;
import Entidades.EvalEvaluadores;
import Entidades.Evalconvocatorias;
import Entidades.Evalplanillas;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import InterfaceAdministrar.AdministrarNReporteEvalCompetenciaInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaAsociacionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaEvalConvocatoriasInterface;
import InterfacePersistencia.PersistenciaEvalEvaluadoresInterface;
import InterfacePersistencia.PersistenciaEvalPlanillasInterface;
import InterfacePersistencia.PersistenciaGruposConceptosInterface;
import InterfacePersistencia.PersistenciaInforeportesInterface;
import InterfacePersistencia.PersistenciaParametrosReportesInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaTiposAsociacionesInterface;
import InterfacePersistencia.PersistenciaTiposTrabajadoresInterface;
import InterfacePersistencia.PersistenciaUbicacionesGeograficasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarNReporteEvalCompetencia implements AdministrarNReporteEvalCompetenciaInterface {

   private static Logger log = Logger.getLogger(AdministrarNReporteEvalCompetencia.class);

    @EJB
    PersistenciaInforeportesInterface persistenciaInforeportes;
    @EJB
    PersistenciaParametrosReportesInterface persistenciaParametrosReportes;
    @EJB
    PersistenciaActualUsuarioInterface persistenciaActualUsuario;
    @EJB
    PersistenciaEmpresasInterface persistenciaEmpresas;
    @EJB
    PersistenciaGruposConceptosInterface persistenciaGruposConceptos;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    @EJB
    PersistenciaUbicacionesGeograficasInterface persistenciaUbicacionesGeograficas;
    @EJB
    PersistenciaEstructurasInterface persistenciaEstructuras;
    @EJB
    PersistenciaTiposTrabajadoresInterface persistenciaTiposTrabajadores;
    @EJB
    PersistenciaTercerosInterface persistenciaTerceros;
    @EJB
    PersistenciaTiposAsociacionesInterface persistenciaTiposAsociaciones;
    @EJB
    PersistenciaProcesosInterface persistenciaProcesos;
    @EJB
    PersistenciaAsociacionesInterface persistenciaAsociaciones;
    @EJB
    PersistenciaEvalConvocatoriasInterface persistenciaConvocatorias;
    @EJB
    PersistenciaEvalEvaluadoresInterface persistenciaEvaluadores;
    @EJB
    PersistenciaEvalPlanillasInterface persistenciaPlanillas;
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    List<Inforeportes> listInforeportes;
    ParametrosReportes parametroReporte;
    String usuarioActual;
    /////////Lista de valores
    List<Empresas> listEmpresas;
    List<Empleados> listEmpleados;
    List<Estructuras> listEstructuras;
    List<Evalplanillas> listPlanillas;
    List<EvalEvaluadores> listEvaluadores;
    List<Evalconvocatorias> listConvocatorias;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public ParametrosReportes parametrosDeReporte() {
        try {
            if (usuarioActual == null) {
                usuarioActual = persistenciaActualUsuario.actualAliasBD(em);
            }
            if (parametroReporte == null) {
                parametroReporte = persistenciaParametrosReportes.buscarParametroInformeUsuario(em, usuarioActual);
            }
            return parametroReporte;
        } catch (Exception e) {
            log.warn("Error parametrosDeReporte Administrar" + e);
            return null;
        }
    }

    @Override
    public List<Inforeportes> listInforeportesUsuario() {
        try {
            listInforeportes = persistenciaInforeportes.buscarInforeportesUsuarioCapacitacion(em);
            return listInforeportes;
        } catch (Exception e) {
            log.warn("Error listInforeportesUsuario " + e);
            return null;
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
    public List<Empleados> listEmpleados() {
        try {
            listEmpleados = persistenciaEmpleado.buscarEmpleados(em);
            log.warn(this.getClass().getName() + ".listEmpleados() fin.");
            return listEmpleados;
        } catch (Exception e) {
            log.warn(this.getClass().getName() + " error " + e.toString());
            return null;
        }
    }

    @Override
    public List<Estructuras> listEstructuras() {
        try {
            listEstructuras = persistenciaEstructuras.buscarEstructuras(em);
            return listEstructuras;
        } catch (Exception e) {
            log.warn("Error listEstructuras : " + e.toString());
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
    public void guardarCambiosInfoReportes(List<Inforeportes> listaIR) {
        try {
            for (int i = 0; i < listaIR.size(); i++) {
                persistenciaInforeportes.editar(em, listaIR.get(i));
            }
        } catch (Exception e) {
            log.warn("Error guardarCambiosInfoReportes Admi : " + e.toString());
        }
    }

    @Override
    public List<Evalplanillas> listPlanillas() {
        try {
            listPlanillas = persistenciaPlanillas.consultarEvalPlanilla(em);
            return listPlanillas;
        } catch (Exception e) {
            log.warn("Error listConvocatorias Administrar : " + e.toString());
            return null;
        }
    }

    @Override
    public List<EvalEvaluadores> listEvaluadores() {
        try {
            listEvaluadores = persistenciaEvaluadores.buscarEvalEvaluadores(em);
            return listEvaluadores;
        } catch (Exception e) {
            log.warn("Error listConvocatorias Administrar : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Evalconvocatorias> listConvocatorias(BigInteger secEmpleado) {
        try {
            listConvocatorias = persistenciaConvocatorias.consultarEvalConvocatorias(em, secEmpleado);
            return listConvocatorias;
        } catch (Exception e) {
            log.warn("Error listConvocatorias Administrar : " + e.toString());
            return null;
        }

    }

    @Override
    public List<Evalconvocatorias> listConvocatorias() {
        try {
            listConvocatorias = persistenciaConvocatorias.consultarEvalConvocatorias(em);
            return listConvocatorias;
        } catch (Exception e) {
            log.warn("Error listConvocatorias Administrar : " + e.toString());
            return null;
        }

    }

}
