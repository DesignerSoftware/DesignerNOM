/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.Indicadores;
import Entidades.TiposIndicadores;
import Entidades.VigenciasIndicadores;
import InterfaceAdministrar.AdministrarEmplVigenciaIndicadorInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaIndicadoresInterface;
import InterfacePersistencia.PersistenciaTiposIndicadoresInterface;
import InterfacePersistencia.PersistenciaVigenciasIndicadoresInterface;
import java.math.BigInteger;
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
public class AdministrarEmplVigenciaIndicador implements AdministrarEmplVigenciaIndicadorInterface {

   private static Logger log = Logger.getLogger(AdministrarEmplVigenciaIndicador.class);

    @EJB
    PersistenciaVigenciasIndicadoresInterface persistenciaVigenciasIndicadores;
    @EJB
    PersistenciaTiposIndicadoresInterface persistenciaTiposIndicadores;
    @EJB
    PersistenciaIndicadoresInterface persistenciaIndicadores;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
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
    public List<Indicadores> listIndicadores() {
        try {
            List<Indicadores> retorno = persistenciaIndicadores.buscarIndicadores(em);
            return retorno;
        } catch (Exception e) {
            log.warn("Error listIndicadores Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<TiposIndicadores> listTiposIndicadores() {
        try {
            List<TiposIndicadores> retorno = persistenciaTiposIndicadores.buscarTiposIndicadores(em);
            return retorno;
        } catch (Exception e) {
            log.warn("Error listTiposIndicadores Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Empleados empleadoActual(BigInteger secuencia) {
        try {
            Empleados actual = persistenciaEmpleado.buscarEmpleado(em,secuencia);
            return actual;
        } catch (Exception e) {
            log.warn("Error empleadoActual Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<VigenciasIndicadores> listVigenciasIndicadoresEmpleado(BigInteger secuencia) {
        try {
            List<VigenciasIndicadores> retorno = persistenciaVigenciasIndicadores.indicadoresTotalesEmpleadoSecuencia(em,secuencia);
            return retorno;
        } catch (Exception e) {
            log.warn("Error listVigenciasAfiliacioneaEmpleado Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearVigenciasIndicadores(List<VigenciasIndicadores> listVI) {
        try {
            for (int i = 0; i < listVI.size(); i++) {
                if (listVI.get(i).getIndicador().getSecuencia() == null) {
                    listVI.get(i).setIndicador(null);
                }
                if (listVI.get(i).getTipoindicador().getSecuencia() == null) {
                    listVI.get(i).setTipoindicador(null);
                }
                persistenciaVigenciasIndicadores.crear(em,listVI.get(i));
            }
        } catch (Exception e) {
            log.warn("Error crearVigenciasIndicadores Admi : " + e.toString());
        }
    }

    @Override
    public void editarVigenciasIndicadores(List<VigenciasIndicadores> listVI) {
        try {
            for (int i = 0; i < listVI.size(); i++) {
                if (listVI.get(i).getIndicador().getSecuencia() == null) {
                    listVI.get(i).setIndicador(null);
                }
                if (listVI.get(i).getTipoindicador().getSecuencia() == null) {
                    listVI.get(i).setTipoindicador(null);
                }
                persistenciaVigenciasIndicadores.editar(em,listVI.get(i));
            }
        } catch (Exception e) {
            log.warn("Error editarVigenciasIndicadores Admi : " + e.toString());
        }
    }

    @Override
    public void borrarVigenciasIndicadores(List<VigenciasIndicadores> listVI) {
        try {
            for (int i = 0; i < listVI.size(); i++) {
                if (listVI.get(i).getIndicador().getSecuencia() == null) {
                    listVI.get(i).setIndicador(null);
                }
                if (listVI.get(i).getTipoindicador().getSecuencia() == null) {
                    listVI.get(i).setTipoindicador(null);
                }
                persistenciaVigenciasIndicadores.borrar(em,listVI.get(i));
            }
        } catch (Exception e) {
            log.warn("Error borrarVigenciasIndicadores Admi : " + e.toString());
        }
    }
}
