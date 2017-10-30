/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Enfermedades;
import InterfaceAdministrar.AdministrarEnfermedadesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEnfermedadesInterface;
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
public class AdministrarEnfermedades implements AdministrarEnfermedadesInterface {

    private static Logger log = Logger.getLogger(AdministrarEnfermedades.class);

    @EJB
    PersistenciaEnfermedadesInterface persistenciaEnfermedades;

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

    public String modificarEnfermedades(Enfermedades enfermedad) {
        try {
            return persistenciaEnfermedades.editar(getEm(), enfermedad);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".modificarEnfermedades() ERROR: " + e);
            return e.getMessage();
        }
    }

    public String borrarEnfermedades(Enfermedades enfermedad) {
        try {
            return persistenciaEnfermedades.borrar(getEm(), enfermedad);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".borrarEnfermedades() ERROR: " + e);
            return e.getMessage();
        }
    }

    public String crearEnfermedades(Enfermedades enfermedad) {
        try {
            return persistenciaEnfermedades.crear(getEm(), enfermedad);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".crearEnfermedades() ERROR: " + e);
            return e.getMessage();
        }
    }

    public List<Enfermedades> consultarEnfermedades() {
        try {
            return persistenciaEnfermedades.buscarEnfermedades(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarEnfermedades() ERROR: " + e);
            return null;
        }
    }

    public Enfermedades consultarEnfermedad(BigInteger secEnfermedad) {
        try {
            return persistenciaEnfermedades.buscarEnfermedad(getEm(), secEnfermedad);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarEnfermedad() ERROR: " + e);
            return null;
        }
    }

    public BigInteger verificarAusentimos(BigInteger secuenciaTiposAuxilios) {
        try {
            return persistenciaEnfermedades.contadorAusentimos(getEm(), secuenciaTiposAuxilios);
        } catch (Exception e) {
            log.error("ERROR ADMINISTRARENFERMEDADES contadorAusentimos ERROR :" + e);
            return null;
        }
    }

    public BigInteger verificarDetallesLicencias(BigInteger secuenciaTiposAuxilios) {
        try {
            return persistenciaEnfermedades.contadorDetallesLicencias(getEm(), secuenciaTiposAuxilios);
        } catch (Exception e) {
            log.error("ERROR ADMINISTRARENFERMEDADES contadorDetallesLicencias ERROR :" + e);
            return null;
        }
    }

    public BigInteger verificarEnfermedadesPadecidas(BigInteger secuenciaTiposAuxilios) {
        try {
            return persistenciaEnfermedades.contadorEnfermedadesPadecidas(getEm(), secuenciaTiposAuxilios);
        } catch (Exception e) {
            log.error("ERROR ADMINISTRARENFERMEDADES contadorEnfermedadesPadecidas ERROR :" + e);
            return null;
        }
    }

    public BigInteger verificarSoAusentismos(BigInteger secuenciaTiposAuxilios) {
        try {
            return persistenciaEnfermedades.contadorSoausentismos(getEm(), secuenciaTiposAuxilios);
        } catch (Exception e) {
            log.error("ERROR ADMINISTRARENFERMEDADES contadorSoausentismos ERROR :" + e);
            return null;
        }
    }

    public BigInteger verificarSoRevisionesSistemas(BigInteger secuenciaTiposAuxilios) {
        try {
            return persistenciaEnfermedades.contadorSorevisionessSistemas(getEm(), secuenciaTiposAuxilios);
        } catch (Exception e) {
            log.error("ERROR ADMINISTRARENFERMEDADES contadorSorevisionessSistemas ERROR :" + e);
            return null;
        }
    }
}
