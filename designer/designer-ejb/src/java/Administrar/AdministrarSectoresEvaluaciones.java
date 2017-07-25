/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.SectoresEvaluaciones;
import InterfaceAdministrar.AdministrarSectoresEvaluacionesInterface;
import InterfacePersistencia.PersistenciaSectoresEvaluacionesInterface;
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
public class AdministrarSectoresEvaluaciones implements AdministrarSectoresEvaluacionesInterface {

   private static Logger log = Logger.getLogger(AdministrarSectoresEvaluaciones.class);

    @EJB
    PersistenciaSectoresEvaluacionesInterface persistenciaSectoresEvaluaciones;
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
    public void modificarSectoresEvaluaciones(List<SectoresEvaluaciones> listaSectoresEvaluaciones) {
        for (int i = 0; i < listaSectoresEvaluaciones.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaSectoresEvaluaciones.editar(em, listaSectoresEvaluaciones.get(i));
        }
    }

    @Override
    public void borrarSectoresEvaluaciones(List<SectoresEvaluaciones> listaSectoresEvaluaciones) {
        for (int i = 0; i < listaSectoresEvaluaciones.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaSectoresEvaluaciones.borrar(em, listaSectoresEvaluaciones.get(i));
        }
    }

    @Override
    public void crearSectoresEvaluaciones(List<SectoresEvaluaciones> listaSectoresEvaluaciones) {
        for (int i = 0; i < listaSectoresEvaluaciones.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaSectoresEvaluaciones.crear(em, listaSectoresEvaluaciones.get(i));
        }
    }

    @Override
    public List<SectoresEvaluaciones> consultarSectoresEvaluaciones() {
        List<SectoresEvaluaciones> listaSectoresEvaluaciones;
        listaSectoresEvaluaciones = persistenciaSectoresEvaluaciones.consultarSectoresEvaluaciones(em);
        return listaSectoresEvaluaciones;
    }

    @Override
    public SectoresEvaluaciones consultarSectorEvaluacion(BigInteger secSectoresEvaluaciones) {
        SectoresEvaluaciones soActosInseguros;
        soActosInseguros = persistenciaSectoresEvaluaciones.consultarSectorEvaluacion(em, secSectoresEvaluaciones);
        return soActosInseguros;
    }

}
