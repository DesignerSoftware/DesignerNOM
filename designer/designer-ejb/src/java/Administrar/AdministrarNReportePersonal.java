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
import javax.persistence.EntityManagerFactory;
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
   String usuarioActual;
   //
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
         return persistenciaParametrosReportes.buscarParametroInformeUsuario(getEm(), usuarioActual);
      } catch (Exception e) {
         log.warn("Error parametrosDeReporte Administrar" + e);
         return null;
      }
   }

   @Override
   public List<Inforeportes> listInforeportesUsuario() {
      try {
         return persistenciaInforeportes.buscarInforeportesUsuarioPersonal(getEm());
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
         return persistenciaEmpresas.consultarEmpresas(getEm());
      } catch (Exception e) {
         log.warn("Error listEmpresas Administrar : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Idiomas> listIdiomas() {
      try {
         return persistenciaIdiomas.buscarIdiomas(getEm());
      } catch (Exception e) {
         log.warn("Error listIdiomas Administrar : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Aficiones> listAficiones() {
      try {
         return persistenciaAficiones.buscarAficiones(getEm());
      } catch (Exception e) {
         log.warn("Error listAficiones Administrar : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Deportes> listDeportes() {
      try {
         return persistenciaDeportes.buscarDeportes(getEm());
      } catch (Exception e) {
         log.warn("Error listDeportes Administrar : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Ciudades> listCiudades() {
      try {
         return persistenciaCiudades.consultarCiudades(getEm());
      } catch (Exception e) {
         log.warn("Error listCiudades Administrar : " + e.toString());
         return null;
      }
   }

   @Override
   public List<TiposTrabajadores> listTiposTrabajadores() {
      try {
         return persistenciaTiposTrabajadores.buscarTiposTrabajadores(getEm());
      } catch (Exception e) {
         log.warn("Error listTiposTrabajadores Administrar : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Estructuras> listEstructuras() {
      try {
         return persistenciaEstructuras.buscarEstructuras(getEm());
      } catch (Exception e) {
         log.warn("Error listEstructuras Administrar : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Empleados> listEmpleados() {
      try {
         return persistenciaEmpleado.buscarEmpleadosActivos(getEm());
      } catch (Exception e) {
         log.warn("Error listEmpleados Administrar : " + e.toString());
         return null;
      }
   }

   @Override
   public List<TiposTelefonos> listTiposTelefonos() {
      try {
         return persistenciaTiposTelefonos.tiposTelefonos(getEm());
      } catch (Exception e) {
         log.warn("Error listTiposTelefonos Administrar : " + e.toString());
         return null;
      }
   }

   @Override
   public List<EstadosCiviles> listEstadosCiviles() {
      try {
         return persistenciaEstadosCiviles.consultarEstadosCiviles(getEm());
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
            persistenciaInforeportes.editar(getEm(), listaIR.get(i));
         }
         log.warn("Sali try AdministrarNReportePersonal.guardarCambiosInfoReportes()");
      } catch (Exception e) {
         log.warn("Error guardarCambiosInfoReportes Admi : " + e.toString());
      }
   }
}
