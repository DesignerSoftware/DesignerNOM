/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.Estructuras;
import InterfaceAdministrar.AdministrarReingresarEmpleadoInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import java.math.BigDecimal;
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
public class AdministrarReingresarEmpleado implements AdministrarReingresarEmpleadoInterface {

   private static Logger log = Logger.getLogger(AdministrarReingresarEmpleado.class);

   @EJB
   PersistenciaPersonasInterface persistenciaPersonas;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   @EJB
   PersistenciaEstructurasInterface persistenciaEstructuras;
   @EJB
   AdministrarSesionesInterface administrarSesiones;
   private EntityManager em;
   private List<Empleados> lovEmpleados;
   private List<Estructuras> lovEstructuras;
   private Date fechaDeRetiro;

   @Override
   public void obtenerConexion(String idSesion) {
      em = administrarSesiones.obtenerConexionSesion(idSesion);
   }

   public Date obtenerFechaRetiro(BigInteger secuenciaEmpleado) {
      fechaDeRetiro = persistenciaEmpleado.verificarFecha(em, secuenciaEmpleado);
      return fechaDeRetiro;
   }

   public void reintegrarEmpleado(BigDecimal codigoEmpleado, String centroCosto, Date fechaReingreso, short empresa, Date fechaFinal) {
      try {
         persistenciaEmpleado.reingresarEmpleado(em, codigoEmpleado, centroCosto, fechaReingreso, empresa, fechaFinal);
      } catch (Exception e) {
         log.warn("ERROR - AdministrarReingresarEmpleado.reintegrarEmpleado() ERROR");
      }
   }

   public List<Empleados> listaEmpleados() {
      return persistenciaEmpleado.consultarEmpleadosReingreso(em);
   }

   public List<Estructuras> listaEstructuras() {
      return persistenciaEstructuras.consultarEstructurasReingreso(em);
   }

}
