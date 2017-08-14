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
public class AdministrarParametrosCorreccionAutoL implements AdministrarParametrosCorreccionAutoLInterface {

   private static Logger log = Logger.getLogger(AdministrarParametrosCorreccionAutoL.class);

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
   private EntityManagerFactory emf;
   private EntityManager em;

   private EntityManager getEm() {
      try {
         if (this.em != null) {
            if (this.em.isOpen()) {
               this.em.close();
            }
         }
         this.em = emf.createEntityManager();
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " getEm() ERROR : " + e);
      }
      return this.em;
   }

   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<ParametrosCorreccionesAutoL> consultarParametrosCorreccionesAutoliq() {
      try {
         List<ParametrosCorreccionesAutoL> lista = persistenciaParametrosCorreccionAuto.consultarParametrosCorreccionesAutoL(getEm());
         return lista;
      } catch (Exception e) {
         log.warn("Error consultarParametrosAutoliq Admi : " + e.toString());
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
            persistenciaParametrosCorreccionAuto.crearCorreccion(getEm(), listaPCA.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearParametrosCorreccionAutoliq Admi : " + e.toString());
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
            persistenciaParametrosCorreccionAuto.editarCorreccion(getEm(), listaPCA.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarParametrosCorreccionAutoliq Admi : " + e.toString());
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
            persistenciaParametrosCorreccionAuto.borrarCorreccion(getEm(), listaPCA.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarParametrosCorreccionAutoliq Admi : " + e.toString());
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
            persistenciaAportesCorrecciones.crear(getEm(), listaAC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearAportesCorrecciones Admi : " + e.toString());
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
            } else if (listAC.get(i).getTerceroRegistro().getSecuencia() == null) {
               listAC.get(i).setTercero(null);
            } else if (listAC.get(i).getTerceroRegistro().getSecuencia() != null) {
               listAC.get(i).setTercero(listAC.get(i).getTerceroRegistro().getSecuencia());
            }
            persistenciaAportesCorrecciones.editar(getEm(), listAC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarAportesCorrecciones Admi : " + e.toString());
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
            } else if (listAC.get(i).getTerceroRegistro().getSecuencia() == null) {
               listAC.get(i).setTercero(null);
            } else if (listAC.get(i).getTerceroRegistro().getSecuencia() != null) {
               listAC.get(i).setTercero(listAC.get(i).getTerceroRegistro().getSecuencia());
            }
            persistenciaAportesCorrecciones.borrar(getEm(), listAC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarAportesCorrecciones Admi : " + e.toString());
      }
   }

   @Override
   public List<AportesCorrecciones> consultarAportesCorrecciones() {
      try {
         List<AportesCorrecciones> lista = persistenciaAportesCorrecciones.consultarAportesEntidades(getEm());
         if (lista != null) {
            for (int i = 0; i < lista.size(); i++) {
               if (lista.get(i).getTercero() != null) {
                  Terceros tercero = persistenciaTerceros.buscarTercerosSecuencia(getEm(), lista.get(i).getTercero());
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
         log.warn("Error consultarAportesCorrecciones Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<AportesCorrecciones> consultarLovAportesCorrecciones() {
      try {
         return persistenciaAportesCorrecciones.consultarAportesEntidades(getEm());
      } catch (Exception e) {
         log.warn("Error consultarAportesCorrecciones Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<TiposTrabajadores> lovTiposTrabajadores() {
      try {
         return persistenciaTiposTrabajadores.buscarTiposTrabajadores(getEm());
      } catch (Exception e) {
         log.warn("Error lovTiposTrabajadores Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Empleados> lovEmpleados() {
      try {
         return persistenciaEmpleados.consultarEmpleadosParametroAutoliq(getEm());
      } catch (Exception e) {
         log.warn("Error lovEmpleados Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<TiposEntidades> lovTiposEntidades() {
      try {
         return persistenciaTiposEntidades.buscarTiposEntidadesParametroAutoliq(getEm());
      } catch (Exception e) {
         log.warn("Error lovTiposEntidades Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Terceros> lovTerceros() {
      try {
         return persistenciaTerceros.buscarTercerosParametrosAutoliq(getEm());
      } catch (Exception e) {
         log.warn("Error lovTerceros Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Empresas> lovEmpresas() {
      try {
         return persistenciaEmpresas.buscarEmpresas(getEm());
      } catch (Exception e) {
         log.warn("Error lovEmpresas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public ActualUsuario obtenerActualUsuario() {
      try {
         return persistenciaActualUsuario.actualUsuarioBD(getEm());
      } catch (Exception e) {
         log.warn("Error obtenerActualUsuario Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public ParametrosInformes buscarParametroInforme(String usuario) {
      try {
         return persistenciaParametrosInformes.buscarParametroInformeUsuario(getEm(), usuario);
      } catch (Exception e) {
         log.warn("Error buscarParametroInforme Admi : " + e.toString());
         return null;
      }

   }

   @Override
   public void modificarParametroInforme(ParametrosInformes parametro) {
      try {
         persistenciaParametrosInformes.editar(getEm(), parametro);
      } catch (Exception e) {
         log.warn("Error modificarParametroInforme Admi : " + e.toString());
      }
   }

   @Override
   public ParametrosEstructuras buscarParametroEstructura(String usuario) {
      try {
         return persistenciaParametrosEstructuras.buscarParametro(getEm(), usuario);
      } catch (Exception e) {
         log.warn("Error buscarParametroEstructura Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void modificarParametroEstructura(ParametrosEstructuras parametro) {
      try {
         persistenciaParametrosEstructuras.editar(getEm(), parametro);
      } catch (Exception e) {
         log.warn("Error modificarParametroEstructura Admi : " + e.toString());
      }
   }

   @Override
   public void borrarAportesCorreccionesProcesoAutomatico(BigInteger empresa, short mes, short ano) {
      try {
         persistenciaAportesCorrecciones.borrarAportesCorreccionesProcesoAutomatico(getEm(), empresa, mes, ano);
      } catch (Exception e) {
         log.warn("Error borrarAportesCorreccionesProcesoAutomatico Admi : " + e.toString());
      }
   }

   @Override
   public String ejecutarPKGActualizarNovedadesCorreccion(short ano, short mes, BigInteger secuenciaEmpresa) {
      try {
         return persistenciaAportesCorrecciones.ejecutarPKGActualizarNovedadesCorreccion(getEm(), secuenciaEmpresa, mes, ano);
      } catch (Exception e) {
         log.warn("Error ejecutarPKGActualizarNovedadesCorreccion Admi : " + e.toString());
         return "ERROR_ADMINISTRAR";
      }
   }

   @Override
   public String ejecutarPKGInsertarCorreccion(Date fechaIni, Date fechaFin, BigInteger secTipoTrabajador, BigInteger secuenciaEmpresa) {
      try {
         return persistenciaAportesCorrecciones.ejecutarPKGInsertarCorreccion(getEm(), fechaIni, fechaFin, secTipoTrabajador, secuenciaEmpresa);
      } catch (Exception e) {
         log.warn("Error ejecutarPKGInsertarCorreccion Admi : " + e.toString());
         return "ERROR_ADMINISTRAR";
      }
   }

   @Override
   public String ejecutarPKGIdentificaCorreccion(short ano, short mes, BigInteger secuenciaEmpresa) {
      try {
         return persistenciaAportesCorrecciones.ejecutarPKGIdentificaCorreccion(getEm(), secuenciaEmpresa, mes, ano);
      } catch (Exception e) {
         log.warn("Error ejecutarPKGIdentificaCorreccion Admi : " + e.toString());
         return "ERROR_ADMINISTRAR";
      }
   }
}
