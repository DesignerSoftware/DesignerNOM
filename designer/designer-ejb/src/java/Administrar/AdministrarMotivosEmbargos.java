/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarMotivosEmbargosInterface;
import Entidades.MotivosEmbargos;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMotivosEmbargosInterface;
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
public class AdministrarMotivosEmbargos implements AdministrarMotivosEmbargosInterface {

    private static Logger log = Logger.getLogger(AdministrarMotivosEmbargos.class);

    @EJB
    PersistenciaMotivosEmbargosInterface persistenciaMotivosEmbargos;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
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

    public String modificarMotivosEmbargos(MotivosEmbargos motivo) {
        try {
            return persistenciaMotivosEmbargos.editar(getEm(), motivo);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".modificarMotivosEmbargos() ERROR: " + e);
            return e.getMessage();
        }
    }

    public String borrarMotivosEmbargos(MotivosEmbargos motivo) {
        try {
            return persistenciaMotivosEmbargos.borrar(getEm(), motivo);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".borrarMotivosEmbargos() ERROR: " + e);
            return e.getMessage();
        }
    }

    public String crearMotivosEmbargos(MotivosEmbargos motivo) {
        try {
            return persistenciaMotivosEmbargos.crear(getEm(), motivo);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".crearMotivosEmbargos() ERROR: " + e);
            return e.getMessage();
        }
    }

    public List<MotivosEmbargos> mostrarMotivosEmbargos() {
        try {
            return persistenciaMotivosEmbargos.buscarMotivosEmbargos(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".mostrarMotivosEmbargos() ERROR: " + e);
            return null;
        }
    }

    public MotivosEmbargos mostrarMotivoEmbargo(BigInteger secMotivoPrestamo) {
        try {
            return persistenciaMotivosEmbargos.buscarMotivoEmbargo(getEm(), secMotivoPrestamo);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".mostrarMotivoEmbargo() ERROR: " + e);
            return null;
        }
    }

    public BigInteger contarEersPrestamosMotivoEmbargo(BigInteger secuenciaTiposDias) {
        try {
            return persistenciaMotivosEmbargos.contadorEersPrestamos(getEm(), secuenciaTiposDias);
        } catch (Exception e) {
            log.error("ERROR ADMINISTRARMOTIVOSEMBARGOS VERIFICAREERSPRESTAMOS ERROR :" + e);
            return null;
        }
    }

    public BigInteger contarEmbargosMotivoEmbargo(BigInteger secuenciaTiposDias) {
        try {
            return persistenciaMotivosEmbargos.contadorEmbargos(getEm(), secuenciaTiposDias);
        } catch (Exception e) {
            log.error("ERROR ADMINISTRARMOTIVOSEMBARGOS VERIFICAREMBARGOS ERROR :" + e);
            return null;
        }
    }
}
