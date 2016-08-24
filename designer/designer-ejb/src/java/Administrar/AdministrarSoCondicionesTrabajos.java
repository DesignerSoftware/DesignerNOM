/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarSoCondicionesTrabajosInterface;
import Entidades.SoCondicionesTrabajos;
import InterfacePersistencia.PersistenciaSoCondicionesTrabajosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateless
public class AdministrarSoCondicionesTrabajos implements AdministrarSoCondicionesTrabajosInterface {

    @EJB
    PersistenciaSoCondicionesTrabajosInterface persistenciaSoCondicionesTrabajos;
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
    public void modificarSoCondicionesTrabajos(List<SoCondicionesTrabajos> listSoCondicionesTrabajos) {
        for (int i = 0; i < listSoCondicionesTrabajos.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaSoCondicionesTrabajos.editar(em, listSoCondicionesTrabajos.get(i));
        }
    }
    @Override
    public void borrarSoCondicionesTrabajos(List<SoCondicionesTrabajos> listSoCondicionesTrabajos) {
        for (int i = 0; i < listSoCondicionesTrabajos.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaSoCondicionesTrabajos.borrar(em, listSoCondicionesTrabajos.get(i));
        }
    }
    @Override
    public void crearSoCondicionesTrabajos(List<SoCondicionesTrabajos> listSoCondicionesTrabajos) {
        for (int i = 0; i < listSoCondicionesTrabajos.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaSoCondicionesTrabajos.crear(em, listSoCondicionesTrabajos.get(i));
        }
    }
    @Override
    public List<SoCondicionesTrabajos> consultarSoCondicionesTrabajos() {
        List<SoCondicionesTrabajos> listSoCondicionesTrabajos;
        listSoCondicionesTrabajos = persistenciaSoCondicionesTrabajos.buscarSoCondicionesTrabajos(em);
        return listSoCondicionesTrabajos;
    }
    @Override
    public SoCondicionesTrabajos consultarSoCondicionTrabajo(BigInteger secSoCondicionesTrabajos) {
        SoCondicionesTrabajos soCondicionesTrabajos;
        soCondicionesTrabajos = persistenciaSoCondicionesTrabajos.buscarSoCondicionTrabajo(em, secSoCondicionesTrabajos);
        return soCondicionesTrabajos;
    }
    @Override
    public BigInteger contarInspeccionesSoCondicionTrabajo(BigInteger secuenciaElementos) {
        try {
            System.err.println("Secuencia Borrado Elementos" + secuenciaElementos);
            BigInteger verificarInspecciones;
            return verificarInspecciones = persistenciaSoCondicionesTrabajos.contadorInspecciones(em, secuenciaElementos);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarSoCondicionesTrabajos verificarInspecciones ERROR :" + e);
            return null;
        }
    }
    @Override
    public BigInteger contarSoAccidentesMedicosSoCondicionTrabajo(BigInteger secuenciaElementos) {
        try {
            BigInteger verificarSoAccidtenesMedicos;
            System.err.println("Secuencia Borrado Elementos" + secuenciaElementos);
            return verificarSoAccidtenesMedicos = persistenciaSoCondicionesTrabajos.contadorSoAccidentesMedicos(em, secuenciaElementos);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarSoCondicionesTrabajos verificarSoAccidtenesMedicos ERROR :" + e);
            return null;
        }
    }
    @Override
    public BigInteger contarSoDetallesPanoramasSoCondicionTrabajo(BigInteger secuenciaElementos) {
        try {
            System.err.println("Secuencia Borrado Elementos" + secuenciaElementos);
            BigInteger verificarSoDetallesPanoramas;
            return verificarSoDetallesPanoramas = persistenciaSoCondicionesTrabajos.contadorSoDetallesPanoramas(em, secuenciaElementos);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarSoCondicionesTrabajos verificarSoDetallesPanoramas ERROR :" + e);
            return null;
        }
    }
    @Override
    public BigInteger contarSoExposicionesFrSoCondicionTrabajo(BigInteger secuenciaElementos) {
        try {
            System.err.println("Secuencia Borrado Elementos" + secuenciaElementos);
            BigInteger verificarSoExposicionesFr;
            return verificarSoExposicionesFr = persistenciaSoCondicionesTrabajos.contadorSoExposicionesFr(em, secuenciaElementos);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarClasesAccidentes verificarSoExposicionesFr ERROR :" + e);
            return null;
        }
    }

}
