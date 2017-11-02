/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.UsuariosContratos;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarUsuariosContratosInterface;
import InterfacePersistencia.PersistenciaUsuariosContratosInterface;
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
public class AdministrarUsuariosContratos implements AdministrarUsuariosContratosInterface {

    private static Logger log = Logger.getLogger(AdministrarUsuariosContratos.class);

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaUsuariosContratosInterface persistenciaUsuariosContratos;

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
    public List<UsuariosContratos> consultarUsuariosC() {
        try {
            return persistenciaUsuariosContratos.buscarUsuariosContratos(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarUsuariosC() ERROR: " + e);
            return null;
        }
    }

    @Override
    public String modificarUsuarioC(UsuariosContratos usuarioC) {
        try {
            return persistenciaUsuariosContratos.editar(getEm(), usuarioC);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".modificarUsuarioC() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String borrarUsuarioC(UsuariosContratos usuarioC) {
        try {
            return persistenciaUsuariosContratos.borrar(getEm(), usuarioC);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".borrarUsuarioC() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String crearUsuarioC(UsuariosContratos usuarioC) {
        try {
            return persistenciaUsuariosContratos.crear(getEm(), usuarioC);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".crearUsuarioC() ERROR: " + e);
            return e.getMessage();
        }
    }

}
