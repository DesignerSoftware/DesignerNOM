/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Jornadas;
import Entidades.JornadasLaborales;
import Entidades.JornadasSemanales;
import InterfaceAdministrar.AdministrarJornadasSemanalesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaJornadasInterface;
import InterfacePersistencia.PersistenciaJornadasLaboralesInterface;
import InterfacePersistencia.PersistenciaJornadasSemanalesInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
@Stateful
public class AdministrarJornadasSemanales implements AdministrarJornadasSemanalesInterface {

    @EJB
    PersistenciaJornadasSemanalesInterface persistenciaJornadasSemanales;
    @EJB
    PersistenciaJornadasInterface persistenciaJornadasInterface;
    @EJB
    PersistenciaJornadasLaboralesInterface persistenciaJornadasLaboralesInterface;
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    public List<JornadasSemanales> consultarJornadasSemanales() {
        List<JornadasSemanales> listaJornadasSemanales;
        listaJornadasSemanales = persistenciaJornadasSemanales.buscarJornadasSemanales(em);
        return listaJornadasSemanales;
    }

    public List<JornadasLaborales> consultarJornadasLaborales() {
        List<JornadasLaborales> listaJornadasLaborales;
        listaJornadasLaborales = persistenciaJornadasLaboralesInterface.buscarJornadasLaborales(em);
        return listaJornadasLaborales;
    }

    public List<Jornadas> consultarJornadas() {
        List<Jornadas> listaJornadas;
        listaJornadas = persistenciaJornadasInterface.consultarJornadas(em);
        return listaJornadas;
    }

    @Override
    public void modificarJornadasSemanales(List<JornadasSemanales> listaJornadasSemanales) {
        JornadasSemanales c;
        for (int i = 0; i < listaJornadasSemanales.size(); i++) {
            System.out.println("Modificando...");
            c = listaJornadasSemanales.get(i);
            persistenciaJornadasSemanales.editar(em, c);
        }
    }

    @Override
    public void borrarJornadasSemanales(List<JornadasSemanales> listaJornadasSemanales) {
        for (int i = 0; i < listaJornadasSemanales.size(); i++) {
            System.out.println("Borrando...");
            persistenciaJornadasSemanales.borrar(em, listaJornadasSemanales.get(i));
        }
    }

    @Override
    public void crearJornadasSemanales(List<JornadasSemanales> listaJornadasSemanales) {
        for (int i = 0; i < listaJornadasSemanales.size(); i++) {
            System.out.println("Creando...");
            persistenciaJornadasSemanales.crear(em, listaJornadasSemanales.get(i));
        }
    }

}
