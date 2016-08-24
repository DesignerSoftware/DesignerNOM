/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Jornadas;
import InterfaceAdministrar.AdministrarJornadasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaJornadasInterface;
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
public class AdministrarJornadas implements AdministrarJornadasInterface {

    @EJB
    PersistenciaJornadasInterface persistenciaJornadas;
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
    
    public void modificarJornadas(List<Jornadas> listaJornadas) {
        for (int i = 0; i < listaJornadas.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaJornadas.editar(em, listaJornadas.get(i));
        }
    }

    public void borrarJornadas(List<Jornadas> listaJornadas) {
        for (int i = 0; i < listaJornadas.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaJornadas.borrar(em, listaJornadas.get(i));
        }
    }

    public void crearJornadas(List<Jornadas> listaJornadas) {
        for (int i = 0; i < listaJornadas.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaJornadas.crear(em, listaJornadas.get(i));
        }
    }

    @Override
    public List<Jornadas> consultarJornadas() {
        List<Jornadas> listMotivosCambiosCargos;
        listMotivosCambiosCargos = persistenciaJornadas.consultarJornadas(em);
        return listMotivosCambiosCargos;
    }

    public Jornadas consultarJornada(BigInteger secJornadas) {
        Jornadas subCategoria;
        subCategoria = persistenciaJornadas.consultarJornada(em, secJornadas);
        return subCategoria;
    }

    public BigInteger contarJornadasLaboralesJornada(BigInteger secJornadas) {
        BigInteger contarJornadasLaboralesJornada = null;

        try {
            return contarJornadasLaboralesJornada = persistenciaJornadas.contarJornadasLaboralesJornada(em, secJornadas);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarJornadas contarJornadasLaboralesJornada ERROR : " + e);
            return null;
        }
    }

    public BigInteger contarTarifasEscalafonesJornada(BigInteger secJornadas) {
        BigInteger contarTarifasEscalafonesJornada = null;

        try {
            return contarTarifasEscalafonesJornada = persistenciaJornadas.contarTarifasEscalafonesJornada(em, secJornadas);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarJornadas contarTarifasEscalafonesJornada ERROR : " + e);
            return null;
        }
    }
}
