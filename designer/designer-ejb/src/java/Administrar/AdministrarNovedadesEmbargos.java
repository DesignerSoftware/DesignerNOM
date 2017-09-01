/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.DetallesFormasDtos;
import Entidades.EersPrestamos;
import Entidades.EersPrestamosDtos;
import Entidades.Empleados;
import Entidades.FormasDtos;
import Entidades.Juzgados;
import Entidades.MotivosEmbargos;
import Entidades.Periodicidades;
import Entidades.Terceros;
import Entidades.TiposEmbargos;
import Entidades.VWPrestamoDtosRealizados;
import InterfaceAdministrar.AdministrarNovedadesEmbargosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaDetallesFormasDtosInterface;
import InterfacePersistencia.PersistenciaEersPrestamosDtosInterface;
import InterfacePersistencia.PersistenciaEersPrestamosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaFormasDtosInterface;
import InterfacePersistencia.PersistenciaJuzgadosInterface;
import InterfacePersistencia.PersistenciaMotivosEmbargosInterface;
import InterfacePersistencia.PersistenciaPeriodicidadesInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaTiposEmbargosInterface;
import InterfacePersistencia.PersistenciaVWPrestamoDtosRealizadosInterface;
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
public class AdministrarNovedadesEmbargos implements AdministrarNovedadesEmbargosInterface {

   private static Logger log = Logger.getLogger(AdministrarNovedadesEmbargos.class);

   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleados;
   @EJB
   PersistenciaJuzgadosInterface persistenciaJuzgados;
   @EJB
   PersistenciaTiposEmbargosInterface persistenciaTiposEmbargos;
   @EJB
   PersistenciaMotivosEmbargosInterface persistenciaMotivosEmbargos;
   @EJB
   PersistenciaTercerosInterface persistenciaTerceros;
   @EJB
   PersistenciaEersPrestamosInterface persistenciaEers;
   @EJB
   PersistenciaEersPrestamosDtosInterface persistenciaEersDtos;
   @EJB
   PersistenciaFormasDtosInterface persistenciaFormasDtos;
   @EJB
   PersistenciaPeriodicidadesInterface persistenciaPeriodicidades;
   @EJB
   PersistenciaDetallesFormasDtosInterface persistenciaDetallesFormasDtos;
   @EJB
   PersistenciaVWPrestamoDtosRealizadosInterface persistenciaVWPrestamo;
   public EersPrestamosDtos dE;
   public EersPrestamos e;
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
   public List<Empleados> listaEmpleados() {
      try {
         return persistenciaEmpleados.buscarEmpleadosActivos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

//   @Override
//   public List<Empleados> lovEmpleados() {
//      try {
//         return persistenciaEmpleados.buscarEmpleadosActivos(getEm());
//      } catch (Exception e) {
//         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
//         return null;
//      }
//   }

   public List<TiposEmbargos> lovTiposEmbargos() {
      try {
         return persistenciaTiposEmbargos.buscarTiposEmbargos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Juzgados> lovJuzgados() {
      try {
         return persistenciaJuzgados.buscarJuzgados(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<MotivosEmbargos> lovMotivosEmbargos() {
      try {
         return persistenciaMotivosEmbargos.buscarMotivosEmbargos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Terceros> lovTerceros() {
      try {
         return persistenciaTerceros.buscarTerceros(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Periodicidades> lovPeriodicidades() {
      try {
         return persistenciaPeriodicidades.consultarPeriodicidades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<FormasDtos> lovFormasDtos(BigInteger tipoEmbargo) {
      try {
         return persistenciaFormasDtos.formasDescuentos(getEm(), tipoEmbargo);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   //Segunda Tabla
   public List<EersPrestamos> eersPrestamosEmpleado(BigInteger secuenciaEmpleado) {
      try {
         return persistenciaEers.eersPrestamosEmpleado(getEm(), secuenciaEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   //LOV Formas de descuento segunda tabla
   public List<DetallesFormasDtos> lovDetallesFormasDescuentos(BigInteger formasDtos) {
      try {
         return persistenciaDetallesFormasDtos.detallesFormasDescuentos(getEm(), formasDtos);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<FormasDtos> formasDescuentos(BigInteger tipoEmbargo) {
      try {
         return persistenciaFormasDtos.formasDescuentos(getEm(), tipoEmbargo);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   //Tercera Tabla
   public List<EersPrestamosDtos> eersPrestamosEmpleadoDtos(BigInteger secuenciaEersPrestamo) {
      try {
         return persistenciaEersDtos.eersPrestamosDtosEmpleado(getEm(), secuenciaEersPrestamo);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<VWPrestamoDtosRealizados> prestamosRealizados(BigInteger secuencia) {
      try {
         return persistenciaVWPrestamo.buscarPrestamosDtos(getEm(), secuencia);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   //AGREGAR, BORRAR Y MODIFICAR DE LA TABLA DE ABAJO //
   @Override
   public void modificarDetalleEmbargo(List<EersPrestamosDtos> listaDetallesEmbargosModificar) {
      try {
         for (int i = 0; i < listaDetallesEmbargosModificar.size(); i++) {
            log.warn("Modificando...");
            if (listaDetallesEmbargosModificar.get(i).getValor() == null) {
               listaDetallesEmbargosModificar.get(i).setValor(null);
            }
            if (listaDetallesEmbargosModificar.get(i).getPorcentaje() == null) {
               listaDetallesEmbargosModificar.get(i).setPorcentaje(null);
            }
            if (listaDetallesEmbargosModificar.get(i).getSaldoinicial() == null) {
               listaDetallesEmbargosModificar.get(i).setSaldoinicial(null);
            }

            persistenciaEersDtos.editar(getEm(), dE);
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarDetalleEmbargo(EersPrestamosDtos detallesEmbargos) {
      try {
         persistenciaEersDtos.borrar(getEm(), detallesEmbargos);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearDetalleEmbargo(EersPrestamosDtos detallesEmbargos) {
      try {
         persistenciaEersDtos.crear(getEm(), detallesEmbargos);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }
   //AGREGAR, BORRAR Y MODIFICAR DE LA TABLA DE Arriba //

   @Override
   public void modificarEmbargo(List<EersPrestamos> listaEmbargosModificar) {
      try {
         for (int i = 0; i < listaEmbargosModificar.size(); i++) {
            log.warn("ListaEmbargosModificar " + listaEmbargosModificar.size());
            log.warn("Modificando...");
            if (listaEmbargosModificar.get(i).getTipoembargo() == null) {
               listaEmbargosModificar.get(i).setTipoembargo(null);
            }
            if (listaEmbargosModificar.get(i).getCancelaciondocumento() == null) {
               listaEmbargosModificar.get(i).setCancelaciondocumento(null);
            }
            if (listaEmbargosModificar.get(i).getCancelacionfechahasta() == null) {
               listaEmbargosModificar.get(i).setCancelacionfechahasta(null);
            }
            if (listaEmbargosModificar.get(i).getMotivoembargo() == null) {
               listaEmbargosModificar.get(i).setMotivoembargo(null);
            }
            if (listaEmbargosModificar.get(i).getJuzgado() == null) {
               listaEmbargosModificar.get(i).setJuzgado(null);
            }
            if (listaEmbargosModificar.get(i).getNumeroproceso() == null) {
               listaEmbargosModificar.get(i).setNumeroproceso(null);
            }

            persistenciaEers.editar(getEm(), listaEmbargosModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarEmbargo(EersPrestamos embargos) {
      try {
         persistenciaEers.borrar(getEm(), embargos);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearEmbargo(EersPrestamos embargos) {
      try {
         persistenciaEers.crear(getEm(), embargos);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }
}
