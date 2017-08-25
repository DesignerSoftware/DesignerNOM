/**
 * Documentación a cargo de AndresPineda
 */
package Administrar;

import Entidades.Cargos;
import Entidades.Ciudades;
import Entidades.DetallesEmpresas;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.Personas;
import InterfaceAdministrar.AdministrarDetallesEmpresasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCargosInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaDetallesEmpresasInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'DetalleEmpresa'.
 *
 * @author AndresPineda
 */
@Stateful
public class AdministrarDetallesEmpresas implements AdministrarDetallesEmpresasInterface {

   private static Logger log = Logger.getLogger(AdministrarDetallesEmpresas.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaDetallesEmpresas'.
    */
   @EJB
   PersistenciaDetallesEmpresasInterface persistenciaDetallesEmpresas;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaCiudades'.
    */
   @EJB
   PersistenciaCiudadesInterface persistenciaCiudades;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaPersonas'.
    */
   @EJB
   PersistenciaPersonasInterface persistenciaPersonas;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaCargos'.
    */
   @EJB
   PersistenciaCargosInterface persistenciaCargos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaEmpleados'.
    */
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleados;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaEmpresas'.
    */
   @EJB
   PersistenciaEmpresasInterface persistenciaEmpresas;
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

   //--------------------------------------------------------------------------
   //MÉTODOS
   //--------------------------------------------------------------------------
   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<DetallesEmpresas> listaDetallesEmpresasPorSecuencia(BigInteger secEmpresa) {
      try {
         List<DetallesEmpresas> lista = null;
         if (secEmpresa != null) {
            DetallesEmpresas detalle = persistenciaDetallesEmpresas.buscarDetalleEmpresaPorSecuencia(getEm(), secEmpresa);
            lista = new ArrayList<DetallesEmpresas>();
            lista.add(detalle);
         }
         return lista;

      } catch (Exception e) {
         log.warn("Error listaDetallesEmpresasPorSecuencia Admi : " + e.toString());
         return null;
      }
   }

   public List<DetallesEmpresas> listaDetallesEmpresas() {
      try {
         return persistenciaDetallesEmpresas.buscarDetallesEmpresas(getEm());
      } catch (Exception e) {
         log.warn("Error listaDetallesEmpresas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearDetalleEmpresa(List<DetallesEmpresas> listaDE) {
      try {
         for (int i = 0; i < listaDE.size(); i++) {
//            if (listaDE.get(i).getCiudaddocumentorepresentante().getSecuencia() == null) {
//               listaDE.get(i).setCiudaddocumentorepresentante(null);
//            }
//            if (listaDE.get(i).getCargofirmaconstancia().getSecuencia() == null) {
//               listaDE.get(i).setCargofirmaconstancia(null);
//            }
//            if (listaDE.get(i).getGerentegeneral().getSecuencia() == null) {
//               listaDE.get(i).setGerentegeneral(null);
//            }
//            if (listaDE.get(i).getPersonafirmaconstancia().getSecuencia() == null) {
//               listaDE.get(i).setPersonafirmaconstancia(null);
//            }
//            if (listaDE.get(i).getRepresentantecir().getSecuencia() == null) {
//               listaDE.get(i).setRepresentantecir(null);
//            }
//            if (listaDE.get(i).getSubgerente().getSecuencia() == null) {
//               listaDE.get(i).setSubgerente(null);
//            }
//            if (listaDE.get(i).getCiudad().getSecuencia() == null) {
//               listaDE.get(i).setCiudad(null);
//            }
            persistenciaDetallesEmpresas.crear(getEm(), listaDE.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearDetalleEmpresa Admi : " + e.toString());
      }
   }

   @Override
   public void editarDetalleEmpresa(List<DetallesEmpresas> listaDE) {
      try {
         log.warn("AdministrarDetallesEmpresas.editarDetalleEmpresa()");
         for (int i = 0; i < listaDE.size(); i++) {
            persistenciaDetallesEmpresas.editar(getEm(), listaDE.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarDetalleEmpresa Admi : " + e.toString());
      }
   }

   @Override
   public void borrarDetalleEmpresa(List<DetallesEmpresas> listaDE) {
      try {
         for (int i = 0; i < listaDE.size(); i++) {
//            if (listaDE.get(i).getCiudaddocumentorepresentante().getSecuencia() == null) {
//               listaDE.get(i).setCiudaddocumentorepresentante(null);
//            }
//            if (listaDE.get(i).getCargofirmaconstancia().getSecuencia() == null) {
//               listaDE.get(i).setCargofirmaconstancia(null);
//            }
//            if (listaDE.get(i).getGerentegeneral().getSecuencia() == null) {
//               listaDE.get(i).setGerentegeneral(null);
//            }
//            if (listaDE.get(i).getPersonafirmaconstancia().getSecuencia() == null) {
//               listaDE.get(i).setPersonafirmaconstancia(null);
//            }
//            if (listaDE.get(i).getRepresentantecir().getSecuencia() == null) {
//               listaDE.get(i).setRepresentantecir(null);
//            }
//            if (listaDE.get(i).getSubgerente().getSecuencia() == null) {
//               listaDE.get(i).setSubgerente(null);
//            }
//            if (listaDE.get(i).getCiudad().getSecuencia() == null) {
//               listaDE.get(i).setCiudad(null);
//            }
            persistenciaDetallesEmpresas.borrar(getEm(), listaDE.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarDetalleEmpresa Admi : " + e.toString());
      }
   }

   @Override
   public List<Ciudades> lovCiudades() {
      try {
         return persistenciaCiudades.consultarCiudades(getEm());
      } catch (Exception e) {
         log.warn("Error lovCiudades Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Empleados> lovEmpleados() {
      try {
         return persistenciaEmpleados.buscarEmpleadosActivos(getEm());
      } catch (Exception e) {
         log.warn("Error lovEmpleados Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Personas> lovPersonas() {
      try {
         return persistenciaPersonas.consultarPersonas(getEm());
      } catch (Exception e) {
         log.warn("Error lovPersonas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Cargos> lovCargos() {
      try {
         return persistenciaCargos.consultarCargos(getEm());
      } catch (Exception e) {
         log.warn("Error lovCargos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Empresas> lovEmpresas() {
      try {
         return persistenciaEmpresas.consultarEmpresas(getEm());
      } catch (Exception e) {
         log.warn("Error lovPersonas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Empresas empresaActual(BigInteger secEmpresa) {
      try {
         return persistenciaEmpresas.buscarEmpresasSecuencia(getEm(), secEmpresa);
      } catch (Exception e) {
         log.warn("Error empresaActual Admi : " + e.toString());
         return null;
      }
   }

}
