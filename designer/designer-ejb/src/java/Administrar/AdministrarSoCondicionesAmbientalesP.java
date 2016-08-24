/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarSoCondicionesAmbientalesPInterface;
import Entidades.SoCondicionesAmbientalesP;
import InterfacePersistencia.PersistenciaSoCondicionesAmbientalesPInterface;
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
public class AdministrarSoCondicionesAmbientalesP implements AdministrarSoCondicionesAmbientalesPInterface {

    @EJB
    PersistenciaSoCondicionesAmbientalesPInterface persistenciaSoCondicionesAmbientalesP;
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
    public void modificarSoCondicionesAmbientalesP(List<SoCondicionesAmbientalesP> listSoCondicionesAmbientalesP) {
        for (int i = 0; i < listSoCondicionesAmbientalesP.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaSoCondicionesAmbientalesP.editar(em, listSoCondicionesAmbientalesP.get(i));
        }
    }

    @Override
    public void borrarSoCondicionesAmbientalesP(List<SoCondicionesAmbientalesP> listSoCondicionesAmbientalesP) {
        for (int i = 0; i < listSoCondicionesAmbientalesP.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaSoCondicionesAmbientalesP.borrar(em, listSoCondicionesAmbientalesP.get(i));
        }
    }

    @Override
    public void crearSoCondicionesAmbientalesP(List<SoCondicionesAmbientalesP> listSoCondicionesAmbientalesP) {
        for (int i = 0; i < listSoCondicionesAmbientalesP.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaSoCondicionesAmbientalesP.crear(em, listSoCondicionesAmbientalesP.get(i));
        }
    }

    @Override
    public List<SoCondicionesAmbientalesP> consultarSoCondicionesAmbientalesP() {
        List<SoCondicionesAmbientalesP> listSoCondicionesAmbientalesP;
        listSoCondicionesAmbientalesP = persistenciaSoCondicionesAmbientalesP.buscarSoCondicionesAmbientalesP(em);
        return listSoCondicionesAmbientalesP;
    }

    @Override
    public SoCondicionesAmbientalesP consultarSoCondicionAmbientalP(BigInteger secSoCondicionesAmbientalesP) {
        SoCondicionesAmbientalesP soCondicionesAmbientalesP;
        soCondicionesAmbientalesP = persistenciaSoCondicionesAmbientalesP.buscarSoCondicionAmbientalP(em, secSoCondicionesAmbientalesP);
        return soCondicionesAmbientalesP;
    }

    @Override
    public BigInteger verificarSoAccidentesMedicos(BigInteger secuenciaElementos) {
        BigInteger verificarSoAccidtenesMedicos;
        try {
            System.err.println("Secuencia Borrado Elementos" + secuenciaElementos);
            return verificarSoAccidtenesMedicos = persistenciaSoCondicionesAmbientalesP.contadorSoAccidentesMedicos(em, secuenciaElementos);
        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRARSOCONDICIONSEAMBIENTALESP verificarSoAccidtenesMedicos ERROR :" + e);
            return null;
        }
    }
}
