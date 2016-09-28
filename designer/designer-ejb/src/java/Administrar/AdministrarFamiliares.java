/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Ciudades;
import Entidades.Empleados;
import Entidades.Familiares;
import Entidades.Personas;
import Entidades.TiposDocumentos;
import Entidades.TiposFamiliares;
import InterfaceAdministrar.AdministrarFamiliaresInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaFamiliaresInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import InterfacePersistencia.PersistenciaTiposDocumentosInterface;
import InterfacePersistencia.PersistenciaTiposFamiliaresInterface;
import Persistencia.PersistenciaTiposFamiliares;
import java.math.BigDecimal;
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
public class AdministrarFamiliares implements AdministrarFamiliaresInterface {

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaFamiliaresInterface persistenciaFamiliares;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    @EJB
    PersistenciaPersonasInterface persistenciaPersona;
    @EJB
    PersistenciaTiposDocumentosInterface persistenciaTipoDocumento;
    @EJB
    PersistenciaCiudadesInterface persistenciaCiudades;
    @EJB
    PersistenciaTiposFamiliaresInterface persistenciaTiposFamiliares;
    
    
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void modificarFamiliares(List<Familiares> listaModificar) {
        for (int i = 0; i < listaModificar.size(); i++) {
             if(listaModificar.get(i).getPersona() == null){
             listaModificar.get(i).setPersona(new Personas());
            }
            System.out.println("Administrar Modificando...");
            persistenciaFamiliares.editar(em, listaModificar.get(i));
        }
    }

    @Override
    public void borrarFamiliares(List<Familiares> listaBorrar) {
        for (int i = 0; i < listaBorrar.size(); i++) {
            if(listaBorrar.get(i).getPersona() == null){
             listaBorrar.get(i).setPersona(new Personas());
            }
            System.out.println("Administrar Borrando...");
            persistenciaFamiliares.borrar(em, listaBorrar.get(i));
        }
    }

    @Override
    public void crearFamilares(List<Familiares> listaCrear) {
        for (int i = 0; i < listaCrear.size(); i++) {
            System.out.println("Administrar Creando...");
            if(listaCrear.get(i).getPersona() == null){
             listaCrear.get(i).setPersona(new Personas());
            }
            persistenciaFamiliares.crear(em, listaCrear.get(i));
        }
    }

    @Override
    public List<Familiares> consultarFamiliares(BigInteger secuenciaEmp) {
        List<Familiares> listaFamiliares;
        listaFamiliares = persistenciaFamiliares.familiaresPersona(em, secuenciaEmp);
        return listaFamiliares;
    }

    @Override
    public Empleados empleadoActual(BigInteger secuenciaP) {
        try {
            Empleados retorno = persistenciaEmpleado.buscarEmpleado(em, secuenciaP);
            return retorno;
        } catch (Exception e) {
            System.out.println("Error empleadoActual Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearPersona(Personas persona) {
        persistenciaPersona.crear(em, persona);
    }

    @Override
    public List<TiposFamiliares> consultarTiposFamiliares() {
        List<TiposFamiliares> listTiposFamiliares;
        listTiposFamiliares = persistenciaTiposFamiliares.buscarTiposFamiliares(em);
        return listTiposFamiliares;
    }

    @Override
    public List<TiposDocumentos> consultarTiposDocumentos() {
       List<TiposDocumentos> listTiposDocumentos;
        listTiposDocumentos = persistenciaTipoDocumento.consultarTiposDocumentos(em);
        return listTiposDocumentos;
    }

    @Override
    public List<Ciudades> consultarCiudades() {
        List <Ciudades> listCiudades;
        listCiudades = persistenciaCiudades.consultarCiudades(em);
        return listCiudades;
    }

    @Override
    public List<Personas> consultarPersonas() {
        List<Personas> listPersonas;
        listPersonas = persistenciaPersona.consultarPersonas(em);
        return listPersonas;
    }
    
    
    
}
