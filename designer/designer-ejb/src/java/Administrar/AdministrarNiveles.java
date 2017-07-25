/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarNivelesInterface;
import Entidades.Niveles;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaNivelesInterface;
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
public class AdministrarNiveles implements AdministrarNivelesInterface {

   private static Logger log = Logger.getLogger(AdministrarNiveles.class);

    @EJB
    PersistenciaNivelesInterface persistenciaNiveles;
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
    public void modificarNiveles(List<Niveles> listaNiveles) {
        for (int i = 0; i < listaNiveles.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaNiveles.editar(em, listaNiveles.get(i));
        }
    }

    public void borrarNiveles(List<Niveles> listaNiveles) {
        for (int i = 0; i < listaNiveles.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaNiveles.borrar(em, listaNiveles.get(i));
        }
    }

    public void crearNiveles(List<Niveles> listaNiveles) {
        for (int i = 0; i < listaNiveles.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaNiveles.crear(em, listaNiveles.get(i));
        }
    }

    public List<Niveles> consultarNiveles() {
        List<Niveles> listMotivosCambiosCargos;
        listMotivosCambiosCargos = persistenciaNiveles.consultarNiveles(em);
        return listMotivosCambiosCargos;
    }

    public Niveles consultarNivel(BigInteger secNiveles) {
        Niveles motivoCambioCargo;
        motivoCambioCargo = persistenciaNiveles.consultarNivel(em, secNiveles);
        return motivoCambioCargo;
    }

    public BigInteger contarEvalConvocatoriasNivel(BigInteger secNiveles) {
        BigInteger contarEvalConvocatoriasNivel = null;

        try {
            return contarEvalConvocatoriasNivel = persistenciaNiveles.contarEvalConvocatoriasNivel(em, secNiveles);
        } catch (Exception e) {
            log.error("ERROR AdministrarNiveles contarEvalConvocatoriasNivel ERROR :" + e);
            return null;
        }
    }

    public BigInteger contarPlantasNivel(BigInteger secNiveles) {
        BigInteger contarPlantasNivel = null;

        try {
            return contarPlantasNivel = persistenciaNiveles.contarPlantasNivel(em, secNiveles);
        } catch (Exception e) {
            log.error("ERROR AdministrarNiveles contarPlantasNivel ERROR :" + e);
            return null;
        }
    }

    public BigInteger contarPlantasPersonalesNivel(BigInteger secNiveles) {
        BigInteger contarPlantasPersonalesNivel = null;

        try {
            return contarPlantasPersonalesNivel = persistenciaNiveles.contarPlantasPersonalesNivel(em, secNiveles);
        } catch (Exception e) {
            log.error("ERROR AdministrarNiveles verificarBorradoVNE ERROR :" + e);
            return null;
        }
    }
}
