/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.ColumnasEscenarios;
import InterfaceAdministrar.AdministrarConfigurarColumnasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaColumnasEscenariosInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author AndresPineda
 */
@Stateful
public class AdministrarConfigurarColumnas implements AdministrarConfigurarColumnasInterface {

   private static Logger log = Logger.getLogger(AdministrarConfigurarColumnas.class);

    @EJB
    PersistenciaColumnasEscenariosInterface persistenciaColumnasEscenarios;
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
    public List<ColumnasEscenarios> listaColumnasEscenarios() {
        try {
            List<ColumnasEscenarios> lista = persistenciaColumnasEscenarios.buscarColumnasEscenarios(em);
            return lista;
        } catch (Exception e) {
            log.warn("Error listaColumnasEscenarios Admi : " + e.toString());
            return null;
        }
    }

}
