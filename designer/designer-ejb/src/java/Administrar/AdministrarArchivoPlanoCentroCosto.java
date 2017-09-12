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
   public void crear(List<TempProrrateos> listaTempPP) {
      try {
         for (int i = 0; i < listaTempPP.size(); i++) {
            persistenciaTempProrrateos.crear(getEm(), listaTempPP.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crear() ERROR: " + e);
      }
   }

   @Override
   public void editar(TempProrrateos tempPP) {
      try {
         persistenciaTempProrrateos.editar(getEm(), tempPP);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".editar() ERROR: " + e);
      }
   }

   @Override
   public void borrar(TempProrrateos tempPP) {
      try {
         persistenciaTempProrrateos.borrar(getEm(), tempPP);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrar() ERROR: " + e);
      }
   }

   @Override
   public void borrarRegistrosTempProrrateos(String usuarioBD) {
      try {
         persistenciaTempProrrateos.borrarRegistrosTempProrrateos(getEm(), usuarioBD);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarRegistrosTempProrrateos() ERROR: " + e);
      }
   }

   @Override
   public List<TempProrrateos> obtenerTempProrrateos(String usuarioBD) {
      try {
         return persistenciaTempProrrateos.obtenerTempProrrateos(getEm(), usuarioBD);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".obtenerTempProrrateos() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<NombresEmpleadosAux> consultarNombresEmpleados() {
      try {
         List<Empleados> listaEmpleados = persistenciaEmpleados.buscarEmpleadosActivos(getEm());
         List<NombresEmpleadosAux> listaNombres = new ArrayList<NombresEmpleadosAux>();
         if (listaEmpleados != null) {
            if (!listaEmpleados.isEmpty()) {
               for (Empleados recEmp : listaEmpleados) {
                  listaNombres.add(new NombresEmpleadosAux(recEmp.getCodigoempleado().toBigInteger(), recEmp.getNombreCompleto()));
               }
            }
         }
         return listaNombres;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarNombresEmpleados() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<String> obtenerDocumentosSoporteCargados() {
      try {
         return persistenciaTempProrrateos.obtenerDocumentosSoporteCargados(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".obtenerDocumentosSoporteCargados() ERROR: " + e);
         return null;
      }
   }

   @Override
   public ActualUsuario actualUsuario() {
      try {
         return persistenciaActualUsuario.actualUsuarioBD(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".actualUsuario() ERROR: " + e);
         return null;
      }
   }

   @Override
   public void cargarTempProrrateos() {
      try {
         persistenciaTempProrrateos.cargarTempProrrateos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".cargarTempProrrateos() ERROR: " + e);
      }
   }

   @Override
   public int reversarTempProrrateos(ActualUsuario usuarioBD, String documentoSoporte) {
      try {
         return persistenciaTempProrrateos.reversarTempProrrateos(getEm(), usuarioBD.getAlias(), documentoSoporte);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".reversarTempProrrateos() ERROR: " + e);
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
