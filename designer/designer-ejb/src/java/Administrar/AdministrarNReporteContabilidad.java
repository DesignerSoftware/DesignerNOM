/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import Entidades.Procesos;
import InterfaceAdministrar.AdministrarNReporteContabilidadInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaInforeportesInterface;
import InterfacePersistencia.PersistenciaParametrosReportesInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author AndresPineda
 */
@Stateless
public class AdministrarNReporteContabilidad implements AdministrarNReporteContabilidadInterface {

   private static Logger log = Logger.getLogger(AdministrarNReporteContabilidad.class);

   @EJB
   PersistenciaInforeportesInterface persistenciaInforeportes;
   @EJB
   PersistenciaParametrosReportesInterface persistenciaParametrosReportes;
   @EJB
   PersistenciaActualUsuarioInterface persistenciaActualUsuario;
   @EJB
   PersistenciaProcesosInterface persistenciaProcesos;
   @EJB
   PersistenciaEmpresasInterface persistenciaEmpresas;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   List<Inforeportes> listInforeportes;
   ParametrosReportes parametroReporte;
   String usuarioActual;
   List<Procesos> listProcesos;
   List<Empleados> listEmpleados;
   List<Empresas> listEmpresas;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

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
   public ParametrosReportes parametrosDeReporte() {
      try {
         usuarioActual = persistenciaActualUsuario.actualAliasBD(getEm());
         parametroReporte = persistenciaParametrosReportes.buscarParametroInformeUsuario(getEm(), usuarioActual);
         return parametroReporte;
      } catch (Exception e) {
         log.warn("Error parametrosDeReporte Administrar" + e);
         return null;
      }
   }

   @Override
   public List<Inforeportes> listInforeportesUsuario() {
      try {
         listInforeportes = persistenciaInforeportes.buscarInforeportesUsuarioContabilidad(getEm());
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
   public List<Procesos> listProcesos() {
      try {
         listProcesos = persistenciaProcesos.buscarProcesos(getEm());
         return listProcesos;
      } catch (Exception e) {
         log.warn("Error en listProcesos Administrar: " + e.toString());
         return null;
      }
   }

   @Override
   public List<Empleados> listEmpleados() {
      try {
         listEmpleados = persistenciaEmpleado.buscarEmpleadosActivos(getEm());
         return listEmpleados;
      } catch (Exception e) {
         log.warn("Error listEmpleados : " + e.toString());
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

   @Override
   public List<Empresas> listEmpresas() {
      try {
         listEmpresas = persistenciaEmpresas.buscarEmpresas(getEm());
         return listEmpresas;
      } catch (Exception e) {
         log.warn("Error en listProcesos Administrar: " + e.toString());
         return null;
      }
   }
}
