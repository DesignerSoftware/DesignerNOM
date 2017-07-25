/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarMotivosMvrsInterface;
import Entidades.Motivosmvrs;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMotivosMvrsInterface;
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
public class AdministrarMotivosMvrs implements AdministrarMotivosMvrsInterface {

   private static Logger log = Logger.getLogger(AdministrarMotivosMvrs.class);

    @EJB
    PersistenciaMotivosMvrsInterface persistenciaMotivosMvrs;
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
    public void modificarMotivosMvrs(List<Motivosmvrs> listaMotivosMvrs) {
        for (int i = 0; i < listaMotivosMvrs.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaMotivosMvrs.editar(em, listaMotivosMvrs.get(i));
        }
    }

    @Override
    public void borrarMotivosMvrs(List<Motivosmvrs> listaMotivosMvrs) {
        for (int i = 0; i < listaMotivosMvrs.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaMotivosMvrs.borrar(em, listaMotivosMvrs.get(i));
        }
    }

    @Override
    public void crearMotivosMvrs(List<Motivosmvrs> listaMotivosMvrs) {
        for (int i = 0; i < listaMotivosMvrs.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaMotivosMvrs.crear(em, listaMotivosMvrs.get(i));
        }
    }

    @Override
    public List<Motivosmvrs> consultarMotivosMvrs() {
        List<Motivosmvrs> listMotivoMvrs;
        listMotivoMvrs = persistenciaMotivosMvrs.buscarMotivosMvrs(em);
        return listMotivoMvrs;
    }

    @Override
    public Motivosmvrs consultarMotivosMvrs(BigInteger secMotivosMvrs) {
        Motivosmvrs motivoMvrs;
        motivoMvrs = persistenciaMotivosMvrs.buscarMotivosMvrs(em, secMotivosMvrs);
        return motivoMvrs;
    }
}
