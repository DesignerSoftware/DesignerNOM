/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.AnterioresContratos;
import Entidades.Cargos;
import InterfaceAdministrar.AdministrarAnterioresContratosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaAnterioresContratosInterface;
import InterfacePersistencia.PersistenciaCargosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.Local;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
@Local
public class AdministrarAnterioresContratos implements AdministrarAnterioresContratosInterface {

   private static Logger log = Logger.getLogger(AdministrarAnterioresContratos.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaAnterioresContratosInterface persistenciaAnterioresContrato;
   @EJB
   PersistenciaCargosInterface persistenciaCargos;
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
   public void crearAnteriorContrato(List<AnterioresContratos> listaCrear) {
      try {
         for (int i = 0; i < listaCrear.size(); i++) {
            persistenciaAnterioresContrato.crear(getEm(), listaCrear.get(i));
         }
      } catch (Exception e) {
         log.warn("Errror crearAnteriorContrato admi : " + e.toString());
      }
   }

   @Override
   public void editarAnteriorContrato(List<AnterioresContratos> listaModificar) {
      try {
         for (int i = 0; i < listaModificar.size(); i++) {
            persistenciaAnterioresContrato.editar(getEm(), listaModificar.get(i));
         }
      } catch (Exception e) {
         log.warn("Errror editarAnteriorContrato admi : " + e.toString());
      }
   }

   @Override
   public void borrarAnteriorContrato(List<AnterioresContratos> listaBorrar) {
      try {
         for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaAnterioresContrato.borrar(getEm(), listaBorrar.get(i));
         }
      } catch (Exception e) {
         log.warn("Errror borrarAnteriorContrato admi : " + e.toString());
      }
   }

   @Override
   public List<AnterioresContratos> listaAnterioresContratos(BigInteger secPersona) {
      try {
         List<AnterioresContratos> listaAC = persistenciaAnterioresContrato.anterioresContratosPersona(getEm(), secPersona);
         return listaAC;
      } catch (Exception e) {
         log.warn("Error listaAnterioresContratos.admi :" + e.toString());
         return null;
      }
   }

   @Override
   public List<Cargos> lovCargos() {
      try {
         List<Cargos> lovCargos = persistenciaCargos.consultarCargos(getEm());
         return lovCargos;
      } catch (Exception e) {
         log.warn("Error lovCargos Admi " + e.getMessage());
         return null;
      }
   }

}
