/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.AportesEntidadesXDia;
import InterfaceAdministrar.AdministrarAportesEntidadesXDiaInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaAportesEntidadesXDiaInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
@Local
public class AdministrarAportesEntidadesXDia implements AdministrarAportesEntidadesXDiaInterface {

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaAportesEntidadesXDiaInterface persistenciaAportesEntidadesXDia;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<AportesEntidadesXDia> consultarAportesEntidadesXDia() {
       List<AportesEntidadesXDia> lista = persistenciaAportesEntidadesXDia.consultarAportesEntidadesXDia(em);
       return lista;
    }

    @Override
    public List<AportesEntidadesXDia> consultarAportesEntidadesPorEmpleadoMesYAnio(BigInteger secEmpleado, short mes, short ano) {
        List<AportesEntidadesXDia> lista = persistenciaAportesEntidadesXDia.consultarAportesEntidadesPorEmpleadoMesYAnio(em, secEmpleado, mes, ano);
        return lista;
    }

    @Override
    public void crearAportesEntidadesXDia(List<AportesEntidadesXDia> listaAE) {
     try {
            for (int i = 0; i < listaAE.size(); i++) {

//                if (listAE.get(i).getTipoentidad().getSecuencia() == null) {
//                    listAE.get(i).setTipoentidad(null);
//                } else if (listAE.get(i).getEmpleado().getSecuencia() == null) {
//                    listAE.get(i).setEmpleado(null);
//                }
                persistenciaAportesEntidadesXDia.crear(em, listaAE.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error crearAportesEntidadesXDia Admi : " + e.toString());
        }
    }

    @Override
    public void editarAportesEntidadesXDia(List<AportesEntidadesXDia> listAE) {
       try {
            for (int i = 0; i < listAE.size(); i++) {

//                if (listAE.get(i).getTipoentidad().getSecuencia() == null) {
//                    listAE.get(i).setTipoentidad(null);
//                } else if (listAE.get(i).getEmpleado().getSecuencia() == null) {
//                    listAE.get(i).setEmpleado(null);
//                }
                persistenciaAportesEntidadesXDia.editar(em, listAE.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error editarAportesEntidadesXDia Admi : " + e.toString());
        }
    }

    @Override
    public void borrarAportesEntidadesXDia(List<AportesEntidadesXDia> listAE) {
        try {
            for (int i = 0; i < listAE.size(); i++) {

//                if (listAE.get(i).getTipoentidad().getSecuencia() == null) {
//                    listAE.get(i).setTipoentidad(null);
//                } else if (listAE.get(i).getEmpleado().getSecuencia() == null) {
//                    listAE.get(i).setEmpleado(null);
//                }
                persistenciaAportesEntidadesXDia.borrar(em, listAE.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error borrarAportesEntidadesXDia Admi : " + e.toString());
        }
    }

    @Override
    public BigDecimal consultarTarifas(BigInteger secEmpresa, short mes, short ano, BigInteger secEmpleado, BigInteger secTipoEntidad) {
       BigDecimal tarifa = persistenciaAportesEntidadesXDia.cosultarTarifa(em, secEmpresa, secEmpleado, mes, ano, secTipoEntidad);
       return tarifa;
    }

}
