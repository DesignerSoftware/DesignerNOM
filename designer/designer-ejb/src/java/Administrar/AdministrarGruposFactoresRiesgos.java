/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.GruposFactoresRiesgos;
import InterfaceAdministrar.AdministrarGruposFactoresRiesgosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaGruposFactoresRiesgosInterface;
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
public class AdministrarGruposFactoresRiesgos implements AdministrarGruposFactoresRiesgosInterface {

   private static Logger log = Logger.getLogger(AdministrarGruposFactoresRiesgos.class);

   @EJB
   PersistenciaGruposFactoresRiesgosInterface persistenciaGruposFactoresRiesgos;
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

   public void modificarGruposFactoresRiesgos(List<GruposFactoresRiesgos> listaGruposFactoresRiesgos) {
      try {
         for (int i = 0; i < listaGruposFactoresRiesgos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaGruposFactoresRiesgos.editar(getEm(), listaGruposFactoresRiesgos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void borrarGruposFactoresRiesgos(List<GruposFactoresRiesgos> listaGruposFactoresRiesgos) {
      try {
         for (int i = 0; i < listaGruposFactoresRiesgos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaGruposFactoresRiesgos.borrar(getEm(), listaGruposFactoresRiesgos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void crearGruposFactoresRiesgos(List<GruposFactoresRiesgos> listaGruposFactoresRiesgos) {
      try {
         for (int i = 0; i < listaGruposFactoresRiesgos.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaGruposFactoresRiesgos.crear(getEm(), listaGruposFactoresRiesgos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<GruposFactoresRiesgos> consultarGruposFactoresRiesgos() {
      try {
         return persistenciaGruposFactoresRiesgos.consultarGruposFactoresRiesgos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public GruposFactoresRiesgos consultarGrupoFactorRiesgo(BigInteger secGruposFactoresRiesgos) {
      try {
         return persistenciaGruposFactoresRiesgos.consultarGrupoFactorRiesgo(getEm(), secGruposFactoresRiesgos);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public BigInteger contarFactoresRiesgoGrupoFactorRiesgo(BigInteger secGruposFactoresRiesgos) {
      try {
         return persistenciaGruposFactoresRiesgos.contarFactoresRiesgoGrupoFactorRiesgo(getEm(), secGruposFactoresRiesgos);
      } catch (Exception e) {
         log.error("ERROR AdministrarGruposFactoresRiesgos contarFactoresRiesgoGrupoFactorRiesgo ERROR : " + e);
         return null;
      }
   }

   public BigInteger contarSoIndicadoresGrupoFactorRiesgo(BigInteger secGruposFactoresRiesgos) {
      try {
         return persistenciaGruposFactoresRiesgos.contarSoIndicadoresGrupoFactorRiesgo(getEm(), secGruposFactoresRiesgos);
      } catch (Exception e) {
         log.error("ERROR AdministrarGruposFactoresRiesgos contarSoIndicadoresGrupoFactorRiesgo ERROR : " + e);
         return null;
      }
   }

   public BigInteger contarSoProActividadesGrupoFactorRiesgo(BigInteger secGruposFactoresRiesgos) {
      try {
         return persistenciaGruposFactoresRiesgos.contarSoProActividadesGrupoFactorRiesgo(getEm(), secGruposFactoresRiesgos);
      } catch (Exception e) {
         log.error("ERROR AdministrarGruposFactoresRiesgos contarSoProActividadesGrupoFactorRiesgo ERROR : " + e);
         return null;
      }
   }
}
