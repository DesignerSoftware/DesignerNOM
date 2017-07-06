/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.RiesgosProfesionales;
import InterfaceAdministrar.AdministrarRiesgosProfesionalesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaRiesgosProfesionalesInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarRiesgosProfesionales implements AdministrarRiesgosProfesionalesInterface {

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaRiesgosProfesionalesInterface persistenciaRiesgos;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<RiesgosProfesionales> listRiesgoProfesional() {
        try {
            List<RiesgosProfesionales> listaRiesgos = persistenciaRiesgos.riesgosProfesionales(em);
            return listaRiesgos;
        } catch (Exception e) {
            System.out.println("error en AdministrarRiesgosProfesionales.listRiesgoProfesional " + e.getMessage());
            return null;
        }
    }

    @Override
    public void crearRiesgoProfesional(List<RiesgosProfesionales> listaVD) {
        try {
            for (int i = 0; i < listaVD.size(); i++) {
                persistenciaRiesgos.crear(em, listaVD.get(i));
            }

        } catch (Exception e) {
            System.out.println("error en crearRiesgoProfesional administrar :" + e.getMessage());
        }

    }

    @Override
    public void editarRiesgoProfesional(List<RiesgosProfesionales> listaVD) {
        try {
            for (int i = 0; i < listaVD.size(); i++) {
                persistenciaRiesgos.editar(em, listaVD.get(i));
            }
        } catch (Exception e) {
            System.out.println("error en editarRiesgoProfesional administrar :" + e.getMessage());
        }
    }

    @Override
    public void borrarRiesgoProfesional(List<RiesgosProfesionales> listaVD) {
        try {
            for (int i = 0; i < listaVD.size(); i++) {
                persistenciaRiesgos.borrar(em, listaVD.get(i));
            }
        } catch (Exception e) {
            System.out.println("error en borrarRiesgoProfesional administrar :" + e.getMessage());
        }
    }

}
