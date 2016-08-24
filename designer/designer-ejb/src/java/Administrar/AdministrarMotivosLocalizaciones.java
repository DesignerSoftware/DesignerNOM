/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.MotivosLocalizaciones;
import InterfaceAdministrar.AdministrarMotivosLocalizacionesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaMotivosLocalizacionesInterface;
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
public class AdministrarMotivosLocalizaciones implements AdministrarMotivosLocalizacionesInterface {

    @EJB
    PersistenciaMotivosLocalizacionesInterface PersistenciaMotivosLocalizaciones;
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
    public void modificarMotivosLocalizaciones(List<MotivosLocalizaciones> listaMotivosLocalizaciones) {
        for (int i = 0; i < listaMotivosLocalizaciones.size(); i++) {
            System.out.println("Administrar Modificando...");
            PersistenciaMotivosLocalizaciones.editar(em, listaMotivosLocalizaciones.get(i));
        }
    }

    @Override
    public void borrarMotivosLocalizaciones(List<MotivosLocalizaciones> listaMotivosLocalizaciones) {
        for (int i = 0; i < listaMotivosLocalizaciones.size(); i++) {
            System.out.println("Administrar Borrando...");
            PersistenciaMotivosLocalizaciones.borrar(em, listaMotivosLocalizaciones.get(i));
        }
    }

    @Override
    public void crearMotivosLocalizaciones(List<MotivosLocalizaciones> listaMotivosLocalizaciones) {
        for (int i = 0; i < listaMotivosLocalizaciones.size(); i++) {
            System.out.println("Administrar Creando...");
            PersistenciaMotivosLocalizaciones.crear(em, listaMotivosLocalizaciones.get(i));
        }
    }

    @Override
    public List<MotivosLocalizaciones> mostrarMotivosCambiosCargos() {
        List<MotivosLocalizaciones> listMotivosLocalizaciones;
        listMotivosLocalizaciones = PersistenciaMotivosLocalizaciones.buscarMotivosLocalizaciones(em);
        return listMotivosLocalizaciones;
    }

    @Override
    public MotivosLocalizaciones mostrarMotivoCambioCargo(BigInteger secMotivosCambiosCargos) {
        MotivosLocalizaciones moticoLocalizacion;
        moticoLocalizacion = PersistenciaMotivosLocalizaciones.buscarMotivoLocalizacionSecuencia(em, secMotivosCambiosCargos);
        return moticoLocalizacion;
    }
    
    public BigInteger contarVigenciasLocalizacionesMotivoLocalizacion (BigInteger secMotivoLocalizacion)
    { BigInteger contarVigencias;
    contarVigencias = PersistenciaMotivosLocalizaciones.contarVigenciasLocalizacionesMotivoLocalizacion(em, secMotivoLocalizacion);
    return contarVigencias;
    }
}
