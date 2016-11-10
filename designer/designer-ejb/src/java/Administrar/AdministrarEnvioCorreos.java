package Administrar;

import ClasesAyuda.EnvioCorreo;
import Entidades.ConfiguracionCorreo;
import Entidades.Empleados;
import InterfaceAdministrar.AdministrarEnvioCorreosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaConfiguracionCorreoInterface;
import InterfacePersistencia.PersistenciaEnvioCorreosInterface;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import Persistencia.PersistenciaActualUsuario;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'EnvioCorreos'.
 *
 */
@Stateful
public class AdministrarEnvioCorreos implements AdministrarEnvioCorreosInterface {

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaEnvioCorreosInterface persistenciaEnvioCorreos;
    @EJB
    PersistenciaConfiguracionCorreoInterface persistenciaConfiguracionCorreo;
    @EJB
    PersistenciaParametrosEstructurasInterface persistenciaParametrosEstructuras;
    @EJB
    PersistenciaActualUsuarioInterface persistenciaActualUsuario;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<Empleados> correoCodigoEmpleado(BigDecimal emplDesde, BigDecimal emplHasta) {
        System.out.println("Administrar.AdministrarRegistroEnvios.consultarEnvioCorreos()");
        System.out.println("emplDesde: " + emplDesde);
        System.out.println("emplHasta: " + emplHasta);
        List<Empleados> correoEmpleados;
        try {
            System.out.println("Ingrese al try");
            correoEmpleados = persistenciaEnvioCorreos.CorreoCodEmpleados(em, emplDesde, emplHasta);
        } catch (Exception e) {
            System.out.println("Ingrese al catch");
            System.out.println("Error Administrar.AdministrarRegistroEnvios.consultarEnvioCorreos() " + e);
            correoEmpleados = new ArrayList<>();
        }
        return correoEmpleados;
    }

    @Override
    public boolean comprobarConfigCorreo(BigInteger secuenciaEmpresa) {
        boolean retorno = false;
        try {
            ConfiguracionCorreo cc = persistenciaConfiguracionCorreo.consultarConfiguracionServidorCorreo(em, secuenciaEmpresa);
//            if (cc.getServidorSmtp().length() != 0) {
//                retorno = true;
//            } else {
//                retorno = false;
//            }
            retorno = cc.getServidorSmtp().length() != 0;
        } catch (NullPointerException npe) {
            retorno = false;
        } catch (Exception e) {
            System.out.println("Administrar.AdministrarEnvioCorreos.comprobarConfigCorreo()");
            System.out.println("Error validando configuracion");
            System.out.println("ex: " + e);
        }
        return retorno;

    }

    @Override
    public boolean enviarCorreo(BigInteger secEmpresa, String destinatario, String asunto, String mensaje, String pathAdjunto, String[] paramResultado) {
        System.out.println("Administrar.AdministrarEnvioCorreos.enviarCorreo()");
        ConfiguracionCorreo cc = persistenciaConfiguracionCorreo.consultarConfiguracionServidorCorreo(em, secEmpresa);
        boolean res = EnvioCorreo.enviarCorreo(cc, destinatario, asunto, mensaje, pathAdjunto, paramResultado);
        if (paramResultado != null) {
            System.out.println("resultado envio: " + paramResultado[0]);
        }
        return res;
    }

    @Override
    public BigInteger empresaAsociada() {
        BigInteger secEmpresa = persistenciaParametrosEstructuras.buscarEmpresaParametros(em, persistenciaActualUsuario.actualAliasBD(em));
        return secEmpresa;
    }
}
