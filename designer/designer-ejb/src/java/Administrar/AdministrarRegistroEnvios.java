/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.EnvioCorreos;
import Entidades.Inforeportes;
import InterfaceAdministrar.AdministrarRegistroEnviosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEnvioCorreosInterface;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarRegistroEnvios implements AdministrarRegistroEnviosInterface {

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaEnvioCorreosInterface persistenciaEnvioCorreos;

    private EntityManager em;
    private EnvioCorreos ec;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);

    }

    @Override
    public List<EnvioCorreos> consultarEnvioCorreos(BigInteger reporte) {
        System.out.println("Administrar.AdministrarRegistroEnvios.consultarEnvioCorreos()");
        List<EnvioCorreos> enviocorreos;
        try {
            enviocorreos = persistenciaEnvioCorreos.consultarEnvios(em, reporte);
        } catch (Exception e) {
            System.out.println("Error Administrar.AdministrarRegistroEnvios.consultarEnvioCorreos() " + e);
            enviocorreos = new ArrayList<>();
        }
        return enviocorreos;
    }

    @Override
    public Inforeportes consultarPorSecuencia(BigInteger envio) {
        System.out.println("Administrar.AdministrarRegistroEnvios.consultarPorSecuencia()");
        System.out.println("envio: " + envio);
        Inforeportes envioConsultado;
        try {
            envioConsultado = persistenciaEnvioCorreos.buscarEnvioCorreoporSecuencia(em, envio);
        } catch (Exception e) {
            envioConsultado = null;
        }
        System.out.println("ec: " + ec);
        return envioConsultado;
    }

    @Override
    public void editarEnvioCorreos(EnvioCorreos listaEC) {
        try {
            System.out.println("Administrar.AdministrarRegistroEnvios.editarEnvioCorreos()  " + listaEC.getSecuencia());
            persistenciaEnvioCorreos.editar(em, listaEC);
        } catch (Exception ex) {
            System.out.println("Error Administrar.AdministrarRegistroEnvios.editarEnvioCorreos() " + ex);
        }
    }

    @Override
    public void modificarEC(List<EnvioCorreos> listECModificadas) {
        for (int i = 0; i < listECModificadas.size(); i++) {
            System.out.println("Modificando...");
            if (listECModificadas.get(i).getCodigoEmpleado() != null && listECModificadas.get(i).getCodigoEmpleado().getSecuencia() == null) {
                listECModificadas.get(i).setCodigoEmpleado(null);
                ec = listECModificadas.get(i);
                persistenciaEnvioCorreos.editar(em, ec);
            } else {
                ec = listECModificadas.get(i);
                persistenciaEnvioCorreos.editar(em, ec);
            }

        }
    }

    @Override
    public void borrarEnvioCorreos(EnvioCorreos listaEC) {
        try {
            persistenciaEnvioCorreos.borrar(em, listaEC);
        } catch (Exception e) {
            System.out.println("Error" + e);
        }
    }

}
