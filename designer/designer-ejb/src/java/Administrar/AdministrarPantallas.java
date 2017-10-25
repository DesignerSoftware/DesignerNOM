/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empresas;
import Entidades.Modulos;
import Entidades.Pantallas;
import Entidades.Tablas;
import InterfaceAdministrar.AdministrarPantallasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaModulosInterface;
import InterfacePersistencia.PersistenciaPantallasInterface;
import InterfacePersistencia.PersistenciaTablasInterface;
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
public class AdministrarPantallas implements AdministrarPantallasInterface {

    private static Logger log = Logger.getLogger(AdministrarPantallas.class);

    @EJB
    PersistenciaPantallasInterface persistenciaPantallas;
    @EJB
    PersistenciaEmpresasInterface persistenciaEmpresas;
    @EJB
    PersistenciaTablasInterface persistenciaTablas;
    @EJB
    AdministrarSesionesInterface administrarSesiones;

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
    public String modificarPantallas(Pantallas pantalla) {
        try {
            return persistenciaPantallas.editar(getEm(), pantalla);
        } catch (Exception e) {
            log.error("Error AdministrarPantallas.modificarPantallas : ", e);
            return e.getMessage();
        }
    }

    @Override
    public String crearPantallas(Pantallas pantalla) {
        try {
            return persistenciaPantallas.crear(getEm(), pantalla);
        } catch (Exception e) {
            log.error("Error AdministrarPantallas.crearPantallas : ", e);
            return e.getMessage();
        }
    }

    @Override
    public String borrarPantallas(Pantallas pantalla) {
        try {
            return persistenciaPantallas.borrar(getEm(), pantalla);
        } catch (Exception e) {
            log.error("Error AdministrarPantallas.borrarPantallas : ", e);
            return e.getMessage();
        }
    }

    @Override
    public List<Pantallas> consultarPantallas() {
        try {
            return persistenciaPantallas.buscarPantallas(getEm());
        } catch (Exception e) {
            log.error("Error AdministrarPantallas.consultarPantallas : ", e);
            return null;
        }
    }

    @Override
    public List<Empresas> consultarEmpresas() {
        try {
            return persistenciaEmpresas.buscarEmpresas(getEm());
        } catch (Exception e) {
            log.error("Error AdministrarPantallas.consultarEmpresas : ", e);
            return null;
        }
    }

    @Override
    public List<Tablas> consultarTablas() {
        try {
            return persistenciaTablas.consultarTablas(getEm());
        } catch (Exception e) {
            log.error("Error AdministrarPantallas.consultarTablas : ", e);
            return null;
        }
    }

}
