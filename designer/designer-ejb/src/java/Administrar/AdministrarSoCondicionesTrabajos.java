/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarSoCondicionesTrabajosInterface;
import Entidades.SoCondicionesTrabajos;
import InterfacePersistencia.PersistenciaSoCondicionesTrabajosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateless
public class AdministrarSoCondicionesTrabajos implements AdministrarSoCondicionesTrabajosInterface {

   private static Logger log = Logger.getLogger(AdministrarSoCondicionesTrabajos.class);

   @EJB
   PersistenciaSoCondicionesTrabajosInterface persistenciaSoCondicionesTrabajos;
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
   public void modificarSoCondicionesTrabajos(List<SoCondicionesTrabajos> listSoCondicionesTrabajos) {
      try {
         for (int i = 0; i < listSoCondicionesTrabajos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaSoCondicionesTrabajos.editar(getEm(), listSoCondicionesTrabajos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarSoCondicionesTrabajos(List<SoCondicionesTrabajos> listSoCondicionesTrabajos) {
      try {
         for (int i = 0; i < listSoCondicionesTrabajos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaSoCondicionesTrabajos.borrar(getEm(), listSoCondicionesTrabajos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearSoCondicionesTrabajos(List<SoCondicionesTrabajos> listSoCondicionesTrabajos) {
      try {
         for (int i = 0; i < listSoCondicionesTrabajos.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaSoCondicionesTrabajos.crear(getEm(), listSoCondicionesTrabajos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<SoCondicionesTrabajos> consultarSoCondicionesTrabajos() {
      try {
         return persistenciaSoCondicionesTrabajos.buscarSoCondicionesTrabajos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public SoCondicionesTrabajos consultarSoCondicionTrabajo(BigInteger secSoCondicionesTrabajos) {
      try {
         return persistenciaSoCondicionesTrabajos.buscarSoCondicionTrabajo(getEm(), secSoCondicionesTrabajos);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarInspeccionesSoCondicionTrabajo(BigInteger secuenciaElementos) {
      try {
         log.error("Secuencia Borrado Elementos" + secuenciaElementos);
         return persistenciaSoCondicionesTrabajos.contadorInspecciones(getEm(), secuenciaElementos);
      } catch (Exception e) {
         log.error("ERROR AdministrarSoCondicionesTrabajos verificarInspecciones ERROR :" + e);
         return null;
      }
   }

   @Override
   public BigInteger contarSoAccidentesMedicosSoCondicionTrabajo(BigInteger secuenciaElementos) {
      try {
         log.error("Secuencia Borrado Elementos" + secuenciaElementos);
         return persistenciaSoCondicionesTrabajos.contadorSoAccidentesMedicos(getEm(), secuenciaElementos);
      } catch (Exception e) {
         log.error("ERROR AdministrarSoCondicionesTrabajos verificarSoAccidtenesMedicos ERROR :" + e);
         return null;
      }
   }

   @Override
   public BigInteger contarSoDetallesPanoramasSoCondicionTrabajo(BigInteger secuenciaElementos) {
      try {
         log.error("Secuencia Borrado Elementos" + secuenciaElementos);
         return persistenciaSoCondicionesTrabajos.contadorSoDetallesPanoramas(getEm(), secuenciaElementos);
      } catch (Exception e) {
         log.error("ERROR AdministrarSoCondicionesTrabajos verificarSoDetallesPanoramas ERROR :" + e);
         return null;
      }
   }

   @Override
   public BigInteger contarSoExposicionesFrSoCondicionTrabajo(BigInteger secuenciaElementos) {
      try {
         log.error("Secuencia Borrado Elementos" + secuenciaElementos);
         return persistenciaSoCondicionesTrabajos.contadorSoExposicionesFr(getEm(), secuenciaElementos);
      } catch (Exception e) {
         log.error("ERROR AdministrarClasesAccidentes verificarSoExposicionesFr ERROR :" + e);
         return null;
      }
   }

}
