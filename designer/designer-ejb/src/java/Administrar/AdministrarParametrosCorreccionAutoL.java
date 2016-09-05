/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.ActualUsuario;
import Entidades.AportesCorrecciones;
import Entidades.Empleados;
import Entidades.Empresas;
import Entidades.ParametrosCorreccionesAutoL;
import Entidades.ParametrosEstructuras;
import Entidades.ParametrosInformes;
import Entidades.Terceros;
import Entidades.TiposEntidades;
import Entidades.TiposTrabajadores;
import InterfaceAdministrar.AdministrarParametrosCorreccionAutoLInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaAportesCorreccionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaParametrosCorreccionAutoLInterface;
import InterfacePersistencia.PersistenciaParametrosEstructurasInterface;
import InterfacePersistencia.PersistenciaParametrosInformesInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaTiposEntidadesInterface;
import InterfacePersistencia.PersistenciaTiposTrabajadoresInterface;
import Persistencia.PersistenciaParametrosCorreccionAutoL;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarParametrosCorreccionAutoL implements AdministrarParametrosCorreccionAutoLInterface {

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
    PersistenciaAportesCorreccionesInterface persistenciaAportesCorrecciones;
    @EJB
    PersistenciaActualUsuarioInterface persistenciaActualUsuario;
    @EJB
    PersistenciaParametrosCorreccionAutoLInterface persistenciaParametrosCorreccionAuto;
    @EJB
    PersistenciaParametrosInformesInterface persistenciaParametrosInformes;
    @EJB
    PersistenciaParametrosEstructurasInterface persistenciaParametrosEstructuras;
    @EJB
    AdministrarSesionesInterface administrarSesiones;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<ParametrosCorreccionesAutoL> consultarParametrosCorreccionesAutoliq() {
        try {
            List<ParametrosCorreccionesAutoL> lista = persistenciaParametrosCorreccionAuto.consultarParametrosCorreccionesAutoL(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error consultarParametrosAutoliq Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearParametrosCorreccionAutoliq(List<ParametrosCorreccionesAutoL> listaPCA) {
        try {
            for (int i = 0; i < listaPCA.size(); i++) {
                if (listaPCA.get(i).getTipotrabajador().getSecuencia() == null) {
                    listaPCA.get(i).setTipotrabajador(null);
                }
                if (listaPCA.get(i).getEmpresa().getSecuencia() == null) {
                    listaPCA.get(i).setEmpresa(null);
                }
                persistenciaParametrosCorreccionAuto.crearCorreccion(em, listaPCA.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error crearParametrosCorreccionAutoliq Admi : " + e.toString());
        }
    }

    @Override
    public void editarParametrosCorreccionAutoliq(List<ParametrosCorreccionesAutoL> listaPCA) {
        try {
            for (int i = 0; i < listaPCA.size(); i++) {
                if (listaPCA.get(i).getTipotrabajador().getSecuencia() == null) {
                    listaPCA.get(i).setTipotrabajador(null);
                }
                if (listaPCA.get(i).getEmpresa().getSecuencia() == null) {
                    listaPCA.get(i).setEmpresa(null);
                }
                persistenciaParametrosCorreccionAuto.editarCorreccion(em, listaPCA.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error editarParametrosCorreccionAutoliq Admi : " + e.toString());
        }
    }

    @Override
    public void borrarParametrosCorreccionAutoliq(List<ParametrosCorreccionesAutoL> listaPCA) {
        try {
            for (int i = 0; i < listaPCA.size(); i++) {
                if (listaPCA.get(i).getTipotrabajador().getSecuencia() == null) {
                    listaPCA.get(i).setTipotrabajador(null);
                }
                if (listaPCA.get(i).getEmpresa().getSecuencia() == null) {
                    listaPCA.get(i).setEmpresa(null);
                }
                persistenciaParametrosCorreccionAuto.borrarCorreccion(em, listaPCA.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error borrarParametrosCorreccionAutoliq Admi : " + e.toString());
        }
    }

    @Override
    public void crearAportesCorrecciones(List<AportesCorrecciones> listaAC) {
        try {
            for (int i = 0; i < listaAC.size(); i++) {

                if (listaAC.get(i).getTipoentidad().getSecuencia() == null) {
                    listaAC.get(i).setTipoentidad(new TiposEntidades());
                } else if (listaAC.get(i).getEmpleado().getSecuencia() == null) {
                    listaAC.get(i).setEmpleado(null);
                }
                
                 if (listaAC.get(i).getTerceroRegistro() != null) {
                    if (listaAC.get(i).getTerceroRegistro().getSecuencia() == null) {
                        listaAC.get(i).setTercero(null);
                    } else {
                        listaAC.get(i).setTercero(listaAC.get(i).getTerceroRegistro().getSecuencia());

                    }
                }
                persistenciaAportesCorrecciones.crear(em, listaAC.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error crearAportesCorrecciones Admi : " + e.toString());
        }
    }

    @Override
    public void editarAportesCorrecciones(List<AportesCorrecciones> listAC) {
        try {
            for (int i = 0; i < listAC.size(); i++) {

                if (listAC.get(i).getTipoentidad().getSecuencia() == null) {
                    listAC.get(i).setTipoentidad(new TiposEntidades());
                } else if (listAC.get(i).getEmpleado().getSecuencia() == null) {
                    listAC.get(i).setEmpleado(null);
                }else if (listAC.get(i).getTerceroRegistro().getSecuencia() == null) {
                    listAC.get(i).setTercero(null);
                } else if (listAC.get(i).getTerceroRegistro().getSecuencia() != null) {
                    listAC.get(i).setTercero(listAC.get(i).getTerceroRegistro().getSecuencia());
                }
                persistenciaAportesCorrecciones.editar(em, listAC.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error editarAportesCorrecciones Admi : " + e.toString());
        }
    }

    @Override
    public void borrarAportesCorrecciones(List<AportesCorrecciones> listAC) {
        try {
            for (int i = 0; i < listAC.size(); i++) {

                if (listAC.get(i).getTipoentidad().getSecuencia() == null) {
                    listAC.get(i).setTipoentidad(new TiposEntidades());
                } else if (listAC.get(i).getEmpleado().getSecuencia() == null) {
                    listAC.get(i).setEmpleado(null);
                }else if (listAC.get(i).getTerceroRegistro().getSecuencia() == null) {
                    listAC.get(i).setTercero(null);
                } else if (listAC.get(i).getTerceroRegistro().getSecuencia() != null) {
                    listAC.get(i).setTercero(listAC.get(i).getTerceroRegistro().getSecuencia());
                }
                persistenciaAportesCorrecciones.borrar(em, listAC.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error editarAportesCorrecciones Admi : " + e.toString());
        }
    }

    @Override
    public List<AportesCorrecciones> consultarAportesCorrecciones() {
        try {
            List<AportesCorrecciones> lista = persistenciaAportesCorrecciones.consultarAportesEntidades(em);
             if (lista != null) {
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
            
            return lista;
        } catch (Exception e) {
            System.out.println("Error consultarAportesCorrecciones Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<AportesCorrecciones> consultarLovAportesCorrecciones() {
        try {
            List<AportesCorrecciones> lista = persistenciaAportesCorrecciones.consultarAportesEntidades(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error consultarAportesCorrecciones Admi : " + e.toString());
            return null;
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
    public void borrarAportesCorreccionesProcesoAutomatico(BigInteger empresa, short mes, short ano) {
        try {
            persistenciaAportesCorrecciones.borrarAportesCorreccionesProcesoAutomatico(em, empresa, mes, ano);
        } catch (Exception e) {
            System.out.println("Error borrarAportesCorreccionesProcesoAutomatico Admi : " + e.toString());
        }
    }

    @Override
    public String ejecutarPKGActualizarNovedadesCorreccion(short ano, short mes, BigInteger secuenciaEmpresa) {
       try {
            String proceso = persistenciaAportesCorrecciones.ejecutarPKGActualizarNovedadesCorreccion(em, secuenciaEmpresa, mes, ano);
            return proceso;
        } catch (Exception e) {
            System.out.println("Error ejecutarPKGActualizarNovedadesCorreccion Admi : " + e.toString());
            return "ERROR_ADMINISTRAR";
        }
    }

    @Override
    public String ejecutarPKGInsertarCorreccion(Date fechaIni, Date fechaFin, BigInteger secTipoTrabajador, BigInteger secuenciaEmpresa) {
        try {
            String proceso = persistenciaAportesCorrecciones.ejecutarPKGInsertarCorreccion(em, fechaIni, fechaFin, secTipoTrabajador, secuenciaEmpresa);
            return proceso;
        } catch (Exception e) {
            System.out.println("Error ejecutarPKGInsertarCorreccion Admi : " + e.toString());
            return "ERROR_ADMINISTRAR";
        }
    }

    @Override
    public String ejecutarPKGIdentificaCorreccion(short ano, short mes, BigInteger secuenciaEmpresa) {
        try {
            String proceso = persistenciaAportesCorrecciones.ejecutarPKGIdentificaCorreccion(em, secuenciaEmpresa, mes, ano);
            return proceso;
        } catch (Exception e) {
            System.out.println("Error ejecutarPKGIdentificaCorreccion Admi : " + e.toString());
            return "ERROR_ADMINISTRAR";
        }
    }
}