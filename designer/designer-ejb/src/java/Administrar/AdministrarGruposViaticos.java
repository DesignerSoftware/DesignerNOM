/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.GruposViaticos;
import InterfaceAdministrar.AdministrarGruposViaticosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaGruposViaticosInterface;
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
public class AdministrarGruposViaticos implements AdministrarGruposViaticosInterface {

   private static Logger log = Logger.getLogger(AdministrarGruposViaticos.class);

   @EJB
   PersistenciaGruposViaticosInterface persistenciaGruposViaticos;
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
   public void modificarGruposViaticos(List<GruposViaticos> listGruposViaticos) {
      try {
         for (int i = 0; i < listGruposViaticos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaGruposViaticos.editar(getEm(), listGruposViaticos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarGruposViaticos(List<GruposViaticos> listGruposViaticos) {
      try {
         for (int i = 0; i < listGruposViaticos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaGruposViaticos.borrar(getEm(), listGruposViaticos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearGruposViaticos(List<GruposViaticos> listGruposViaticos) {
      try {
         for (int i = 0; i < listGruposViaticos.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaGruposViaticos.crear(getEm(), listGruposViaticos.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<GruposViaticos> consultarGruposViaticos() {
      try {
         return persistenciaGruposViaticos.buscarGruposViaticos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public GruposViaticos consultarGrupoViatico(BigInteger secGruposViaticos) {
      try {
         return persistenciaGruposViaticos.buscarGrupoViatico(getEm(), secGruposViaticos);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger verificarCargos(BigInteger secuenciaCargos) {
      try {
         log.error("Secuencia Borrado  Cargos" + secuenciaCargos);
         return persistenciaGruposViaticos.contadorCargos(getEm(), secuenciaCargos);
      } catch (Exception e) {
         log.error("ERROR AdministrarGruposViativos verificarBorradoCargos ERROR :" + e);
         return null;
      }
   }

   @Override
   public BigInteger verificarPlantas(BigInteger secuenciaCargos) {
      try {
         log.error("Secuencia Borrado  Plantas" + secuenciaCargos);
         return persistenciaGruposViaticos.contadorPlantas(getEm(), secuenciaCargos);
      } catch (Exception e) {
         log.error("ERROR AdministrarGruposViativos verificarBorradoPlantas ERROR :" + e);
         return null;
      }
   }

   @Override
   public BigInteger verificarTablasViaticos(BigInteger secuenciaCargos) {
      try {
         log.error("Secuencia Borrado  Tablas Viaticos" + secuenciaCargos);
         return persistenciaGruposViaticos.contadorTablasViaticos(getEm(), secuenciaCargos);
      } catch (Exception e) {
         log.error("ERROR AdministrarGruposViativos verificarTablasViaticos ERROR :" + e);
         return null;
      }
   }

   @Override
   public BigInteger verificarEersViaticos(BigInteger secuenciaCargos) {
      try {
         log.error("Secuencia Borrado  Tablas ErsViaticos" + secuenciaCargos);
         return persistenciaGruposViaticos.contadorEersViaticos(getEm(), secuenciaCargos);
      } catch (Exception e) {
         log.error("ERROR AdministrarGruposViativos verificarEersViaticos ERROR :" + e);
         return null;
      }
   }
}
