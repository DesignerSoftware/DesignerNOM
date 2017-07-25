/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.PlantillasValidaLL;
import Entidades.PlantillasValidaNL;
import Entidades.PlantillasValidaRL;
import Entidades.PlantillasValidaTC;
import Entidades.PlantillasValidaTS;
import Entidades.TiposTrabajadores;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarTiposTrabajadoresPlantillasInterface;
import InterfacePersistencia.PersistenciaPlantillasValidaLLInterface;
import InterfacePersistencia.PersistenciaPlantillasValidaNLInterface;
import InterfacePersistencia.PersistenciaPlantillasValidaRLInterface;
import InterfacePersistencia.PersistenciaPlantillasValidaTCInterface;
import InterfacePersistencia.PersistenciaPlantillasValidaTSInterface;
import InterfacePersistencia.PersistenciaTiposTrabajadoresInterface;
import InterfacePersistencia.PersistenciaTiposTrabajadoresPlantillasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
@LocalBean
public class AdministrarTiposTrabajadoresPlantillas implements AdministrarTiposTrabajadoresPlantillasInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposTrabajadoresPlantillas.class);

   @EJB
   PersistenciaTiposTrabajadoresInterface persistenciaTipoTrabajador;
   @EJB
   PersistenciaTiposTrabajadoresPlantillasInterface persistenciaTTPlantillas;
   @EJB
   PersistenciaPlantillasValidaTCInterface persistenciaPlantillasTC;
   @EJB
   PersistenciaPlantillasValidaTSInterface persistenciaPlantillasTS;
   @EJB
   PersistenciaPlantillasValidaRLInterface persistenciaPlantillasRL;
   @EJB
   PersistenciaPlantillasValidaLLInterface persistenciaPlantillasLL;
   @EJB
   PersistenciaPlantillasValidaNLInterface persistenciaPlantillasNL;

   @EJB
   AdministrarSesionesInterface administrarSesiones;

   private EntityManager em;

   @Override
   public void obtenerConexion(String idSesion) {
      em = administrarSesiones.obtenerConexionSesion(idSesion);
   }

   @Override
   public void crearTT(TiposTrabajadores tiposTrabajadores) {
      persistenciaTipoTrabajador.crear(em, tiposTrabajadores);
   }

   @Override
   public void editarTT(TiposTrabajadores tiposTrabajadores) {
      persistenciaTipoTrabajador.editar(em, tiposTrabajadores);
   }

   @Override
   public void borrarTT(TiposTrabajadores tiposTrabajadores) {
      persistenciaTipoTrabajador.borrar(em, tiposTrabajadores);
   }

   @Override
   public void crearPlantillaTC(PlantillasValidaTC plantilla) {
      persistenciaPlantillasTC.crear(em, plantilla);
   }

   @Override
   public void editarPlantillaTC(PlantillasValidaTC plantilla) {
      persistenciaPlantillasTC.editar(em, plantilla);
   }

   @Override
   public void borrarPlantillaTC(PlantillasValidaTC plantilla) {
      persistenciaPlantillasTC.borrar(em, plantilla);
   }

   @Override
   public void crearPlantillaTS(PlantillasValidaTS plantilla) {
      persistenciaPlantillasTS.crear(em, plantilla);
   }

   @Override
   public void editarPlantillaTS(PlantillasValidaTS plantilla) {
      persistenciaPlantillasTS.editar(em, plantilla);
   }

   @Override
   public void borrarPlantillaTS(PlantillasValidaTS plantilla) {
      persistenciaPlantillasTS.borrar(em, plantilla);
   }

   @Override
   public void crearPlantillaRL(PlantillasValidaRL plantilla) {
      persistenciaPlantillasRL.crear(em, plantilla);
   }

   @Override
   public void editarPlantillaRL(PlantillasValidaRL plantilla) {
      persistenciaPlantillasRL.editar(em, plantilla);
   }

   @Override
   public void borrarPlantillaRL(PlantillasValidaRL plantilla) {
      persistenciaPlantillasRL.borrar(em, plantilla);
   }

   @Override
   public void crearPlantillaLL(PlantillasValidaLL plantilla) {
      persistenciaPlantillasLL.crear(em, plantilla);
   }

   @Override
   public void editarPlantillaLL(PlantillasValidaLL plantilla) {
      persistenciaPlantillasLL.editar(em, plantilla);
   }

   @Override
   public void borrarPlantillaLL(PlantillasValidaLL plantilla) {
      persistenciaPlantillasLL.borrar(em, plantilla);
   }

   @Override
   public void crearPlantillaNL(PlantillasValidaNL plantilla) {
      persistenciaPlantillasNL.crear(em, plantilla);
   }

   @Override
   public void editarPlantillaNL(PlantillasValidaNL plantilla) {
      persistenciaPlantillasNL.editar(em, plantilla);
   }

   @Override
   public void borrarPlantillaNL(PlantillasValidaNL plantilla) {
      persistenciaPlantillasNL.borrar(em, plantilla);
   }

   @Override
   public List<TiposTrabajadores> listaTT() {
      List<TiposTrabajadores> lista = persistenciaTTPlantillas.consultarTiposTrabajadores(em);
      return lista;
   }

   @Override
   public List<PlantillasValidaTC> listaPlantillaTC(BigInteger secTT) {
      List<PlantillasValidaTC> lista = persistenciaTTPlantillas.consultarPlanillaTC(em, secTT);
      return lista;
   }

   @Override
   public List<PlantillasValidaTS> listaPlantillaTS(BigInteger secTT) {
      List<PlantillasValidaTS> lista = persistenciaTTPlantillas.consultarPlanillaTS(em, secTT);
      return lista;
   }

   @Override
   public List<PlantillasValidaRL> listaPlantillaRL(BigInteger secTT) {
      List<PlantillasValidaRL> lista = persistenciaTTPlantillas.consultarPlanillaRL(em, secTT);
      return lista;
   }

   @Override
   public List<PlantillasValidaLL> listaPlantillaLL(BigInteger secTT) {
      List<PlantillasValidaLL> lista = persistenciaTTPlantillas.consultarPlanillaLL(em, secTT);
      return lista;
   }

   @Override
   public List<PlantillasValidaNL> listaPlantillaNL(BigInteger secTT) {
      List<PlantillasValidaNL> lista = persistenciaTTPlantillas.consultarPlanillaNL(em, secTT);
      return lista;
   }
}
