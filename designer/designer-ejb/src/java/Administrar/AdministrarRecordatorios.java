/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Administrar;

import Entidades.Recordatorios;
import InterfaceAdministrar.AdministrarRecordatoriosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaRecordatoriosInterface;
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
public class AdministrarRecordatorios implements AdministrarRecordatoriosInterface {

   private static Logger log = Logger.getLogger(AdministrarRecordatorios.class);

    @EJB
    PersistenciaRecordatoriosInterface persistenciaRecordatorios;
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
    
    
    public List<Recordatorios> recordatorios(){
        List<Recordatorios> listaRecordatorios;
        listaRecordatorios = persistenciaRecordatorios.proverbiosRecordatorios(em);
        return listaRecordatorios;
    }
    
    
    public List<Recordatorios> mensajesRecordatorios(){
        List<Recordatorios> listaMensajesRecordatorios;
        listaMensajesRecordatorios = persistenciaRecordatorios.mensajesRecordatorios(em);
        return listaMensajesRecordatorios;
    }
    
    
    public void borrar(Recordatorios proverbios) {
        persistenciaRecordatorios.borrar(em, proverbios);
    }

    
    public void crear(Recordatorios proverbios) {
        persistenciaRecordatorios.crear(em, proverbios);
    }

    
    public void modificar(List<Recordatorios> listaProverbiosModificar) {
        for (int i = 0; i < listaProverbiosModificar.size(); i++) {
            if (listaProverbiosModificar.get(i).getMensaje() == null) {
                listaProverbiosModificar.get(i).setMensaje(null);
            }
            persistenciaRecordatorios.editar(em, listaProverbiosModificar.get(i));
        }
    }
    
    @Override
    public void borrarMU(Recordatorios mensajeUsuario) {
        persistenciaRecordatorios.borrar(em, mensajeUsuario);
    }

    @Override
    public void crearMU(Recordatorios mensajeUsuario) {
        persistenciaRecordatorios.crear(em, mensajeUsuario);
    }

    @Override
    public void modificarMU(List<Recordatorios> listaMensajesUsuariosModificar) {
        for (int i = 0; i < listaMensajesUsuariosModificar.size(); i++) {
            if (listaMensajesUsuariosModificar.get(i).getMensaje() == null) {
                listaMensajesUsuariosModificar.get(i).setMensaje(null);
            }
            if (listaMensajesUsuariosModificar.get(i).getAno()== null) {
                listaMensajesUsuariosModificar.get(i).setAno(null);
            }
            if (listaMensajesUsuariosModificar.get(i).getMes() == null) {
                listaMensajesUsuariosModificar.get(i).setMes(null);
            }
            if (listaMensajesUsuariosModificar.get(i).getDia() == null) {
                listaMensajesUsuariosModificar.get(i).setDia(null);
            }
            persistenciaRecordatorios.editar(em, listaMensajesUsuariosModificar.get(i));
        }
    }
}
