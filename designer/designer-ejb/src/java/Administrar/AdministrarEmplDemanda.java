/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Demandas;
import Entidades.Empleados;
import Entidades.MotivosDemandas;
import InterfaceAdministrar.AdministrarEmplDemandaInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaDemandasInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaMotivosDemandasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateless
public class AdministrarEmplDemanda implements AdministrarEmplDemandaInterface {

    @EJB
    PersistenciaDemandasInterface persistenciaDemadas;
    @EJB
    PersistenciaMotivosDemandasInterface persistenciaMotivosDemandas;
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
    public Empleados actualEmpleado(BigInteger secuencia) {
        try {
            Empleados empl = persistenciaEmpleado.buscarEmpleado(em,secuencia);
            return empl;
        } catch (Exception e) {
            System.out.println("Error actualEmpleado Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<MotivosDemandas> listMotivosDemandas() {
        try {
            List<MotivosDemandas> listMotivosD = persistenciaMotivosDemandas.buscarMotivosDemandas(em);
            return listMotivosD;
        } catch (Exception e) {
            System.out.println("Error listMotivosDemandas Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Demandas> listDemandasEmpleadoSecuencia(BigInteger secuencia) {
        try {
            List<Demandas> listDemandas = persistenciaDemadas.demandasPersona(em,secuencia);
            return listDemandas;
        } catch (Exception e) {
            System.out.println("Error listDemandasEmpleadoSecuencia Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearDemandas(List<Demandas> listD) {
        try {
            for (int i = 0; i < listD.size(); i++) {
                if (listD.get(i).getMotivo().getSecuencia() == null) {
                    listD.get(i).setMotivo(null);
                }
                persistenciaDemadas.crear(em,listD.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error crearDemandas Admi : " + e.toString());
        }
    }

    @Override
    public void editarDemandas(List<Demandas> listD) {
        try {
            for (int i = 0; i < listD.size(); i++) {
                if (listD.get(i).getMotivo().getSecuencia() == null) {
                    listD.get(i).setMotivo(null);
                }
                persistenciaDemadas.editar(em,listD.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error editarDemandas Admi : " + e.toString());
        }
    }

    @Override
    public void borrarDemandas(List<Demandas> listD) {
        try {
            for (int i = 0; i < listD.size(); i++) {
                if (listD.get(i).getMotivo().getSecuencia() == null) {
                    listD.get(i).setMotivo(null);
                }
                persistenciaDemadas.borrar(em,listD.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error borrarDemandas Admi : " + e.toString());
        }
    }
}
