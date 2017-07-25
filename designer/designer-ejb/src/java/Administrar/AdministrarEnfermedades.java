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

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    public void modificarEnfermedades(List<Enfermedades> listDeportesModificadas) {
        for (int i = 0; i < listDeportesModificadas.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaEnfermedades.editar(em,listDeportesModificadas.get(i));
        }
    }

    public void borrarEnfermedades(List<Enfermedades> listDeportesModificadas) {
        for (int i = 0; i < listDeportesModificadas.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaEnfermedades.borrar(em,listDeportesModificadas.get(i));
        }
    }

    public void crearEnfermedades(List<Enfermedades> listDeportesModificadas) {
        for (int i = 0; i < listDeportesModificadas.size(); i++) {
            log.warn("Administrar Crear...");
            persistenciaEnfermedades.crear(em,listDeportesModificadas.get(i));
        }
    }

    public List<Enfermedades> consultarEnfermedades() {
        List<Enfermedades> listEnfermedades;
        listEnfermedades = persistenciaEnfermedades.buscarEnfermedades(em);
        return listEnfermedades;
    }

    public Enfermedades consultarEnfermedad(BigInteger secDeportes) {
        Enfermedades enfermedad;
        enfermedad = persistenciaEnfermedades.buscarEnfermedad(em,secDeportes);
        return enfermedad;
    }

    public BigInteger verificarAusentimos(BigInteger secuenciaTiposAuxilios) {
        BigInteger contadorAusentimos = null;
        try {
            contadorAusentimos = persistenciaEnfermedades.contadorAusentimos(em,secuenciaTiposAuxilios);
        } catch (Exception e) {
            log.error("ERROR ADMINISTRARENFERMEDADES contadorAusentimos ERROR :" + e);
        } finally {
            return contadorAusentimos;
        }
    }

    public BigInteger verificarDetallesLicencias(BigInteger secuenciaTiposAuxilios) {
        BigInteger contadorDetallesLicencias = null;
        try {
            contadorDetallesLicencias = persistenciaEnfermedades.contadorDetallesLicencias(em,secuenciaTiposAuxilios);
        } catch (Exception e) {
            log.error("ERROR ADMINISTRARENFERMEDADES contadorDetallesLicencias ERROR :" + e);
        } finally {
            return contadorDetallesLicencias;
        }
    }

    public BigInteger verificarEnfermedadesPadecidas(BigInteger secuenciaTiposAuxilios) {
        BigInteger contadorEnfermedadesPadecidas = null;
        try {
            contadorEnfermedadesPadecidas = persistenciaEnfermedades.contadorEnfermedadesPadecidas(em,secuenciaTiposAuxilios);
        } catch (Exception e) {
            log.error("ERROR ADMINISTRARENFERMEDADES contadorEnfermedadesPadecidas ERROR :" + e);
        } finally {
            return contadorEnfermedadesPadecidas;
        }
    }

    public BigInteger verificarSoAusentismos(BigInteger secuenciaTiposAuxilios) {
        BigInteger contadorSoausentismos = null;
        try {
            contadorSoausentismos = persistenciaEnfermedades.contadorSoausentismos(em,secuenciaTiposAuxilios);
        } catch (Exception e) {
            log.error("ERROR ADMINISTRARENFERMEDADES contadorSoausentismos ERROR :" + e);
        } finally {
            return contadorSoausentismos;
        }
    }

    public BigInteger verificarSoRevisionesSistemas(BigInteger secuenciaTiposAuxilios) {
        BigInteger contadorSorevisionessSistemas = null;
        try {
            contadorSorevisionessSistemas = persistenciaEnfermedades.contadorSorevisionessSistemas(em,secuenciaTiposAuxilios);
        } catch (Exception e) {
            log.error("ERROR ADMINISTRARENFERMEDADES contadorSorevisionessSistemas ERROR :" + e);
        } finally {
            return contadorSorevisionessSistemas;
        }
    }
}
