/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Administrar;

import Entidades.SoPoblacionObjetivos;
import InterfaceAdministrar.AdministrarSoPoblacionObjetivosInterface;
import InterfacePersistencia.PersistenciaSoPoblacionObjetivosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarSoPoblacionObjetivos implements AdministrarSoPoblacionObjetivosInterface {

   private static Logger log = Logger.getLogger(AdministrarSoPoblacionObjetivos.class);
    @EJB
    PersistenciaSoPoblacionObjetivosInterface persistenciaSoPoblacionObjetivos;
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

    public void modificarSoPoblacionObjetivos(List<SoPoblacionObjetivos> listSoPoblacionObjetivos) {
        for (int i = 0; i < listSoPoblacionObjetivos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaSoPoblacionObjetivos.editar(em, listSoPoblacionObjetivos.get(i));
        }
    }

    public void borrarSoPoblacionObjetivos(List<SoPoblacionObjetivos> listSoPoblacionObjetivos) {
        for (int i = 0; i < listSoPoblacionObjetivos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaSoPoblacionObjetivos.borrar(em, listSoPoblacionObjetivos.get(i));
        }
    }

    public void crearSoPoblacionObjetivos(List<SoPoblacionObjetivos> listSoPoblacionObjetivos) {
        for (int i = 0; i < listSoPoblacionObjetivos.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaSoPoblacionObjetivos.crear(em, listSoPoblacionObjetivos.get(i));
        }
    }

 @Override
    public List<SoPoblacionObjetivos> consultarSoPoblacionObjetivos() {
        List<SoPoblacionObjetivos> listSoPoblacionObjetivos;
        listSoPoblacionObjetivos = persistenciaSoPoblacionObjetivos.consultarSoPoblacionObjetivos(em);
        return listSoPoblacionObjetivos;
    }

    public SoPoblacionObjetivos consultarEvalCompetencia(BigInteger secSoPoblacionObjetivo) {
        SoPoblacionObjetivos evalCompetencias;
        evalCompetencias = persistenciaSoPoblacionObjetivos.buscarSoPoblacionObjetivo(em, secSoPoblacionObjetivo);
        return evalCompetencias;
    }

}
