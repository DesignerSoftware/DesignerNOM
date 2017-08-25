/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Estructuras;
import Entidades.Inforeportes;
import Entidades.ParametrosReportes;
import InterfaceAdministrar.AdministrarNReporteCapacitacionInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaInforeportesInterface;
import InterfacePersistencia.PersistenciaParametrosReportesInterface;
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
public class AdministrarNReporteCapacitacion implements AdministrarNReporteCapacitacionInterface {

   private static Logger log = Logger.getLogger(AdministrarNReporteCapacitacion.class);

   @EJB
   PersistenciaInforeportesInterface persistenciaInforeportes;
   @EJB
   PersistenciaParametrosReportesInterface persistenciaParametrosReportes;
   @EJB
   PersistenciaActualUsuarioInterface persistenciaActualUsuario;
   @EJB
   PersistenciaEmpresasInterface persistenciaEmpresas;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   @EJB
   PersistenciaEstructurasInterface persistenciaEstructuras;
   @EJB
   AdministrarSesionesInterface administrarSesiones;

   List<Inforeportes> listInforeportes;
   ParametrosReportes parametroReporte;
   String usuarioActual;
   /////////Lista de valores
   List<Empresas> listEmpresas;
   List<Empleados> listEmpleados;
   List<Estructuras> listEstructuras;

   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
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
   public List<Empleados> listEmpleados() {
      try {
         listEmpleados = persistenciaEmpleado.buscarEmpleadosActivos(getEm());
         log.warn(this.getClass().getName() + ".listEmpleados() fin.");
         return listEmpleados;
      } catch (Exception e) {
         log.warn(this.getClass().getName() + " error " + e.toString());
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
   public List<Estructuras> listEstructuras() {
      try {
         listEstructuras = persistenciaEstructuras.buscarEstructuras(getEm());
         return listEstructuras;
      } catch (Exception e) {
         log.warn("Error listEstructuras : " + e.toString());
         return null;
      }
   }
}
