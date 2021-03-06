package Administrar;

import ClasesAyuda.EnvioCorreo;
import Entidades.ConfiguracionCorreo;
import Entidades.Empleados;
import Entidades.EnvioCorreos;
import InterfaceAdministrar.AdministrarEnvioCorreosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaConfiguracionCorreoInterface;
import InterfacePersistencia.PersistenciaEnvioCorreosInterface;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'EnvioCorreos'.
 *
 */
@Stateful
public class AdministrarEnvioCorreos implements AdministrarEnvioCorreosInterface {

    private static Logger log = Logger.getLogger(AdministrarEnvioCorreos.class);

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

    private EntityManagerFactory emf;
    private EntityManager em;
    private String idSesionBck;

    private EntityManager getEm() {
        try {
            if (this.emf != null) {
                if (this.em != null) {
                    if (this.em.isOpen()) {
                        this.em.close();
                    }
                }
            } else {
                this.emf = administrarSesiones.obtenerConexionSesionEMF(idSesionBck);
            }
            this.em = emf.createEntityManager();
        } catch (Exception e) {
            log.fatal(this.getClass().getSimpleName() + " getEm() ERROR : " + e);
        }
        return this.em;
    }

    @Override
    public void obtenerConexion(String idSesion) {
        idSesionBck = idSesion;
        try {
            emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
        } catch (Exception e) {
            log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
        }
    }

    @Override
    public List<Empleados> correoCodigoEmpleado(BigDecimal emplDesde, BigDecimal emplHasta) {
        log.warn("AdministrarEnvioCorreos.consultarEnvioCorreos()");
        log.warn("emplDesde: " + emplDesde);
        log.warn("emplHasta: " + emplHasta);
        List<Empleados> correoEmpleados;
        try {
            log.warn("Ingrese al try");
            correoEmpleados = persistenciaEnvioCorreos.CorreoCodEmpleados(getEm(), emplDesde, emplHasta);
        } catch (Exception e) {
            log.warn("Ingrese al catch");
            log.warn("Error Administrar.AdministrarEnvioCorreos.consultarEnvioCorreos() " + e);
            correoEmpleados = new ArrayList<>();
        }
        return correoEmpleados;
    }

    @Override
    public List<String> correos() {
        log.warn("AdministrarEnvioCorreos.correos()");
        List<String> correo;
        try {
            log.warn("Ingrese al try");
            correo = persistenciaEnvioCorreos.Correos(getEm());
        } catch (Exception e) {
            log.warn("Ingrese al catch");
            log.warn("Error Administrar.AdministrarEnvioCorreos.correos() " + e);
            correo = new ArrayList<>();
        }
        return correo;
    }

    @Override
    public boolean comprobarConfigCorreo(BigInteger secuenciaEmpresa) {
        try {
            boolean retorno = false;
            try {
                ConfiguracionCorreo cc = persistenciaConfiguracionCorreo.consultarConfiguracionServidorCorreo(getEm(), secuenciaEmpresa);
                retorno = cc.getServidorSmtp().length() != 0;
            } catch (NullPointerException npe) {
                retorno = false;
            } catch (Exception e) {
                log.warn("AdministrarEnvioCorreos.comprobarConfigCorreo()");
                log.warn("Error validando configuracion");
                log.warn("ex: " + e);
            }
            return retorno;
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".comprobarConfigCorreo() ERROR: " + e);
            return false;
        }
    }

    @Override
    public boolean enviarCorreo(BigInteger secEmpresa, String destinatario, String asunto, String mensaje, String pathAdjunto, String[] paramResultado) {
        try {
            log.warn("AdministrarEnvioCorreos.enviarCorreo()");
            ConfiguracionCorreo cc = persistenciaConfiguracionCorreo.consultarConfiguracionServidorCorreo(getEm(), secEmpresa);
            boolean res = EnvioCorreo.enviarCorreo(cc, destinatario, asunto, mensaje, pathAdjunto, paramResultado);
            if (paramResultado != null) {
                log.warn("resultado envio: " + paramResultado[0]);
            }
            return res;
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".enviarCorreo() ERROR: " + e);
            return false;
        }
    }

//    @Override
//    public boolean enviarCorreo(BigInteger secuenciaEmpresa, String destinatario, String asunto, String mensaje, String pathAdjunto) {
//        ConfiguracionCorreo cc = persistenciaConfiguracionCorreo.consultarConfiguracionServidorCorreo(em, secuenciaEmpresa);
////        EnvioCorreo enviarCorreo = new EnvioCorreo();
////        return enviarCorreo.enviarCorreo(cc, destinatario, asunto, mensaje, pathAdjunto);
//        return EnvioCorreo.enviarCorreo(cc, destinatario, asunto, mensaje, pathAdjunto);
//    }
    @Override
    public BigInteger empresaAsociada() {
        try {
            String aux = persistenciaActualUsuario.actualAliasBD(getEm());
            return persistenciaParametrosEstructuras.buscarEmpresaParametros(getEm(), aux);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".empresaAsociada() ERROR: " + e);
            return null;
        }
    }

    @Override
    public ConfiguracionCorreo consultarRemitente(BigInteger secEmpresa) {
        try {
            return persistenciaEnvioCorreos.consultarRemitente(getEm(), secEmpresa);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".consultarRemitente() ERROR: " + e);
            return null;
        }
    }

    @Override
    public void insertarRegistroEnvios(EnvioCorreos envioCorreo) {
        try {
            persistenciaEnvioCorreos.insertarFalloCorreos(getEm(), envioCorreo);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".insertarRegistroEnvios() ERROR: " + e);
        }
    }
}
