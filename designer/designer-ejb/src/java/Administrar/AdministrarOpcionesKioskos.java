/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.OpcionesKioskos;
import InterfaceAdministrar.AdministrarOpcionesKioskosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaOpcionesKioskosInterface;
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
public class AdministrarOpcionesKioskos implements AdministrarOpcionesKioskosInterface {

   private static Logger log = Logger.getLogger(AdministrarOpcionesKioskos.class);

    @EJB
    PersistenciaOpcionesKioskosInterface persistenciaOpcionesKioskos;
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
         em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<OpcionesKioskos> consultarOpcionesKioskos() {
        List<OpcionesKioskos> lista = persistenciaOpcionesKioskos.consultarOpcionesKioskos(em);
        return lista;
    }

}
