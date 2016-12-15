package Administrar;

import Entidades.CambiosMasivos;
import Entidades.Parametros;
import Entidades.ParametrosCambiosMasivos;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaParametrosInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import InterfaceAdministrar.AdministrarCambiosMasivosInterface;
//import Entidades.CentrosCostos;
//import Entidades.Clasesausentismos;
//import Entidades.Conceptos;
//import Entidades.Empleados;
//import Entidades.Estructuras;
//import Entidades.Formulas;
//import Entidades.Ibcs;
//import Entidades.MotivosCambiosSueldos;
//import Entidades.MotivosDefinitivas;
//import Entidades.MotivosRetiros;
//import Entidades.Papeles;
//import Entidades.Periodicidades;
//import Entidades.Terceros;
//import Entidades.TercerosSucursales;
//import Entidades.TiposEntidades;
//import Entidades.TiposSueldos;
//import Entidades.Tiposausentismos;
//import Entidades.Unidades;

@Stateful
public class AdministrarCambiosMasivos implements AdministrarCambiosMasivosInterface {

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaParametrosInterface persistenciaParametros;
   private EntityManager em;
   @EJB
   PersistenciaActualUsuarioInterface persistenciaActualUsuario;

//   private String usuarioBD = " ";
   @Override
   public void obtenerConexion(String idSesion) {
      System.out.println("Administrar.AdministrarCambiosMasivos.obtenerConexion()");
      em = administrarSesiones.obtenerConexionSesion(idSesion);
   }

   /**
    *
    * @return
    */
   @Override
   public List<Parametros> consultarEmpleadosParametros() {
      System.out.println("Administrar.AdministrarCambiosMasivos.consultarEmpleadosParametros()");
      try {
//         if (usuarioBD.equals(" ")) {
         String usuarioBD = persistenciaActualUsuario.actualAliasBD(em);
//         }
         return persistenciaParametros.empleadosParametros(em, usuarioBD);
      } catch (Exception e) {
         System.out.println("ERROR Administrar.AdministrarCambiosMasivos.consultarEmpleadosParametros()");
         System.out.println("ERROR : " + e);
         return null;
      }
   }

   @Override
   public List<CambiosMasivos> consultarUltimosCambiosMasivos() {
      System.out.println("Administrar.AdministrarCambiosMasivos.consultarUltimosCambiosMasivos()");
      try {
         return persistenciaParametros.consultarCambiosMasivos(em);
      } catch (Exception e) {
         System.out.println("ERROR Administrar.AdministrarCambiosMasivos.consultarUltimosCambiosMasivos()");
         System.out.println("ERROR : " + e);
         return null;
      }
   }

   @Override
   public ParametrosCambiosMasivos consultarParametrosCambiosMasivos() {
      System.out.println("Administrar.AdministrarCambiosMasivos.consultarParametrosCambiosMasivos()");
      try {
//         if (usuarioBD.equals(" ")) {
         String usuarioBD = persistenciaActualUsuario.actualAliasBD(em);
//         }
         return persistenciaParametros.consultarParametroCambiosMasivos(em, usuarioBD);
      } catch (Exception e) {
         System.out.println("ERROR Administrar.AdministrarCambiosMasivos.consultarParametrosCambiosMasivos()");
         System.out.println("ERROR : " + e);
         return null;
      }
   }
}
//
//   @Override
//   public List<Estructuras> consultarLovCargos_Extructuras() {
//      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//   }
//
//   @Override
//   public List<MotivosDefinitivas> consultarLovMotivosDefinitivas() {
//      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//   }
//
//   @Override
//   public List<MotivosRetiros> consultarLovMotivosRetiros() {
//      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//   }
//
//   @Override
//   public List<TiposEntidades> consultarLovTiposEntidades() {
//      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//   }
//
//   @Override
//   public List<TercerosSucursales> consultarLovTercerosSucursales() {
//      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//   }
//
//   @Override
//   public List<CentrosCostos> consultarLovCentrosCostos() {
//      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//   }
//
//   @Override
//   public List<Periodicidades> consultarLovPeriodicidades() {
//      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//   }
//
//   @Override
//   public List<Conceptos> consultarLovConceptos() {
//      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//   }
//
//   @Override
//   public List<Formulas> consultarLovFormulas() {
//      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//   }
//
//   @Override
//   public List<Terceros> consultarLovTerceros() {
//      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//   }
//
//   @Override
//   public List<MotivosCambiosSueldos> consultarLovMotivosCambiosSueldos() {
//      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//   }
//
//   @Override
//   public List<TiposSueldos> consultarLovTiposSueldos() {
//      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//   }
//
//   @Override
//   public List<Tiposausentismos> consultarLovTiposausentismos() {
//      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//   }
//
//   @Override
//   public List<Unidades> consultarLovUnidades() {
//      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//   }
//
//   @Override
//   public List<Empleados> consultarLovEmpleados() {
//      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//   }
//
//   @Override
//   public List<Clasesausentismos> consultarLovExtructuras() {
//      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//   }
//
//   @Override
//   public List<Ibcs> consultarLovIbcs() {
//      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//   }
//
//   @Override
//   public List<Papeles> consultarLovPapeles() {
//      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//   }
