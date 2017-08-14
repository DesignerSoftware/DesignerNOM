/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.GruposInfAdicionales;
import InterfaceAdministrar.AdministrarGruposInfAdicionalesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaGruposInfAdicionalesInterface;
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
public class AdministrarGruposInfAdicionales implements AdministrarGruposInfAdicionalesInterface {

   private static Logger log = Logger.getLogger(AdministrarGruposInfAdicionales.class);

   @EJB
   PersistenciaGruposInfAdicionalesInterface persistenciaGruposInfAdicionales;
   private GruposInfAdicionales grupoInfAdicionalSeleccionado;
   private GruposInfAdicionales gruposInfAdicionales;
   private List<GruposInfAdicionales> listGruposInfAdicionales;
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
   public void modificarGruposInfAdicionales(List<GruposInfAdicionales> listGruposInfAdicionales) {
      try {
         for (int i = 0; i < listGruposInfAdicionales.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaGruposInfAdicionales.editar(getEm(), listGruposInfAdicionales.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarGruposInfAdicionales(List<GruposInfAdicionales> listGruposInfAdicionales) {
      try {
         for (int i = 0; i < listGruposInfAdicionales.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaGruposInfAdicionales.borrar(getEm(), listGruposInfAdicionales.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearGruposInfAdicionales(List<GruposInfAdicionales> listGruposInfAdicionales) {
      try {
         for (int i = 0; i < listGruposInfAdicionales.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaGruposInfAdicionales.crear(getEm(), listGruposInfAdicionales.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<GruposInfAdicionales> consultarGruposInfAdicionales() {
      try {
         listGruposInfAdicionales = persistenciaGruposInfAdicionales.buscarGruposInfAdicionales(getEm());
         return listGruposInfAdicionales;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public GruposInfAdicionales consultarGrupoInfAdicional(BigInteger secDeportes) {
      try {
         gruposInfAdicionales = persistenciaGruposInfAdicionales.buscarGrupoInfAdicional(getEm(), secDeportes);
         return gruposInfAdicionales;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger verificarInformacionesAdicionales(BigInteger secuenciaGruposInfAdicionales) {
      try {
         log.error("Secuencia Grupo Inf Adicional : " + secuenciaGruposInfAdicionales);
         return persistenciaGruposInfAdicionales.contadorInformacionesAdicionales(getEm(), secuenciaGruposInfAdicionales);
      } catch (Exception e) {
         log.error("ERROR AdministrarEstadosCiviles VigenciasEstadoCiviles ERROR :" + e);
         return null;
      }
   }
}
