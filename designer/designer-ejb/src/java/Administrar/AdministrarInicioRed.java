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

@Stateful
public class AdministrarInicioRed implements AdministrarInicioRedInterface, Serializable {

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
            return false;
        }
    }

    @Override
    public boolean conexionInicial(String baseDatos) {
        try {
            if (sessionEMF.getEmf() != null && sessionEMF.getEmf().isOpen()) {
                sessionEMF.getEmf().close();
            }
            //emf = Persistence.createEntityManagerFactory(baseDatos);
            if (sessionEMF.crearFactoryInicial(baseDatos)) {
                return true;
            } else {
                //System.out.println("BASE DE DATOS NO EXISTE");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error AdministrarLogin.conexionInicial" + e);
            return false;
        }
    }

    @Override
    public boolean conexionUsuario(String baseDatos, String usuario, String contraseña) {
        try {
            System.out.println("conexionUsuario 1");
            secPerfil = persistenciaConexionInicial.usuarioLogin(sessionEMF.getEmf().createEntityManager(), usuario);
            System.out.println("conexionUsuario 2");
            perfilUsuario = persistenciaConexionInicial.perfilUsuario(sessionEMF.getEmf().createEntityManager(), secPerfil);
            System.out.println("conexionUsuario 3");
            sessionEMF.getEmf().close();
            System.out.println("conexionUsuario 4");
            boolean resultado = sessionEMF.crearFactoryUsuario(usuario, contraseña, baseDatos);
            System.out.println("conexionUsuario 5");
            System.out.println("RESULTADO: " + resultado);
            return resultado;
        } catch (Exception e) {
            System.out.println("Error creando EMF AdministrarLogin.conexionUsuario: " + e);
            return false;
        }
    }

    @Override
    public boolean validarUsuario(String usuario) {
        try {
            boolean resultado = persistenciaConexionInicial.validarUsuario(sessionEMF.getEmf().createEntityManager(), usuario);
            return resultado;
        } catch (Exception e) {
            System.out.println("Error AdministrarLogin.validarUsuario: " + e);
            return false;
        }
    }

    @Override
    public boolean validarConexionUsuario(String idSesion) {
        try {
            em = persistenciaConexionInicial.validarConexionUsuario(sessionEMF.getEmf());
            System.out.println("2");

            if (em != null) {
                System.out.println("3");
                if (em.isOpen()) {
                    System.out.println("4");
                    persistenciaConexionInicial.setearUsuario(em, perfilUsuario.getDescripcion(), perfilUsuario.getPwd());
                    System.out.println("5");
                    SessionEntityManager sem = new SessionEntityManager(idSesion, sessionEMF.getEmf());
                    System.out.println("6");
                    administrarSessiones.adicionarSesion(sem);
                    System.out.println("7");
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error AdministrarLogin.validarConexionUsuario: " + e);
            return false;
        }
    }

    public void cerrarSession(String idSesion) {
        if (em.isOpen()) {
            em.getEntityManagerFactory().close();
            administrarSessiones.borrarSesion(idSesion);
        }
    }

    @Override
    public Recordatorios recordatorioAleatorio() {
        if (sessionEMF.getEmf() != null && sessionEMF.getEmf().isOpen()) {
            return persistenciaRecordatorios.recordatorioRandom(sessionEMF.getEmf().createEntityManager());
        } else {
            return null;
        }
    }

    @Override
    public String nombreEmpresaPrincipal() {
        System.out.println("Admi Nombre Empresa Principal");
        try {
            if (sessionEMF.getEmf() != null && sessionEMF.getEmf().isOpen()) {
                return persistenciaEmpresas.nombreEmpresa(sessionEMF.getEmf().createEntityManager());
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("AdministrarInicioRed.nombreEmpresaPrincipal excepcion");
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
