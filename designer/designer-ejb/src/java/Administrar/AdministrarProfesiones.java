/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Profesiones;
import InterfaceAdministrar.AdministrarProfesionesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaProfesionesInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarProfesiones implements AdministrarProfesionesInterface {

   private static Logger log = Logger.getLogger(AdministrarProfesiones.class);

    @EJB
    PersistenciaProfesionesInterface persistenciaprofesiones;
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<Entidades.Profesiones> Profesiones() {
        List<Profesiones> listaProfesiones;
        listaProfesiones = persistenciaprofesiones.profesiones(em);
        return listaProfesiones;
    }

    @Override
    public List<Entidades.Profesiones> lovProfesiones() {
        return persistenciaprofesiones.profesiones(em);
    }

    @Override
    public void crear(List<Entidades.Profesiones> listaCrear) {
        try {
            for (int i = 0; i < listaCrear.size(); i++) {
                persistenciaprofesiones.crear(em, listaCrear.get(i));
            }
        } catch (Exception e) {
            log.warn("Error en AdministrarProfesiones.crear : " + e.toString());
        }
    }

    @Override
    public void editar(List<Entidades.Profesiones> listaEditar) {
       try{
           for (int i = 0; i < listaEditar.size(); i++) {
            persistenciaprofesiones.editar(em, listaEditar.get(i));
           }
    
       }catch(Exception e){
           log.warn("Error en AdministrarProfesiones.editar : " + e.toString());
       }
    }

    @Override
    public void borrar(List<Entidades.Profesiones> listaBorrar) {
        try{
            for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaprofesiones.borrar(em, listaBorrar.get(i));
            }
   
        } catch(Exception e){
            log.warn("Error en AdministrarProfesiones.borrar : " + e.toString());
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
