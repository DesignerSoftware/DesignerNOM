/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Operandos;
import InterfaceAdministrar.AdministrarOperandosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
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
public class AdministrarOperandos implements AdministrarOperandosInterface {

   private static Logger log = Logger.getLogger(AdministrarOperandos.class);

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
   public List<Operandos> buscarOperandos() {
      try {
         return persistenciaOperandos.buscarOperandos(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void borrarOperando(Operandos operandos) {
      try {
         persistenciaOperandos.borrar(getEm(), operandos);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearOperando(Operandos operandos) {
      try {
         persistenciaOperandos.crear(getEm(), operandos);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void modificarOperando(List<Operandos> listaOperandosModificar) {
      try {
         for (int i = 0; i < listaOperandosModificar.size(); i++) {
            log.warn("Modificando...");
            persistenciaOperandos.editar(getEm(), listaOperandosModificar.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public String buscarValores(BigInteger secuenciaOperando) {
      try {
         return persistenciaOperandos.valores(getEm(), secuenciaOperando);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Operandos consultarOperandoActual(BigInteger secOperando) {
      try {
         return persistenciaOperandos.operandosPorSecuencia(getEm(), secOperando);
      } catch (Exception e) {
         log.warn("Error conceptoActual Admi : " + e.toString());
         return null;
      }
   }

   public String clonarOperando(short codigoO, String nombreDes, String descripcionDes) {
      try {
         return persistenciaOperandos.clonarOperando(getEm(), codigoO, nombreDes, descripcionDes);
      } catch (Exception e) {
         log.warn("AdministrarOperandos.clonarOperando() Error : " + e.toString());
         return "ERROR EJECUTANDO LA TRANSACCION DESDE EL SISTEMA";
      }
   }

}
