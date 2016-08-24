/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarMotivosCambiosSueldosInterface;
import Entidades.MotivosCambiosSueldos;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMotivosCambiosSueldosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarMotivosCambiosSueldos implements AdministrarMotivosCambiosSueldosInterface {

    @EJB
    PersistenciaMotivosCambiosSueldosInterface persistenciaMotivosCambiosSueldos;
    private MotivosCambiosSueldos motivoCambioSueldoSeleccionado;
    private MotivosCambiosSueldos motivoCambioSueldo;
    private List<MotivosCambiosSueldos> listMotivosCambiosCargos;
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
    public void modificarMotivosCambiosSueldos(List<MotivosCambiosSueldos> listaMotivosCambiosSueldos) {
        for (int i = 0; i < listaMotivosCambiosSueldos.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaMotivosCambiosSueldos.editar(em, listaMotivosCambiosSueldos.get(i));
        }
    }

    @Override
    public void borrarMotivosCambiosSueldos(List<MotivosCambiosSueldos> listaMotivosCambiosSueldos) {
        for (int i = 0; i < listaMotivosCambiosSueldos.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaMotivosCambiosSueldos.borrar(em, listaMotivosCambiosSueldos.get(i));
        }
    }

    @Override
    public void crearMotivosCambiosSueldos(List<MotivosCambiosSueldos> listaMotivosCambiosSueldos) {
        for (int i = 0; i < listaMotivosCambiosSueldos.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaMotivosCambiosSueldos.crear(em, listaMotivosCambiosSueldos.get(i));
        }
    }

    @Override
    public List<MotivosCambiosSueldos> consultarMotivosCambiosSueldos() {
        listMotivosCambiosCargos = persistenciaMotivosCambiosSueldos.buscarMotivosCambiosSueldos(em);
        return listMotivosCambiosCargos;
    }

    @Override
    public MotivosCambiosSueldos consultarMotivoCambioCargo(BigInteger secMotivosCambiosSueldos) {
        motivoCambioSueldo = persistenciaMotivosCambiosSueldos.buscarMotivoCambioSueldoSecuencia(em, secMotivosCambiosSueldos);
        return motivoCambioSueldo;
    }

    @Override
    public BigInteger contarVigenciasSueldosMotivoCambioSueldo(BigInteger secuenciaMovitoCambioSueldo) {
        BigInteger verificadorVS = null;

        try {
            return verificadorVS = persistenciaMotivosCambiosSueldos.verificarBorradoVigenciasSueldos(em, secuenciaMovitoCambioSueldo);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarMotivosCambiosSueldos verificarBorradoVS ERROR :" + e);
            return null;
        }
    }
}
