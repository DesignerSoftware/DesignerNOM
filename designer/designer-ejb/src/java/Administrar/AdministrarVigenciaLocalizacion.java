package Administrar;

import Entidades.CentrosCostos;
import Entidades.Empleados;
import Entidades.Estructuras;
import Entidades.MotivosLocalizaciones;
import Entidades.Proyectos;
import Entidades.VigenciasLocalizaciones;
import Entidades.VigenciasProrrateos;
import Entidades.VigenciasProrrateosProyectos;
import InterfaceAdministrar.AdministrarVigenciaLocalizacionInterface;
import InterfacePersistencia.PersistenciaCentrosCostosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaMotivosLocalizacionesInterface;
import InterfacePersistencia.PersistenciaProyectosInterface;
import InterfacePersistencia.PersistenciaVigenciasLocalizacionesInterface;
import InterfacePersistencia.PersistenciaVigenciasProrrateosInterface;
import InterfacePersistencia.PersistenciaVigenciasProrrateosProyectosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;

/**
 *
 * @author AndresPineda
 */
@Stateful
public class AdministrarVigenciaLocalizacion implements AdministrarVigenciaLocalizacionInterface {

    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleado;
    @EJB
    PersistenciaMotivosLocalizacionesInterface persistenciaMotivosLocalizaciones;
    @EJB
    PersistenciaEstructurasInterface persistenciaEstructuras;
    @EJB
    PersistenciaVigenciasLocalizacionesInterface persistenciaVigenciasLocalizaciones;
    @EJB
    PersistenciaProyectosInterface persistenciaProyectos;
    @EJB
    PersistenciaVigenciasProrrateosInterface persistenciaVigenciasProrrateos;
    @EJB
    PersistenciaCentrosCostosInterface persistenciaCentrosCostos;
    @EJB
    PersistenciaVigenciasProrrateosProyectosInterface persistenciaVigenciasProrrateosProyectos;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    //Vigencias Prorrateos
    List<VigenciasProrrateos> vigenciasProrrateos;
    VigenciasProrrateos vigenciaProrrateo;
    //Vigencias Localizaciones
    List<VigenciasLocalizaciones> vigenciasLocalizaciones;
    VigenciasLocalizaciones vigenciaLocalizacion;
    //Vigencias Prorrateos Proyectos
    List<VigenciasProrrateosProyectos> vigenciasProrrateosProyectos;
    VigenciasProrrateosProyectos vigenciaProrrateoProyecto;
    //Empleado
    Empleados empleado;
    //Listas Adicionales
    List<MotivosLocalizaciones> motivosLocalizaciones;
    List<Estructuras> estructuras;
    List<Proyectos> proyectos;
    List<CentrosCostos> centrosCostos;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<VigenciasLocalizaciones> VigenciasLocalizacionesEmpleado(BigInteger secEmpleado) {
        try {
            vigenciasLocalizaciones = persistenciaVigenciasLocalizaciones.buscarVigenciasLocalizacionesEmpleado(em, secEmpleado);
        } catch (Exception e) {
            System.out.println("Error en Administrar Vigencias Localizaciones (VigenciasLocalizacionesEmpleado) : " + e.toString());
            vigenciasLocalizaciones = null;
        }
        return vigenciasLocalizaciones;
    }

    @Override
    public void modificarVL(List<VigenciasLocalizaciones> listVLModificadas) {
        try {
            for (int i = 0; i < listVLModificadas.size(); i++) {
                if (listVLModificadas.get(i).getProyecto().getSecuencia() == null) {
                    listVLModificadas.get(i).setProyecto(null);
                }
                if (listVLModificadas.get(i).getLocalizacion().getSecuencia() == null) {
                    listVLModificadas.get(i).setLocalizacion(null);
                }
                if (listVLModificadas.get(i).getMotivo().getSecuencia() == null) {
                    listVLModificadas.get(i).setLocalizacion(null);
                }
                vigenciaLocalizacion = listVLModificadas.get(i);
                persistenciaVigenciasLocalizaciones.editar(em, vigenciaLocalizacion);
            }
        } catch (Exception e) {
            System.out.println("Error modificarVL AdmiVigLoc : " + e.toString());
        }
    }

    @Override
    public void borrarVL(VigenciasLocalizaciones vigenciasLocalizaciones) {
        try {
            persistenciaVigenciasLocalizaciones.borrar(em, vigenciasLocalizaciones);
        } catch (Exception e) {
            System.out.println("Error borrarVL AdmiVigLoc : " + e.toString());
        }
    }

    @Override
    public void crearVL(VigenciasLocalizaciones vigenciasLocalizaciones) {
        try {
            persistenciaVigenciasLocalizaciones.crear(em, vigenciasLocalizaciones);
        } catch (Exception e) {
            System.out.println("Error crearVL AdmiVigLoc : " + e.toString());
        }
    }

    @Override
    public Empleados buscarEmpleado(BigInteger secuencia) {
        try {
            empleado = persistenciaEmpleado.buscarEmpleadoSecuencia(em, secuencia);
            return empleado;
        } catch (Exception e) {
            System.out.println("Error buscarEmpleado Adm: " + e.toString());
            empleado = null;
            return empleado;
        }
    }

    @Override
    public List<MotivosLocalizaciones> motivosLocalizaciones() {
        try {
            motivosLocalizaciones = persistenciaMotivosLocalizaciones.buscarMotivosLocalizaciones(em);
            return motivosLocalizaciones;
        } catch (Exception e) {
            System.out.println("Error motivosLocalizaciones AdministrarVigenciasdLocalizaciones: " + e.toString());
            return null;
        }
    }

    @Override
    public List<Estructuras> estructuras() {
        try {
            estructuras = persistenciaEstructuras.buscarEstructuras(em);
            return estructuras;
        } catch (Exception e) {
            System.out.println("Error estructuras AdministrarVigenciasdLocalizaciones: " + e.toString());
            return null;
        }
    }

    @Override
    public List<Proyectos> proyectos() {
        try {
            proyectos = persistenciaProyectos.proyectos(em);
            return proyectos;
        } catch (Exception e) {
            System.out.println("Error proyectos AdministrarVigenciasdLocalizaciones: " + e.toString());
            return null;
        }
    }

    @Override
    public List<VigenciasProrrateos> VigenciasProrrateosVigencia(BigInteger secVigencia) {
        try {
            vigenciasProrrateos = persistenciaVigenciasProrrateos.buscarVigenciasProrrateosVigenciaSecuencia(em, secVigencia);
        } catch (Exception e) {
            System.out.println("Error en Administrar Vigencias Localizaciones (VigenciasProrrateosVigencia): " + e.toString());
            vigenciasProrrateos = null;
        }
        return vigenciasProrrateos;
    }

    @Override
    public void modificarVP(List<VigenciasProrrateos> listVPModificadas) {
        try {
            for (int i = 0; i < listVPModificadas.size(); i++) {
                if (listVPModificadas.get(i).getProyecto().getSecuencia() == null) {
                    listVPModificadas.get(i).setProyecto(null);
                }
                if (listVPModificadas.get(i).getCentrocosto().getSecuencia() == null) {
                    listVPModificadas.get(i).setCentrocosto(null);
                }
                if (listVPModificadas.get(i).getViglocalizacion().getProyecto().getSecuencia() == null) {
                    listVPModificadas.get(i).getViglocalizacion().setProyecto(null);
                }
                vigenciaProrrateo = listVPModificadas.get(i);
                persistenciaVigenciasProrrateos.editar(em, vigenciaProrrateo);
            }
        } catch (Exception e) {
            System.out.println("Error modificarVP AdmiVigLoc: " + e.toString());
        }
    }

    @Override
    public void borrarVP(VigenciasProrrateos vigenciasProrrateos) {
        try {
            persistenciaVigenciasProrrateos.borrar(em, vigenciasProrrateos);
        } catch (Exception e) {
            System.out.println("Error borrarVP AdmiVigLoc: " + e.toString());
        }
    }

    @Override
    public void crearVP(VigenciasProrrateos vigenciasProrrateos) {
        try {
            persistenciaVigenciasProrrateos.crear(em, vigenciasProrrateos);
        } catch (Exception e) {
            System.out.println("Error crearVP AdmiVigLoc: " + e.toString());
        }
    }

    @Override
    public List<CentrosCostos> centrosCostos() {
        try {
            centrosCostos = persistenciaCentrosCostos.buscarCentrosCostos(em);
            return centrosCostos;
        } catch (Exception e) {
            System.out.println("Error centrosCostos Admi: " + e.toString());
            return null;
        }
    }

    @Override
    public List<VigenciasProrrateosProyectos> VigenciasProrrateosProyectosVigencia(BigInteger secVigencia) {
        try {
            vigenciasProrrateosProyectos = persistenciaVigenciasProrrateosProyectos.buscarVigenciasProrrateosProyectosVigenciaSecuencia(em, secVigencia);
        } catch (Exception e) {
            vigenciasProrrateosProyectos = null;
            System.out.println("Error VigenciasProrrateosProyectosVigencia Admi : " + e.toString());
        }
        return vigenciasProrrateosProyectos;
    }

    @Override
    public void modificarVPP(List<VigenciasProrrateosProyectos> listVPPModificadas) {
        try {
            for (int i = 0; i < listVPPModificadas.size(); i++) {
                if (listVPPModificadas.get(i).getProyecto().getSecuencia() == null) {
                    listVPPModificadas.get(i).setProyecto(null);
                }
                if (listVPPModificadas.get(i).getVigencialocalizacion().getProyecto().getSecuencia() == null) {
                    listVPPModificadas.get(i).getVigencialocalizacion().setProyecto(null);
                }
                vigenciaProrrateoProyecto = listVPPModificadas.get(i);
                persistenciaVigenciasProrrateosProyectos.editar(em, vigenciaProrrateoProyecto);
            }
        } catch (Exception e) {
            System.out.println("Error modificarVPP AdmiVigLoc: " + e.toString());
        }
    }

    @Override
    public void borrarVPP(VigenciasProrrateosProyectos vigenciasProrrateosProyectos) {
        try {
            persistenciaVigenciasProrrateosProyectos.borrar(em, vigenciasProrrateosProyectos);
        } catch (Exception e) {
            System.out.println("Error borrarVPP AdmiVigLoc: " + e.toString());
        }
    }

    @Override
    public void crearVPP(VigenciasProrrateosProyectos vigenciasProrrateosProyectos) {
        try {
            persistenciaVigenciasProrrateosProyectos.crear(em, vigenciasProrrateosProyectos);
        } catch (Exception e) {
            System.out.println("Error crearVPP AdmiVigLoc: " + e.toString());
        }
    }

    @Remove
    @Override
    public void salir() {
        vigenciaLocalizacion = null;
    }
}
