/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarVigenciasPlantasInterface;
import Entidades.VigenciasPlantas;
import InterfacePersistencia.PersistenciaVigenciasPlantasInterface;
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
public class AdministrarVigenciasPlantas implements AdministrarVigenciasPlantasInterface {

    @EJB
    PersistenciaVigenciasPlantasInterface persistenciaVigenciasPlantas;
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
    
    public void modificarVigenciasPlantas(List<VigenciasPlantas> listaVigenciasPlantas) {
        for (int i = 0; i < listaVigenciasPlantas.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaVigenciasPlantas.editar(em, listaVigenciasPlantas.get(i));
        }
    }

    public void borrarVigenciasPlantas(List<VigenciasPlantas> listaVigenciasPlantas) {
        for (int i = 0; i < listaVigenciasPlantas.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaVigenciasPlantas.borrar(em, listaVigenciasPlantas.get(i));
        }
    }

    public void crearVigenciasPlantas(List<VigenciasPlantas> listaVigenciasPlantas) {
        for (int i = 0; i < listaVigenciasPlantas.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaVigenciasPlantas.crear(em, listaVigenciasPlantas.get(i));
        }
    }

    public List<VigenciasPlantas> consultarVigenciasPlantas() {
        List<VigenciasPlantas> listMotivosCambiosCargos;
        listMotivosCambiosCargos = persistenciaVigenciasPlantas.consultarVigenciasPlantas(em);
        return listMotivosCambiosCargos;
    }

    public VigenciasPlantas consultarVigenciaPlanta(BigInteger secVigenciasPlantas) {
        VigenciasPlantas motivoCambioCargo;
        motivoCambioCargo = persistenciaVigenciasPlantas.consultarVigenciaPlanta(em, secVigenciasPlantas);
        return motivoCambioCargo;
    }

    public BigInteger contarPlantasVigenciaPlanta(BigInteger secVigenciasPlantas) {
        BigInteger contarPlantasVigenciaPlanta = null;

        try {
            return contarPlantasVigenciaPlanta = persistenciaVigenciasPlantas.contarPlantasVigenciaPlanta(em, secVigenciasPlantas);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarVigenciasPlantas verificarBorradoVNE ERROR :" + e);
            return null;
        }
    }
}
