/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.MotivosRetiros;
import InterfaceAdministrar.AdministrarMotivosRetirosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMotivosRetirosInterface;
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
public class AdministrarMotivosRetiros implements AdministrarMotivosRetirosInterface {

   private static Logger log = Logger.getLogger(AdministrarMotivosRetiros.class);

    @EJB
    PersistenciaMotivosRetirosInterface persistenciaMotivosRetiros;
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
    
    @Override
    public void modificarMotivosRetiros(List<MotivosRetiros> listaMotivosRetiros) {
        for (int i = 0; i < listaMotivosRetiros.size(); i++) {
            log.warn("Administrar Modificando...");
            log.warn("Nombre " + listaMotivosRetiros.get(i).getNombre() + " Codigo " + listaMotivosRetiros.get(i).getCodigo());
            persistenciaMotivosRetiros.editar(em, listaMotivosRetiros.get(i));
        }
    }

    @Override
    public void borrarMotivosRetiros(List<MotivosRetiros> listaMotivosRetiros) {
        for (int i = 0; i < listaMotivosRetiros.size(); i++) {
            log.warn("Administrar Borrando...");
            log.warn("Nombre " + listaMotivosRetiros.get(i).getNombre() + " Codigo " + listaMotivosRetiros.get(i).getCodigo());
            persistenciaMotivosRetiros.borrar(em, listaMotivosRetiros.get(i));
        }
    }

    @Override
    public void crearMotivosRetiros(List<MotivosRetiros> listaMotivosRetiros) {
        for (int i = 0; i < listaMotivosRetiros.size(); i++) {
            log.warn("Administrar Creando...");
            log.warn("Nombre " + listaMotivosRetiros.get(i).getNombre() + " Codigo " + listaMotivosRetiros.get(i).getCodigo());
            persistenciaMotivosRetiros.crear(em, listaMotivosRetiros.get(i));
        }
    }

    public List<MotivosRetiros> consultarMotivosRetiros() {
        List<MotivosRetiros> listMotivosCambiosCargos;
        listMotivosCambiosCargos = persistenciaMotivosRetiros.consultarMotivosRetiros(em);
        return listMotivosCambiosCargos;
    }

    @Override
    public MotivosRetiros consultarMotivoRetiro(BigInteger secMotivosRetiros) {
        MotivosRetiros subCategoria;
        subCategoria = persistenciaMotivosRetiros.consultarMotivoRetiro(em, secMotivosRetiros);
        return subCategoria;
    }

    @Override
    public BigInteger contarHVExperienciasLaboralesMotivoRetiro(BigInteger secMotivosRetiros) {
        BigInteger contarHVExperienciasLaboralesMotivoRetiro = null;

        try {
            return contarHVExperienciasLaboralesMotivoRetiro = persistenciaMotivosRetiros.contarHVExperienciasLaboralesMotivoRetiro(em, secMotivosRetiros);
        } catch (Exception e) {
            log.error("ERROR AdministrarMotivosRetiros contarEscalafones ERROR : " + e);
            return null;
        }
    }

    @Override
    public BigInteger contarNovedadesSistemasMotivoRetiro(BigInteger secMotivosRetiros) {
        BigInteger contarNovedadesSistemasMotivoRetiro = null;

        try {
            return contarNovedadesSistemasMotivoRetiro = persistenciaMotivosRetiros.contarNovedadesSistemasMotivoRetiro(em, secMotivosRetiros);
        } catch (Exception e) {
            log.error("ERROR AdministrarMotivosRetiros contarEscalafones ERROR : " + e);
            return null;
        }
    }

    @Override
    public BigInteger contarRetiMotivosRetirosMotivoRetiro(BigInteger secMotivosRetiros) {
        BigInteger contarRetiMotivosRetirosMotivoRetiro = null;

        try {
            return contarRetiMotivosRetirosMotivoRetiro = persistenciaMotivosRetiros.contarRetiMotivosRetirosMotivoRetiro(em, secMotivosRetiros);
        } catch (Exception e) {
            log.error("ERROR AdministrarMotivosRetiros contarEscalafones ERROR : " + e);
            return null;
        }
    }

    @Override
    public BigInteger contarRetiradosMotivoRetiro(BigInteger secMotivosRetiros) {
        BigInteger contarRetiradosMotivoRetiro = null;

        try {
            return contarRetiradosMotivoRetiro = persistenciaMotivosRetiros.contarRetiradosMotivoRetiro(em, secMotivosRetiros);
        } catch (Exception e) {
            log.error("ERROR AdministrarMotivosRetiros contarRetiradosMotivoRetiro ERROR : " + e);
            return null;
        }
    }
}
