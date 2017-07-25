/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.AdiestramientosF;
import Entidades.Cargos;
import Entidades.Ciudades;
import Entidades.Direcciones;
import Entidades.Empleados;
import Entidades.EstadosCiviles;
import Entidades.Familiares;
import Entidades.HVHojasDeVida;
import Entidades.HvExperienciasLaborales;
import Entidades.Instituciones;
import Entidades.MotivosRetiros;
import Entidades.Personas;
import Entidades.Profesiones;
import Entidades.SectoresEconomicos;
import Entidades.SoAntecedentes;
import Entidades.SoAntecedentesMedicos;
import Entidades.SoTiposAntecedentes;
import Entidades.Telefonos;
import Entidades.TiposDocumentos;
import Entidades.TiposEducaciones;
import Entidades.TiposFamiliares;
import Entidades.TiposTelefonos;
import Entidades.VigenciasDomiciliarias;
import Entidades.VigenciasEstadosCiviles;
import Entidades.VigenciasFormales;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarVigenciasDomiciliariasInterface;
import InterfacePersistencia.PersistenciaAdiestramientosFInterface;
import InterfacePersistencia.PersistenciaCargosInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaDireccionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEstadosCivilesInterface;
import InterfacePersistencia.PersistenciaFamiliaresInterface;
import InterfacePersistencia.PersistenciaHVHojasDeVidaInterface;
import InterfacePersistencia.PersistenciaHvExperienciasLaboralesInterface;
import InterfacePersistencia.PersistenciaInstitucionesInterface;
import InterfacePersistencia.PersistenciaMotivosRetirosInterface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import InterfacePersistencia.PersistenciaProfesionesInterface;
import InterfacePersistencia.PersistenciaSectoresEconomicosInterface;
import InterfacePersistencia.PersistenciaSoAntecedentesInterface;
import InterfacePersistencia.PersistenciaSoAntecedentesMedicosInterface;
import InterfacePersistencia.PersistenciaSoTiposAntecedentesInterface;
import InterfacePersistencia.PersistenciaTelefonosInterface;
import InterfacePersistencia.PersistenciaTiposDocumentosInterface;
import InterfacePersistencia.PersistenciaTiposEducacionesInterface;
import InterfacePersistencia.PersistenciaTiposFamiliaresInterface;
import InterfacePersistencia.PersistenciaTiposTelefonosInterface;
import InterfacePersistencia.PersistenciaVigenciasDomiciliariasInterface;
import InterfacePersistencia.PersistenciaVigenciasEstadosCivilesInterface;
import InterfacePersistencia.PersistenciaVigenciasFormalesInterface;
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
public class AdministrarVigenciasDomiciliarias implements AdministrarVigenciasDomiciliariasInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciasDomiciliarias.class);

    @EJB
    AdministrarSesionesInterface administrarSesiones;
    @EJB
    PersistenciaVigenciasDomiciliariasInterface persistenciaVigenciasDomiciliarias;
    @EJB
    PersistenciaCargosInterface persistenciaCargos;
    @EJB
    PersistenciaTiposEducacionesInterface persistenciaTiposEducaciones;
    @EJB
    PersistenciaProfesionesInterface persistenciaProfesiones;
    @EJB
    PersistenciaAdiestramientosFInterface persistenciaAdiestramientos;
    @EJB
    PersistenciaInstitucionesInterface persistenciaInstituciones;
    @EJB
    PersistenciaMotivosRetirosInterface persistenciaMotivosRetiros;
    @EJB
    PersistenciaTiposFamiliaresInterface persistenciaTiposFamiliares;
    @EJB
    PersistenciaPersonasInterface persistenciaPersonas;
    @EJB
    PersistenciaSoAntecedentesInterface persistenciaAntecedentes;
    @EJB
    PersistenciaSoTiposAntecedentesInterface persistenciaTiposAntecedentes;
    @EJB
    PersistenciaEstadosCivilesInterface persistenciaEstadosCiviles;
    @EJB
    PersistenciaTiposTelefonosInterface persistenciaTiposTelefonos;
    @EJB
    PersistenciaCiudadesInterface persistenciaCiudades;
    @EJB
    PersistenciaDireccionesInterface persistenciaDirecciones;
    @EJB
    PersistenciaTelefonosInterface persistenciaTelefonos;
    @EJB
    PersistenciaVigenciasEstadosCivilesInterface persistenciaVigEstadosCiviles;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleados;
    @EJB
    PersistenciaSoAntecedentesMedicosInterface persistenciaAntecdentesM;
    @EJB
    PersistenciaFamiliaresInterface persistenciaFamiliares;
    @EJB
    PersistenciaVigenciasFormalesInterface persistenciaVigenciasFormales;
    @EJB
    PersistenciaHVHojasDeVidaInterface persistenciahv;
    @EJB
    PersistenciaHvExperienciasLaboralesInterface persistenciaHvExp;
    @EJB
    PersistenciaSectoresEconomicosInterface persistenciaSectoresE;
    @EJB
    PersistenciaTiposDocumentosInterface persistenciaTipoDocumento;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public void modificarVigencia(List<VigenciasDomiciliarias> listaModificar) {
        for (int i = 0; i < listaModificar.size(); i++) {
            persistenciaVigenciasDomiciliarias.editar(em, listaModificar.get(i));
        }
    }

    @Override
    public void crearVigencia(List<VigenciasDomiciliarias> listaCrear) {
        for (int i = 0; i < listaCrear.size(); i++) {
            persistenciaVigenciasDomiciliarias.crear(em, listaCrear.get(i));
        }
    }

    @Override
    public void borrarVigencia(List<VigenciasDomiciliarias> listaBorrar) {
        for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaVigenciasDomiciliarias.borrar(em, listaBorrar.get(i));
        }
    }

    @Override
    public List<VigenciasDomiciliarias> vigenciasDomiciliariasporPersona(BigInteger secPersona) {
        List<VigenciasDomiciliarias> listVigencias = persistenciaVigenciasDomiciliarias.visitasDomiciliariasPersona(em, secPersona);
        return listVigencias;
    }

    @Override
    public List<MotivosRetiros> lovMotivosRetiros() {
        List<MotivosRetiros> lovMotivosRetiros = persistenciaMotivosRetiros.consultarMotivosRetiros(em);
        return lovMotivosRetiros;
    }

    @Override
    public List<EstadosCiviles> lovVigenciasEstadosCiviles() {
        List<EstadosCiviles> lovEstadosCiviles = persistenciaEstadosCiviles.consultarEstadosCiviles(em);
        return lovEstadosCiviles;
    }

    @Override
    public List<TiposTelefonos> lovTiposTelefonos() {
        List<TiposTelefonos> lovTiposTelefonos = persistenciaTiposTelefonos.tiposTelefonos(em);
        return lovTiposTelefonos;
    }

    @Override
    public List<Ciudades> lovCiudades() {
        List<Ciudades> lovCiudades = persistenciaCiudades.lovCiudades(em);
        return lovCiudades;
    }

    @Override
    public List<Cargos> lovCargos() {
        List<Cargos> lovCargos = persistenciaCargos.lovCargos(em);
        return lovCargos;
    }

    @Override
    public List<Direcciones> direccionesPersona(BigInteger secPersona) {
        List<Direcciones> direccion = persistenciaDirecciones.listaDireccionPersona(em, secPersona);
        return direccion;
    }

    @Override
    public Telefonos telefonoActualPersona(BigInteger secPersona) {
        Telefonos telefono = persistenciaTelefonos.telefonoActual(em, secPersona);
        return telefono;
    }

    @Override
    public VigenciasEstadosCiviles estadoCivilActualPersona(BigInteger secPersona) {
        VigenciasEstadosCiviles vigestadoC = persistenciaVigEstadosCiviles.estadoCivilActual(em, secPersona);
        return vigestadoC;
    }

    @Override
    public Empleados buscarEmpleado(BigInteger secPersona) {
        Empleados empleado = persistenciaEmpleados.buscarEmpleadoSecuenciaPersona(em, secPersona);
        return empleado;
    }

    ////metodos direcciones
    @Override
    public List<Direcciones> consultarDireccionesPersona(BigInteger secPersona) {
        List<Direcciones> listDirecciones = persistenciaDirecciones.listaDireccionPersona(em, secPersona);
        return listDirecciones;
    }

    @Override
    public void crearDirecciones(List<Direcciones> listaCrear) {
        for (int i = 0; i < listaCrear.size(); i++) {
            persistenciaDirecciones.crear(em, listaCrear.get(i));
        }
    }

    @Override
    public void borrarDirecciones(List<Direcciones> listaBorrar) {
        for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaDirecciones.borrar(em, listaBorrar.get(i));
        }
    }

    @Override
    public void modificarDirecciones(List<Direcciones> listaModificar) {
        for (int i = 0; i < listaModificar.size(); i++) {
            persistenciaDirecciones.editar(em, listaModificar.get(i));
        }
    }

    ///métodos teléfonos
    @Override
    public List<Telefonos> telefonosPersona(BigInteger secPersona) {
        List<Telefonos> listTelefonos = persistenciaTelefonos.telefonosPersona(em, secPersona);
        return listTelefonos;
    }

    @Override
    public void crearTelefonos(Telefonos telefono) {
        persistenciaTelefonos.crear(em, telefono);
    }

    @Override
    public void borrarTelefonos(Telefonos telefono) {
        persistenciaTelefonos.borrar(em, telefono);
    }

    @Override
    public void modificarTelefonos(List<Telefonos> listModificar) {
        for (int i = 0; i < listModificar.size(); i++) {
            persistenciaTelefonos.editar(em, listModificar.get(i));
        }
    }

    ///métodos estados civiles
    @Override
    public List<VigenciasEstadosCiviles> estadosCivilesPersona(BigInteger secPersona) {
        List<VigenciasEstadosCiviles> listVigEstadosCiviles = persistenciaVigEstadosCiviles.consultarVigenciasEstadosCivilesPorPersona(em, secPersona);
        return listVigEstadosCiviles;
    }

    @Override
    public void modificarVigenciasEstadosCiviles(List<VigenciasEstadosCiviles> listaVigenciasEstadosCiviles) {
        for (int i = 0; i < listaVigenciasEstadosCiviles.size(); i++) {
            persistenciaVigEstadosCiviles.editar(em, listaVigenciasEstadosCiviles.get(i));
        }
    }

    @Override
    public void borrarVigenciasEstadosCiviles(List<VigenciasEstadosCiviles> listaVigenciasEstadosCiviles) {
        for (int i = 0; i < listaVigenciasEstadosCiviles.size(); i++) {
            persistenciaVigEstadosCiviles.borrar(em, listaVigenciasEstadosCiviles.get(i));
        }
    }

    @Override
    public void crearVigenciasEstadosCiviles(List<VigenciasEstadosCiviles> listaVigenciasEstadosCiviles) {
        for (int i = 0; i < listaVigenciasEstadosCiviles.size(); i++) {
            persistenciaVigEstadosCiviles.crear(em, listaVigenciasEstadosCiviles.get(i));
        }
    }

/// métodos antecedentes médicos
    @Override
    public List<SoAntecedentes> lovAntecedentes(BigInteger secTipoAntecedente) {
        List<SoAntecedentes> lovAntecedentes = persistenciaAntecedentes.lovAntecedentes(em, secTipoAntecedente);
        return lovAntecedentes;
    }

    @Override
    public List<SoTiposAntecedentes> lovTiposAntecedentes() {
        List<SoTiposAntecedentes> lovTiposAntecedentes = persistenciaTiposAntecedentes.lovTiposAntecedentes(em);
        return lovTiposAntecedentes;
    }

    @Override
    public List<SoAntecedentesMedicos> buscarAntecedentesMedicos(BigInteger secPersona) {
        List<SoAntecedentesMedicos> listAntecedentesM = persistenciaAntecdentesM.listaAntecedentesMedicos(em, secPersona);
        return listAntecedentesM;
    }

    @Override
    public void crearAntecedenteM(List<SoAntecedentesMedicos> listaCrear) {
        for (int i = 0; i < listaCrear.size(); i++) {
            if(listaCrear.get(i).getAntecedente() == null){
                listaCrear.get(i).setAntecedente(new SoAntecedentes());
            }
            
            if(listaCrear.get(i).getEmpleado() == null){
                listaCrear.get(i).setEmpleado(new Empleados());
            }
            persistenciaAntecdentesM.crear(em, listaCrear.get(i));
        }
    }

    @Override
    public void borrarAntecedenteM(List<SoAntecedentesMedicos> listaBorrar) {
        for (int i = 0; i < listaBorrar.size(); i++) {
            persistenciaAntecdentesM.borrar(em, listaBorrar.get(i));
        }
    }

    @Override
    public void modificarAntecedenteM(List<SoAntecedentesMedicos> listaModificar) {
        for (int i = 0; i < listaModificar.size(); i++) {
            persistenciaAntecdentesM.editar(em, listaModificar.get(i));
        }
    }

    // métodos vigencias formales
    @Override
    public void modificarVigenciaFormal(List<VigenciasFormales> listaVigenciasFormalesModificar) {
        for (int i = 0; i < listaVigenciasFormalesModificar.size(); i++) {
            log.warn("Modificando...");
            if (listaVigenciasFormalesModificar.get(i).getTipoeducacion().getSecuencia() == null) {
                listaVigenciasFormalesModificar.get(i).setTipoeducacion(null);
            }
            if (listaVigenciasFormalesModificar.get(i).getProfesion().getSecuencia() == null) {
                listaVigenciasFormalesModificar.get(i).setProfesion(null);
            }
            if (listaVigenciasFormalesModificar.get(i).getInstitucion().getSecuencia() == null) {
                listaVigenciasFormalesModificar.get(i).setInstitucion(null);
            }
            if (listaVigenciasFormalesModificar.get(i).getAdiestramientof().getSecuencia() == null) {
                listaVigenciasFormalesModificar.get(i).setAdiestramientof(null);
            }
            persistenciaVigenciasFormales.editar(em, listaVigenciasFormalesModificar.get(i));
        }
    }

    @Override
    public void borrarVigenciaFormal(List<VigenciasFormales> listaVigenciasFormalesBorrar) {
        for (int i = 0; i < listaVigenciasFormalesBorrar.size(); i++) {
            log.warn("Borrando...");
            if (listaVigenciasFormalesBorrar.get(i).getTipoeducacion().getSecuencia() == null) {
                listaVigenciasFormalesBorrar.get(i).setTipoeducacion(null);
            }
            if (listaVigenciasFormalesBorrar.get(i).getProfesion().getSecuencia() == null) {
                listaVigenciasFormalesBorrar.get(i).setProfesion(null);
            }
            if (listaVigenciasFormalesBorrar.get(i).getInstitucion().getSecuencia() == null) {
                listaVigenciasFormalesBorrar.get(i).setInstitucion(null);
            }
            persistenciaVigenciasFormales.borrar(em, listaVigenciasFormalesBorrar.get(i));
        }
    }

    @Override
    public void crearVigenciaFormal(List<VigenciasFormales> listaVigenciasFormalesCrear) {
        for (int i = 0; i < listaVigenciasFormalesCrear.size(); i++) {
            log.warn("Modificando...");
            if (listaVigenciasFormalesCrear.get(i).getTipoeducacion().getSecuencia() == null) {
                listaVigenciasFormalesCrear.get(i).setTipoeducacion(null);
            }
            if (listaVigenciasFormalesCrear.get(i).getProfesion().getSecuencia() == null) {
                listaVigenciasFormalesCrear.get(i).setProfesion(null);
            }
            if (listaVigenciasFormalesCrear.get(i).getInstitucion().getSecuencia() == null) {
                listaVigenciasFormalesCrear.get(i).setInstitucion(null);
            }
            if (listaVigenciasFormalesCrear.get(i).getAdiestramientof().getSecuencia() == null) {
                listaVigenciasFormalesCrear.get(i).setAdiestramientof(null);
            }
            if (listaVigenciasFormalesCrear.get(i).getNumerotarjeta() != null) {
                listaVigenciasFormalesCrear.get(i).setTarjetaprofesional("S");
            } else {
                listaVigenciasFormalesCrear.get(i).setTarjetaprofesional("N");
            }

            if (listaVigenciasFormalesCrear.get(i).getAdiestramientof().getDescripcion() != null) {
                listaVigenciasFormalesCrear.get(i).setAcargo("S");
            } else {
                listaVigenciasFormalesCrear.get(i).setAcargo("N");
            }
            persistenciaVigenciasFormales.crear(em, listaVigenciasFormalesCrear.get(i));
        }
    }

    @Override
    public List<TiposEducaciones> lovTiposEducaciones() {
        List<TiposEducaciones> lovTiposEducaciones = persistenciaTiposEducaciones.tiposEducaciones(em);
        return lovTiposEducaciones;
    }

    @Override
    public List<Profesiones> lovProfesiones() {
        List<Profesiones> lovProfesiones = persistenciaProfesiones.profesiones(em);
        return lovProfesiones;
    }

    @Override
    public List<AdiestramientosF> lovAdiestramientos() {
        List<AdiestramientosF> lovAdiestramientosF = persistenciaAdiestramientos.adiestramientosF(em);
        return lovAdiestramientosF;
    }

    @Override
    public List<Instituciones> lovInstituciones() {
        List<Instituciones> lovInstituciones = persistenciaInstituciones.lovInstituciones(em);
        return lovInstituciones;
    }

    @Override
    public List<VigenciasFormales> vigenciasFormalesPersona(BigInteger secPersona) {
        List<VigenciasFormales> listEducacion = persistenciaVigenciasFormales.vigenciasFormalesPersona(em, secPersona);
        return listEducacion;
    }
//////métodos exp laboral

    @Override
    public void crearExperienciaLaboral(List<HvExperienciasLaborales> listHEL) {
        try {
            for (int i = 0; i < listHEL.size(); i++) {
                if (listHEL.get(i).getMotivoretiro().getSecuencia() == null) {
                    listHEL.get(i).setMotivoretiro(null);
                }
                if (listHEL.get(i).getSectoreconomico().getSecuencia() == null) {
                    listHEL.get(i).setSectoreconomico(null);
                }

                if (listHEL.get(i).getHojadevida() == null) {
                    listHEL.get(i).setHojadevida(new HVHojasDeVida());
                }

                persistenciaHvExp.crear(em, listHEL.get(i));
            }
        } catch (Exception e) {
            log.warn("Error crearExperienciaLaboral Admi : " + e.toString());
        }
    }

    @Override
    public void editarExperienciaLaboral(List<HvExperienciasLaborales> listHEL) {
        try {
            for (int i = 0; i < listHEL.size(); i++) {
                if (listHEL.get(i).getMotivoretiro().getSecuencia() == null) {
                    listHEL.get(i).setMotivoretiro(null);
                }
                if (listHEL.get(i).getSectoreconomico().getSecuencia() == null) {
                    listHEL.get(i).setSectoreconomico(null);
                }

                if (listHEL.get(i).getHojadevida() == null) {
                    listHEL.get(i).setHojadevida(new HVHojasDeVida());
                }

                persistenciaHvExp.editar(em, listHEL.get(i));
            }
        } catch (Exception e) {
            log.warn("Error editarExperienciaLaboral Admi : " + e.toString());
        }
    }

    @Override
    public void borrarExperienciaLaboral(List<HvExperienciasLaborales> listHEL) {
        try {
            for (int i = 0; i < listHEL.size(); i++) {
//                if (listHEL.get(i).getMotivoretiro().getSecuencia() == null) {
//                    listHEL.get(i).setMotivoretiro(null);
//                }
                if (listHEL.get(i).getSectoreconomico().getSecuencia() == null) {
                    listHEL.get(i).setSectoreconomico(null);
                }
                persistenciaHvExp.borrar(em, listHEL.get(i));
            }
        } catch (Exception e) {
            log.warn("Error borrarExperienciaLaboral Admi : " + e.toString());
        }
    }

    @Override
    public HVHojasDeVida obtenerHojaVidaPersona(BigInteger secuencia) {
        HVHojasDeVida hojaVida = persistenciahv.hvHojaDeVidaPersona(em, secuencia);
        return hojaVida;
    }

    @Override
    public List<HvExperienciasLaborales> experienciasLaboralesEmpleado(BigInteger secuencia) {
        List<HvExperienciasLaborales> listExp = persistenciaHvExp.experienciasLaboralesSecuenciaEmpleado(em, secuencia);
        return listExp;
    }

    @Override
    public List<SectoresEconomicos> lovSectoresEconomicos() {
        List<SectoresEconomicos> listSectoresEconomicos = persistenciaSectoresE.buscarSectoresEconomicos(em);
        return listSectoresEconomicos;
    }

    ///métodos familiares
    @Override
    public List<TiposFamiliares> lovTiposFamiliares() {
        List<TiposFamiliares> lovTiposFamiliares = persistenciaTiposFamiliares.buscarTiposFamiliares(em);
        return lovTiposFamiliares;
    }

    @Override
    public List<Personas> lovPersonas() {
        List<Personas> lovPersonas = persistenciaPersonas.consultarPersonas(em);
        return lovPersonas;
    }

    @Override
    public Personas encontrarPersona(BigInteger secpersona) {
        Personas persona = persistenciaPersonas.buscarPersona(em, secpersona);
        return persona;
    }

    @Override
    public List<Familiares> buscarFamiliares(BigInteger secPersona) {
        List<Familiares> listFamiliares = persistenciaFamiliares.familiaresPersona(em, secPersona);
        return listFamiliares;
    }

    @Override
    public void crearPersona(Personas persona) {
        persistenciaPersonas.crear(em, persona);
    }

    @Override
    public List<TiposDocumentos> consultarTiposDocumentos() {
        List<TiposDocumentos> listTiposDocumentos;
        listTiposDocumentos = persistenciaTipoDocumento.consultarTiposDocumentos(em);
        return listTiposDocumentos;
    }

    @Override
    public void modificarFamiliares(List<Familiares> listaModificar) {
        for (int i = 0; i < listaModificar.size(); i++) {
            if (listaModificar.get(i).getPersona() == null) {
                listaModificar.get(i).setPersona(new Personas());
            }
            log.warn("Administrar Modificando...");
            persistenciaFamiliares.editar(em, listaModificar.get(i));
        }
    }

    @Override
    public void borrarFamiliares(List<Familiares> listaBorrar) {
        for (int i = 0; i < listaBorrar.size(); i++) {
            if (listaBorrar.get(i).getPersona() == null) {
                listaBorrar.get(i).setPersona(new Personas());
            }
            log.warn("Administrar Borrando...");
            persistenciaFamiliares.borrar(em, listaBorrar.get(i));
        }
    }

    @Override
    public void crearFamilares(List<Familiares> listaCrear) {
        for (int i = 0; i < listaCrear.size(); i++) {
            log.warn("Administrar Creando...");
            if (listaCrear.get(i).getPersona() == null) {
                listaCrear.get(i).setPersona(new Personas());
            }
            persistenciaFamiliares.crear(em, listaCrear.get(i));
        }
    }

    @Override
    public VigenciasDomiciliarias vigenciaDomiciliariaActual(BigInteger secPersona) {
        VigenciasDomiciliarias vigActual = persistenciaVigenciasDomiciliarias.actualVisitaDomiciliariaPersona(em, secPersona);
        return vigActual;
    }

    @Override
    public void editarHojadeVida(List<HVHojasDeVida> listaEditar) {
        for (int i = 0; i < listaEditar.size(); i++) {
            persistenciahv.editar(em, listaEditar.get(i));
        }
    }

    @Override
    public void editarPersona(List<Personas> listaEditar) {
        for (int i = 0; i < listaEditar.size(); i++) {
            persistenciaPersonas.editar(em, listaEditar.get(i));
        }
    }

}
