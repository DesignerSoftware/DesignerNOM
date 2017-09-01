/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.LugaresOcurrencias;
import InterfaceAdministrar.AdministrarLugaresOcurrenciasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaLugaresOcurrenciasInterface;
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
public class AdministrarLugaresOcurrencias implements AdministrarLugaresOcurrenciasInterface {

   private static Logger log = Logger.getLogger(AdministrarLugaresOcurrencias.class);

   @EJB
   PersistenciaLugaresOcurrenciasInterface persistenciaLugaresOcurrencias;
   private LugaresOcurrencias lugarOcurrencia;
   private List<LugaresOcurrencias> listLugarOcurrencia;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

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
   public void modificarLugarOcurrencia(List<LugaresOcurrencias> listaLugaresOcurrencias) {
      try {
         for (int i = 0; i < listaLugaresOcurrencias.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaLugaresOcurrencias.editar(getEm(), listaLugaresOcurrencias.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarLugarOcurrencia(List<LugaresOcurrencias> listaLugaresOcurrencias) {
      try {
         for (int i = 0; i < listaLugaresOcurrencias.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaLugaresOcurrencias.borrar(getEm(), listaLugaresOcurrencias.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearLugarOcurrencia(List<LugaresOcurrencias> listaLugaresOcurrencias) {
      try {
         for (int i = 0; i < listaLugaresOcurrencias.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaLugaresOcurrencias.crear(getEm(), listaLugaresOcurrencias.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<LugaresOcurrencias> consultarLugaresOcurrencias() {
      try {
         listLugarOcurrencia = persistenciaLugaresOcurrencias.buscarLugaresOcurrencias(getEm());
         return listLugarOcurrencia;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public LugaresOcurrencias consultarLugarOcurrencia(BigInteger secLugarOcurrencia) {
      try {
         lugarOcurrencia = persistenciaLugaresOcurrencias.buscarLugaresOcurrencias(getEm(), secLugarOcurrencia);
         return lugarOcurrencia;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger verificarSoAccidentesLugarOcurrencia(BigInteger secuenciaLugaresOcurrencias) {
      try {
         return persistenciaLugaresOcurrencias.contadorSoAccidentes(getEm(), secuenciaLugaresOcurrencias);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARLUGARESOCURRENCIAS VERIFICAR SO ACCIDENTES ERROR : " + e);
         return null;
      }
   }
}
