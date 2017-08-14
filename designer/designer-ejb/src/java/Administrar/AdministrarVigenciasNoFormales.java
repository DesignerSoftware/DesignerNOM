/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.AdiestramientosNF;
import Entidades.Cursos;
import Entidades.Empleados;
import Entidades.Instituciones;
import Entidades.Personas;
import Entidades.VigenciasNoFormales;
import InterfaceAdministrar.AdministrarVigenciasNoFormalesInterface;
import InterfacePersistencia.PersistenciaAdiestramientosNFInterface;
import InterfacePersistencia.PersistenciaCursosInterface;
import InterfacePersistencia.PersistenciaInstitucionesInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import InterfacePersistencia.PersistenciaVigenciasNoFormalesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

@Stateful
public class AdministrarVigenciasNoFormales implements AdministrarVigenciasNoFormalesInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciasNoFormales.class);

   @EJB
   PersistenciaVigenciasNoFormalesInterface persistenciaVigenciasNoFormales;
   @EJB
   PersistenciaPersonasInterface persistenciaPersonas;
   @EJB
   PersistenciaCursosInterface persistenciaCursos;
   @EJB
   PersistenciaInstitucionesInterface persistenciaInstituciones;
   @EJB
   PersistenciaAdiestramientosNFInterface persistenciaAdiestramientosNF;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

   private VigenciasNoFormales vNF;
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
   public List<VigenciasNoFormales> vigenciasNoFormalesPersona(BigInteger secPersona) {
      try {
         return persistenciaVigenciasNoFormales.vigenciasNoFormalesPersona(getEm(), secPersona);
      } catch (Exception e) {
         log.error("Error AdministrarVigenciasNoFormales.vigenciasNoFormalesPersona " + e);
         return null;
      }
   }

   @Override
   public Personas encontrarPersona(BigInteger secPersona) {
      try {
         return persistenciaPersonas.buscarPersonaSecuencia(getEm(), secPersona);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   //Listas de Valores Cursos, Instituciones, Adiestramiento
   @Override
   public List<Cursos> lovCursos() {
      try {
         return persistenciaCursos.cursos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Instituciones> lovInstituciones() {
      try {
         return persistenciaInstituciones.instituciones(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<AdiestramientosNF> lovAdiestramientosNF() {
      try {
         return persistenciaAdiestramientosNF.adiestramientosNF(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void modificarVigenciaNoFormal(List<VigenciasNoFormales> listaVigenciasNoFormalesModificar) {
      try {
         for (int i = 0; i < listaVigenciasNoFormalesModificar.size(); i++) {
            log.warn("Modificando...");
            if (listaVigenciasNoFormalesModificar.get(i).getCurso().getSecuencia() == null) {
               listaVigenciasNoFormalesModificar.get(i).setCurso(null);
            }
            if (listaVigenciasNoFormalesModificar.get(i).getInstitucion().getSecuencia() == null) {
               listaVigenciasNoFormalesModificar.get(i).setInstitucion(null);
            }
//            if (listaVigenciasNoFormalesModificar.get(i).getAdiestramientonf().getSecuencia() == null) {
//                listaVigenciasNoFormalesModificar.get(i).setAdiestramientonf(null);
//            }
            persistenciaVigenciasNoFormales.editar(getEm(), listaVigenciasNoFormalesModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarVigenciaNoFormal(List<VigenciasNoFormales> listaVigenciasNoFormalesBorrar) {
      try {
         for (int i = 0; i < listaVigenciasNoFormalesBorrar.size(); i++) {
            log.warn("Modificando...");
            if (listaVigenciasNoFormalesBorrar.get(i).getCurso().getSecuencia() == null) {
               listaVigenciasNoFormalesBorrar.get(i).setCurso(null);
            }
            if (listaVigenciasNoFormalesBorrar.get(i).getInstitucion().getSecuencia() == null) {
               listaVigenciasNoFormalesBorrar.get(i).setInstitucion(null);
            }
            persistenciaVigenciasNoFormales.borrar(getEm(), listaVigenciasNoFormalesBorrar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearVigenciaNoFormal(List<VigenciasNoFormales> listaVigenciasNoFormalesCrear) {
      try {
         for (int i = 0; i < listaVigenciasNoFormalesCrear.size(); i++) {
            log.warn("Modificando...");
            if (listaVigenciasNoFormalesCrear.get(i).getCurso().getSecuencia() == null) {
               listaVigenciasNoFormalesCrear.get(i).setCurso(null);
            }
            if (listaVigenciasNoFormalesCrear.get(i).getInstitucion().getSecuencia() == null) {
               listaVigenciasNoFormalesCrear.get(i).setInstitucion(null);
            }
            if (listaVigenciasNoFormalesCrear.get(i).getAdiestramientonf().getSecuencia() == null) {
               listaVigenciasNoFormalesCrear.get(i).setAdiestramientonf(null);
               listaVigenciasNoFormalesCrear.get(i).setAcargo("S");
            } else {
               listaVigenciasNoFormalesCrear.get(i).setAcargo("N");
            }
            persistenciaVigenciasNoFormales.crear(getEm(), listaVigenciasNoFormalesCrear.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public Empleados empleadoActual(BigInteger secuenciaE) {
      try {
         return persistenciaEmpleado.buscarEmpleado(getEm(), secuenciaE);
      } catch (Exception e) {
         log.warn("Error empleadoActual Admi : " + e.toString());
         return null;
      }
   }

}
