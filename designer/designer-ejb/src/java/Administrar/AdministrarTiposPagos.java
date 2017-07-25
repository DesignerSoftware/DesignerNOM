/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Tipospagos;
import InterfaceAdministrar.AdministrarTiposPagosInterface;
import InterfacePersistencia.PersistenciaTiposPagosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarTiposPagos implements AdministrarTiposPagosInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposPagos.class);

    @EJB
    PersistenciaTiposPagosInterface persistenciaTiposPagos;
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

    public void modificarTiposPagos(List<Tipospagos> listaTiposPagos) {
        for (int i = 0; i < listaTiposPagos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposPagos.editar(em, listaTiposPagos.get(i));
        }
    }

    public void borrarTiposPagos(List<Tipospagos> listaTiposPagos) {
        for (int i = 0; i < listaTiposPagos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaTiposPagos.borrar(em, listaTiposPagos.get(i));
        }
    }

    public void crearTiposPagos(List<Tipospagos> listaTiposPagos) {
        for (int i = 0; i < listaTiposPagos.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaTiposPagos.crear(em, listaTiposPagos.get(i));
        }
    }

    @Override
    public List<Tipospagos> consultarTiposPagos() {
        List<Tipospagos> listMotivosCambiosCargos;
        listMotivosCambiosCargos = persistenciaTiposPagos.consultarTiposPagos(em);
        return listMotivosCambiosCargos;
    }

    public Tipospagos consultarTipoPago(BigInteger secTiposPagos) {
        Tipospagos subCategoria;
        subCategoria = persistenciaTiposPagos.consultarTipoPago(em, secTiposPagos);
        return subCategoria;
    }

    public BigInteger contarProcesosTipoPago(BigInteger secTiposPagos) {
        BigInteger contarProcesosTipoPago = null;

        try {
            return contarProcesosTipoPago = persistenciaTiposPagos.contarProcesosTipoPago(em, secTiposPagos);
        } catch (Exception e) {
            log.error("ERROR AdministrarTiposPagos contarProcesosTipoPago ERROR : " + e);
            return null;
        }
    }
}
