/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Monedas;
import InterfaceAdministrar.AdministrarMonedasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMonedasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateless
public class AdministrarMonedas implements AdministrarMonedasInterface {

   private static Logger log = Logger.getLogger(AdministrarMonedas.class);

   @EJB
   PersistenciaMonedasInterface persistenciaMonedas;
   private Monedas monedaSeleccionado;
   private Monedas monedas;
   private List<Monedas> listMonedas;
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
   public void modificarMonedas(List<Monedas> listMonedasModificadas) {
      try {
         for (int i = 0; i < listMonedasModificadas.size(); i++) {
            log.warn("Administrar Modificando...");
            monedaSeleccionado = listMonedasModificadas.get(i);
            persistenciaMonedas.editar(getEm(), monedaSeleccionado);
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarMonedas(Monedas monedas) {
      try {
         persistenciaMonedas.borrar(getEm(), monedas);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearMonedas(Monedas monedas) {
      try {
         persistenciaMonedas.crear(getEm(), monedas);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<Monedas> consultarMonedas() {
      try {
         listMonedas = persistenciaMonedas.consultarMonedas(getEm());
         return listMonedas;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Monedas consultarMoneda(BigInteger secMoneda) {
      try {
         monedas = persistenciaMonedas.consultarMoneda(getEm(), secMoneda);
         return monedas;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger verificarMonedasProyecto(BigInteger secuenciaIdiomas) {
      try {
         log.error("Secuencia Borrado Proyecto" + secuenciaIdiomas);
         return persistenciaMonedas.contadorProyectos(getEm(), secuenciaIdiomas);
      } catch (Exception e) {
         log.error("ERROR AdministrarMonedas verificarBorradoProyecto ERROR :" + e);
         return null;
      }
   }
}
