/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import InterfaceAdministrar.AdministrarEmpleadosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import java.math.BigDecimal;
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
public class AdministrarEmpleados implements AdministrarEmpleadosInterface {

    private static Logger log = Logger.getLogger(AdministrarEmpleados.class);

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaEmpleadoInterface persitenciaEmpleados;

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
    public List<Empleados> listaEmpleados() {
        try {
            return persitenciaEmpleados.todosEmpleados(getEm());
        } catch (Exception e) {
            log.warn("error en AdministrarEmpleados.listaEmpleados" + e);
            return null;
        }
    }

    @Override
    public String editarEmpleado(Empleados empleado) {
        try {
            return persitenciaEmpleados.editar(getEm(), empleado);
        } catch (Exception e) {
            log.warn("erroe en AdministrarEmpleados.editarEmpleado : " + e);
            return e.getMessage();
        }
    }

    @Override
    public void cambiarCodEmpl(BigDecimal codactual, BigDecimal codnuevo) {
        try {
            persitenciaEmpleados.cambiarCodEmpleado(getEm(), codactual, codnuevo);
        } catch (Exception e) {
            log.warn("erroe en AdministrarEmpleados.cambiarCodEmpl : " + e);
        }
    }

    @Override
    public List<Empleados> listaEmpleadosEmpresa() {
        try {
            return persitenciaEmpleados.empleadosEmpresa(getEm());
        } catch (Exception e) {
            log.warn("erroe en AdministrarEmpleados.listaEmpleadosEmpresa : " + e);
            return null;
        }
    }

}
