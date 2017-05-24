/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.TempProrrateosProy;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaTempProrrateosProyInterface {

   public void crear(EntityManager em, TempProrrateosProy tempAusentismos);

   public void editar(EntityManager em, TempProrrateosProy tempAusentismos);

   public void borrar(EntityManager em, TempProrrateosProy tempAusentismos);

   public void borrarRegistrosTempProrrateosProy(EntityManager em, String usuarioBD);

   public List<TempProrrateosProy> obtenerTempProrrateosProy(EntityManager em, String usuarioBD);

   public List<String> obtenerDocumentosSoporteCargados(EntityManager em, String usuarioBD);

   public void cargarTempProrrateosProy(EntityManager em, String fechaInicial, BigInteger secEmpresa);

   public void reversarTempProrrateosProy(EntityManager em, String documentoSoporte);
}
