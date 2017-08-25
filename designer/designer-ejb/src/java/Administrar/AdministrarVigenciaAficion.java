/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Aficiones;
import Entidades.Empleados;
import Entidades.Personas;
import Entidades.VigenciasAficiones;
import InterfaceAdministrar.AdministrarVigenciaAficionInterface;
import InterfacePersistencia.PersistenciaAficionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaVigenciasAficionesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarVigenciaAficion implements AdministrarVigenciaAficionInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciaAficion.class);

   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   @EJB
   PersistenciaVigenciasAficionesInterface persistenciaVigenciasAficiones;
   @EJB
   PersistenciaAficionesInterface persistenciaAficiones;
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
   public List<VigenciasAficiones> listVigenciasAficionesPersona(BigInteger secuenciaP) {
      try {
         return persistenciaVigenciasAficiones.aficionesTotalesSecuenciaPersona(getEm(), secuenciaP);
      } catch (Exception e) {
         log.warn("Error listVigenciasAficionesPersona Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearVigenciasAficiones(List<VigenciasAficiones> listVA) {
      try {
         for (int i = 0; i < listVA.size(); i++) {
            if (listVA.get(i).getAficion().getSecuencia() == null) {
               listVA.get(i).setAficion(null);
            }
            persistenciaVigenciasAficiones.crear(getEm(), listVA.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearVigenciasAficiones Admi : " + e.toString());
      }
   }

   @Override
   public void editarVigenciasAficiones(List<VigenciasAficiones> listVA) {
      try {
         for (int i = 0; i < listVA.size(); i++) {
            if (listVA.get(i).getAficion().getSecuencia() == null) {
               listVA.get(i).setAficion(null);
            }
            persistenciaVigenciasAficiones.editar(getEm(), listVA.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarVigenciasAficiones Admi : " + e.toString());
      }
   }

   @Override
   public void borrarVigenciasAficiones(List<VigenciasAficiones> listVA) {
      try {
         for (int i = 0; i < listVA.size(); i++) {
            if (listVA.get(i).getAficion().getSecuencia() == null) {
               listVA.get(i).setAficion(null);
            }
            persistenciaVigenciasAficiones.borrar(getEm(), listVA.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarVigenciasAficiones Admi : " + e.toString());
      }
   }

   @Override
   public List<Aficiones> listAficiones() {
      try {
         return persistenciaAficiones.buscarAficiones(getEm());
      } catch (Exception e) {
         log.warn("Error listAficiones Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Empleados empleadoActual(BigInteger secuencia) {
      try {
         return persistenciaEmpleado.buscarEmpleado(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error empleadoActual Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Personas obtenerPersonaPorEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaEmpleado.buscarPersonaPorEmpleado(em, secEmpleado);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + " ERROR en obtenerPersonaPorEmpleado() : " + e.getMessage());
         return null;
      }
   }
}
