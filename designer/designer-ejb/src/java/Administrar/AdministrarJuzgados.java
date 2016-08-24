/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarJuzgadosInterface;
import Entidades.Ciudades;
import Entidades.Juzgados;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaJuzgadosInterface;
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
public class AdministrarJuzgados implements AdministrarJuzgadosInterface {

    /**
     * CREACION EJB
     */
    @EJB
    PersistenciaJuzgadosInterface persistenciaJuzgados;
    @EJB
    PersistenciaCiudadesInterface persistenciaCiudades;
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
    /**
     *
     */
    @Override
    public List<Ciudades> consultarLOVCiudades() {
        List<Ciudades> listaCiudades;
        try {
            return listaCiudades = persistenciaCiudades.consultarCiudades(em);
        } catch (Exception e) {
            System.out.println("ADMINISTRARJUZGADOS BUSCARCIUDADES /n" + e.getMessage());
            return listaCiudades = null;
        }
    }

    @Override
    public void modificarJuzgados(List<Juzgados> listaJuzgados) {
        try {
            for (int i = 0; i < listaJuzgados.size(); i++) {
                System.out.println("Administrar Modificando");
                persistenciaJuzgados.editar(em, listaJuzgados.get(i));
            }
        } catch (Exception e) {
            System.out.println("AdministrarCentrosCostos: Falló al editar el CentroCosto /n" + e.getMessage());
        }
    }

    @Override
    public void borrarJuzgados(List<Juzgados> listaJuzgados) {
        try {
            for (int i = 0; i < listaJuzgados.size(); i++) {
                System.out.println("Administrar Borrando");
                persistenciaJuzgados.borrar(em, listaJuzgados.get(i));
            }
        } catch (Exception e) {
            System.out.println("ERROR ADNUBUSTRARJUZGADOS BORRARJUZGADOS" + e.getMessage());
        }
    }

    @Override
    public void crearJuzgados(List<Juzgados> listaJuzgados) {
        try {
            for (int i = 0; i < listaJuzgados.size(); i++) {
                System.out.println("Administrar Creando");
                persistenciaJuzgados.crear(em, listaJuzgados.get(i));
            }
        } catch (Exception e) {
            System.out.println("ERROR ADMINISTRARJUZGADOS CREAR JUZGADO " + e.getMessage());
        }
    }

    public List<Juzgados> consultarJuzgadosPorCiudad(BigInteger secCiudad) {
        List<Juzgados> listaJuzgados;
        try {
            return listaJuzgados = persistenciaJuzgados.buscarJuzgadosPorCiudad(em, secCiudad);
        } catch (Exception e) {
            System.out.println("Error en ADMINISTRARJUZGADOS BUSCARJUZGADOPORCIUDAD");
            listaJuzgados = null;

            return listaJuzgados;
        }
    }

    @Override
    public List<Juzgados> LOVJuzgadosPorCiudadGeneral() {
        List<Juzgados> listaJuzgados;
        try {
            return listaJuzgados = persistenciaJuzgados.buscarJuzgados(em);
        } catch (Exception e) {
            System.out.println("Error en ADMINISTRARJUZGADOS BUSCARJUZGADOSPORCIUDADGENERAL " + e);
            listaJuzgados = null;

            return listaJuzgados;
        }
    }

    @Override
    public BigInteger verificarEerPrestamos(BigInteger secuenciaJuzgados) {
        BigInteger verificarBorradoEerPrestamos = null;
        try {
            System.out.println("Administrar SecuenciaBorrar " + secuenciaJuzgados);
            verificarBorradoEerPrestamos = persistenciaJuzgados.contadorEerPrestamos(em, secuenciaJuzgados);
        } catch (Exception e) {
            System.err.println("ERROR ADMINISTRARJUZGADOS VERIFICAREXTRASRECARGOS ERROR :" + e);
        } finally {
            return verificarBorradoEerPrestamos;
        }
    }

    @Override
    public boolean isNumeric(String cadena) {
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

}
