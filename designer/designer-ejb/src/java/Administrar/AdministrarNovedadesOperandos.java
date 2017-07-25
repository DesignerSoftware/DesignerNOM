/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Formulas;
import Entidades.NovedadesOperandos;
import Entidades.Operandos;
import InterfaceAdministrar.AdministrarNovedadesOperandosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaFormulasInterface;
import InterfacePersistencia.PersistenciaNovedadesOperandosInterface;
import InterfacePersistencia.PersistenciaOperandosInterface;
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
public class AdministrarNovedadesOperandos implements AdministrarNovedadesOperandosInterface {

   private static Logger log = Logger.getLogger(AdministrarNovedadesOperandos.class);

    @EJB
    PersistenciaNovedadesOperandosInterface persistenciaNovedadesOperandos;
    @EJB
    PersistenciaOperandosInterface persistenciaOperandos;
    @EJB
    PersistenciaFormulasInterface persistenciaFormulas;
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
    public List<NovedadesOperandos> buscarNovedadesOperandos(BigInteger secuenciaOperando) {
        List<NovedadesOperandos> listaNovedadesOperandos;
        listaNovedadesOperandos = persistenciaNovedadesOperandos.novedadesOperandos(em, secuenciaOperando);
        return listaNovedadesOperandos;
    }

    @Override
    public void borrarNovedadesOperandos(NovedadesOperandos novedadesOperandos) {
        persistenciaNovedadesOperandos.borrar(em, novedadesOperandos);
    }

    @Override
    public void crearNovedadesOperandos(NovedadesOperandos novedadesOperandos) {
        persistenciaNovedadesOperandos.crear(em, novedadesOperandos);
    }

    @Override
    public void modificarNovedadesOperandos(NovedadesOperandos novedadesOperandos) {
        persistenciaNovedadesOperandos.editar(em, novedadesOperandos);

    }

    @Override
    public List<Operandos> buscarOperandos() {
        List<Operandos> listaOperandos;
        listaOperandos = persistenciaOperandos.buscarOperandos(em);
        return listaOperandos;
    }

}
