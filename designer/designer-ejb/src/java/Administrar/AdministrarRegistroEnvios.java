/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.EnvioCorreos;
import Entidades.Inforeportes;
import InterfaceAdministrar.AdministrarRegistroEnviosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEnvioCorreosInterface;
import java.math.BigInteger;
import java.util.ArrayList;
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
public class AdministrarRegistroEnvios implements AdministrarRegistroEnviosInterface {

   private static Logger log = Logger.getLogger(AdministrarRegistroEnvios.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaEnvioCorreosInterface persistenciaEnvioCorreos;

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
   private EnvioCorreos ec;

   @Override
   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<EnvioCorreos> consultarEnvioCorreos(BigInteger reporte) {
      log.warn("Administrar.AdministrarRegistroEnvios.consultarEnvioCorreos()");
      try {
         return persistenciaEnvioCorreos.consultarEnvios(getEm(), reporte);
      } catch (Exception e) {
         log.warn("Error Administrar.AdministrarRegistroEnvios.consultarEnvioCorreos() " + e);
         return new ArrayList<>();
      }
   }

   @Override
   public Inforeportes consultarPorSecuencia(BigInteger envio) {
      log.warn("Administrar.AdministrarRegistroEnvios.consultarPorSecuencia()");
      log.warn("envio: " + envio);
      try {
         return persistenciaEnvioCorreos.buscarEnvioCorreoporSecuencia(getEm(), envio);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarPorSecuencia() ERROR: " + e);
         return null;
      }
   }

   @Override
   public void editarEnvioCorreos(EnvioCorreos listaEC) {
      try {
         log.warn("Administrar.AdministrarRegistroEnvios.editarEnvioCorreos()  " + listaEC.getSecuencia());
         persistenciaEnvioCorreos.editar(getEm(), listaEC);
      } catch (Exception ex) {
         log.warn("Error Administrar.AdministrarRegistroEnvios.editarEnvioCorreos() " + ex);
      }
   }

   @Override
   public void modificarEC(List<EnvioCorreos> listECModificadas) {
      try {
         for (int i = 0; i < listECModificadas.size(); i++) {
            log.warn("Modificando...");
            if (listECModificadas.get(i).getCodigoEmpleado() != null && listECModificadas.get(i).getCodigoEmpleado().getSecuencia() == null) {
               listECModificadas.get(i).setCodigoEmpleado(null);
               ec = listECModificadas.get(i);
               persistenciaEnvioCorreos.editar(getEm(), ec);
            } else {
               ec = listECModificadas.get(i);
               persistenciaEnvioCorreos.editar(getEm(), ec);
            }
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarEC() ERROR: " + e);
      }
   }

   @Override
   public void borrarEnvioCorreos(EnvioCorreos listaEC) {
      try {
         persistenciaEnvioCorreos.borrar(getEm(), listaEC);
      } catch (Exception e) {
         log.warn("Error" + e);
      }
   }

}
