/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Ciudades;
import Entidades.Empleados;
import Entidades.Familiares;
import Entidades.Personas;
import Entidades.TiposDocumentos;
import Entidades.TiposFamiliares;
import InterfaceAdministrar.AdministrarFamiliaresInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaFamiliaresInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import InterfacePersistencia.PersistenciaTiposDocumentosInterface;
import InterfacePersistencia.PersistenciaTiposFamiliaresInterface;
import java.math.BigInteger;
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
public class AdministrarFamiliares implements AdministrarFamiliaresInterface {

   private static Logger log = Logger.getLogger(AdministrarFamiliares.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaFamiliaresInterface persistenciaFamiliares;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   @EJB
   PersistenciaPersonasInterface persistenciaPersona;
   @EJB
   PersistenciaTiposDocumentosInterface persistenciaTipoDocumento;
   @EJB
   PersistenciaCiudadesInterface persistenciaCiudades;
   @EJB
   PersistenciaTiposFamiliaresInterface persistenciaTiposFamiliares;

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
   public void modificarFamiliares(List<Familiares> listaModificar) {
      try {
         for (int i = 0; i < listaModificar.size(); i++) {
            if (listaModificar.get(i).getPersona() == null) {
               listaModificar.get(i).setPersona(new Personas());
            }
            log.warn("Administrar Modificando...");
            persistenciaFamiliares.editar(getEm(), listaModificar.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarFamiliares(List<Familiares> listaBorrar) {
      try {
         for (int i = 0; i < listaBorrar.size(); i++) {
            if (listaBorrar.get(i).getPersona() == null) {
               listaBorrar.get(i).setPersona(new Personas());
            }
            log.warn("Administrar Borrando...");
            persistenciaFamiliares.borrar(getEm(), listaBorrar.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearFamilares(List<Familiares> listaCrear) {
      try {
         for (int i = 0; i < listaCrear.size(); i++) {
            log.warn("Administrar Creando...");
            if (listaCrear.get(i).getPersona() == null) {
               listaCrear.get(i).setPersona(new Personas());
            }
            persistenciaFamiliares.crear(getEm(), listaCrear.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<Familiares> consultarFamiliares(BigInteger secuenciaEmp) {
      try {
         return persistenciaFamiliares.familiaresPersona(getEm(), secuenciaEmp);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Empleados empleadoActual(BigInteger secuenciaP) {
      try {
         return persistenciaEmpleado.buscarEmpleado(getEm(), secuenciaP);
      } catch (Exception e) {
         log.warn("Error empleadoActual Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearPersona(Personas persona) {
      try {
         persistenciaPersona.crear(getEm(), persona);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<TiposFamiliares> consultarTiposFamiliares() {
      try {
         return persistenciaTiposFamiliares.buscarTiposFamiliares(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<TiposDocumentos> consultarTiposDocumentos() {
      try {
         return persistenciaTipoDocumento.consultarTiposDocumentos(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Ciudades> consultarCiudades() {
      try {
         return persistenciaCiudades.consultarCiudades(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Personas> consultarPersonas() {
      try {
         return persistenciaPersona.consultarPersonas(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Personas consultarPersona(BigInteger secPersona) {
      try {
         return persistenciaPersona.buscarPersonaSecuencia(getEm(), secPersona);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

}
