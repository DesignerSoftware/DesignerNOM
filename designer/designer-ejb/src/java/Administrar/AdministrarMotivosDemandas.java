/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarMotivosDemandasInterface;
import Entidades.MotivosDemandas;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMotivosDemandasInterface;
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
public class AdministrarMotivosDemandas implements AdministrarMotivosDemandasInterface {

   private static Logger log = Logger.getLogger(AdministrarMotivosDemandas.class);

   @EJB
   PersistenciaMotivosDemandasInterface persistenciaMotivosDemandas;
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
   public void modificarMotivosDemandas(List<MotivosDemandas> listMotivosDemandas) {
      try {
         for (int i = 0; i < listMotivosDemandas.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaMotivosDemandas.editar(getEm(), listMotivosDemandas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarMotivosDemandas() ERROR: " + e);
      }
   }

   @Override
   public void borrarMotivosDemandas(List<MotivosDemandas> listMotivosDemandas) {
      try {
         for (int i = 0; i < listMotivosDemandas.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaMotivosDemandas.borrar(getEm(), listMotivosDemandas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarMotivosDemandas() ERROR: " + e);
      }
   }

   @Override
   public void crearMotivosDemandas(List<MotivosDemandas> listMotivosDemandas) {
      try {
         for (int i = 0; i < listMotivosDemandas.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaMotivosDemandas.crear(getEm(), listMotivosDemandas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearMotivosDemandas() ERROR: " + e);
      }
   }

   @Override
   public List<MotivosDemandas> consultarMotivosDemandas() {
      try {
         return persistenciaMotivosDemandas.buscarMotivosDemandas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarMotivosDemandas() ERROR: " + e);
         return null;
      }
   }

   @Override
   public MotivosDemandas consultarMotivoDemanda(BigInteger secMotivoDemanda) {
      try {
         return persistenciaMotivosDemandas.buscarMotivoDemanda(getEm(), secMotivoDemanda);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarMotivoDemanda() ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarDemandasMotivoDemanda(BigInteger secuenciaEventos) {
      try {
         log.error("Secuencia Motivo Demanda " + secuenciaEventos);
         return persistenciaMotivosDemandas.contadorDemandas(getEm(), secuenciaEventos);
      } catch (Exception e) {
         log.error("ERROR AdmnistrarMotivoDemanda verificarBorradoDemanda ERROR :" + e);
         return null;
      }
   }
}
