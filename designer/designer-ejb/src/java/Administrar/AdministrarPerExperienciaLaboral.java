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

/**
 *
 * @author user
 */
@Stateful
public class AdministrarPerExperienciaLaboral implements AdministrarPerExperienciaLaboralInterface {

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
            System.out.println("Error empleadoActual Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<SectoresEconomicos> listSectoresEconomicos() {
        try {
            List<SectoresEconomicos> retorno = persistenciaSectoresEconomicos.buscarSectoresEconomicos(em);
            return retorno;
        } catch (Exception e) {
            System.out.println("Error listSectoresEconomicos Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<MotivosRetiros> listMotivosRetiros() {
        try {
            List<MotivosRetiros> retorno = persistenciaMotivosRetiros.consultarMotivosRetiros(em);
            return retorno;
        } catch (Exception e) {
            System.out.println("Error listMotivosRetiros Admi : " + e.toString());
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
                String aux1, aux2, aux3;
                aux1 = listHEL.get(i).getAlcance().toUpperCase();
                listHEL.get(i).setAlcance(aux1);
                aux2 = listHEL.get(i).getJefeinmediato().toUpperCase();
                listHEL.get(i).setJefeinmediato(aux2);
                aux3 = listHEL.get(i).getEmpresa().toUpperCase();
                listHEL.get(i).setEmpresa(aux3);
                persistenciaHvExperienciasLaborales.crear(em, listHEL.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error crearExperienciaLaboral Admi : " + e.toString());
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
                String aux1, aux2, aux3;
                aux1 = listHEL.get(i).getAlcance().toUpperCase();
                listHEL.get(i).setAlcance(aux1);
                aux2 = listHEL.get(i).getJefeinmediato().toUpperCase();
                listHEL.get(i).setJefeinmediato(aux2);
                aux3 = listHEL.get(i).getEmpresa().toUpperCase();
                listHEL.get(i).setEmpresa(aux3);
                persistenciaHvExperienciasLaborales.editar(em, listHEL.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error editarExperienciaLaboral Admi : " + e.toString());
        }
    }

    @Override
    public void borrarExperienciaLaboral(List<HvExperienciasLaborales> listHEL) {
        try {
            for (int i = 0; i < listHEL.size(); i++) {
                if (listHEL.get(i).getMotivoretiro().getSecuencia() == null) {
                    listHEL.get(i).setMotivoretiro(null);
                }
                if (listHEL.get(i).getSectoreconomico().getSecuencia() == null) {
                    listHEL.get(i).setSectoreconomico(null);
                }
                persistenciaHvExperienciasLaborales.borrar(em, listHEL.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error borrarExperienciaLaboral Admi : " + e.toString());
        }
    }

    @Override
    public List<HvExperienciasLaborales> listExperienciasLaboralesSecuenciaEmpleado(BigInteger secuencia) {
        try {
            List<HvExperienciasLaborales> retorno = persistenciaHvExperienciasLaborales.experienciasLaboralesSecuenciaEmpleado(em, secuencia);
            return retorno;
        } catch (Exception e) {
            System.out.println("Error listExperienciasLaboralesSecuenciaEmpleado Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public HVHojasDeVida obtenerHojaVidaPersona(BigInteger secuencia) {
        try {
            HVHojasDeVida hojaVida = persistenciaHVHojasDeVida.hvHojaDeVidaPersona(em, secuencia);
            return hojaVida;
        } catch (Exception e) {
            System.out.println("Error obtenerHojaVidaPersona Admi : "+e.toString());
            return null;
        }
    }
}
