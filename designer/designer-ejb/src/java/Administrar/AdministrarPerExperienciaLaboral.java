/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.HVHojasDeVida;
import Entidades.HvExperienciasLaborales;
import Entidades.MotivosRetiros;
import Entidades.SectoresEconomicos;
import InterfaceAdministrar.AdministrarPerExperienciaLaboralInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaHVHojasDeVidaInterface;
import InterfacePersistencia.PersistenciaHvExperienciasLaboralesInterface;
import InterfacePersistencia.PersistenciaMotivosRetirosInterface;
import InterfacePersistencia.PersistenciaSectoresEconomicosInterface;
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
public class AdministrarPerExperienciaLaboral implements AdministrarPerExperienciaLaboralInterface {

   private static Logger log = Logger.getLogger(AdministrarPerExperienciaLaboral.class);

    @EJB
    PersistenciaHvExperienciasLaboralesInterface persistenciaHvExperienciasLaborales;
    @EJB
    PersistenciaMotivosRetirosInterface persistenciaMotivosRetiros;
    @EJB
    PersistenciaSectoresEconomicosInterface persistenciaSectoresEconomicos;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    @EJB
    PersistenciaHVHojasDeVidaInterface persistenciaHVHojasDeVida;
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
    public Empleados empleadoActual(BigInteger secuencia) {
        try {
            Empleados empl = persistenciaEmpleado.buscarEmpleado(em, secuencia);
            return empl;
        } catch (Exception e) {
            log.warn("Error empleadoActual Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<SectoresEconomicos> listSectoresEconomicos() {
        try {
            List<SectoresEconomicos> retorno = persistenciaSectoresEconomicos.buscarSectoresEconomicos(em);
            return retorno;
        } catch (Exception e) {
            log.warn("Error listSectoresEconomicos Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<MotivosRetiros> listMotivosRetiros() {
        try {
            List<MotivosRetiros> retorno = persistenciaMotivosRetiros.consultarMotivosRetiros(em);
            return retorno;
        } catch (Exception e) {
            log.warn("Error listMotivosRetiros Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearExperienciaLaboral(List<HvExperienciasLaborales> listHEL) {
        try {
            for (int i = 0; i < listHEL.size(); i++) {
                if (listHEL.get(i).getMotivoretiro().getSecuencia() == null) {
                    listHEL.get(i).setMotivoretiro(null);
                }
                if (listHEL.get(i).getSectoreconomico().getSecuencia() == null) {
                    listHEL.get(i).setSectoreconomico(null);
                }

                if (listHEL.get(i).getHojadevida() == null) {
                    listHEL.get(i).setHojadevida(new HVHojasDeVida());
                }

                persistenciaHvExperienciasLaborales.crear(em, listHEL.get(i));
            }
        } catch (Exception e) {
            log.warn("Error crearExperienciaLaboral Admi : " + e.toString());
        }
    }

    @Override
    public void editarExperienciaLaboral(List<HvExperienciasLaborales> listHEL) {
        try {
            for (int i = 0; i < listHEL.size(); i++) {
                if (listHEL.get(i).getMotivoretiro().getSecuencia() == null) {
                    listHEL.get(i).setMotivoretiro(null);
                }
                if (listHEL.get(i).getSectoreconomico().getSecuencia() == null) {
                    listHEL.get(i).setSectoreconomico(null);
                }

                if (listHEL.get(i).getHojadevida() == null) {
                    listHEL.get(i).setHojadevida(new HVHojasDeVida());
                }

                persistenciaHvExperienciasLaborales.editar(em, listHEL.get(i));
            }
        } catch (Exception e) {
            log.warn("Error editarExperienciaLaboral Admi : " + e.toString());
        }
    }

    @Override
    public void borrarExperienciaLaboral(List<HvExperienciasLaborales> listHEL) {
        try {
            for (int i = 0; i < listHEL.size(); i++) {
//                if (listHEL.get(i).getMotivoretiro().getSecuencia() == null) {
//                    listHEL.get(i).setMotivoretiro(null);
//                }
                if (listHEL.get(i).getSectoreconomico().getSecuencia() == null) {
                    listHEL.get(i).setSectoreconomico(null);
                }
                persistenciaHvExperienciasLaborales.borrar(em, listHEL.get(i));
            }
        } catch (Exception e) {
            log.warn("Error borrarExperienciaLaboral Admi : " + e.toString());
        }
    }

    @Override
    public List<HvExperienciasLaborales> listExperienciasLaboralesSecuenciaEmpleado(BigInteger secuencia) {
        try {
            List<HvExperienciasLaborales> retorno = persistenciaHvExperienciasLaborales.experienciasLaboralesSecuenciaEmpleado(em, secuencia);
            return retorno;
        } catch (Exception e) {
            log.warn("Error listExperienciasLaboralesSecuenciaEmpleado Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public HVHojasDeVida obtenerHojaVidaPersona(BigInteger secuencia) {
        try {
            HVHojasDeVida hojaVida = persistenciaHVHojasDeVida.hvHojaDeVidaPersona(em, secuencia);
            return hojaVida;
        } catch (Exception e) {
            log.warn("Error obtenerHojaVidaPersona Admi : " + e.toString());
            return null;
        }
    }
}
