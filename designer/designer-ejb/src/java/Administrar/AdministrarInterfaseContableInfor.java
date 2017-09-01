/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.ActualUsuario;
import Entidades.Empresas;
import Entidades.InterconInfor;
import Entidades.ParametrosContables;
import Entidades.ParametrosEstructuras;
import Entidades.Procesos;
import Entidades.SolucionesNodos;
import Entidades.UsuariosInterfases;
import InterfaceAdministrar.AdministrarInterfaseContableInforInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaContabilizacionesInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaGeneralesInterface;
import InterfacePersistencia.PersistenciaInterconInforInterface;
import InterfacePersistencia.PersistenciaParametrosContablesInterface;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import InterfacePersistencia.PersistenciaSolucionesNodosInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaUsuariosInterfasesInterface;
import InterfacePersistencia.PersistenciaVWActualesFechasInterface;
import InterfacePersistencia.PersistenciaVWMensajeSAPBOV8Interface;
import excepciones.ExcepcionBD;
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
 * @author user
 */
@Stateful
public class AdministrarInterfaseContableInfor implements AdministrarInterfaseContableInforInterface {

   private static Logger log = Logger.getLogger(AdministrarInterfaseContableInfor.class);

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
   PersistenciaInterconInforInterface persistenciaInterconInfor;
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
   private EntityManager em;
   private String idSesionBck;

   private EntityManager getEm() {
      try {
         if (this.emf != null) {
            if (this.em != null) {
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
   public void obtenerConexion(String idSesion) {
      idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<ParametrosContables> obtenerParametrosContablesUsuarioBD(String usuarioBD) {
      try {
         List<ParametrosContables> listaParametros = persistenciaParametrosContables.buscarParametrosContablesUsuarioBD(getEm(), usuarioBD);
         int n = 0;
         if (listaParametros == null) {
            n++;
         } else if (listaParametros.isEmpty()) {
            n++;
         }

         if (n > 0) {
            Empresas empresaAux = persistenciaEmpresas.consultarPrimeraEmpresaSinPaquete(getEm());
            ParametrosContables parametro = new ParametrosContables();
            parametro.setSecuencia(BigInteger.ONE);
            parametro.setEmpresaCodigo(empresaAux.getCodigo());
            parametro.setEmpresaRegistro(empresaAux);
            parametro.setUsuario(usuarioBD);
            parametro.setFechafinalcontabilizacion(new Date());
            parametro.setFechainicialcontabilizacion(new Date());
            parametro.setArchivo("NOMINA");
            listaParametros.add(parametro);
         }

         if (listaParametros != null) {
            if (!listaParametros.isEmpty()) {
               for (int i = 0; i < listaParametros.size(); i++) {
                  Empresas empresa = null;
                  if (listaParametros.get(i).getEmpresaRegistro() == null) {
                     empresa = persistenciaEmpresas.consultarEmpresaPorCodigo(getEm(), listaParametros.get(i).getEmpresaCodigo());
                  }
                  if (empresa != null) {
                     listaParametros.get(i).setEmpresaRegistro(empresa);
                  }
                  if (listaParametros.get(i).getProceso() == null) {
                     listaParametros.get(i).setProceso(new Procesos());
                  }
               }
            }
         }
         return listaParametros;
      } catch (Exception e) {
         log.warn("Error obtenerParametroContableUsuarioBD Admi : " + e.toString());
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
         return persistenciaSolucionesNodos.buscarSolucionesNodosParaParametroContable(getEm(), fechaInicial, fechaFinal);
      } catch (Exception e) {
         log.warn("Error obtenerSolucionesNodosParametroContable Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<InterconInfor> obtenerInterconInforParametroContable(Date fechaInicial, Date fechaFinal) {
      try {
         return persistenciaInterconInfor.buscarInterconInforParametroContable(getEm(), fechaInicial, fechaFinal);
      } catch (Exception e) {
         log.warn("Error obtenerInterconInforParametroContable Admi : " + e.toString());
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
         return persistenciaContabilizaciones.obtenerFechaMaximaContabilizaciones(getEm());
      } catch (Exception e) {
         log.warn("Error obtenerMaxFechaContabilizaciones Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Date obtenerMaxFechaInterconInfor() {
      try {
         return persistenciaInterconInfor.obtenerFechaMaxInterconInfor(getEm());
      } catch (Exception e) {
         log.warn("Error obtenerMaxFechaInterconInfor Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public ParametrosEstructuras parametrosLiquidacion() {
      try {
         String usuarioBD = persistenciaActualUsuario.actualAliasBD(getEm());
         return persistenciaParametrosEstructuras.buscarParametro(getEm(), usuarioBD);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void actualizarFlagInterconInfor(Date fechaInicial, Date fechaFinal, Short empresa) {
      try {
         persistenciaInterconInfor.actualizarFlagInterconInfor(getEm(), fechaInicial, fechaFinal, empresa);
      } catch (Exception e) {
         log.warn("Error actualizarFlagInterconInfor Admi : " + e.toString());
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
   public void ejecutarPKGUbicarnuevointercon_Infor(BigInteger secuencia, Date fechaIni, Date fechaFin, BigInteger proceso) {
      try {
         persistenciaInterconInfor.ejecutarPKGUbicarNewInterCon_Infor(getEm(), secuencia, fechaIni, fechaFin, proceso);
      } catch (Exception e) {
         log.warn("Error ejecutarPKGUbicarnuevointercon_Infor Admi : " + e.toString());
      }
   }

   @Override
   public void actualizarFlagInterconInforProcesoDeshacer(Date fechaInicial, Date fechaFinal, BigInteger proceso) {
      try {
         persistenciaInterconInfor.actualizarFlagInterconInforProcesoDeshacer(getEm(), fechaInicial, fechaFinal, proceso);
      } catch (Exception e) {
         log.warn("Error actualizarFlagInterconInforProcesoDeshacer Admi : " + e.toString());
      }
   }

   @Override
   public void eliminarInterconInfor(Date fechaInicial, Date fechaFinal, Short empresa, BigInteger proceso) {
      try {
         persistenciaInterconInfor.eliminarInterconInfor(getEm(), fechaInicial, fechaFinal, empresa, proceso);
      } catch (Exception e) {
         log.warn("Error eliminarInterconInfor Admi : " + e.toString());
      }
   }

   @Override
   public int contarProcesosContabilizadosInterconInfor(Date fechaInicial, Date fechaFinal) {
      try {
         return persistenciaInterconInfor.contarProcesosContabilizadosInterconInfor(getEm(), fechaInicial, fechaFinal);
      } catch (Exception e) {
         log.warn("Error contarProcesosContabilizadosInterconInfor Admi : " + e.toString());
         return -1;
      }
   }

   @Override
   public void cerrarProcesoContabilizacion(Date fechaIni, Date fechaFin, Short empresa, BigInteger proceso) {
      try {
         persistenciaInterconInfor.cerrarProcesoContabilizacion(getEm(), fechaIni, fechaFin, empresa, proceso);
      } catch (Exception e) {
         log.warn("Error cerrarProcesoContabilizacion Admi : " + e.toString());
      }
   }

   @Override
   public Integer obtenerContadorFlagGeneradoFechasInfor(Date fechaIni, Date fechaFin) {
      try {
         return persistenciaContabilizaciones.obtenerContadorFlagGeneradoFechasTotal(getEm(), fechaIni, fechaFin);
      } catch (Exception e) {
         log.warn("Error obtenerContadorFlagGeneradoFechasInfor Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void ejecutarPKGRecontabilizacion(Date fechaIni, Date fechaFin) throws ExcepcionBD {
      try {
         persistenciaInterconInfor.ejecutarPKGRecontabilizacion(getEm(), fechaIni, fechaFin);
      } catch (ExcepcionBD ebd) {
         log.warn("Error ejecutarPKGRecontabilizacion Admi ebd : " + ebd.toString());
         throw ebd;
      } catch (Exception e) {
         log.warn("Error ejecutarPKGRecontabilizacion Admi e : " + e.toString());
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
   public String ejecutarPKGCrearArchivoPlano(Date fechaIni, Date fechaFin, BigInteger CodigoEmpresa, BigInteger proceso, String nombreArchivo) {
      try {
         return persistenciaInterconInfor.ejecutarPKGCrearArchivoPlanoInfor(getEm(), fechaIni, fechaFin, CodigoEmpresa, proceso, nombreArchivo);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
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

   @Override
   public void borrarRegistroGenerado(List<SolucionesNodos> listBorrar) {
      try {
         for (int i = 0; i < listBorrar.size(); i++) {
            persistenciaSolucionesNodos.borrar(getEm(), listBorrar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarRegistroIntercon(List<InterconInfor> listBorrar) {
      try {
         for (int i = 0; i < listBorrar.size(); i++) {
            persistenciaInterconInfor.borrar(getEm(), listBorrar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

}
