/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Indices;
import Entidades.Usuarios;
import Entidades.UsuariosIndices;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarUsuariosIndicesInterface;
import InterfacePersistencia.PersistenciaUsuariosIndicesInterface;
import java.math.BigInteger;
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
public class AdministrarUsuariosIndices implements AdministrarUsuariosIndicesInterface {

    private static Logger log = Logger.getLogger(AdministrarUsuariosIndices.class);

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaUsuariosIndicesInterface persistenciaUsuariosIndices;

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
    public String crear(UsuariosIndices usuarioIndice) {
        try {
            return persistenciaUsuariosIndices.crear(getEm(), usuarioIndice);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".crear() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String editar(UsuariosIndices usuarioIndice) {
        try {
            return persistenciaUsuariosIndices.editar(getEm(), usuarioIndice);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".crear() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String borrar(UsuariosIndices usuarioIndice) {
        try {
            return persistenciaUsuariosIndices.borrar(getEm(), usuarioIndice);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".crear() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public List<Indices> lovIR() {
        try {
            return persistenciaUsuariosIndices.lovIndices(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".lovIR() ERROR: " + e);
            return null;
        }
    }

    @Override
    public List<Usuarios> listaUsuarios() {
        try {
            return persistenciaUsuariosIndices.listaUsuarios(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".listaUsuarios() ERROR: " + e);
            return null;
        }
    }

    @Override
    public List<UsuariosIndices> listaUsuariosIndices(BigInteger secUsuario) {
        try {
            return persistenciaUsuariosIndices.listaUsuariosIndices(getEm(), secUsuario);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".listaUsuarios() ERROR: " + e);
            return null;
        }
    }
}
