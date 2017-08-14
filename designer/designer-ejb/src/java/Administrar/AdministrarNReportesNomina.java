/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Asociaciones;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Estructuras;
import Entidades.GruposConceptos;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import Entidades.Procesos;
import Entidades.Terceros;
import Entidades.TiposAsociaciones;
import Entidades.TiposTrabajadores;
import Entidades.UbicacionesGeograficas;
import InterfaceAdministrar.AdministrarNReportesNominaInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaAsociacionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
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
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author AndresPineda
 */
@Stateful
public class AdministrarNReportesNomina implements AdministrarNReportesNominaInterface {

   private static Logger log = Logger.getLogger(AdministrarNReportesNomina.class);

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

   List<Inforeportes> listInforeportes;
   ParametrosReportes parametroReporte;
   String usuarioActual;
   /////////Lista de valores
   List<Empresas> listEmpresas;
   List<GruposConceptos> listGruposConceptos;
   List<Empleados> listEmpleados;
   List<UbicacionesGeograficas> listUbicacionesGeograficas;
   List<TiposAsociaciones> listTiposAsociaciones;
   List<Estructuras> listEstructuras;
   List<TiposTrabajadores> listTiposTrabajadores;
   List<Terceros> listTerceros;
   List<Procesos> listProcesos;
   List<Asociaciones> listAsociaciones;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

   private EntityManagerFactory emf;
   private EntityManager em;

   private EntityManager getEm() {
      try {
         if (this.em != null) {
            if (this.em.isOpen()) {
               this.em.close();
            }
         }
         this.em = emf.createEntityManager();
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " getEm() ERROR : " + e);
      }
      return this.em;
   }

   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public ParametrosReportes parametrosDeReporte() {
      try {
         if (usuarioActual == null) {
            usuarioActual = persistenciaActualUsuario.actualAliasBD(getEm());
         }
         if (parametroReporte == null) {
            parametroReporte = persistenciaParametrosReportes.buscarParametroInformeUsuario(getEm(), usuarioActual);
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
         listInforeportes = persistenciaInforeportes.buscarInforeportesUsuarioNomina(getEm());
         return listInforeportes;
      } catch (Exception e) {
         log.warn("Error listInforeportesUsuario " + e);
         return null;
      }
   }

   @Override
   public void modificarParametrosReportes(ParametrosReportes parametroInforme) {
      try {
         persistenciaParametrosReportes.editar(getEm(), parametroInforme);
      } catch (Exception e) {
         log.warn("Error modificarParametrosReportes : " + e.toString());
      }
   }

   @Override
   public List<Empresas> listEmpresas() {
      try {
         listEmpresas = persistenciaEmpresas.consultarEmpresas(getEm());
         return listEmpresas;
      } catch (Exception e) {
         log.warn("Error listEmpresas Administrar : " + e.toString());
         return null;
      }
   }

   @Override
   public List<GruposConceptos> listGruposConcetos() {
      try {
         listGruposConceptos = persistenciaGruposConceptos.buscarGruposConceptos(getEm());
         return listGruposConceptos;
      } catch (Exception e) {
         log.warn("Error listGruposConcetos : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Empleados> listEmpleados() {
      log.warn(this.getClass().getName() + ".listEmpleados()");
      try {
         listEmpleados = persistenciaEmpleado.buscarEmpleados(getEm());
         log.warn(this.getClass().getName() + ".listEmpleados() fin.");
         return listEmpleados;
      } catch (Exception e) {
         log.warn(this.getClass().getName() + " error " + e.toString());
         return null;
      }
   }

   @Override
   public List<UbicacionesGeograficas> listUbicacionesGeograficas() {
      try {
         listUbicacionesGeograficas = persistenciaUbicacionesGeograficas.consultarUbicacionesGeograficas(getEm());
         return listUbicacionesGeograficas;
      } catch (Exception e) {
         log.warn("Error listUbicacionesGeograficas : " + e.toString());
         return null;
      }
   }

   @Override
   public List<TiposAsociaciones> listTiposAsociaciones() {
      try {
         listTiposAsociaciones = persistenciaTiposAsociaciones.buscarTiposAsociaciones(getEm());
         return listTiposAsociaciones;
      } catch (Exception e) {
         log.warn("Error listTiposAsociaciones : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Estructuras> listEstructuras() {
      try {
         listEstructuras = persistenciaEstructuras.buscarEstructuras(getEm());
         return listEstructuras;
      } catch (Exception e) {
         log.warn("Error listEstructuras : " + e.toString());
         return null;
      }
   }

   @Override
   public List<TiposTrabajadores> listTiposTrabajadores() {
      try {
         listTiposTrabajadores = persistenciaTiposTrabajadores.buscarTiposTrabajadores(getEm());
         return listTiposTrabajadores;
      } catch (Exception e) {
         log.warn("Error listTiposTrabajadores : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Terceros> listTercerosSecEmpresa(BigInteger secEmpresa) {
      try {
         //listTerceros = persistenciaTerceros.buscarTerceros(getEm());
         listTerceros = persistenciaTerceros.lovTerceros(getEm(), secEmpresa);
         return listTerceros;
      } catch (Exception e) {
         log.warn("Error listTerceros : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Terceros> listTerceros() {
      try {
         listTerceros = persistenciaTerceros.todosTerceros(getEm());
         return listTerceros;
      } catch (Exception e) {
         log.warn("Error listTerceros : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Procesos> listProcesos() {
      try {
         listProcesos = persistenciaProcesos.buscarProcesos(getEm());
         return listProcesos;
      } catch (Exception e) {
         log.warn("Error listProcesos : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Asociaciones> listAsociaciones() {
      try {
         listAsociaciones = persistenciaAsociaciones.buscarAsociaciones(getEm());
         return listAsociaciones;
      } catch (Exception e) {
         log.warn("Error listAsociaciones : " + e.toString());
         return null;
      }
   }

   @Override
   public void guardarCambiosInfoReportes(List<Inforeportes> listaIR) {
      try {
         for (int i = 0; i < listaIR.size(); i++) {
            persistenciaInforeportes.editar(getEm(), listaIR.get(i));
         }
      } catch (Exception e) {
         log.warn("Error guardarCambiosInfoReportes Admi : " + e.toString());
      }
   }

}
