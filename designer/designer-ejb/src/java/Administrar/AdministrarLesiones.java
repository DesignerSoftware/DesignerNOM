/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarLesionesInterface;
import Entidades.Lesiones;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaLesionesInterface;
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
public class AdministrarLesiones implements AdministrarLesionesInterface {

   private static Logger log = Logger.getLogger(AdministrarLesiones.class);

   @EJB
   PersistenciaLesionesInterface persistenciaLesiones;
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
   public void modificarLesiones(List<Lesiones> listaLesiones) {
      try {
         for (int i = 0; i < listaLesiones.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaLesiones.editar(getEm(), listaLesiones.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarLesiones(List<Lesiones> listaLesiones) {
      try {
         for (int i = 0; i < listaLesiones.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaLesiones.borrar(getEm(), listaLesiones.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearLesiones(List<Lesiones> listaLesiones) {
      try {
         for (int i = 0; i < listaLesiones.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaLesiones.crear(getEm(), listaLesiones.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<Lesiones> consultarLesiones() {
      try {
         return persistenciaLesiones.buscarLesiones(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Lesiones consultarLesion(BigInteger secLesion) {
      try {
         return persistenciaLesiones.buscarLesion(getEm(), secLesion);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarDetallesLicensiasLesion(BigInteger secuenciaLesiones) {
      try {
         return persistenciaLesiones.contadorDetallesLicensias(getEm(), secuenciaLesiones);
      } catch (Exception e) {
         log.error("ERROR AdministrarLesiones verificarBorradoDetallesLicensias ERROR :" + e);
         return null;
      }
   }

   @Override
   public BigInteger contarSoAccidentesDomesticosLesion(BigInteger secuenciaVigenciasExamenesMedicos) {
      try {
         return persistenciaLesiones.contadorSoAccidentesDomesticos(getEm(), secuenciaVigenciasExamenesMedicos);
      } catch (Exception e) {
         log.error("ERROR AdministrarLesiones verificarBorradoSoAccidentesDomesticos ERROR :" + e);
         return null;
      }
   }
}
