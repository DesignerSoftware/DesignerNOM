/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.TiposTrabajadores;
import Entidades.VigenciasDiasTT;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarTiposTrabajadoresInterface {

   public void obtenerConexion(String idSesion);

   public List<TiposTrabajadores> buscarTiposTrabajadoresTT();

   public void crearTT(TiposTrabajadores tiposTrabajadores);

   public void editarTT(TiposTrabajadores tiposTrabajadores);

   public void borrarTT(TiposTrabajadores tiposTrabajadores);

   public TiposTrabajadores buscarTipoTrabajadorSecuencia(BigInteger secuencia);

   public TiposTrabajadores buscarTipoTrabajadorCodigoTiposhort(short codigo);

   public void crearVD(VigenciasDiasTT vigenciasDiasTT);

   public void editarVD(VigenciasDiasTT vigenciasDiasTT);

   public void borrarVD(VigenciasDiasTT vigenciasDiasTT);

   public List<VigenciasDiasTT> consultarDiasPorTipoT(BigInteger secuenciaTT);

   public String clonarTT(String nombreNuevo, Short codigoNuevo, Short codClonado);

   public boolean hayRegistrosSecundarios(BigInteger secuencia);
}
