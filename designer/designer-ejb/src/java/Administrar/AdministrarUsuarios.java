/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Ciudades;
import Entidades.Pantallas;
import Entidades.Perfiles;
import Entidades.Personas;
import Entidades.TiposDocumentos;
import Entidades.Usuarios;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarUsuariosInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaPantallasInterface;
import InterfacePersistencia.PersistenciaPerfilesInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import InterfacePersistencia.PersistenciaTiposDocumentosInterface;
import InterfacePersistencia.PersistenciaUsuariosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrador
 */
@Stateful
public class AdministrarUsuarios implements AdministrarUsuariosInterface {

    private static Logger log = Logger.getLogger(AdministrarUsuarios.class);

    @EJB
    PersistenciaUsuariosInterface persistenciaUsuarios;
    @EJB
    PersistenciaPersonasInterface persistenciaPersonas;
    @EJB
    PersistenciaPerfilesInterface persistenciaPerfiles;
    @EJB
    PersistenciaPantallasInterface persistenciaPantallas;
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaCiudadesInterface persistenciaCiudades;
    @EJB
    PersistenciaTiposDocumentosInterface persistenciaTipoDocumento;

    private EntityManagerFactory emf;
    private EntityManager em;
    private String idSesionBck;

    private EntityManager getEm() {
        try {
            if (this.emf != null) {
                if (this.em != null) {
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

    // Metodos
    @Override
    public void obtenerConexion(String idSesion) {
        idSesionBck = idSesion;
        try {
            emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
        } catch (Exception e) {
            log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
        }
    }

    public List<Usuarios> consultarUsuarios() {
        try {
            return persistenciaUsuarios.buscarUsuarios(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarUsuarios() ERROR: " + e);
            return null;
        }
    }

    @Override
    public String crearUsuariosBD(String alias) {
        try {
            return persistenciaUsuarios.crearUsuario(getEm(), alias);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".crearUsuariosBD() ERROR: " + e);
            return null;
        }
    }

    @Override
    public String CrearUsuarioPerfilBD(String alias, String perfil) {
        try {
            return persistenciaUsuarios.crearUsuarioPerfil(getEm(), alias, perfil);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".CrearUsuarioPerfilBD() ERROR: " + e);
            return null;
        }
    }

    @Override
    public String eliminarUsuariosBD(String alias) {
        try {
            return persistenciaUsuarios.borrarUsuario(getEm(), alias);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".eliminarUsuariosBD() ERROR: " + e);
            return null;
        }
    }

    @Override
    public String eliminarUsuarioTotalBD(String alias) {
        try {
            return persistenciaUsuarios.borrarUsuarioTotal(getEm(), alias);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".eliminarUsuarioTotalBD() ERROR: " + e);
            return null;
        }
    }

    @Override
    public String clonarUsuariosBD(BigInteger usuarioOrigen, BigInteger usuarioDestino) {
        try {
            return persistenciaUsuarios.clonarUsuario(getEm(), usuarioOrigen, usuarioDestino);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".clonarUsuariosBD() ERROR: " + e);
            return null;
        }
    }

    @Override
    public String desbloquearUsuariosBD(String alias) {
        try {
            return persistenciaUsuarios.desbloquearUsuario(getEm(), alias);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".desbloquearUsuariosBD() ERROR: " + e);
            return null;
        }
    }

    @Override
    public String restaurarUsuariosBD(String alias, String fecha) {
        try {
            return persistenciaUsuarios.restaurarUsuario(getEm(), alias, fecha);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".restaurarUsuariosBD() ERROR: " + e);
            return null;
        }
    }

    public List<Personas> consultarPersonas() {
        try {
            return persistenciaPersonas.consultarPersonas(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarPersonas() ERROR: " + e);
            return null;
        }
    }

    public List<Perfiles> consultarPerfiles() {
        try {
            return persistenciaPerfiles.consultarPerfiles(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarPerfiles() ERROR: " + e);
            return null;
        }
    }

    public List<Pantallas> consultarPantallas() {
        try {
            return persistenciaPantallas.buscarPantallas(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarPantallas() ERROR: " + e);
            return null;
        }
    }

    @Override
    public String modificarUsuarios(Usuarios Usuario) {
        try {
            return persistenciaUsuarios.editar(getEm(), Usuario);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".modificarUsuarios() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String borrarUsuarios(Usuarios Usuario) {
        try {
            return persistenciaUsuarios.borrar(getEm(), Usuario);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".borrarUsuarios() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String crearUsuarios(Usuarios Usuario) {
        try {
            return persistenciaUsuarios.crear(getEm(), Usuario);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".crearUsuarios() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public List<Ciudades> lovCiudades() {
        try {
            return persistenciaCiudades.lovCiudades(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".lovCiudades() ERROR: " + e);
            return null;
        }
    }

    @Override
    public List<TiposDocumentos> consultarTiposDocumentos() {
        try {
            return persistenciaTipoDocumento.consultarTiposDocumentos(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarTiposDocumentos() ERROR: " + e);
            return null;
        }
    }

    @Override
    public void crearPersona(Personas persona) {
        try {
            persistenciaPersonas.crear(getEm(), persona);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".crearPersona() ERROR: " + e);
        }
    }

}
