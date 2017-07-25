/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.ClasesPensiones;
import InterfaceAdministrar.AdministrarClasesPensionesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaClasesPensionesInterface;
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
public class AdministrarClasesPensiones implements AdministrarClasesPensionesInterface {

   private static Logger log = Logger.getLogger(AdministrarClasesPensiones.class);

    @EJB
    PersistenciaClasesPensionesInterface persistenciaClasesPensiones;
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
    public void modificarClasesPensiones(List<ClasesPensiones> listaClasesPensiones) {
        for (int i = 0; i < listaClasesPensiones.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaClasesPensiones.editar(em,listaClasesPensiones.get(i));
        }
    }

    @Override
    public void borrarClasesPensiones(List<ClasesPensiones> listaClasesPensiones) {
        for (int i = 0; i < listaClasesPensiones.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaClasesPensiones.borrar(em,listaClasesPensiones.get(i));
        }
    }

    @Override
    public void crearClasesPensiones(List<ClasesPensiones> listaClasesPensiones) {
        for (int i = 0; i < listaClasesPensiones.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaClasesPensiones.crear(em,listaClasesPensiones.get(i));
        }
    }

    public List<ClasesPensiones> consultarClasesPensiones() {
        List<ClasesPensiones> listMotivosCambiosCargos;
        listMotivosCambiosCargos = persistenciaClasesPensiones.consultarClasesPensiones(em);
        return listMotivosCambiosCargos;
    }

    @Override
    public ClasesPensiones consultarClasePension(BigInteger secClasesPensiones) {
        ClasesPensiones subCategoria;
        subCategoria = persistenciaClasesPensiones.consultarClasePension(em,secClasesPensiones);
        return subCategoria;
    }

    @Override
    public BigInteger contarRetiradosClasePension(BigInteger secClasesPensiones) {
        BigInteger contarRetiradosClasePension = null;

        try {
            return contarRetiradosClasePension = persistenciaClasesPensiones.contarRetiradosClasePension(em,secClasesPensiones);
        } catch (Exception e) {
            log.error("ERROR AdministrarClasesPensiones contarEscalafones ERROR : " + e);
            return null;
        }
    }
}
