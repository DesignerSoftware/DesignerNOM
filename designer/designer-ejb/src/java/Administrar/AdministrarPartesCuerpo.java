/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarPartesCuerpoInterface;
import Entidades.PartesCuerpo;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaPartesCuerpoInterface;
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
public class AdministrarPartesCuerpo implements AdministrarPartesCuerpoInterface {

    @EJB
    PersistenciaPartesCuerpoInterface persistenciaPartesCuerpo;
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
    
    public void modificarPartesCuerpo(List<PartesCuerpo> listPartesCuerpo) {
        for (int i = 0; i < listPartesCuerpo.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaPartesCuerpo.editar(em, listPartesCuerpo.get(i));
        }
    }

    public void borrarPartesCuerpo(List<PartesCuerpo> listPartesCuerpo) {
        for (int i = 0; i < listPartesCuerpo.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaPartesCuerpo.borrar(em, listPartesCuerpo.get(i));
        }
    }

    public void crearPartesCuerpo(List<PartesCuerpo> listPartesCuerpo) {
        for (int i = 0; i < listPartesCuerpo.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaPartesCuerpo.crear(em, listPartesCuerpo.get(i));
        }
    }

    public List<PartesCuerpo> consultarPartesCuerpo() {
        List<PartesCuerpo> listPartesCuerpo;
        listPartesCuerpo = persistenciaPartesCuerpo.buscarPartesCuerpo(em);
        return listPartesCuerpo;
    }

    public PartesCuerpo consultarParteCuerpo(BigInteger secElementosCausasAccidentes) {
        PartesCuerpo partesCuerpo;
        partesCuerpo = persistenciaPartesCuerpo.buscarParteCuerpo(em, secElementosCausasAccidentes);
        return partesCuerpo;
    }

    public BigInteger contarSoAccidentesMedicosParteCuerpo(BigInteger secuenciaElementosCausasAccidentes) {
        try {
            BigInteger verificarSoAccidentesMedicos;

            return verificarSoAccidentesMedicos = persistenciaPartesCuerpo.contadorSoAccidentesMedicos(em, secuenciaElementosCausasAccidentes);
        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRARPARTESCUERPO verificarBorradoDetallesLicensias ERROR :" + e);
            return null;
        }
    }

    public BigInteger contarDetallesExamenesParteCuerpo(BigInteger secuenciaElementosCausasAccidentes) {
        try {
            BigInteger verificarBorradoDetallesExamenes;
            return verificarBorradoDetallesExamenes = persistenciaPartesCuerpo.contadorDetallesExamenes(em, secuenciaElementosCausasAccidentes);
        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRARPARTESCUERPO verificarBorradoSoAccidentesDomesticos ERROR :" + e);
            return null;
        }
    }

    public BigInteger contarSoDetallesRevisionesParteCuerpo(BigInteger secuenciaElementosCausasAccidentes) {
        try {
            BigInteger verificarBorradoSoDetallesRevisiones;
            return verificarBorradoSoDetallesRevisiones = persistenciaPartesCuerpo.contadorSoDetallesRevisiones(em, secuenciaElementosCausasAccidentes);
        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRARPARTESCUERPO verificarBorradoSoAccidentesDomesticos ERROR :" + e);
            return null;
        }
    }
}
