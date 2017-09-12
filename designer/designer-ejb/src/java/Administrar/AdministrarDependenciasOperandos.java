/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.DependenciasOperandos;
import Entidades.Operandos;
import InterfaceAdministrar.AdministrarDependenciasOperandosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaDependenciasOperandosInterface;
import InterfacePersistencia.PersistenciaOperandosInterface;
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
public class AdministrarDependenciasOperandos implements AdministrarDependenciasOperandosInterface {

   private static Logger log = Logger.getLogger(AdministrarDependenciasOperandos.class);

   @EJB
   PersistenciaDependenciasOperandosInterface persistenciaDependenciasOperandos;
   @EJB
   PersistenciaOperandosInterface persistenciaOperandos;
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
   public List<DependenciasOperandos> buscarDependenciasOperandos(BigInteger secuenciaOperando) {
      try {
         return persistenciaDependenciasOperandos.dependenciasOperandos(getEm(), secuenciaOperando);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".buscarDependenciasOperandos() ERROR: " + e);
         return null;
      }
   }

   @Override
   public void borrarDependenciasOperandos(DependenciasOperandos tiposConstantes) {
      try {
         persistenciaDependenciasOperandos.borrar(getEm(), tiposConstantes);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarDependenciasOperandos() ERROR: " + e);
      }
   }

   @Override
   public void crearDependenciasOperandos(DependenciasOperandos tiposConstantes) {
      try {
         persistenciaDependenciasOperandos.crear(getEm(), tiposConstantes);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearDependenciasOperandos() ERROR: " + e);
      }
   }

   @Override
   public void modificarDependenciasOperandos(DependenciasOperandos tiposConstantes) {
      try {
         persistenciaDependenciasOperandos.editar(getEm(), tiposConstantes);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarDependenciasOperandos() ERROR: " + e);
      }
   }

   @Override
   public List<Operandos> buscarOperandos() {
      try {
         return persistenciaOperandos.buscarOperandos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".buscarOperandos() ERROR: " + e);
         return null;
      }
   }

   @Override
   public String nombreOperandos(int codigo) {
      try {
         return persistenciaDependenciasOperandos.nombreOperandos(getEm(), codigo);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".nombreOperandos() ERROR: " + e);
         return null;
      }
   }
}
