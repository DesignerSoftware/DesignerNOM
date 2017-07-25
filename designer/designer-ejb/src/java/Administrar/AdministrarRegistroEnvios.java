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
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarRegistroEnvios implements AdministrarRegistroEnviosInterface {

   private static Logger log = Logger.getLogger(AdministrarRegistroEnvios.class);

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
        log.warn("Administrar.AdministrarRegistroEnvios.consultarEnvioCorreos()");
        List<EnvioCorreos> enviocorreos;
        try {
            enviocorreos = persistenciaEnvioCorreos.consultarEnvios(em, reporte);
        } catch (Exception e) {
            log.warn("Error Administrar.AdministrarRegistroEnvios.consultarEnvioCorreos() " + e);
            enviocorreos = new ArrayList<>();
        }
        return enviocorreos;
    }

    @Override
    public Inforeportes consultarPorSecuencia(BigInteger envio) {
        log.warn("Administrar.AdministrarRegistroEnvios.consultarPorSecuencia()");
        log.warn("envio: " + envio);
        Inforeportes envioConsultado;
        try {
            envioConsultado = persistenciaEnvioCorreos.buscarEnvioCorreoporSecuencia(em, envio);
        } catch (Exception e) {
            envioConsultado = null;
        }
        log.warn("ec: " + ec);
        return envioConsultado;
    }

    @Override
    public void editarEnvioCorreos(EnvioCorreos listaEC) {
        try {
            log.warn("Administrar.AdministrarRegistroEnvios.editarEnvioCorreos()  " + listaEC.getSecuencia());
            persistenciaEnvioCorreos.editar(em, listaEC);
        } catch (Exception ex) {
            log.warn("Error Administrar.AdministrarRegistroEnvios.editarEnvioCorreos() " + ex);
        }
    }

    @Override
    public void modificarEC(List<EnvioCorreos> listECModificadas) {
        for (int i = 0; i < listECModificadas.size(); i++) {
            log.warn("Modificando...");
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
            log.warn("Error" + e);
        }
    }

}
