/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.ActualUsuario;
import Entidades.Empresas;
import Entidades.ParametrosContables;
import Entidades.ParametrosEstructuras;
import Entidades.Procesos;
import Entidades.VWContabilidadDetallada;
import Entidades.VWContabilidadResumida1;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarVWContabilidadResumida1Interface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaContabilizacionesInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaInterconSapBOInterface;
import InterfacePersistencia.PersistenciaParametrosContablesInterface;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import InterfacePersistencia.PersistenciaVWContabilidadResumida1Interface;
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
public class AdministrarVWContabilidadResumida implements AdministrarVWContabilidadResumida1Interface {

   private static Logger log = Logger.getLogger(AdministrarVWContabilidadResumida.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaVWContabilidadResumida1Interface persistenciaContabilidadResumida;
   @EJB
   PersistenciaParametrosEstructurasInterface persistenciaParametrosEstructuras;
   @EJB
   PersistenciaEmpresasInterface persistenciaEmpresas;
   @EJB
   PersistenciaProcesosInterface persistenciaProcesos;
   @EJB
   PersistenciaParametrosContablesInterface persistenciaParametrosContables;
   @EJB
   PersistenciaActualUsuarioInterface persistenciaActualUsuario;
   @EJB
   PersistenciaContabilizacionesInterface persistenciaContabilizaciones;
   @EJB
   PersistenciaInterconSapBOInterface persistenciaInterconSapBO;
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
   public List<VWContabilidadResumida1> obtenerContabilidadResumida(Date FechaIni, Date FechaFin, BigInteger secProceso) {
      try {
         return persistenciaContabilidadResumida.buscarContabilidadResumidaParametroContable(getEm(), FechaIni, FechaFin, secProceso);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
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
   public List<Procesos> lovProcesos() {
      try {
         return persistenciaProcesos.buscarProcesos(getEm());
      } catch (Exception e) {
         log.warn("Error lovProcesos Admi : " + e.toString());
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
   public List<Empresas> lovEmpresas() {
      try {
         return persistenciaEmpresas.buscarEmpresas(getEm());
      } catch (Exception e) {
         log.warn("Error lovEmpresas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Date obtenerFechaMaxContabilizaciones() {
      try {
         return persistenciaContabilizaciones.obtenerFechaMaximaContabilizaciones(getEm());
      } catch (Exception e) {
         log.warn("Error obtenerFechaMaxContabilizaciones Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Date obtenerFechaMaxInterconSapBO() {
      try {
         return persistenciaInterconSapBO.obtenerFechaMaxInterconSAPBO(getEm());
      } catch (Exception e) {
         log.warn("Error obtenerFechaMaxInterconTotal Admi : " + e.toString());
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
   public List<VWContabilidadDetallada> obtenerContabilidadDetallada(Date FechaIni, Date FechaFin, BigInteger secProceso) {
      try {
         return persistenciaContabilidadResumida.buscarContabilidadDetalladaParametroContable(getEm(), FechaIni, FechaFin, secProceso);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
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
   public Integer abrirPeriodoContable(Date FechaIni, Date FechaFin, BigInteger secProceso) {
      try {
         return persistenciaContabilidadResumida.abrirPeriodoContable(getEm(), FechaIni, FechaFin, secProceso);
      } catch (Exception e) {
         log.warn("Error en abrirPeriodoContable Admi : " + e.getMessage());
         return null;
      }
   }

   @Override
   public void actualizarPeriodoContable(Date FechaIni, Date FechaFin, BigInteger secProceso) {
      try {
         persistenciaContabilidadResumida.actualizarPeriodoContable(getEm(), FechaIni, FechaFin, secProceso);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

}
