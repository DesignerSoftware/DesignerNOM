/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Bancos;
import InterfaceAdministrar.AdministrarBancosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaBancosInterface;
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
public class AdministrarBancos implements AdministrarBancosInterface {

   private static Logger log = Logger.getLogger(AdministrarBancos.class);

   @EJB
   PersistenciaBancosInterface persistenciaBancos;
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
   public void modificarBanco(List<Bancos> listaModificar) {
      try {
         for (int i = 0; i < listaModificar.size(); i++) {
            persistenciaBancos.editar(getEm(), listaModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarBanco() ERROR: " + e);
      }
   }

   @Override
   public void borrarBanco(List<Bancos> listaBorrar) {
      try {
         for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaBancos.borrar(getEm(), listaBorrar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarBanco() ERROR: " + e);
      }
   }

   @Override
   public void crearBanco(List<Bancos> listaCrear) {
      try {
         for (int i = 0; i < listaCrear.size(); i++) {
            persistenciaBancos.crear(getEm(), listaCrear.get(i));
         }
      } catch (Exception e) {
         log.warn("error en AdministrarBancos.crearBanco() " + e.toString());
      }
   }

   @Override
   public List<Bancos> consultarBancos() {
      try {
         return persistenciaBancos.buscarBancos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarBancos() ERROR: " + e);
         return null;
      }
   }

   @Override
   public Bancos consultarBancosPorSecuencia(BigInteger secuencia) {
      try {
         return persistenciaBancos.buscarBancosPorSecuencia(getEm(), secuencia);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarBancosPorSecuencia() ERROR: " + e);
         return null;
      }
   }
}
