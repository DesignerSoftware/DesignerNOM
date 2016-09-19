/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Instituciones;
import InterfaceAdministrar.AdministrarInstitucionesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaInstitucionesInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

@Stateful
public class AdministrarInstituciones implements AdministrarInstitucionesInterface{

    @EJB
    PersistenciaInstitucionesInterface persistenciaInstituciones;
    
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
    public List<Instituciones> Instituciones(){
        List<Instituciones> listaInstituciones;
        listaInstituciones = persistenciaInstituciones.instituciones(em);
        return listaInstituciones;
    }

    @Override
    public List<Instituciones>  lovInstituciones(){
        return persistenciaInstituciones.instituciones(em);
    }

    @Override
    public void crear(List<Instituciones> listaCrear) {
        try {
            for (int i = 0; i < listaCrear.size(); i++) {
                persistenciaInstituciones.crear(em, listaCrear.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error en AdministrarInstituciones.crear : " + e.toString());
        }
    }

    @Override
    public void editar(List<Instituciones> listaEditar) {
        try {
            for (int i = 0; i < listaEditar.size(); i++) {
                persistenciaInstituciones.editar(em, listaEditar.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error en AdministrarInstituciones.editar : " + e.toString());
        }
    }

    @Override
    public void borrar(List<Instituciones> listaBorrar) {
        try {
            for (int i = 0; i < listaBorrar.size(); i++) {
                persistenciaInstituciones.borrar(em, listaBorrar.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error en AdministrarInstituciones.borrar : " + e.toString());
        }
    }
}