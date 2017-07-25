/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.MotivosReemplazos;
import InterfaceAdministrar.AdministrarMotivosReemplazosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMotivosReemplazosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

@Stateful
public class AdministrarMotivosReemplazos implements AdministrarMotivosReemplazosInterface {

   private static Logger log = Logger.getLogger(AdministrarMotivosReemplazos.class);

    @EJB
    PersistenciaMotivosReemplazosInterface persistenciaMotivosReemplazos;

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
    public List<MotivosReemplazos> MotivosReemplazos() {
        List<MotivosReemplazos> listaMotivosReemplazos;
        listaMotivosReemplazos = persistenciaMotivosReemplazos.motivosReemplazos(em);
        return listaMotivosReemplazos;
    }

    @Override
    public List<MotivosReemplazos> lovTiposReemplazos() {
        return persistenciaMotivosReemplazos.motivosReemplazos(em);
    }

    public void modificarMotivosReemplazos(List<MotivosReemplazos> listaMotivosReemplazos) {
        for (int i = 0; i < listaMotivosReemplazos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaMotivosReemplazos.editar(em, listaMotivosReemplazos.get(i));
        }
    }

    public void borrarMotivosReemplazos(List<MotivosReemplazos> listaMotivosReemplazos) {
        for (int i = 0; i < listaMotivosReemplazos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaMotivosReemplazos.borrar(em, listaMotivosReemplazos.get(i));
        }
    }

    public void crearMotivosReemplazos(List<MotivosReemplazos> listaMotivosReemplazos) {
        for (int i = 0; i < listaMotivosReemplazos.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaMotivosReemplazos.crear(em, listaMotivosReemplazos.get(i));
        }
    }

    public BigInteger contarEncargaturasMotivoReemplazo(BigInteger secMotivoReemplazo) {
        BigInteger encargaturasMotivoReemplazo = persistenciaMotivosReemplazos.contarEncargaturasMotivoReemplazo(em, secMotivoReemplazo);
        return encargaturasMotivoReemplazo;
    }

}
