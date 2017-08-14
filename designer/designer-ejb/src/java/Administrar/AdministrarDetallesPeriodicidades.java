/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.DetallesPeriodicidades;
import Entidades.Periodicidades;
import InterfaceAdministrar.AdministrarDetallesPeriodicidadesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaDetallesPeriodicidadesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.Local;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
@Local
public class AdministrarDetallesPeriodicidades implements AdministrarDetallesPeriodicidadesInterface {

   private static Logger log = Logger.getLogger(AdministrarDetallesPeriodicidades.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaDetallesPeriodicidadesInterface persistenciaDetallesPeriodicidades;

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
   public void modificarDetallePeriodicidad(List<DetallesPeriodicidades> listaModificar) {
      try {
         for (int i = 0; i < listaModificar.size(); i++) {
            persistenciaDetallesPeriodicidades.editar(getEm(), listaModificar.get(i));
         }
      } catch (Exception e) {
         log.warn("error en AdministrarDetallesPeriodicidades.modificarDetallePeriodicidad () " + e.toString());
      }
   }

   @Override
   public void borrarDetallePeriodicidad(List<DetallesPeriodicidades> listaBorrar) {
      try {
         for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaDetallesPeriodicidades.borrar(getEm(), listaBorrar.get(i));
         }
      } catch (Exception e) {
         log.warn("error en AdministrarDetallesPeriodicidades.borrarDetallePeriodicidad () " + e.toString());
      }
   }

   @Override
   public void crearDetallePeriodicidad(List<DetallesPeriodicidades> listaCrear) {
      try {
         for (int i = 0; i < listaCrear.size(); i++) {
            persistenciaDetallesPeriodicidades.crear(getEm(), listaCrear.get(i));
         }
      } catch (Exception e) {
         log.warn("error en AdministrarDetallesPeriodicidades.crearDetallePeriodicidad () " + e.toString());
      }
   }

   @Override
   public List<DetallesPeriodicidades> consultarDetallesPeriodicidades(BigInteger secuenciaPeriodicidad) {
      try {
         return persistenciaDetallesPeriodicidades.buscarDetallesPeriodicidad(getEm(), secuenciaPeriodicidad);
      } catch (Exception e) {
         log.warn("error en AdministrarDetallesPeriodicidades.consultarDetallesPeriodicidades() " + e.toString());
         return null;
      }
   }

   @Override
   public Periodicidades consultarPeriodicidadPorSecuencia(BigInteger secuenciaPeriodicidad) {
      try {
         return persistenciaDetallesPeriodicidades.buscarPeriodicidadPorSecuencia(getEm(), secuenciaPeriodicidad);
      } catch (Exception e) {
         log.warn("error en AdministrarDetallesPeriodicidades.consultarPeriodicidadPorSecuencia() " + e.toString());
         return null;
      }
   }
}
