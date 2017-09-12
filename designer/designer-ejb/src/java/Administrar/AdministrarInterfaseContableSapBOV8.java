/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.ActualUsuario;
import Entidades.Empresas;
import Entidades.InterconSapBO;
import Entidades.ParametrosContables;
import Entidades.ParametrosEstructuras;
import Entidades.Procesos;
import Entidades.SolucionesNodos;
import Entidades.UsuariosInterfases;
import Entidades.VWMensajeSAPBOV8;
import InterfaceAdministrar.AdministrarInterfaseContableSapBOV8Interface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaContabilizacionesInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaGeneralesInterface;
import InterfacePersistencia.PersistenciaInterconSapBOInterface;
import InterfacePersistencia.PersistenciaParametrosContablesInterface;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import InterfacePersistencia.PersistenciaSolucionesNodosInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaUsuariosInterfasesInterface;
import InterfacePersistencia.PersistenciaVWActualesFechasInterface;
import InterfacePersistencia.PersistenciaVWMensajeSAPBOV8Interface;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrador
 */
@Stateful
public class AdministrarInterfaseContableSapBOV8 implements AdministrarInterfaseContableSapBOV8Interface {

   private static Logger log = Logger.getLogger(AdministrarInterfaseContableSapBOV8.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaParametrosContablesInterface persistenciaParametrosContables;
   @EJB
   PersistenciaActualUsuarioInterface persistenciaActualUsuario;
   @EJB
   PersistenciaProcesosInterface persistenciaProcesos;
   @EJB
   PersistenciaEmpresasInterface persistenciaEmpresas;
   @EJB
   PersistenciaSolucionesNodosInterface persistenciaSolucionesNodos;
   @EJB
   PersistenciaInterconSapBOInterface persistenciaInterconSap;
   @EJB
   PersistenciaTercerosInterface persistenciaTerceros;
   @EJB
   PersistenciaContabilizacionesInterface persistenciaContabilizaciones;
   @EJB
   PersistenciaParametrosEstructurasInterface persistenciaParametrosEstructuras;
   @EJB
   PersistenciaVWActualesFechasInterface persistenciaVWActualesFechas;
   @EJB
   PersistenciaVWMensajeSAPBOV8Interface persistenciaVWMensajesAPBOV8;
   @EJB
   PersistenciaUsuariosInterfasesInterface persistenciaUsuariosInterfases;
   @EJB
   PersistenciaGeneralesInterface persistenciaGenerales;

   private EntityManagerFactory emf;
   private EntityManager em; private String idSesionBck;

   private EntityManager getEm() {
      try {
         if (this.emf != null) { if (this.em != null) {
            if (this.em.isOpen()) {
               this.em.close();
            }
         }
         } else {
            this.emf = administrarSesiones.obtenerConexionSesionEMF(idSesionBck);
         }
         this.em = emf.createEntityManager();
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " getEm() ERROR : " + e);
      }
      return this.em;
   }

   @Override
   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<ParametrosContables> obtenerParametrosContablesUsuarioBD(String usuarioBD) {
      try {
         List<ParametrosContables> parametro = persistenciaParametrosContables.buscarParametrosContablesUsuarioBD(getEm(), usuarioBD);
         if (parametro != null) {
            for (int i = 0; i < parametro.size(); i++) {
               Empresas empresa = persistenciaEmpresas.consultarEmpresaPorCodigo(getEm(), parametro.get(i).getEmpresaCodigo());
               if (empresa != null) {
                  parametro.get(i).setEmpresaRegistro(empresa);
               }
               if (parametro.get(i).getProceso() == null) {
                  parametro.get(i).setProceso(new Procesos());
               }
            }
         }
         return parametro;
      } catch (Exception e) {
         log.warn("Error obtenerParametrosContablesUsuarioBD Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void modificarParametroContable(ParametrosContables parametro) {
      try {
         if (parametro.getProceso().getSecuencia() == null) {
            parametro.setProceso(null);
         }
         persistenciaParametrosContables.editar(getEm(), parametro);
      } catch (Exception e) {
         log.warn("Error modificarParametroContable Admi : " + e.toString());

      }
   }

   @Override
   public void borrarParametroContable(List<ParametrosContables> listPC) {
      try {
         for (int i = 0; i < listPC.size(); i++) {
            if (listPC.get(i).getProceso().getSecuencia() == null) {
               listPC.get(i).setProceso(null);
            }
            persistenciaParametrosContables.borrar(getEm(), listPC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarParametroContable Admi : " + e.toString());
      }
   }

   @Override
   public void crearParametroContable(ParametrosContables parametro) {
      try {
         if (parametro.getProceso().getSecuencia() == null) {
            parametro.setProceso(null);
         }
         persistenciaParametrosContables.crear(getEm(), parametro);
      } catch (Exception e) {
         log.warn("Error modificarParametroContable Admi : " + e.toString());

      }
   }

   @Override
   public List<SolucionesNodos> obtenerSolucionesNodosParametroContable(Date fechaInicial, Date fechaFinal) {
      try {
         return persistenciaSolucionesNodos.buscarSolucionesNodosParaParametroContable_SAP(getEm(), fechaInicial, fechaFinal);
      } catch (Exception e) {
         log.warn("Error obtenerSolucionesNodosParametroContable Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<InterconSapBO> obtenerInterconSapBOParametroContable(Date fechaInicial, Date fechaFinal) {
      try {
         return persistenciaInterconSap.buscarInterconSAPBOParametroContable(getEm(), fechaInicial, fechaFinal);
      } catch (Exception e) {
         log.warn("Error obtenerInterconSapBOParametroContable Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Procesos> lovProcesos() {
      try {
         return persistenciaProcesos.buscarProcesos(getEm());
      } catch (Exception e) {
         log.warn("Error lovProcesos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Empresas> lovEmpresas() {
      try {
         return persistenciaEmpresas.buscarEmpresas(getEm());
      } catch (Exception e) {
         log.warn("Error lovEmpresas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public ActualUsuario obtenerActualUsuario() {
      try {
         return persistenciaActualUsuario.actualUsuarioBD(getEm());
      } catch (Exception e) {
         log.warn("Error obtenerActualUsuario Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Date obtenerMaxFechaContabilizaciones() {
      try {
         return persistenciaContabilizaciones.obtenerFechaMaximaContabilizacionesSAPBOV8(getEm());
      } catch (Exception e) {
         log.warn("Error obtenerMaxFechaContabilizaciones Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Date obtenerMaxFechaIntercoSapBO() {
      try {
         return persistenciaInterconSap.obtenerFechaMaxInterconSAPBO(getEm());
      } catch (Exception e) {
         log.warn("Error obtenerMaxFechaIntercoSapBO Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public ParametrosEstructuras parametrosLiquidacion() {
      try {
         String usuarioBD = persistenciaActualUsuario.actualAliasBD(getEm());
         return persistenciaParametrosEstructuras.buscarParametro(getEm(), usuarioBD);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".parametrosLiquidacion() ERROR: " + e);
         return null;
      }
   }

   @Override
   public void actualizarFlagProcesoAnularInterfaseContableSAPBOV8(Date fechaIni, Date fechaFin) {
      try {
         persistenciaInterconSap.actualizarFlagProcesoAnularInterfaseContableSAPBOV8(getEm(), fechaIni, fechaFin);
      } catch (Exception e) {
         log.warn("Error actualizarFlagProcesoAnularInterfaseContableSAPBOV8 Admi : " + e.toString());
      }
   }

   @Override
   public Date buscarFechaHastaVWActualesFechas() {
      try {
         return persistenciaVWActualesFechas.actualFechaHasta(getEm());
      } catch (Exception e) {
         log.warn("Error buscarFechaHastaVWActualesFechas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Date buscarFechaDesdeVWActualesFechas() {
      try {
         return persistenciaVWActualesFechas.actualFechaDesde(getEm());
      } catch (Exception e) {
         log.warn("Error buscarFechaDesdeVWActualesFechas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void ejeuctarPKGUbicarnuevointercon_SAPBOV8(BigInteger secuencia, Date fechaIni, Date fechaFin, BigInteger proceso) {
      try {
         persistenciaInterconSap.ejeuctarPKGUbicarnuevointercon_SAPBOV8(getEm(), secuencia, fechaIni, fechaFin, proceso);
      } catch (Exception e) {
         log.warn("Error ejeuctarPKGUbicarnuevointercon_SAPBOV8 Admi : " + e.toString());
      }
   }

   @Override
   public void cambiarFlagInterconContableSAPBOV8(Date fechaIni, Date fechaFin, BigInteger proceso) {
      try {
         persistenciaContabilizaciones.actualizarFlahInterconContableSAPBOV8(getEm(), fechaIni, fechaFin, proceso);
      } catch (Exception e) {
         log.warn("Error cambiarFlagInterconContableSAPBOV8 Admi : " + e.toString());
      }
   }

   @Override
   public void ejecutarDeleteInterconSAP(Date fechaIni, Date fechaFin, BigInteger proceso) {
      try {
         persistenciaInterconSap.ejecutarDeleteInterconSAPBOV8(getEm(), fechaIni, fechaFin, proceso);
      } catch (Exception e) {
         log.warn("Error ejecutarDeleteInterconSAP Admi : " + e.toString());
      }
   }

   @Override
   public void cerrarProcesoLiquidacion(Date fechaIni, Date fechaFin, BigInteger proceso) {
      try {
         persistenciaInterconSap.cerrarProcesoLiquidacion(getEm(), fechaIni, fechaFin, proceso);
      } catch (Exception e) {
         log.warn("Error cerrarProcesoLiquidacion Admi : " + e.toString());
      }
   }

   @Override
   public Integer obtenerContadorFlagGeneradoFechasSAP(Date fechaIni, Date fechaFin) {
      try {
         return persistenciaContabilizaciones.obtenerContadorFlagGeneradoFechasSAP(getEm(), fechaIni, fechaFin);
      } catch (Exception e) {
         log.warn("Error obtenerContadorFlagGeneradoFechasSAP Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void ejecutarPKGRecontabilizacion(Date fechaIni, Date fechaFin) {
      try {
         persistenciaInterconSap.ejecutarPKGRecontabilizacion(getEm(), fechaIni, fechaFin);
      } catch (Exception e) {
         log.warn("Error obtenerContadorFlagGeneradoFechasSAP Admi : " + e.toString());
      }
   }

   @Override
   public List<VWMensajeSAPBOV8> obtenerErroresSAPBOV8() {
      try {
         return persistenciaVWMensajesAPBOV8.buscarListaErroresSAPBOV8(getEm());
      } catch (Exception e) {
         log.warn("Error obtenerErroresSAPBOV8 Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public int contarProcesosContabilizadosInterconSAPBO(Date fechaInicial, Date fechaFinal) {
      try {
         return persistenciaInterconSap.contarProcesosContabilizadosInterconSAPBO(getEm(), fechaInicial, fechaFinal);
      } catch (Exception e) {
         log.warn("Error contarProcesosContabilizadosInterconTotal Admi : " + e.toString());
         return -1;
      }
   }

   @Override
   public String obtenerDescripcionProcesoArchivo(BigInteger proceso) {
      try {
         return persistenciaProcesos.obtenerDescripcionProcesoPorSecuencia(getEm(), proceso);
      } catch (Exception e) {
         log.warn("Error obtenerDescripcionProcesoArchivo Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public String obtenerPathServidorWeb() {
      try {
         return persistenciaGenerales.obtenerPathServidorWeb(getEm());
      } catch (Exception e) {
         log.warn("Error obtenerPathServidorWeb Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public String obtenerPathProceso() {
      try {
         return persistenciaGenerales.obtenerPathProceso(getEm());
      } catch (Exception e) {
         log.warn("Error obtenerPathProceso Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public String ejecutarPKGCrearArchivoPlano(Date fechaIni, Date fechaFin, BigInteger proceso, String descripcionProceso, String nombreArchivo) {
      try {
         return persistenciaInterconSap.ejecutarPKGCrearArchivoPlanoSAPV8(getEm(), fechaIni, fechaFin, proceso, descripcionProceso, nombreArchivo);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".ejecutarPKGCrearArchivoPlano() ERROR: " + e);
         return null;
      }
   }

   public String obtenerEnvioInterfaseContabilidadEmpresa(short codigoEmpresa) {
      try {
         return persistenciaEmpresas.obtenerEnvioInterfaseContabilidadEmpresa(getEm(), codigoEmpresa);
      } catch (Exception e) {
         log.warn("Error obtenerEnvioInterfaseContabilidadEmpresa Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public UsuariosInterfases obtenerUsuarioInterfaseContabilizacion() {
      try {
         return persistenciaUsuariosInterfases.obtenerUsuarioInterfaseContabilidad(getEm());
      } catch (Exception e) {
         log.warn("Error obtenerUsuarioInterfaseContabilizacion Admi : " + e.toString());
         return null;
      }
   }
}
