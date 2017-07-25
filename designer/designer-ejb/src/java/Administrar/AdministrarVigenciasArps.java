/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Cargos;
import Entidades.Estructuras;
import Entidades.VigenciasArps;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarVigenciasArpsInterface;
import InterfacePersistencia.PersistenciaCargosInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaVigenciasArpsInterface;
import java.math.BigInteger;
import java.util.Date;
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
public class AdministrarVigenciasArps implements AdministrarVigenciasArpsInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciasArps.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaVigenciasArpsInterface persistenciaVigenciasArp;
   @EJB
   PersistenciaEstructurasInterface persistenciaEstructuras;
   @EJB
   PersistenciaCargosInterface persistenciaCargos;
   EntityManager em;

   @Override
   public void obtenerConexion(String idSesion) {
      em = administrarSesiones.obtenerConexionSesion(idSesion);
   }

   @Override
   public void modificarVArp(VigenciasArps vigarp) {
      persistenciaVigenciasArp.editar(em, vigarp);
   }

   @Override
   public void borrarVArp(VigenciasArps vigarp) {
      persistenciaVigenciasArp.borrar(em, vigarp);
   }

   @Override
   public void crearVArp(VigenciasArps vigarp) {
      persistenciaVigenciasArp.crear(em, vigarp);
   }

   @Override
   public String buscarPorcentaje(BigInteger estructura, BigInteger cargo, Date fecha) {
      log.warn("estructura : " + estructura);
      log.warn("cargo : " + cargo);
      log.warn("fecha : " + fecha);
      try {
         String resultado = persistenciaVigenciasArp.actualARP(em, estructura, cargo, fecha);
         return resultado;
      } catch (Exception e) {
         log.warn("error en AdministrarVigenciasArps.buscarPorcentaje() : " + e.toString());
         return null;
      }
   }
   
   public int contarVigenciasARPsPorEstructuraYCargo(BigInteger estructura, BigInteger cargo){
      return persistenciaVigenciasArp.contarVigenciasARPsPorEstructuraYCargo(em, estructura, cargo);
   }

   public List<VigenciasArps> consultarVigenciasArps() {
      return persistenciaVigenciasArp.consultarVigenciasArps(em);
   }

   @Override
   public List<Estructuras> consultarTodoEstructuras() {
      List<Estructuras> estructuras;
      try {
         estructuras = persistenciaEstructuras.buscarEstructuras(em);
      } catch (Exception ex) {
         estructuras = null;
      }
      return estructuras;
   }

   @Override
   public List<Cargos> consultarTodoCargos() {
      List<Cargos> cargos;
      try {
         cargos = persistenciaCargos.consultarCargos(em);
      } catch (Exception ex) {
         cargos = null;
      }
      return cargos;
   }
}
