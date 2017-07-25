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

   private EntityManager em;

   @Override
   public void obtenerConexion(String idSesion) {
      em = administrarSesiones.obtenerConexionSesion(idSesion);
   }

   @Override
   public List<Operandos> buscarOperandos() {
      List<Operandos> listaOperandos;
      listaOperandos = persistenciaOperandos.buscarOperandos(em);
      return listaOperandos;
   }

   @Override
   public void borrarOperando(Operandos operandos) {
      persistenciaOperandos.borrar(em, operandos);
   }

   @Override
   public void crearOperando(Operandos operandos) {
      persistenciaOperandos.crear(em, operandos);
   }

   @Override
   public void modificarOperando(List<Operandos> listaOperandosModificar) {
      for (int i = 0; i < listaOperandosModificar.size(); i++) {
         log.warn("Modificando...");
         persistenciaOperandos.editar(em, listaOperandosModificar.get(i));
      }
   }

   public String buscarValores(BigInteger secuenciaOperando) {
      String valores;
      valores = persistenciaOperandos.valores(em, secuenciaOperando);
      return valores;
   }

   @Override
   public Operandos consultarOperandoActual(BigInteger secOperando) {
      try {
         Operandos actual = persistenciaOperandos.operandosPorSecuencia(em, secOperando);
         return actual;
      } catch (Exception e) {
         log.warn("Error conceptoActual Admi : " + e.toString());
         return null;
      }
   }

   public String clonarOperando(short codigoO, String nombreDes, String descripcionDes) {
      try {
         return persistenciaOperandos.clonarOperando(em, codigoO, nombreDes, descripcionDes);
      } catch (Exception e) {
         log.warn("AdministrarOperandos.clonarOperando() Error : " + e.toString());
         return "ERROR EJECUTANDO LA TRANSACCION DESDE EL SISTEMA";
      }
   }

}
