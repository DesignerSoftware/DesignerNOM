/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.MotivosCambiosCargos;
import InterfaceAdministrar.AdministrarMotivosCambiosCargosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMotivosCambiosCargosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
@Stateful
public class AdministrarMotivosCambiosCargos implements AdministrarMotivosCambiosCargosInterface {

   private static Logger log = Logger.getLogger(AdministrarMotivosCambiosCargos.class);

    @EJB
    PersistenciaMotivosCambiosCargosInterface persistenciaMotivosCambiosCargos;

    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;
    
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }
    
    @Override
    public List<MotivosCambiosCargos> consultarMotivosCambiosCargos() {
        List<MotivosCambiosCargos> motivosCambiosCargos = null;
        try {
            motivosCambiosCargos = persistenciaMotivosCambiosCargos.buscarMotivosCambiosCargos(em);
        } catch (Exception e) {
            motivosCambiosCargos = null;
            log.warn("AdministrarMotivosCambiosCargos.consultarMotivosCambiosCargos.");
            log.warn("Excepcion.");
            log.warn(e);
        } finally {
            return motivosCambiosCargos;
        }
    }

    @Override
    public MotivosCambiosCargos consultarMotivoCambioCargo(BigInteger secuenciaMCC) {
        MotivosCambiosCargos mcc = null;
        try {
            mcc = persistenciaMotivosCambiosCargos.buscarMotivoCambioCargo(em, secuenciaMCC);
        } catch (Exception e) {
            mcc = null;
        } finally {
            return mcc;
        }
    }

    @Override
    public void modificarMotivosCambiosCargos(List<MotivosCambiosCargos> listaMotivosCambiosCargos) {
        for (int i = 0; i < listaMotivosCambiosCargos.size(); i++) {
            log.warn("Administrar Modificando");
            persistenciaMotivosCambiosCargos.editar(em, listaMotivosCambiosCargos.get(i));
        }
    }

    @Override
    public void borrarMotivosCambiosCargos(List<MotivosCambiosCargos> listaMotivosCambiosCargos) {
        for (int i = 0; i < listaMotivosCambiosCargos.size(); i++) {
            log.warn("Administrar Borrando");
            persistenciaMotivosCambiosCargos.borrar(em, listaMotivosCambiosCargos.get(i));
        }
    }

    @Override
    public void crearMotivosCambiosCargos(List<MotivosCambiosCargos> listaMotivosCambiosCargos) {
        for (int i = 0; i < listaMotivosCambiosCargos.size(); i++) {
            log.warn("Administrar Creando");
            persistenciaMotivosCambiosCargos.crear(em, listaMotivosCambiosCargos.get(i));
        }
    }

    @Override
    public BigInteger contarVigenciasCargosMotivoCambioCargo(BigInteger secuenciaMovitoCambioCargo) {
        BigInteger verificadorVC = null;
        try {
            verificadorVC = persistenciaMotivosCambiosCargos.verificarBorradoVigenciasCargos(em, secuenciaMovitoCambioCargo);
        } catch (Exception e) {
            log.warn("AdministrarMotivosCambiosCargos.verificarBorradoVC.");
            log.error("Excepcion.");
            log.warn(e);
        } finally {
            return verificadorVC;
        }
    }
}
