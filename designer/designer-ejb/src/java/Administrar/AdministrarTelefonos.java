package Administrar;

import Entidades.Ciudades;
import Entidades.Empleados;
import Entidades.Personas;
import Entidades.Telefonos;
import Entidades.TiposTelefonos;
import InterfaceAdministrar.AdministrarTelefonosInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import InterfacePersistencia.PersistenciaTelefonosInterface;
import InterfacePersistencia.PersistenciaTiposTelefonosInterface;
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
public class AdministrarTelefonos implements AdministrarTelefonosInterface {

   private static Logger log = Logger.getLogger(AdministrarTelefonos.class);

   @EJB
   PersistenciaTelefonosInterface persistenciaTelefonos;
   @EJB
   PersistenciaPersonasInterface persistenciaPersonas;
   @EJB
   PersistenciaTiposTelefonosInterface persistenciaTiposTelefonos;
   @EJB
   PersistenciaCiudadesInterface PersistenciaCiudades;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

   private Telefonos t;
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
   public List<Telefonos> telefonosPersona(BigInteger secPersona) {
      try {
         return persistenciaTelefonos.telefonosPersona(getEm(), secPersona);
      } catch (Exception e) {
         log.error("Error AdministrarTelefonos.telefonosPersona " + e);
         return null;
      }
   }

   @Override
   public Personas encontrarPersona(BigInteger secPersona) {
      try {
         return persistenciaPersonas.buscarPersonaSecuencia(getEm(), secPersona);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".encontrarPersona() ERROR: " + e);
         return null;
      }
   }

   //Lista de Valores TiposTelefonos
   @Override
   public List<TiposTelefonos> lovTiposTelefonos() {
      try {
         return persistenciaTiposTelefonos.tiposTelefonos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".lovTiposTelefonos() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Ciudades> lovCiudades() {
      try {
         return PersistenciaCiudades.consultarCiudades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".lovCiudades() ERROR: " + e);
         return null;
      }
   }

   @Override
   public void modificarTelefono(List<Telefonos> listaTelefonosModificar) {
      try {
         for (int i = 0; i < listaTelefonosModificar.size(); i++) {
            log.warn("Modificando...");
            if (listaTelefonosModificar.get(i).getTipotelefono().getSecuencia() == null) {
               listaTelefonosModificar.get(i).setTipotelefono(null);
               t = listaTelefonosModificar.get(i);
            } else {
               t = listaTelefonosModificar.get(i);
            }
            if (listaTelefonosModificar.get(i).getCiudad().getSecuencia() == null) {
               listaTelefonosModificar.get(i).setCiudad(null);
               t = listaTelefonosModificar.get(i);
            } else {
               t = listaTelefonosModificar.get(i);
            }
            persistenciaTelefonos.editar(getEm(), t);
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarTelefono() ERROR: " + e);
      }
   }

   @Override
   public void borrarTelefono(Telefonos telefonos) {
      try {
         persistenciaTelefonos.borrar(getEm(), telefonos);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarTelefono() ERROR: " + e);
      }
   }

   @Override
   public void crearTelefono(Telefonos telefonos) {
      try {
         persistenciaTelefonos.crear(getEm(), telefonos);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearTelefono() ERROR: " + e);
      }
   }

   @Override
   public Empleados empleadoActual(BigInteger secuenciaP) {
      try {
         return persistenciaEmpleado.buscarEmpleado(getEm(), secuenciaP);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "Error empleadoActual() : " + e.toString());
         return null;
      }
   }

}
