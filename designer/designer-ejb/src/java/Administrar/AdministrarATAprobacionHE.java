package Administrar;

import Entidades.ActualUsuario;
import Entidades.EersCabeceras;
import Entidades.EersDetalles;
import Entidades.EersFlujos;
import Entidades.Empleados;
import Entidades.Estructuras;
import InterfaceAdministrar.AdministrarATAprobacionHEInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaEersCabecerasInterface;
import InterfacePersistencia.PersistenciaEersDetallesInterface;
import InterfacePersistencia.PersistenciaEersFlujosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import java.math.BigInteger;
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
public class AdministrarATAprobacionHE implements AdministrarATAprobacionHEInterface {

   private static Logger log = Logger.getLogger(AdministrarATAprobacionHE.class);

   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   @EJB
   PersistenciaEstructurasInterface persistenciaEstructuras;
   @EJB
   PersistenciaEersCabecerasInterface persistenciaEersCabeceras;
   @EJB
   PersistenciaEersDetallesInterface persistenciaEersDetalles;
   @EJB
   PersistenciaEersFlujosInterface persistenciaEersFlujos;
   @EJB
   PersistenciaActualUsuarioInterface persistenciaActualUsuario;
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

   //--------------------------------------------------------------------------
   //MÉTODOS
   //--------------------------------------------------------------------------
   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<EersCabeceras> obtenerTotalesEersCabeceras() {
      try {
         List<EersCabeceras> lista = persistenciaEersCabeceras.buscarEersCabecerasTotales(getEm());
         return lista;
      } catch (Exception e) {
         log.warn("Error obtenerTotalesEersCabeceras Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<EersCabeceras> obtenerEersCabecerasPorEmpleado(BigInteger secuencia) {
      try {
         List<EersCabeceras> lista = persistenciaEersCabeceras.buscarEersCabecerasTotalesPorEmpleado(getEm(), secuencia);
         return lista;
      } catch (Exception e) {
         log.warn("Error obtenerEersCabecerasPorEmpleado Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearEersCabeceras(List<EersCabeceras> listaEC) {
      try {
         for (int i = 0; i < listaEC.size(); i++) {
            persistenciaEersCabeceras.crear(getEm(), listaEC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearEersCabeceras Admi : " + e.toString());
      }
   }

   @Override
   public void editarEersCabeceras(List<EersCabeceras> listaEC) {
      try {
         for (int i = 0; i < listaEC.size(); i++) {
            persistenciaEersCabeceras.editar(getEm(), listaEC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarEersCabeceras Admi : " + e.toString());
      }
   }

   @Override
   public void borrarEersCabeceras(List<EersCabeceras> listaEC) {
      try {
         for (int i = 0; i < listaEC.size(); i++) {
            persistenciaEersCabeceras.borrar(getEm(), listaEC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarEersCabeceras Admi : " + e.toString());
      }
   }

   @Override
   public List<EersDetalles> obtenerDetallesEersCabecera(BigInteger secuencia) {
      try {
         List<EersDetalles> lista = persistenciaEersDetalles.buscarEersDetallesPorEersCabecera(getEm(), secuencia);
         return lista;
      } catch (Exception e) {
         log.warn("Error obtenerDetallesEersCabecera Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<EersFlujos> obtenerFlujosEersCabecera(BigInteger secuencia) {
      try {
         List<EersFlujos> lista = persistenciaEersFlujos.buscarEersFlujosPorEersCabecera(getEm(), secuencia);
         return lista;
      } catch (Exception e) {
         log.warn("Error obtenerFlujosEersCabecera Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Estructuras> lovEstructuras(BigInteger secuenciaEstado) {
      try {
         List<Estructuras> lista = persistenciaEstructuras.consultarEstructurasEersCabeceras(getEm(), secuenciaEstado);
         return lista;
      } catch (Exception e) {
         log.warn("Error lovEstructuras Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Empleados> lovEmpleados() {
      try {
         List<Empleados> lista = persistenciaEmpleado.buscarEmpleadosActivosPensionados(getEm());
         return lista;
      } catch (Exception e) {
         log.warn("Error lovEmpleados Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public ActualUsuario obtenerActualUsuarioSistema() {
      try {
         ActualUsuario usuario = persistenciaActualUsuario.actualUsuarioBD(getEm());
         return usuario;
      } catch (Exception e) {
         log.warn("Error obtenerActualUsuarioSistema Admi : " + e.toString());
         return null;
      }
   }

}
