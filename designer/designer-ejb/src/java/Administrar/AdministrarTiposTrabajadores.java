/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposTrabajadores;
import Entidades.VigenciasDiasTT;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarTiposTrabajadoresInterface;
import InterfacePersistencia.PersistenciaTiposTrabajadoresInterface;
import InterfacePersistencia.PersistenciaVigenciasDiasTTInterface;
import Persistencia.PersistenciaVigenciasDiasTT;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarTiposTrabajadores implements AdministrarTiposTrabajadoresInterface {

   @EJB
   PersistenciaTiposTrabajadoresInterface persistenciaTiposTrabajadores;
   @EJB
   PersistenciaVigenciasDiasTTInterface persistenciaVigenciasDiasTT;
   @EJB
   AdministrarSesionesInterface administrarSesiones;
   private EntityManager em;

   @Override
   public void obtenerConexion(String idSesion) {
      System.out.println("AdministrarTiposTrabajadores: entro en obtenerConexion");
      em = administrarSesiones.obtenerConexionSesion(idSesion);
   }

   @Override
   public void crearTT(TiposTrabajadores tiposTrabajadores) {
      try {
         persistenciaTiposTrabajadores.crear(em, tiposTrabajadores);
      } catch (Exception e) {
         System.err.println("AdministrarTiposTrabajadores.crearTT ERROR: " + e);
      }
   }

   @Override
   public void editarTT(TiposTrabajadores tiposTrabajadores) {
      try {
         persistenciaTiposTrabajadores.editar(em, tiposTrabajadores);
      } catch (Exception e) {
         System.err.println("AdministrarTiposTrabajadores.editarTT ERROR: " + e);
      }
   }

   @Override
   public void borrarTT(TiposTrabajadores tiposTrabajadores) {
      try {
         persistenciaTiposTrabajadores.borrar(em, tiposTrabajadores);
      } catch (Exception e) {
         System.err.println("AdministrarTiposTrabajadores.borrarTT ERROR: " + e);
      }
   }

   @Override
   public List<TiposTrabajadores> buscarTiposTrabajadoresTT() {
      System.out.println("entro en buscarTiposTrabajadoresTT()");
      try {
         List<TiposTrabajadores> lista = persistenciaTiposTrabajadores.buscarTiposTrabajadores(em);
         return lista;
      } catch (Exception e) {
         System.err.println("AdministrarTiposTrabajadores.buscarTiposTrabajadoresTT ERROR: " + e);
         return null;
      }
   }

   @Override
   public TiposTrabajadores buscarTipoTrabajadorSecuencia(BigInteger secuencia) {
      try {
         TiposTrabajadores tipoT;
         tipoT = persistenciaTiposTrabajadores.buscarTipoTrabajadorSecuencia(em, secuencia);
         return tipoT;
      } catch (Exception e) {
         System.err.println("AdministrarTiposTrabajadores.buscarTipoTrabajadorSecuencia ERROR: " + e);
         return null;
      }
   }

   @Override
   public TiposTrabajadores buscarTipoTrabajadorCodigoTiposhort(short codigo) {
      try {
         TiposTrabajadores tipoT;
         tipoT = persistenciaTiposTrabajadores.buscarTipoTrabajadorCodigoTiposhort(em, codigo);
         return tipoT;
      } catch (Exception e) {
         System.err.println("AdministrarTiposTrabajadores.buscarTipoTrabajadorCodigoTiposhort ERROR: " + e);
         return null;
      }
   }

   // Vigencias Dias Tipos Trabajadores: //
   @Override
   public void crearVD(VigenciasDiasTT vigenciasDiasTT) {
      try {
         persistenciaVigenciasDiasTT.crear(em, vigenciasDiasTT);
      } catch (Exception e) {
         System.err.println("AdministrarTiposTrabajadores.crearVD ERROR: " + e);
      }
   }

   @Override
   public void editarVD(VigenciasDiasTT vigenciasDiasTT) {
      try {
         persistenciaVigenciasDiasTT.editar(em, vigenciasDiasTT);
      } catch (Exception e) {
         System.err.println("AdministrarTiposTrabajadores.editarVD ERROR: " + e);
      }
   }

   @Override
   public void borrarVD(VigenciasDiasTT vigenciasDiasTT) {
      persistenciaVigenciasDiasTT.borrar(em, vigenciasDiasTT);
      try {
      } catch (Exception e) {
         System.err.println("AdministrarTiposTrabajadores.borrarVD ERROR: " + e);
      }
   }

   @Override
   public List<VigenciasDiasTT> consultarDiasPorTipoT(BigInteger secuenciaTT) {
      try {
         List<VigenciasDiasTT> listaVDias = persistenciaVigenciasDiasTT.consultarDiasPorTT(em, secuenciaTT);
         return listaVDias;
      } catch (Exception e) {
         System.err.println("AdministrarTiposTrabajadores.consultarDiasPorTipoT ERROR: " + e);
         return null;
      }
   }

   @Override
   public String clonarTT(String nuevoNombre, Short nuevoCodigo, Short codClonado) {
      try {
         return persistenciaTiposTrabajadores.clonarTipoT(em, nuevoNombre, nuevoCodigo, codClonado);
      } catch (Exception e) {
         System.err.println("AdministrarTiposTrabajadores.clonarTT ERROR : " + e);
         return ("ERROR AdministrarTiposTrabajadores.clonarTT");
      }
   }

   public boolean hayRegistrosSecundarios(BigInteger secuencia) {
      try {
         return persistenciaTiposTrabajadores.hayRegistrosSecundarios(em, secuencia);
      } catch (Exception e) {
         System.err.println("AdministrarTiposTrabajadores.hayRegistrosSecundarios ERROR : " + e);
         return false;
      }
   }

}
