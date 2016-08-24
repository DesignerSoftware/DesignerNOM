/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Cargos;
import Entidades.Empresas;
import Entidades.FirmasReportes;
import Entidades.Personas;
import InterfaceAdministrar.AdministrarFirmasReportesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCargosInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaFirmasReportesInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
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
public class AdministrarFirmasReportes implements AdministrarFirmasReportesInterface {

    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaFirmasReportes'.
     */
    @EJB
    PersistenciaFirmasReportesInterface persistenciaFirmasReportes;
    @EJB
    PersistenciaCargosInterface persistenciaCargos;
    @EJB
    PersistenciaPersonasInterface persistenciaPersonas;
    @EJB
    PersistenciaEmpresasInterface persistenciaEmpresas;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    //--------------------------------------------------------------------------
    //MÉTODOS
    //--------------------------------------------------------------------------

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    public void modificarFirmasReportes(List<FirmasReportes> listaFirmasReportes) {
        for (int i = 0; i < listaFirmasReportes.size(); i++) {
            System.out.println("Administrar Modificando...");
            persistenciaFirmasReportes.editar(em,listaFirmasReportes.get(i));
        }
    }

    public void borrarFirmasReportes(List<FirmasReportes> listaFirmasReportes) {
        for (int i = 0; i < listaFirmasReportes.size(); i++) {
            System.out.println("Administrar Borrando...");
            persistenciaFirmasReportes.borrar(em,listaFirmasReportes.get(i));
        }
    }

    public void crearFirmasReportes(List<FirmasReportes> listaFirmasReportes) {
        for (int i = 0; i < listaFirmasReportes.size(); i++) {
            System.out.println("Administrar Creando...");
            System.out.println("--------------DUPLICAR------------------------");
            System.out.println("CODIGO : " + listaFirmasReportes.get(i).getCodigo());
            System.out.println("NOMBRE: " + listaFirmasReportes.get(i).getDescripcion());
            System.out.println("EMPRESA: " + listaFirmasReportes.get(i).getEmpresa().getNombre());
            System.out.println("SUBTITULO : " + listaFirmasReportes.get(i).getSubtitulofirma());
            System.out.println("PERSONA : " + listaFirmasReportes.get(i).getPersonaFirma().getNombre());
            System.out.println("CARGO : " + listaFirmasReportes.get(i).getCargo().getNombre());
            System.out.println("--------------DUPLICAR------------------------");
            persistenciaFirmasReportes.crear(em,listaFirmasReportes.get(i));
        }
    }

    @Override
    public List<FirmasReportes> consultarFirmasReportes() {
        List<FirmasReportes> listaFirmasReportes;
        listaFirmasReportes = persistenciaFirmasReportes.consultarFirmasReportes(em);
        return listaFirmasReportes;
    }

    public FirmasReportes consultarTipoIndicador(BigInteger secMotivoDemanda) {
        FirmasReportes tiposIndicadores;
        tiposIndicadores = persistenciaFirmasReportes.consultarFirmaReporte(em,secMotivoDemanda);
        return tiposIndicadores;
    }

    public List<Cargos> consultarLOVCargos() {
        List<Cargos> listLOVCargos;
        listLOVCargos = persistenciaCargos.consultarCargos(em);
        return listLOVCargos;
    }

    public List<Personas> consultarLOVPersonas() {
        List<Personas> listLOVPersonas;
        listLOVPersonas = persistenciaPersonas.consultarPersonas(em);
        return listLOVPersonas;
    }

    public List<Empresas> consultarLOVEmpresas() {
        List<Empresas> listLOVEmpresas;
        listLOVEmpresas = persistenciaEmpresas.consultarEmpresas(em);
        return listLOVEmpresas;
    }

}
