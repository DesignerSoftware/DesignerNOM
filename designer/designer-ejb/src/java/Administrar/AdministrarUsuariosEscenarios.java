/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Escenarios;
import Entidades.Usuarios;
import Entidades.UsuariosEscenarios;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarUsuariosEscenariosInterface;
import InterfacePersistencia.PersistenciaUsuariosEscenariosInterface;
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
public class AdministrarUsuariosEscenarios implements AdministrarUsuariosEscenariosInterface {

    private static Logger log = Logger.getLogger(AdministrarUsuariosEscenarios.class);

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaUsuariosEscenariosInterface persistenciaUsuariosEscenarios;

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

    @Override
    public void obtenerConexion(String idSesion) {
        idSesionBck = idSesion;
        try {
            emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
        } catch (Exception e) {
            log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
        }
    }

    @Override
    public List<UsuariosEscenarios> consultarUsuariosEscenarios() {
        try {
            return persistenciaUsuariosEscenarios.listaUsuariosEscenarios(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarUsuariosC() ERROR: " + e);
            return null;
        }
    }

    @Override
    public String modificarUsuarioC(UsuariosEscenarios usuarioE) {
        try {
            return persistenciaUsuariosEscenarios.editar(getEm(), usuarioE);
        } catch (Exception e) {
            log.error("Error persistenciaUsuariosEscenarios.modificarUsuarioC ", e);
            return e.getMessage();
        }
    }

    @Override
    public String borrarUsuarioC(UsuariosEscenarios usuarioE) {
        try {
            return persistenciaUsuariosEscenarios.borrar(getEm(), usuarioE);
        } catch (Exception e) {
            log.error("Error persistenciaUsuariosEscenarios.borrarUsuarioC ", e);
            return e.getMessage();
        }
    }

    @Override
    public String crearUsuarioC(UsuariosEscenarios usuarioE) {
        try {
            return persistenciaUsuariosEscenarios.crear(getEm(), usuarioE);
        } catch (Exception e) {
            log.error("Error persistenciaUsuariosEscenarios.crearUsuarioC ", e);
            return e.getMessage();
        }
    }

    @Override
    public List<Escenarios> lovEscenarios() {
        try {
            return persistenciaUsuariosEscenarios.lovEscenarios(getEm());
        } catch (Exception e) {
            log.error("Error persistenciaUsuariosEscenarios.lovEscenarios ", e);
            return null;
        }
    }

    @Override
    public List<Usuarios> listaUsuarios() {
        try {
            return persistenciaUsuariosEscenarios.listaUsuarios(getEm());
        } catch (Exception e) {
            log.error("Error persistenciaUsuariosEscenarios.listaUsuarios ", e);
            return null;
        }
    }
}
