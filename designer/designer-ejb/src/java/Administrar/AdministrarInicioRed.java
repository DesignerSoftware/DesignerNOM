package Administrar;

import ClasesAyuda.SessionEntityManager;
import Entidades.Conexiones;
import Entidades.Perfiles;
import Entidades.Recordatorios;
import InterfaceAdministrar.AdministrarInicioRedInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaConexionInicialInterface;
import InterfacePersistencia.PersistenciaConexionesInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaRecordatoriosInterface;
import Persistencia.SesionEntityManagerFactory;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

@Stateful
public class AdministrarInicioRed implements AdministrarInicioRedInterface, Serializable {

   private static Logger log = Logger.getLogger(AdministrarInicioRed.class);

   @EJB
   PersistenciaConexionInicialInterface persistenciaConexionInicial;
   @EJB
   PersistenciaRecordatoriosInterface persistenciaRecordatorios;
   @EJB
   PersistenciaEmpresasInterface persistenciaEmpresas;
   @EJB
   AdministrarSesionesInterface administrarSessiones;
   @EJB
   PersistenciaConexionesInterface persistenciaConexiones;
   @EJB
   PersistenciaActualUsuarioInterface persistenciaActualUsuario;
   private EntityManager em;
   private Perfiles perfilUsuario;
   private BigInteger secPerfil;
   private final SesionEntityManagerFactory sessionEMF;

   public AdministrarInicioRed() {
      // FactoryGlobal = new EntityManagerGlobal();
      sessionEMF = new SesionEntityManagerFactory();
   }

   @Override
   public boolean conexionDefault() {
      try {
         conexionInicial("XE");
         persistenciaConexionInicial.accesoDefault(sessionEMF.getEmf().createEntityManager());
         return true;
      } catch (Exception e) {
         log.fatal("Administrar.AdministrarInicioRed.conexionDefault() ERROR: " + e);
         return false;
      }
   }

   @Override
   public boolean conexionInicial(String baseDatos) {
      try {
         if (sessionEMF.getEmf() != null && sessionEMF.getEmf().isOpen()) {
            sessionEMF.getEmf().close();
         }
         if (sessionEMF.crearFactoryInicial(baseDatos)) {
            return true;
         } else {
            log.warn("BASE DE DATOS NO EXISTE");
            return false;
         }
      } catch (Exception e) {
         log.fatal("Error AdministrarLogin.conexionInicial" + e);
         return false;
      }
   }

   @Override
   public boolean conexionUsuario(String baseDatos, String usuario, String contraseña) {
      try {
         log.warn("conexionUsuario 1");
         secPerfil = persistenciaConexionInicial.usuarioLogin(sessionEMF.getEmf().createEntityManager(), usuario);
         log.warn("conexionUsuario 2");
         perfilUsuario = persistenciaConexionInicial.perfilUsuario(sessionEMF.getEmf().createEntityManager(), secPerfil);
         log.warn("conexionUsuario 3");
         sessionEMF.getEmf().close();
         log.warn("conexionUsuario 4");
         boolean resultado = sessionEMF.crearFactoryUsuario(usuario, contraseña, baseDatos);
         log.warn("conexionUsuario 5");
         log.warn("RESULTADO: " + resultado);
         return resultado;
      } catch (Exception e) {
         log.fatal("Error creando EMF AdministrarLogin.conexionUsuario: " + e);
         return false;
      }
   }

   @Override
   public boolean validarUsuario(String usuario) {
      try {
         boolean resultado = persistenciaConexionInicial.validarUsuario(sessionEMF.getEmf().createEntityManager(), usuario);
         return resultado;
      } catch (Exception e) {
         log.fatal("Error AdministrarLogin.validarUsuario: " + e);
         return false;
      }
   }

   @Override
   public boolean validarConexionUsuario(String idSesion) {
      try {
         em = persistenciaConexionInicial.validarConexionUsuario(sessionEMF.getEmf());
         log.warn("validarConexionUsuario 2");
         if (em != null) {
            log.warn("validarConexionUsuario 3");
            if (em.isOpen()) {
               log.warn("validarConexionUsuario 4");
               persistenciaConexionInicial.setearUsuario(em, perfilUsuario.getDescripcion(), perfilUsuario.getPwd());
               log.warn("validarConexionUsuario 5");
               SessionEntityManager sem = new SessionEntityManager(idSesion, sessionEMF.getEmf());
               log.warn("validarConexionUsuario 6");
               administrarSessiones.adicionarSesion(sem);
               log.warn("validarConexionUsuario 7");
               return true;
            }
         }
         return false;
      } catch (Exception e) {
         log.fatal("Error AdministrarLogin.validarConexionUsuario: " + e);
         return false;
      }
   }

   public void cerrarSession(String idSesion) {
      try {
         if (em.isOpen()) {
            em.getEntityManagerFactory().close();
            administrarSessiones.borrarSesion(idSesion);
         }
      } catch (Exception e) {
         log.fatal("Administrar.AdministrarInicioRed.cerrarSession() ERROR: " + e);
      }
   }

   @Override
   public Recordatorios recordatorioAleatorio() {
      try {
         if (sessionEMF.getEmf() != null && sessionEMF.getEmf().isOpen()) {
            return persistenciaRecordatorios.recordatorioRandom(sessionEMF.getEmf().createEntityManager());
         } else {
            return null;
         }
      } catch (Exception e) {
         log.fatal("Administrar.AdministrarInicioRed.recordatorioAleatorio() ERROR: " + e);
         return null;
      }
   }

   @Override
   public String nombreEmpresaPrincipal() {
      log.warn("Admi Nombre Empresa Principal");
      try {
         if (sessionEMF.getEmf() != null && sessionEMF.getEmf().isOpen()) {
            return persistenciaEmpresas.nombreEmpresa(sessionEMF.getEmf().createEntityManager());
         } else {
            return null;
         }
      } catch (Exception e) {
         log.fatal("AdministrarInicioRed.nombreEmpresaPrincipal excepcion");
         e.printStackTrace();
         return null;
      }
   }

   @Override
   public List<String> recordatoriosInicio() {
      if (sessionEMF.getEmf() != null && sessionEMF.getEmf().isOpen()) {
         List<String> listaRecordatorios = persistenciaRecordatorios.recordatoriosInicio(sessionEMF.getEmf().createEntityManager());
         return listaRecordatorios;
      } else {
         return null;
      }
   }

   @Override
   public List<Recordatorios> consultasInicio() {
      if (sessionEMF.getEmf() != null && sessionEMF.getEmf().isOpen()) {
         List<Recordatorios> listaConsultas = persistenciaRecordatorios.consultasInicio(sessionEMF.getEmf().createEntityManager());
         return listaConsultas;
      } else {
         return null;
      }
   }

   @Override
   public int cambioClave(String usuario, String nuevaClave) {
      if (sessionEMF.getEmf() != null && sessionEMF.getEmf().isOpen()) {
         return persistenciaConexionInicial.cambiarClave(em, usuario, nuevaClave);
      } else {
         return -1;
      }
   }

   @Override
   public void guardarDatosConexion(Conexiones conexion) {
      persistenciaConexiones.verificarSID(em, conexion);
   }

   @Override
   public String usuarioBD() {
      return persistenciaActualUsuario.actualAliasBD_EM(em);
   }
}
