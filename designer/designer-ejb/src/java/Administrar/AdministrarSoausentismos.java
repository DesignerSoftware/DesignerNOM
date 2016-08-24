/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Causasausentismos;
import Entidades.Clasesausentismos;
import Entidades.Diagnosticoscategorias;
import Entidades.Empleados;
import Entidades.EnfermeadadesProfesionales;
import Entidades.Enfermedades;
import Entidades.Ibcs;
import Entidades.Soaccidentes;
import Entidades.Soausentismos;
import Entidades.SoausentismosProrrogas;
import Entidades.Terceros;
import Entidades.Tiposausentismos;
import InterfaceAdministrar.AdministrarSoausentismosInterface;
import InterfacePersistencia.PersistenciaCausasAusentismosInterface;
import InterfacePersistencia.PersistenciaClasesAusentismosInterface;
import InterfacePersistencia.PersistenciaDiagnosticosCategoriasInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEnfermedadesInterface;
import InterfacePersistencia.PersistenciaEnfermedadesProfesionalesInterface;
import InterfacePersistencia.PersistenciaIBCSInterface;
import InterfacePersistencia.PersistenciaRelacionesIncapacidadesInterface;
import InterfacePersistencia.PersistenciaSoaccidentesInterface;
import InterfacePersistencia.PersistenciaSoausentismosInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaTiposAusentismosInterface;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;

@Stateful
public class AdministrarSoausentismos implements AdministrarSoausentismosInterface {

    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleados;
    @EJB
    PersistenciaTercerosInterface persistenciaTerceros;
    @EJB
    PersistenciaSoausentismosInterface persistenciaSoausentismos;
    @EJB
    PersistenciaSoaccidentesInterface persistenciaSoaccdicentes;
    @EJB
    PersistenciaTiposAusentismosInterface persistenciaTiposAusentismos;
    @EJB
    PersistenciaClasesAusentismosInterface persistenciaClasesAusentismos;
    @EJB
    PersistenciaCausasAusentismosInterface persistenciaCausasAusentismos;
    @EJB
    PersistenciaIBCSInterface persistenciaIBCS;
    @EJB
    PersistenciaEnfermedadesProfesionalesInterface persistenciaEP;
    @EJB
    PersistenciaDiagnosticosCategoriasInterface persistenciaDiagnosticos;
    @EJB
    PersistenciaEnfermedadesInterface persistenciaEnfermedades;
    @EJB
    PersistenciaRelacionesIncapacidadesInterface persistenciaRelacionesIncapacidades;
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
    public void borrarAusentismos(Soausentismos ausentismos) {
        persistenciaSoausentismos.borrar(em, ausentismos);
    }

    @Override
    public void crearAusentismos(Soausentismos ausentismos) {
        persistenciaSoausentismos.crear(em, ausentismos);
    }

    @Override
    public void modificarAusentismos(List<Soausentismos> listaAusentismosModificar) {
        for (int i = 0; i < listaAusentismosModificar.size(); i++) {
            System.out.println("Modificando...");

                    if (listaAusentismosModificar.get(i).getDias() == null) {
                        listaAusentismosModificar.get(i).setDias(null);
                    }
                    if (listaAusentismosModificar.get(i).getHoras() == null) {
                        listaAusentismosModificar.get(i).setHoras(null);
                    }
                    if (listaAusentismosModificar.get(i).getFechafinaus()== null) {
                        listaAusentismosModificar.get(i).setFechafinaus(null);
                    }
                    if (listaAusentismosModificar.get(i).getFechaexpedicion()== null) {
                        listaAusentismosModificar.get(i).setFechaexpedicion(null);
                    }
                    if (listaAusentismosModificar.get(i).getFechainipago()== null) {
                        listaAusentismosModificar.get(i).setFechainipago(null);
                    }
                    if (listaAusentismosModificar.get(i).getFechafinpago()== null) {
                        listaAusentismosModificar.get(i).setFechafinpago(null);
                    }
                    if (listaAusentismosModificar.get(i).getPorcentajeindividual()== null) {
                        listaAusentismosModificar.get(i).setPorcentajeindividual(null);
                    }
                    if (listaAusentismosModificar.get(i).getBaseliquidacion()== null) {
                        listaAusentismosModificar.get(i).setBaseliquidacion(null);
                    }
                    if (listaAusentismosModificar.get(i).getFormaliquidacion()== null) {
                        listaAusentismosModificar.get(i).setFormaliquidacion(null);
                    }
                    if (listaAusentismosModificar.get(i).getAccidente().getSecuencia()== null) {
                        listaAusentismosModificar.get(i).setAccidente(null);
                    }
                    if (listaAusentismosModificar.get(i).getEnfermedad().getSecuencia()== null) {
                        listaAusentismosModificar.get(i).setEnfermedad(null);
                    }
                    if (listaAusentismosModificar.get(i).getNumerocertificado() == null) {
                        listaAusentismosModificar.get(i).setNumerocertificado(null);
                    }
                    if (listaAusentismosModificar.get(i).getDiagnosticocategoria().getSecuencia()== null) {
                        listaAusentismosModificar.get(i).setDiagnosticocategoria(null);
                    }
                    if (listaAusentismosModificar.get(i).getProrroga().getSecuencia()== null) {
                        listaAusentismosModificar.get(i).setProrroga(null);
                    }
                    if (listaAusentismosModificar.get(i).getRelacion()== null) {
                        listaAusentismosModificar.get(i).setRelacion(null);
                    }
                    if (listaAusentismosModificar.get(i).getRelacionadaBool()== false) {
                        listaAusentismosModificar.get(i).setRelacionada("N");
                    }
                    if (listaAusentismosModificar.get(i).getTercero().getSecuencia()== null) {
                        listaAusentismosModificar.get(i).setTercero(null);
                    }
                    if (listaAusentismosModificar.get(i).getObservaciones()== null) {
                        listaAusentismosModificar.get(i).setObservaciones(null);
                    }
            persistenciaSoausentismos.editar(em, listaAusentismosModificar.get(i));
        }
    }
    
    //Trae los ausentismos del empleado cuya secuencia se envía como parametro//
    @Override
    public List<Soausentismos> ausentismosEmpleado(BigInteger secuenciaEmpleado) {
        try {
            return persistenciaSoausentismos.ausentismosEmpleado(em, secuenciaEmpleado);
        } catch (Exception e) {
            System.err.println("Error AdministrarNovedadesEmpleados.novedadesEmpleado" + e);
            return null;
        }
    }

    //Lista de Valores Empleados
    public List<Empleados> lovEmpleados() {
        return persistenciaEmpleados.empleadosNovedad(em);
    }

    public List<Tiposausentismos> lovTiposAusentismos() {
        return persistenciaTiposAusentismos.consultarTiposAusentismos(em);
    }

    public List<Clasesausentismos> lovClasesAusentismos() {
        return persistenciaClasesAusentismos.buscarClasesAusentismos(em);
    }
    
    public List<Causasausentismos> lovCausasAusentismos() {
        return persistenciaCausasAusentismos.buscarCausasAusentismos(em);
    }
    
    public List<Soaccidentes> lovAccidentes(BigInteger secuenciaEmpleado){
        return persistenciaSoaccdicentes.accidentesEmpleado(em, secuenciaEmpleado);
    }
    
    public List<Terceros> lovTerceros(){
        return persistenciaTerceros.buscarTerceros(em);
    }
    
    public List<Diagnosticoscategorias> lovDiagnosticos(){
        return persistenciaDiagnosticos.buscarDiagnosticos(em);
    }
    
    public List<Ibcs> empleadosIBCS(BigInteger secuenciaEmpleado){
        return persistenciaIBCS.buscarIbcsPorEmpleado(em, secuenciaEmpleado);
    }
    
    public List<Enfermedades> enfermedades(){
        return persistenciaEnfermedades.buscarEnfermedades(em );
    }
    
    public List<EnfermeadadesProfesionales> empleadosEP(BigInteger secuenciaEmpleado){
        return persistenciaEP.buscarEPPorEmpleado(em, secuenciaEmpleado);
    }
    
    //MostrarProrroga
    public String mostrarProrroga(BigInteger secuenciaProrroga){
        return persistenciaSoausentismos.prorrogaMostrar(em, secuenciaProrroga);
    }
    
    //MostrarRelacion
    public String mostrarRelacion(BigInteger secuenciaAusentismo){
        return persistenciaRelacionesIncapacidades.relaciones(em, secuenciaAusentismo);
    }
    
    //LOV Prorrogas
    public List<Soausentismos> lovProrrogas(BigInteger secEmpleado, BigInteger secuenciaCausa, BigInteger secuenciaAusentismo){
        /*if(secuenciaAusentismo == null){
           secuenciaAusentismo = BigInteger.valueOf(0);
        }*/
        return persistenciaSoausentismos.prorrogas(em, secEmpleado, secuenciaCausa, secuenciaAusentismo);
    }
}
