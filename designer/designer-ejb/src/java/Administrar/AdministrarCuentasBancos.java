/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Bancos;
import Entidades.CuentasBancos;
import Entidades.Inforeportes;
import InterfaceAdministrar.AdministrarCuentasBancosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCuentasBancosInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
@Local
public class AdministrarCuentasBancos implements AdministrarCuentasBancosInterface {

   private static Logger log = Logger.getLogger(AdministrarCuentasBancos.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaCuentasBancosInterface persistenciaCuentasBancos;

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
   public void modificarCuentaBanco(List<CuentasBancos> listaModificar) {
      try {
         for (int i = 0; i < listaModificar.size(); i++) {
            persistenciaCuentasBancos.editar(getEm(), listaModificar.get(i));
         }
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " modificarCuentaBanco ERROR: " + e);
      }
   }

   @Override
   public void borrarCuentaBanco(List<CuentasBancos> listaBorrar) {
      try {
         for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaCuentasBancos.borrar(getEm(), listaBorrar.get(i));
         }
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " borrarCuentaBanco ERROR: " + e);
      }
   }

   @Override
   public void crearCuentaBanco(List<CuentasBancos> listaCrear) {
      try {
         for (int i = 0; i < listaCrear.size(); i++) {
            persistenciaCuentasBancos.crear(getEm(), listaCrear.get(i));
         }
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " crearCuentaBanco ERROR: " + e);
      }
   }

   @Override
   public List<CuentasBancos> consultarCuentasBancos() {
      try {
         List<CuentasBancos> listaCuentas = persistenciaCuentasBancos.buscarCuentasBanco(getEm());
         return listaCuentas;
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " consultarCuentasBancos ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Bancos> consultarBancos() {
      try {
         List<Bancos> listaBancos = persistenciaCuentasBancos.buscarBancos(getEm());
         return listaBancos;
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " consultarBancos ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Inforeportes> consultarInfoReportes() {
      try {
         List<Inforeportes> listaReportes = persistenciaCuentasBancos.buscarReportes(getEm());
         return listaReportes;
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " consultarInfoReportes ERROR: " + e);
         return null;
      }
   }
}
