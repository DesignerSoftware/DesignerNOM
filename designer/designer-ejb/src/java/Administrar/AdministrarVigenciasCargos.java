package Administrar;

import Entidades.ClasesRiesgos;
import Entidades.Empleados;
import Entidades.Papeles;
//import Entidades.VWActualesTiposTrabajadores;
import Entidades.VigenciasCargos;
import Entidades.VwTiposEmpleados;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarVigenciasCargosInterface;
import InterfacePersistencia.PersistenciaClasesRiesgosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaPapelesInterface;
import InterfacePersistencia.PersistenciaVigenciasArpsInterface;
//import InterfacePersistencia.PersistenciaVWActualesTiposTrabajadoresInterface;
import InterfacePersistencia.PersistenciaVigenciasCargosInterface;
import InterfacePersistencia.PersistenciaVwTiposEmpleadosInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author Hugo Sin y -Felipphe-
 */
@Stateful
public class AdministrarVigenciasCargos implements AdministrarVigenciasCargosInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciasCargos.class);

   @EJB
   PersistenciaVigenciasCargosInterface persistenciaVigenciasCargos;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   @EJB
   PersistenciaVwTiposEmpleadosInterface persistenciaTiposEmpleados;
   @EJB
   PersistenciaClasesRiesgosInterface persistenciaClasesRiesgos;
   @EJB
   PersistenciaVigenciasArpsInterface persistenciaVigenciaArp;
   @EJB
   PersistenciaPapelesInterface persistenciaPapeles;
   @EJB
   PersistenciaEmpresasInterface persistenciaEmpresas;
   //PersistenciaVWActualesTiposTrabajadoresInterface persistenciaVWActualesTiposTrabajadores;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

//   private List<VigenciasCargos> vigenciasCargos;
//   public List<VwTiposEmpleados> tipoEmpleadoLista;
//   private VigenciasCargos vc;
//   private Empleados empleado;
   private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
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

   /*
     public AdministrarVigenciasCargos() {
     persistenciaVigenciasCargos = new PersistenciaVigenciasCargos();
     }
    */
   @Override
   public List<VigenciasCargos> consultarTodo() {
      try {
         return persistenciaVigenciasCargos.buscarVigenciasCargos(getEm());
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   public VigenciasCargos consultarPorSecuencia(BigInteger secuenciaVC) {
      try {
         return persistenciaVigenciasCargos.buscarVigenciaCargo(getEm(), secuenciaVC);
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   public List<VigenciasCargos> vigenciasEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVigenciasCargos.buscarVigenciasCargosEmpleado(getEm(), secEmpleado);
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   public void editarVigenciaCargo(VigenciasCargos vigencia) {
      try {
         log.warn("administrarEmplVig editarVig: editar Vigencia = " + vigencia.getSecuencia());
         persistenciaVigenciasCargos.editar(getEm(), vigencia);
      } catch (Exception ex) {
         log.warn("administrarEmplVig editarVig: FALLO EN EL EDITAR");
      }
   }

   public void modificarVC(List<VigenciasCargos> listVCModificadas) {
      try {
         for (int i = 0; i < listVCModificadas.size(); i++) {
            log.warn("Modificando...");
            if (listVCModificadas.get(i).getEmpleadojefe() != null && listVCModificadas.get(i).getEmpleadojefe().getSecuencia() == null) {
               listVCModificadas.get(i).setEmpleadojefe(null);
            }
            persistenciaVigenciasCargos.editar(getEm(), listVCModificadas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void borrarVC(VigenciasCargos vigenciasCargos) {
      try {
         persistenciaVigenciasCargos.borrar(getEm(), vigenciasCargos);
      } catch (Exception e) {
         log.warn("Error" + e);
      }

   }

   public void crearVC(VigenciasCargos vigenciasCargos) {
      try {
         persistenciaVigenciasCargos.crear(getEm(), vigenciasCargos);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public Empleados buscarEmpleado(BigInteger secuencia) {
      try {
         return persistenciaEmpleado.buscarEmpleadoSecuencia(getEm(), secuencia);
      } catch (Exception e) {
         return null;
      }
   }

   public List<VwTiposEmpleados> FiltrarTipoTrabajador() {
      try {
         return persistenciaTiposEmpleados.buscarTiposEmpleadosPorTipo(getEm(), "ACTIVO");
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   public List<ClasesRiesgos> lovClasesRiesgos() {
      try {
         return persistenciaClasesRiesgos.consultarListaClasesRiesgos(getEm());
      } catch (Exception e) {
         log.warn("error en AdministrarVigenciasCargos.lovClasesRiesgos : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Papeles> lovPapeles(BigInteger secEmpresa) {
      try {
         log.warn("secuencia de la empresa en lovPapeles : " + secEmpresa);
         return persistenciaPapeles.consultarPapelesEmpresa(getEm(), secEmpresa);
      } catch (Exception e) {
         log.warn("error en AdministrarVigenciasCargos.lovPapeles: " + e.toString());
         return null;
      }
   }

   @Override
   public BigDecimal consultarEmpresaPorEmpl(BigInteger secEmpl) {
      try {
         return persistenciaEmpresas.consultarEmpresaPorEmpleado(getEm(), secEmpl);
      } catch (Exception e) {
         log.warn("error en AdministrarVigenciasCargos.consultarEmpresaPorEmpl : " + e.toString());
         return null;
      }
   }
}
