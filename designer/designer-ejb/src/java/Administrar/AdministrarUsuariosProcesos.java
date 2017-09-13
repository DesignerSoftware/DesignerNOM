/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.UsuariosProcesos;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarUsuariosProcesosInterface;
import InterfacePersistencia.PersistenciaUsuariosProcesosInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarUsuariosProcesos implements AdministrarUsuariosProcesosInterface {

    private static Logger log = Logger.getLogger(AdministrarUsuariosTiposSueldos.class);
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaUsuariosProcesosInterface persistenciaUsuariosProcesos;
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
    public List<UsuariosProcesos> consultarUsuariosProcesos() {
        try {
            return persistenciaUsuariosProcesos.buscarUsuariosProcesos(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarUsuariosProcesos() ERROR: " + e);
            return null;
        }
    }

    @Override
    public void modificarUsuariosProcesos(List<UsuariosProcesos> listaUsuarios) {
        try {
            for (int i = 0; i < listaUsuarios.size(); i++) {
                persistenciaUsuariosProcesos.editar(getEm(), listaUsuarios.get(i));
            }
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".modificarUsuariosProcesos() ERROR: " + e);
        }
    }

    @Override
    public void borrarUsuariosProcesos(List<UsuariosProcesos> listaUsuarios) {
        try {

            for (int i = 0; i < listaUsuarios.size(); i++) {
                persistenciaUsuariosProcesos.borrar(getEm(), listaUsuarios.get(i));
            }
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".borrarUsuariosProcesos() ERROR: " + e);
        }
    }

    @Override
    public void crearUsuariosProcesos(List<UsuariosProcesos> listaUsuarios) {
        try {

            for (int i = 0; i < listaUsuarios.size(); i++) {
                persistenciaUsuariosProcesos.crear(getEm(), listaUsuarios.get(i));
            }
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".crearUsuariosProcesos() ERROR: " + e);
        }
    }

}
