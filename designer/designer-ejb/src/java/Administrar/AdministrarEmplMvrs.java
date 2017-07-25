/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.Motivosmvrs;
import Entidades.Mvrs;
import Entidades.OtrosCertificados;
import Entidades.TiposCertificados;
import InterfaceAdministrar.AdministrarEmplMvrsInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaMotivosMvrsInterface;
import InterfacePersistencia.PersistenciaMvrsInterface;
import InterfacePersistencia.PersistenciaOtrosCertificadosInterface;
import InterfacePersistencia.PersistenciaTiposCertificadosInterface;
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
public class AdministrarEmplMvrs implements AdministrarEmplMvrsInterface {

   private static Logger log = Logger.getLogger(AdministrarEmplMvrs.class);

    @EJB
    PersistenciaOtrosCertificadosInterface persistenciaOtrosCertificados;
    @EJB
    PersistenciaMvrsInterface persistenciaMvrs;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleados;
    @EJB
    PersistenciaMotivosMvrsInterface persistenciaMotivos;
    @EJB
    PersistenciaTiposCertificadosInterface persistenciaTiposCertificados;
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
    public List<OtrosCertificados> listOtrosCertificadosEmpleado(BigInteger secuencia) {
        try {
            List<OtrosCertificados> listOtrosCertificados;
            listOtrosCertificados = persistenciaOtrosCertificados.buscarOtrosCertificadosEmpleado(em,secuencia);
            return listOtrosCertificados;
        } catch (Exception e) {
            log.warn("Error listOtrosCertificadosEmpleado Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Mvrs> listMvrsEmpleado(BigInteger secuenciaEmpl) {
        try {
            List<Mvrs> listMvrs;
            listMvrs = persistenciaMvrs.buscarMvrsEmpleado(em,secuenciaEmpl);
            return listMvrs;
        } catch (Exception e) {
            log.warn("Error listMvrsEmpleado Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearOtrosCertificados(List<OtrosCertificados> crearOtrosCertificados) {
        try {
            for (int i = 0; i < crearOtrosCertificados.size(); i++) {
                if (crearOtrosCertificados.get(i).getTipocertificado().getSecuencia() == null) {
                    crearOtrosCertificados.get(i).setTipocertificado(null);
                }
                persistenciaOtrosCertificados.crear(em,crearOtrosCertificados.get(i));
            }
        } catch (Exception e) {
            log.warn("Error en crearOtrosCertificados Admi : " + e.toString());
        }
    }

    @Override
    public void modificarOtrosCertificados(List<OtrosCertificados> modificarOtrosCertificados) {
        try {
            for (int i = 0; i < modificarOtrosCertificados.size(); i++) {
                if (modificarOtrosCertificados.get(i).getTipocertificado().getSecuencia() == null) {
                    modificarOtrosCertificados.get(i).setTipocertificado(null);
                }
                persistenciaOtrosCertificados.editar(em,modificarOtrosCertificados.get(i));
            }
        } catch (Exception e) {
            log.warn("Error en modificarOtrosCertificados Admi : " + e.toString());
        }
    }

    @Override
    public void borrarOtrosCertificados(List<OtrosCertificados> borrarOtrosCertificados) {
        try {
            for (int i = 0; i < borrarOtrosCertificados.size(); i++) {
                if (borrarOtrosCertificados.get(i).getTipocertificado().getSecuencia() == null) {
                    borrarOtrosCertificados.get(i).setTipocertificado(null);
                }
                persistenciaOtrosCertificados.borrar(em,borrarOtrosCertificados.get(i));
            }
        } catch (Exception e) {
            log.warn("Error en borrarOtrosCertificados Admi : " + e.toString());
        }
    }

    @Override
    public void crearMvrs(List<Mvrs> crearMvrs) {
        try {
            for (int i = 0; i < crearMvrs.size(); i++) {
                if (crearMvrs.get(i).getMotivo().getSecuencia() == null) {
                    crearMvrs.get(i).setMotivo(null);
                }
                persistenciaMvrs.crear(em,crearMvrs.get(i));
            }
        } catch (Exception e) {
            log.warn("Error en crearMvrs Admi : " + e.toString());
        }
    }

    @Override
    public void modificarMvrs(List<Mvrs> modificarMvrs) {
        try {
            for (int i = 0; i < modificarMvrs.size(); i++) {
                if (modificarMvrs.get(i).getMotivo().getSecuencia() == null) {
                    modificarMvrs.get(i).setMotivo(null);
                }
                persistenciaMvrs.editar(em,modificarMvrs.get(i));
            }
        } catch (Exception e) {
            log.warn("Error en modificarMvrs Admi : " + e.toString());
        }
    }

    @Override
    public void borrarMvrs(List<Mvrs> borrarMvrs) {
        try {
            for (int i = 0; i < borrarMvrs.size(); i++) {
                if (borrarMvrs.get(i).getMotivo().getSecuencia() == null) {
                    borrarMvrs.get(i).setMotivo(null);
                }
                persistenciaMvrs.borrar(em,borrarMvrs.get(i));
            }
        } catch (Exception e) {
            log.warn("Error en borrarMvrs Admi : " + e.toString());
        }
    }

    @Override
    public List<Motivosmvrs> listMotivos() {
        try {
            List<Motivosmvrs> listMotivos;
            listMotivos = persistenciaMotivos.buscarMotivosMvrs(em);
            return listMotivos;
        } catch (Exception e) {
            log.warn("Error listMotivos Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<TiposCertificados> listTiposCertificados() {
        try {
            List<TiposCertificados> listTiposCertificados;
            listTiposCertificados = persistenciaTiposCertificados.buscarTiposCertificados(em);
            return listTiposCertificados;
        } catch (Exception e) {
            log.warn("Error listTiposCertificados Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public Empleados empleadoActual(BigInteger secuencia) {
        try {
            Empleados empl = persistenciaEmpleados.buscarEmpleado(em,secuencia);
            return empl;
        } catch (Exception e) {
            log.warn("Error empleadoActual Admi : " + e.toString());
            return null;
        }
    }
}
