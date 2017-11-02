/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.TempProrrateos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaTempProrrateosInterface {

   public void crear(EntityManager em, TempProrrateos tempAusentismos);

   public void editar(EntityManager em, TempProrrateos tempAusentismos);

   public void borrar(EntityManager em, TempProrrateos tempAusentismos);

   public void borrarRegistrosTempProrrateos(EntityManager em, String usuarioBD);

   public List<TempProrrateos> obtenerTempProrrateos(EntityManager em, String usuarioBD);

   public List<String> obtenerDocumentosSoporteCargados(EntityManager em);

//   public void cargarTempProrrateos(EntityManager em, String fechaInicial, BigInteger secEmpresa);
   public void cargarTempProrrateos(EntityManager em);

   public int reversarTempProrrateos(EntityManager em, String usuario, String documentoSoporte);

   public boolean verificarCodigoCentroCosto_Empresa(EntityManager em, BigInteger codigoCC, BigInteger secEmpresa);
   
   public boolean verificarCodigoProyecto_Empresa(EntityManager em, String codigoProy, BigInteger secEmpresa);
}
