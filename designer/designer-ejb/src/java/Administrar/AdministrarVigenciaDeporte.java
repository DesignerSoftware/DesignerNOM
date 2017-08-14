/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Deportes;
import Entidades.Empleados;
import Entidades.VigenciasDeportes;
import InterfaceAdministrar.AdministrarVigenciaDeporteInterface;
import InterfacePersistencia.PersistenciaDeportesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaVigenciasDeportesInterface;
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
public class AdministrarVigenciaDeporte implements AdministrarVigenciaDeporteInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciaDeporte.class);

   @EJB
   PersistenciaVigenciasDeportesInterface persistenciaVigenciasDeportes;
   @EJB
   PersistenciaDeportesInterface persistenciaDeportes;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
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
   public List<VigenciasDeportes> listVigenciasDeportesPersona(BigInteger secuenciaP) {
      try {
         return persistenciaVigenciasDeportes.deportesTotalesSecuenciaPersona(getEm(), secuenciaP);
      } catch (Exception e) {
         log.warn("Error listVigenciasDeportesPersona Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearVigenciasDeportes(List<VigenciasDeportes> listaVD) {
      try {
         for (int i = 0; i < listaVD.size(); i++) {
            if (listaVD.get(i).getDeporte().getSecuencia() == null) {
               listaVD.get(i).setDeporte(null);
            }
            persistenciaVigenciasDeportes.crear(getEm(), listaVD.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearVigenciasDeportes Admi : " + e.toString());
      }
   }

   @Override
   public void editarVigenciasDeportes(List<VigenciasDeportes> listaVD) {
      try {
         for (int i = 0; i < listaVD.size(); i++) {
            if (listaVD.get(i).getDeporte().getSecuencia() == null) {
               listaVD.get(i).setDeporte(null);
            }
            persistenciaVigenciasDeportes.editar(getEm(), listaVD.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarVigenciasDeportes Admi : " + e.toString());
      }
   }

   @Override
   public void borrarVigenciasDeportes(List<VigenciasDeportes> listaVD) {
      try {
         for (int i = 0; i < listaVD.size(); i++) {
            if (listaVD.get(i).getDeporte().getSecuencia() == null) {
               listaVD.get(i).setDeporte(null);
            }
            persistenciaVigenciasDeportes.borrar(getEm(), listaVD.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarVigenciasDeportes Admi : " + e.toString());
      }
   }

   @Override
   public List<Deportes> listDeportes() {
      try {
         return persistenciaDeportes.buscarDeportes(getEm());
      } catch (Exception e) {
         log.warn("Error listDeportes Admi : " + e.toString());
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
}
