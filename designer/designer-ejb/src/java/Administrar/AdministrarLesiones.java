/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarLesionesInterface;
import Entidades.Lesiones;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaLesionesInterface;
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
public class AdministrarLesiones implements AdministrarLesionesInterface {

    @EJB
    PersistenciaLesionesInterface persistenciaLesiones;
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
    public void modificarLesiones(List<Lesiones> listaLesiones) {
        for (int i = 0; i < listaLesiones.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaLesiones.editar(em, listaLesiones.get(i));
        }
    }

    @Override
    public void borrarLesiones(List<Lesiones> listaLesiones) {
        for (int i = 0; i < listaLesiones.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaLesiones.borrar(em, listaLesiones.get(i));
        }
    }

    @Override
    public void crearLesiones(List<Lesiones> listaLesiones) {
        for (int i = 0; i < listaLesiones.size(); i++) {
            System.out.println("Administrar Creando...");
            persistenciaLesiones.crear(em, listaLesiones.get(i));
        }
    }

    @Override
    public List<Lesiones> consultarLesiones() {
        List<Lesiones> listLesiones;
        listLesiones = persistenciaLesiones.buscarLesiones(em);
        return listLesiones;
    }

    @Override
    public Lesiones consultarLesion(BigInteger secLesion) {
        Lesiones lesiones;
        lesiones = persistenciaLesiones.buscarLesion(em, secLesion);
        return lesiones;
    }

    @Override
    public BigInteger contarDetallesLicensiasLesion(BigInteger secuenciaLesiones) {
        try {
            BigInteger verificarBorradoDetallesLicensias;
            return verificarBorradoDetallesLicensias = persistenciaLesiones.contadorDetallesLicensias(em, secuenciaLesiones);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarLesiones verificarBorradoDetallesLicensias ERROR :" + e);
            return null;
        }
    }

    @Override
    public BigInteger contarSoAccidentesDomesticosLesion(BigInteger secuenciaVigenciasExamenesMedicos) {
        try {
            BigInteger verificadorVigenciasExamenesMedicos;
            return verificadorVigenciasExamenesMedicos = persistenciaLesiones.contadorSoAccidentesDomesticos(em, secuenciaVigenciasExamenesMedicos);
        } catch (Exception e) {
            System.err.println("ERROR AdministrarLesiones verificarBorradoSoAccidentesDomesticos ERROR :" + e);
            return null;
        }
    }
}
