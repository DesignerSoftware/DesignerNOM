/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarMotivosContratosInterface;
import Entidades.MotivosContratos;
import InterfacePersistencia.PersistenciaMotivosContratosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarMotivosContratos implements AdministrarMotivosContratosInterface {

    @EJB
    PersistenciaMotivosContratosInterface persistenciaMotivosContratos;
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
    public void modificarMotivosContratos(List<MotivosContratos> listaMotivosContratos) {
        for (int i = 0; i < listaMotivosContratos.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaMotivosContratos.editar(em, listaMotivosContratos.get(i));
        }
    }

    @Override
    public void borrarMotivosContratos(List<MotivosContratos> listaMotivosContratos) {
        for (int i = 0; i < listaMotivosContratos.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaMotivosContratos.borrar(em, listaMotivosContratos.get(i));
        }
    }

    @Override
    public void crearMotivosContratos(List<MotivosContratos> listaMotivosContratos) {
        for (int i = 0; i < listaMotivosContratos.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaMotivosContratos.crear(em, listaMotivosContratos.get(i));
        }
    }

    @Override
    public List<MotivosContratos> consultarMotivosContratos() {
        List<MotivosContratos> listMotivosCambiosCargos;
        listMotivosCambiosCargos = persistenciaMotivosContratos.buscarMotivosContratos(em);
        return listMotivosCambiosCargos;
    }

    @Override
    public MotivosContratos consultarMotivoContrato(BigInteger secMotivosCambiosCargos) {
        MotivosContratos motivoCambioCargo;
        motivoCambioCargo = persistenciaMotivosContratos.buscarMotivoContrato(em, secMotivosCambiosCargos);
        return motivoCambioCargo;
    }

    @Override
    public BigInteger contarVigenciasTiposContratosMotivoContrato(BigInteger secuenciaMovitoCambioCargo) {
        BigInteger verificadorVTC = null;

        try {
            return verificadorVTC = persistenciaMotivosContratos.verificarBorradoVigenciasTiposContratos(em, secuenciaMovitoCambioCargo);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarMotivosContratos contarVigenciasTiposContratosMotivoContrato ERROR :" + e);
            return null;
        }
    }
}
