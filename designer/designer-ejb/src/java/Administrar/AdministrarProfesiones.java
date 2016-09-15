/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Profesiones;
import InterfaceAdministrar.AdministrarProfesionesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaProfesionesInterface;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

@Stateful
public class AdministrarProfesiones implements AdministrarProfesionesInterface,Serializable {

    @EJB
    PersistenciaProfesionesInterface persistenciaProfesiones;
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
    public List<Profesiones> Profesiones() {
        List<Profesiones> listaProfesiones;
        listaProfesiones = persistenciaProfesiones.profesiones(em);
        return listaProfesiones;
    }

    @Override
    public List<Profesiones> lovProfesiones() {
        return persistenciaProfesiones.profesiones(em);
    }

    @Override
    public void crear(List<Profesiones> listaCrear) {
        try {
            for (int i = 0; i < listaCrear.size(); i++) {
                persistenciaProfesiones.crear(em, listaCrear.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error en AdministrarProfesiones.crear : " + e.toString());
        }
    }

    @Override
    public void editar(List<Profesiones> listaEditar) {
        try {
            for (int i = 0; i < listaEditar.size(); i++) {
                persistenciaProfesiones.editar(em, listaEditar.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error en AdministrarProfesiones.editar : " + e.toString());
        }
    }

    @Override
    public void borrar(List<Profesiones> listaBorrar) {
        try {
            for (int i = 0; i < listaBorrar.size(); i++) {
                persistenciaProfesiones.borrar(em, listaBorrar.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error en AdministrarProfesiones.editar : " + e.toString());
        }
    }
}
