/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.ActualUsuario;
import Entidades.Empleados;
import Entidades.Generales;
import Entidades.NombresEmpleadosAux;
import Entidades.TempProrrateos;
import InterfaceAdministrar.AdministrarArchivoPlanoCentroCostoInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaGeneralesInterface;
import InterfacePersistencia.PersistenciaTempProrrateosInterface;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

@Stateful
@Local
public class AdministrarArchivoPlanoCentroCosto implements AdministrarArchivoPlanoCentroCostoInterface {

   private static Logger log = Logger.getLogger(AdministrarArchivoPlanoCentroCosto.class);

   @EJB
   PersistenciaTempProrrateosInterface persistenciaTempProrrateos;
   @EJB
   PersistenciaGeneralesInterface persistenciaGenerales;
   @EJB
   PersistenciaActualUsuarioInterface persistenciaActualUsuario;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleados;
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
   public void crear(List<TempProrrateos> listaTempPP) {
      try {
         for (int i = 0; i < listaTempPP.size(); i++) {
            persistenciaTempProrrateos.crear(getEm(), listaTempPP.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void editar(TempProrrateos tempPP) {
      try {
         persistenciaTempProrrateos.editar(getEm(), tempPP);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrar(TempProrrateos tempPP) {
      try {
         persistenciaTempProrrateos.borrar(getEm(), tempPP);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarRegistrosTempProrrateos(String usuarioBD) {
      try {
         persistenciaTempProrrateos.borrarRegistrosTempProrrateos(getEm(), usuarioBD);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<TempProrrateos> obtenerTempProrrateos(String usuarioBD) {
      try {
         return persistenciaTempProrrateos.obtenerTempProrrateos(getEm(), usuarioBD);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<NombresEmpleadosAux> consultarNombresEmpleados() {
      try {
         List<Empleados> listaEmpleados = persistenciaEmpleados.buscarEmpleados(getEm());
         List<NombresEmpleadosAux> listaNombres = new ArrayList<NombresEmpleadosAux>();
         if (listaEmpleados != null) {
            if (!listaEmpleados.isEmpty()) {
               for (Empleados recEmp : listaEmpleados) {
                  listaNombres.add(new NombresEmpleadosAux(recEmp.getCodigoempleado().toBigInteger(), recEmp.getPersona().getNombreCompleto()));
               }
            }
         }
         return listaNombres;
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<String> obtenerDocumentosSoporteCargados() {
      try {
         return persistenciaTempProrrateos.obtenerDocumentosSoporteCargados(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public ActualUsuario actualUsuario() {
      try {
         return persistenciaActualUsuario.actualUsuarioBD(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void cargarTempProrrateos() {
      try {
         persistenciaTempProrrateos.cargarTempProrrateos(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public int reversarTempProrrateos(ActualUsuario usuarioBD, String documentoSoporte) {
      try {
         return persistenciaTempProrrateos.reversarTempProrrateos(getEm(), usuarioBD.getAlias(), documentoSoporte);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return 0;
      }
   }

   @Override
   public String consultarRuta() {
      try {
         Generales general = persistenciaGenerales.obtenerRutas(getEm());
         return general.getUbicareportes();
      } catch (Exception e) {
         log.warn("ERROR AdministrarArchivoPlanoCentroCosto.consultarRuta() e: " + e);
         return "C:\\DesignerRHN\\Reportes\\ArchivosPlanos\\";
      }
   }

}
