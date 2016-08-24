/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.GruposConceptos;
import Entidades.Operandos;
import Entidades.OperandosGruposConceptos;
import Entidades.Procesos;
import InterfaceAdministrar.AdministrarOperandosGruposConceptosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaGruposConceptosInterface;
import InterfacePersistencia.PersistenciaOperandosGruposConceptosInterface;
import InterfacePersistencia.PersistenciaOperandosInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author Victor Algarin
 */
@Stateful
public class AdministrarOperandosGruposConceptos implements AdministrarOperandosGruposConceptosInterface {

    @EJB
    PersistenciaOperandosGruposConceptosInterface persistenciaOperandosGruposConceptos;
    @EJB
    PersistenciaGruposConceptosInterface persistenciaGruposConceptos;
    @EJB
    PersistenciaOperandosInterface persistenciaOperandos;
    @EJB
    PersistenciaProcesosInterface persistenciaProcesos;
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
    
//Procesos
    @Override
    public void borrarProcesos(Procesos procesos) {
        persistenciaProcesos.borrar(em, procesos);
    }

    @Override
    public void crearProcesos(Procesos procesos) {
        persistenciaProcesos.crear(em, procesos);
    }

    @Override
    public void modificarProcesos(List<Procesos> listaVigenciasRetencionesModificar) {
        for (int i = 0; i < listaVigenciasRetencionesModificar.size(); i++) {

            persistenciaProcesos.editar(em, listaVigenciasRetencionesModificar.get(i));
        }
    }

    public List<Procesos> consultarProcesos() {
        try {
            List<Procesos> actual = persistenciaProcesos.buscarProcesos(em);
            return actual;
        } catch (Exception e) {
            System.out.println("Error consultarVigenciasRetenciones: " + e.toString());
            return null;
        }
    }
    
    public List<Operandos> consultarOperandos(){
        try {
            List<Operandos> actual = persistenciaOperandos.buscarOperandos(em);
            return actual;
        } catch (Exception e) {
            System.out.println("Error consultarVigenciasRetenciones: " + e.toString());
            return null;
        }
    }
    
    public List<GruposConceptos> consultarGrupos(){
        try {
            List<GruposConceptos> actual = persistenciaGruposConceptos.buscarGruposConceptos(em);
            return actual;
        } catch (Exception e) {
            System.out.println("Error consultarVigenciasRetenciones: " + e.toString());
            return null;
        }
    }

//OperandosGruposConceptos
    
    @Override
    public void borrarOperandosGrupos(OperandosGruposConceptos operandos) {
        persistenciaOperandosGruposConceptos.borrar(em, operandos);
    }

    @Override
    public void crearOperandosGrupos(OperandosGruposConceptos operandos) {
        persistenciaOperandosGruposConceptos.crear(em, operandos);
    }

    @Override
    public void modificarOperandosGrupos(List<OperandosGruposConceptos> listaOperandosGruposConceptosModificar) {
        for (int i = 0; i < listaOperandosGruposConceptosModificar.size(); i++) {
            System.out.println("Modificando...");

            persistenciaOperandosGruposConceptos.editar(em, listaOperandosGruposConceptosModificar.get(i));
        }
    }

    @Override
    public List<OperandosGruposConceptos> consultarOperandosGrupos(BigInteger secProceso) {
        try {
            List<OperandosGruposConceptos> actual = persistenciaOperandosGruposConceptos.buscarOperandosGruposConceptosPorProcesoSecuencia(em, secProceso);
            return actual;
        } catch (Exception e) {
            System.out.println("Error conceptoActual Admi : " + e.toString());
            return null;
        }
    }
    
    

}
