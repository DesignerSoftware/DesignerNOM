package Administrar;

import Entidades.ActualUsuario;
import Entidades.AportesCorrecciones;
import Entidades.AportesEntidades;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.ParametrosAutoliq;
import Entidades.ParametrosCorreccionesAutoL;
import Entidades.ParametrosEstructuras;
import Entidades.ParametrosInformes;
import Entidades.Terceros;
import Entidades.TiposEntidades;
import Entidades.TiposTrabajadores;
import InterfaceAdministrar.AdministrarParametroAutoliqInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaAportesCorreccionesInterface;
import InterfacePersistencia.PersistenciaAportesEntidadesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaParametrosAutoliqInterface;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import InterfacePersistencia.PersistenciaParametrosInformesInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaTiposEntidadesInterface;
import InterfacePersistencia.PersistenciaTiposTrabajadoresInterface;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
@Stateful
public class AdministrarParametroAutoliq implements AdministrarParametroAutoliqInterface {

    @EJB
    PersistenciaParametrosAutoliqInterface persistenciaParametrosAutoliq;
    @EJB
    PersistenciaAportesEntidadesInterface persistenciaAportesEntidades;
    @EJB
    PersistenciaTiposTrabajadoresInterface persistenciaTiposTrabajadores;
    @EJB
    PersistenciaEmpleadoInterface persistenciaEmpleados;
    @EJB
    PersistenciaTercerosInterface persistenciaTerceros;
    @EJB
    PersistenciaTiposEntidadesInterface persistenciaTiposEntidades;
    @EJB
    PersistenciaEmpresasInterface persistenciaEmpresas;
    @EJB
    PersistenciaActualUsuarioInterface persistenciaActualUsuario;
    @EJB
    PersistenciaParametrosInformesInterface persistenciaParametrosInformes;
    @EJB
    PersistenciaParametrosEstructurasInterface persistenciaParametrosEstructuras;

    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    //@Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<ParametrosAutoliq> consultarParametrosAutoliq() {
        try {
            List<ParametrosAutoliq> lista = persistenciaParametrosAutoliq.consultarParametrosAutoliqPorEmpresas(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error consultarParametrosAutoliq Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearParametrosAutoliq(List<ParametrosAutoliq> listaPA) {
        try {
            for (int i = 0; i < listaPA.size(); i++) {
                if (listaPA.get(i).getTipotrabajador().getSecuencia() == null) {
                    listaPA.get(i).setTipotrabajador(null);
                }
                if (listaPA.get(i).getEmpresa().getSecuencia() == null) {
                    listaPA.get(i).setEmpresa(null);
                }
                persistenciaParametrosAutoliq.crear(em, listaPA.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error crearParametrosAutoliq Admi : " + e.toString());
        }
    }

    @Override
    public void editarParametrosAutoliq(List<ParametrosAutoliq> listaPA) {
        try {
            for (int i = 0; i < listaPA.size(); i++) {
                if (listaPA.get(i).getTipotrabajador().getSecuencia() == null) {
                    listaPA.get(i).setTipotrabajador(null);
                }
                if (listaPA.get(i).getEmpresa().getSecuencia() == null) {
                    listaPA.get(i).setEmpresa(null);
                }
                persistenciaParametrosAutoliq.editar(em, listaPA.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error editarParametrosAutoliq Admi : " + e.toString());
        }
    }

    @Override
    public void borrarParametrosAutoliq(List<ParametrosAutoliq> listaPA) {
        try {
            for (int i = 0; i < listaPA.size(); i++) {
                if (listaPA.get(i).getTipotrabajador().getSecuencia() == null) {
                    listaPA.get(i).setTipotrabajador(null);
                }
                if (listaPA.get(i).getEmpresa().getSecuencia() == null) {
                    listaPA.get(i).setEmpresa(null);
                }
                persistenciaParametrosAutoliq.borrar(em, listaPA.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error borrarParametrosAutoliq Admi : " + e.toString());
        }
    }

    @Override
    public List<AportesEntidades> consultarAportesEntidadesPorParametroAutoliq(BigInteger empresa, short mes, short ano) {
        try {
            System.out.println("entró en: consultarAportesEntidadesPorParametroAutoliq ");
            List<AportesEntidades> lista = persistenciaAportesEntidades.consultarAportesEntidadesPorEmpresaMesYAnio(em, empresa, mes, ano);

            if (lista != null) {
                System.out.println("ya tiene lista : " + lista.size());
                for (int i = 0; i < lista.size(); i++) {
                    if (lista.get(i).getTercero() != null) {
                        Terceros tercero = persistenciaTerceros.buscarTercerosSecuencia(em, lista.get(i).getTercero());
                        if (tercero != null) {
                            lista.get(i).setTerceroRegistro(tercero);
                        } else {
                            lista.get(i).setTerceroRegistro(new Terceros());
                        }
                    } else {
                        lista.get(i).setTerceroRegistro(new Terceros());
                    }
                }
            }
            System.out.println("terminó el for de la lista aportes");
            return lista;
        } catch (Exception e) {
            System.out.println("Error consultarAportesEntidadesPorParametroAutoliq Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void editarAportesEntidades(List<AportesEntidades> listAE) {
        try {
            for (int i = 0; i < listAE.size(); i++) {

                if (listAE.get(i).getTipoentidad().getSecuencia() == null) {
                    System.out.println("if 2 editarAportesEntidades");
                    listAE.get(i).setTipoentidad(new TiposEntidades());
                } else if (listAE.get(i).getEmpleado().getSecuencia() == null) {
                    System.out.println("if 3 editarAportesEntidades");
                    listAE.get(i).setEmpleado(null);
                } else if (listAE.get(i).getTerceroRegistro().getSecuencia() == null) {
                    System.out.println("if 4 editarAportesEntidades");
                    listAE.get(i).setTercero(null);
                } else if (listAE.get(i).getTerceroRegistro().getSecuencia() != null) {
                    System.out.println("if 5 editarAportesEntidades");
                    listAE.get(i).setTercero(listAE.get(i).getTerceroRegistro().getSecuencia());
                }
                persistenciaAportesEntidades.editar(em, listAE.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error editarAportesEntidades Admi : " + e.toString());
        }
    }

    @Override
    public void borrarAportesEntidades(List<AportesEntidades> listAE) {
        try {
            for (int i = 0; i < listAE.size(); i++) {

                if (listAE.get(i).getTipoentidad().getSecuencia() == null) {
                    listAE.get(i).setTipoentidad(null);
                } else if (listAE.get(i).getEmpleado().getSecuencia() == null) {
                    listAE.get(i).setEmpleado(null);
                } else if (listAE.get(i).getTerceroRegistro().getSecuencia() == null) {
                    listAE.get(i).setTercero(null);
                } else if (listAE.get(i).getTerceroRegistro().getSecuencia() != null) {
                    listAE.get(i).setTercero(listAE.get(i).getTerceroRegistro().getSecuencia());
                }
                persistenciaAportesEntidades.borrar(em, listAE.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error borrarAportesEntidades Admi : " + e.toString());
        }
    }

    @Override
    public void crearAportesEntidades(List<AportesEntidades> listaAE) {
        try {
            System.out.println("entró al crearAportesEntidades");
            for (int i = 0; i < listaAE.size(); i++) {

                if (listaAE.get(i).getTipoentidad() != null) {
                    if (listaAE.get(i).getTipoentidad().getSecuencia() == null) {
                        listaAE.get(i).setTipoentidad(new TiposEntidades());
                        System.out.println("if 2 crearAportesEntidades");
                    }
                }

                if (listaAE.get(i).getEmpleado() != null) {
                    if (listaAE.get(i).getEmpleado().getSecuencia() == null) {
                        listaAE.get(i).setEmpleado(null);
                        System.out.println("if 3 crearAportesEntidades");
                    }
                }

                if (listaAE.get(i).getTerceroRegistro() != null) {
                    if (listaAE.get(i).getTerceroRegistro().getSecuencia() == null) {
                        System.out.println("if 4 crearAportesEntidades");
                        listaAE.get(i).setTercero(null);
                    } else {
                        listaAE.get(i).setTercero(listaAE.get(i).getTerceroRegistro().getSecuencia());

                    }
                }
                persistenciaAportesEntidades.crear(em, listaAE.get(i));
            }
            System.out.println("sale del for crearAportesEntidades");
        } catch (Exception e) {
            System.out.println("Error crearAportesEntidades Admi : " + e.toString());
        }
    }

    @Override
    public List<TiposTrabajadores> lovTiposTrabajadores() {
        try {
            List<TiposTrabajadores> lista = persistenciaTiposTrabajadores.buscarTiposTrabajadores(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error lovTiposTrabajadores Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Empleados> lovEmpleados() {
        try {
            List<Empleados> lista = persistenciaEmpleados.consultarEmpleadosParametroAutoliq(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error lovEmpleados Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<TiposEntidades> lovTiposEntidades() {
        try {
            List<TiposEntidades> lista = persistenciaTiposEntidades.buscarTiposEntidadesParametroAutoliq(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error lovTiposEntidades Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Terceros> lovTerceros() {
        try {
            List<Terceros> lista = persistenciaTerceros.buscarTercerosParametrosAutoliq(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error lovTerceros Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Empresas> lovEmpresas() {
        try {
            List<Empresas> lista = persistenciaEmpresas.buscarEmpresas(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error lovEmpresas Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void borrarAportesEntidadesProcesoAutomatico(BigInteger empresa, short mes, short ano) {
        try {
            persistenciaAportesEntidades.borrarAportesEntidadesProcesoAutomatico(em, empresa, mes, ano);
        } catch (Exception e) {
            System.out.println("Error borrarAportesEntidadesProcesoAutomatico Admi : " + e.toString());
        }
    }

    //@Override
    public ActualUsuario obtenerActualUsuario() {
        try {
            ActualUsuario actual = persistenciaActualUsuario.actualUsuarioBD(em);
            return actual;
        } catch (Exception e) {
            System.out.println("Error obtenerActualUsuario Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public ParametrosInformes buscarParametroInforme(String usuario) {
        try {
            ParametrosInformes parametro = persistenciaParametrosInformes.buscarParametroInformeUsuario(em, usuario);
            return parametro;
        } catch (Exception e) {
            System.out.println("Error buscarParametroInforme Admi : " + e.toString());
            return null;
        }

    }

    @Override
    public void modificarParametroInforme(ParametrosInformes parametro) {
        try {
            persistenciaParametrosInformes.editar(em, parametro);
        } catch (Exception e) {
            System.out.println("Error modificarParametroInforme Admi : " + e.toString());
        }
    }

    @Override
    public ParametrosEstructuras buscarParametroEstructura(String usuario) {
        try {
            ParametrosEstructuras parametro = persistenciaParametrosEstructuras.buscarParametro(em, usuario);
            return parametro;
        } catch (Exception e) {
            System.out.println("Error buscarParametroEstructura Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void modificarParametroEstructura(ParametrosEstructuras parametro) {
        try {
            persistenciaParametrosEstructuras.editar(em, parametro);
        } catch (Exception e) {
            System.out.println("Error modificarParametroEstructura Admi : " + e.toString());
        }
    }

    @Override
    public String ejecutarPKGActualizarNovedades(short ano, short mes, BigInteger secuencia) {
        try {
            String proceso = persistenciaAportesEntidades.ejecutarPKGActualizarNovedades(em, secuencia, mes, ano);
            return proceso;
        } catch (Exception e) {
            System.out.println("Error ejecutarPKGActualizarNovedades Admi : " + e.toString());
            return "ERROR_ADMINISTRAR";
        }
    }

    @Override
    public String ejecutarPKGInsertar(Date fechaIni, Date fechaFin, BigInteger secTipoTrabajador, BigInteger secuenciaEmpresa) {
        try {
            String proceso = persistenciaAportesEntidades.ejecutarPKGInsertar(em, fechaIni, fechaFin, secTipoTrabajador, secuenciaEmpresa);
            return proceso;
        } catch (Exception e) {
            System.out.println("Error ejecutarPKGActualizarNovedades Admi : " + e.toString());
            return "ERROR_ADMINISTRAR";
        }
    }

    @Override
    public void ejecutarPKGAcumularDiferencia(short ano, short mes, BigInteger secuencia) {
        try {
            persistenciaAportesEntidades.ejecutarPKGActualizarNovedades(em, secuencia, mes, ano);
        } catch (Exception e) {
            System.out.println("Error ejecutarPKGActualizarNovedades Admi : " + e.toString());
        }
    }

}
