/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Causasausentismos;
import Entidades.Clasesausentismos;
import Entidades.Diagnosticoscategorias;
import Entidades.Empleados;
import Entidades.EnfermeadadesProfesionales;
import Entidades.Enfermedades;
import Entidades.Ibcs;
import Entidades.Soaccidentes;
import Entidades.Soausentismos;
import Entidades.Terceros;
import Entidades.Tiposausentismos;
import InterfaceAdministrar.AdministrarSoausentismosInterface;
import InterfacePersistencia.PersistenciaCausasAusentismosInterface;
import InterfacePersistencia.PersistenciaClasesAusentismosInterface;
import InterfacePersistencia.PersistenciaDiagnosticosCategoriasInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEnfermedadesInterface;
import InterfacePersistencia.PersistenciaEnfermedadesProfesionalesInterface;
import InterfacePersistencia.PersistenciaIBCSInterface;
import InterfacePersistencia.PersistenciaRelacionesIncapacidadesInterface;
import InterfacePersistencia.PersistenciaSoaccidentesInterface;
import InterfacePersistencia.PersistenciaSoausentismosInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaTiposAusentismosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

@Stateful
public class AdministrarSoausentismos implements AdministrarSoausentismosInterface {

   private static Logger log = Logger.getLogger(AdministrarSoausentismos.class);

   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleados;
   @EJB
   PersistenciaTercerosInterface persistenciaTerceros;
   @EJB
   PersistenciaSoausentismosInterface persistenciaSoausentismos;
   @EJB
   PersistenciaSoaccidentesInterface persistenciaSoaccdicentes;
   @EJB
   PersistenciaTiposAusentismosInterface persistenciaTiposAusentismos;
   @EJB
   PersistenciaClasesAusentismosInterface persistenciaClasesAusentismos;
   @EJB
   PersistenciaCausasAusentismosInterface persistenciaCausasAusentismos;
   @EJB
   PersistenciaIBCSInterface persistenciaIBCS;
   @EJB
   PersistenciaEnfermedadesProfesionalesInterface persistenciaEP;
   @EJB
   PersistenciaDiagnosticosCategoriasInterface persistenciaDiagnosticos;
   @EJB
   PersistenciaEnfermedadesInterface persistenciaEnfermedades;
   @EJB
   PersistenciaRelacionesIncapacidadesInterface persistenciaRelacionesIncapacidades;
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
   public void borrarAusentismos(Soausentismos ausentismos) {
      try {
         persistenciaSoausentismos.borrar(getEm(), ausentismos);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearAusentismos(Soausentismos ausentismos) {
      try {
         persistenciaSoausentismos.crear(getEm(), ausentismos);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void modificarAusentismos(List<Soausentismos> listaAusentismosModificar) {
      try {
         for (int i = 0; i < listaAusentismosModificar.size(); i++) {
            log.warn("Modificando...");

            if (listaAusentismosModificar.get(i).getDias() == null) {
               listaAusentismosModificar.get(i).setDias(null);
            }
            if (listaAusentismosModificar.get(i).getHoras() == null) {
               listaAusentismosModificar.get(i).setHoras(null);
            }
            if (listaAusentismosModificar.get(i).getFechafinaus() == null) {
               listaAusentismosModificar.get(i).setFechafinaus(null);
            }
            if (listaAusentismosModificar.get(i).getFechaexpedicion() == null) {
               listaAusentismosModificar.get(i).setFechaexpedicion(null);
            }
            if (listaAusentismosModificar.get(i).getFechainipago() == null) {
               listaAusentismosModificar.get(i).setFechainipago(null);
            }
            if (listaAusentismosModificar.get(i).getFechafinpago() == null) {
               listaAusentismosModificar.get(i).setFechafinpago(null);
            }
            if (listaAusentismosModificar.get(i).getPorcentajeindividual() == null) {
               listaAusentismosModificar.get(i).setPorcentajeindividual(null);
            }
            if (listaAusentismosModificar.get(i).getBaseliquidacion() == null) {
               listaAusentismosModificar.get(i).setBaseliquidacion(null);
            }
            if (listaAusentismosModificar.get(i).getFormaliquidacion() == null) {
               listaAusentismosModificar.get(i).setFormaliquidacion(null);
            }
            if (listaAusentismosModificar.get(i).getAccidente().getSecuencia() == null) {
               listaAusentismosModificar.get(i).setAccidente(null);
            }
            if (listaAusentismosModificar.get(i).getEnfermedad().getSecuencia() == null) {
               listaAusentismosModificar.get(i).setEnfermedad(null);
            }
            if (listaAusentismosModificar.get(i).getNumerocertificado() == null) {
               listaAusentismosModificar.get(i).setNumerocertificado(null);
            }
            if (listaAusentismosModificar.get(i).getDiagnosticocategoria().getSecuencia() == null) {
               listaAusentismosModificar.get(i).setDiagnosticocategoria(null);
            }
            if (listaAusentismosModificar.get(i).getProrroga().getSecuencia() == null) {
               listaAusentismosModificar.get(i).setProrroga(null);
            }
            if (listaAusentismosModificar.get(i).getRelacion() == null) {
               listaAusentismosModificar.get(i).setRelacion(null);
            }
            if (listaAusentismosModificar.get(i).getRelacionadaBool() == false) {
               listaAusentismosModificar.get(i).setRelacionada("N");
            }
            if (listaAusentismosModificar.get(i).getTercero().getSecuencia() == null) {
               listaAusentismosModificar.get(i).setTercero(null);
            }
            if (listaAusentismosModificar.get(i).getObservaciones() == null) {
               listaAusentismosModificar.get(i).setObservaciones(null);
            }
            persistenciaSoausentismos.editar(getEm(), listaAusentismosModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   //Trae los ausentismos del empleado cuya secuencia se envía como parametro//
   @Override
   public List<Soausentismos> ausentismosEmpleado(BigInteger secuenciaEmpleado) {
      try {
         return persistenciaSoausentismos.ausentismosEmpleado(getEm(), secuenciaEmpleado);
      } catch (Exception e) {
         log.error("Error AdministrarNovedadesEmpleados.novedadesEmpleado" + e);
         return null;
      }
   }

   //Lista de Valores Empleados
   @Override
   public List<Empleados> lovEmpleados() {
      try {
         log.warn("Administrar.AdministrarSoausentismos.lovEmpleados()");
         return persistenciaEmpleados.buscarEmpleadosActivos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Tiposausentismos> lovTiposAusentismos() {
      try {
         return persistenciaTiposAusentismos.consultarTiposAusentismos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Clasesausentismos> lovClasesAusentismos() {
      try {
         return persistenciaClasesAusentismos.buscarClasesAusentismos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Causasausentismos> lovCausasAusentismos() {
      try {
         return persistenciaCausasAusentismos.buscarCausasAusentismos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Soaccidentes> lovAccidentes(BigInteger secuenciaEmpleado) {
      try {
         return persistenciaSoaccdicentes.accidentesEmpleado(getEm(), secuenciaEmpleado);
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

   public List<Diagnosticoscategorias> lovDiagnosticos() {
      try {
         return persistenciaDiagnosticos.buscarDiagnosticos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Ibcs> empleadosIBCS(BigInteger secuenciaEmpleado) {
      try {
         return persistenciaIBCS.buscarIbcsPorEmpleado(getEm(), secuenciaEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Enfermedades> enfermedades() {
      try {
         return persistenciaEnfermedades.buscarEnfermedades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<EnfermeadadesProfesionales> empleadosEP(BigInteger secuenciaEmpleado) {
      try {
         return persistenciaEP.buscarEPPorEmpleado(getEm(), secuenciaEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   //MostrarProrroga
   public String mostrarProrroga(BigInteger secuenciaProrroga) {
      try {
         return persistenciaSoausentismos.prorrogaMostrar(getEm(), secuenciaProrroga);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   //MostrarRelacion
   public String mostrarRelacion(BigInteger secuenciaAusentismo) {
      try {
         return persistenciaRelacionesIncapacidades.relaciones(getEm(), secuenciaAusentismo);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   //LOV Prorrogas
   public List<Soausentismos> lovProrrogas(BigInteger secEmpleado, BigInteger secuenciaCausa, BigInteger secuenciaAusentismo) {
      try {
         return persistenciaSoausentismos.prorrogas(getEm(), secEmpleado, secuenciaCausa, secuenciaAusentismo);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }
}
