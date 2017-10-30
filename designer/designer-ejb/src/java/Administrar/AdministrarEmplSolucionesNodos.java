/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.CentrosCostos;
import Entidades.Conceptos;
import Entidades.Cuentas;
import Entidades.Empleados;
import Entidades.Formulas;
import Entidades.Periodicidades;
import Entidades.Procesos;
import Entidades.SolucionesNodos;
import Entidades.Terceros;
import InterfaceAdministrar.AdministrarEmplSolucionesNodosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCentrosCostosInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaCuentasInterface;
import InterfacePersistencia.PersistenciaEmplSolucionesNodosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaFormulasInterface;
import InterfacePersistencia.PersistenciaPeriodicidadesInterface;
import InterfacePersistencia.PersistenciaProcesosInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarEmplSolucionesNodos implements AdministrarEmplSolucionesNodosInterface {

    private static Logger log = Logger.getLogger(AdministrarEmplSolucionesNodos.class);

    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleados;
    @EJB
    PersistenciaEmplSolucionesNodosInterface persistenciaEmplSolucionesNodos;
    @EJB
    PersistenciaConceptosInterface persistenciaConceptos;
    @EJB
    PersistenciaTercerosInterface persistenciaTerceros;
    @EJB
    PersistenciaFormulasInterface persistenciaFormulas;
    @EJB
    PersistenciaCuentasInterface persistenciaCuentas;
    @EJB
    PersistenciaCentrosCostosInterface persistenciaCentroCosto;
    @EJB
    PersistenciaProcesosInterface persistenciaProcesos;
    @EJB
    PersistenciaPeriodicidadesInterface persistenciaPeriodicidades;
    @EJB
    AdministrarSesionesInterface administrarSesiones;

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
    public String crear(SolucionesNodos sn) {
        try {
            return persistenciaEmplSolucionesNodos.crear(getEm(), sn);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".crear() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String editar(SolucionesNodos sn) {
        try {
            return persistenciaEmplSolucionesNodos.editar(getEm(), sn);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".editar() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public String borrar(SolucionesNodos sn) {
        try {
            return persistenciaEmplSolucionesNodos.borrar(getEm(), sn);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".borrar() ERROR: " + e);
            return e.getMessage();
        }
    }

    @Override
    public List<SolucionesNodos> solucionesNodosEmpl(BigInteger secEmpleado) {
        try {
            return persistenciaEmplSolucionesNodos.solucionesNodosXEmpleado(getEm(), secEmpleado);
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".solucionesNodosEmpl() ERROR: " + e);
            return null;
        }
    }

    @Override
    public List<Empleados> emplSolucionesNodos() {
        try {
            return persistenciaEmpleados.empleadosNovedad(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".emplSolucionesNodos() ERROR: " + e);
            return null;
        }
    }

    @Override
    public List<Conceptos> buscarConceptos() {
        try {
            return persistenciaConceptos.buscarConceptos(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".buscarConceptos() ERROR: " + e);
            return null;
        }
    }

    @Override
    public List<Terceros> buscarTerceros() {
        try {
            return persistenciaTerceros.buscarTerceros(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".buscarTerceros() ERROR: " + e);
            return null;
        }
    }

    @Override
    public List<Formulas> buscarFormulas() {
        try {
            return persistenciaFormulas.buscarFormulas(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".buscarFormulas() ERROR: " + e);
            return null;
        }
    }

    @Override
    public List<Cuentas> buscarCuentas() {
        try {
            return persistenciaCuentas.buscarCuentas(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".buscarCuentas() ERROR: " + e);
            return null;
        }
    }

    @Override
    public List<CentrosCostos> buscarCentrosCostos() {
        try {
            return persistenciaCentroCosto.buscarCentrosCostos(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".buscarCentrosCostos() ERROR: " + e);
            return null;
        }
    }

    @Override
    public List<Procesos> buscarProcesos() {
        try {
            return persistenciaProcesos.buscarProcesos(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".buscarProcesos() ERROR: " + e);
            return null;
        }
    }

    @Override
    public List<Periodicidades> buscarPeriodicidades() {
        try {
            return persistenciaPeriodicidades.consultarPeriodicidades(getEm());
        } catch (Exception e) {
            log.error(this.getClass().getSimpleName() + ".buscarPeriodicidades() ERROR: " + e);
            return null;
        }
    }

    @Override
    public BigDecimal buscarTipoTrabjadorXEmpl(BigInteger secEmpleado) {
      try{
         return persistenciaEmplSolucionesNodos.tipottXEmpleado(getEm(), secEmpleado);
      }catch(Exception e){
            log.error(this.getClass().getSimpleName() + ".buscarTipoTrabjadorXEmpl() ERROR: " + e);
          return null;
      }
    }
}
