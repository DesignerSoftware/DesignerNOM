/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.TempProrrateos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaTempProrrateosInterface {

   public void crear(EntityManager em, TempProrrateos tempProrrateos);

   public void editar(EntityManager em, TempProrrateos tempProrrateos);

   public void borrar(EntityManager em, TempProrrateos tempProrrateos);

   public void borrarRegistrosTempProrrateos(EntityManager em, String usuarioBD);

   public List<TempProrrateos> obtenerTempProrrateos(EntityManager em, String usuarioBD);

   public List<String> obtenerDocumentosSoporteCargados(EntityManager em, String usuarioBD);

   public void cargarTempProrrateos(EntityManager em, String fechaInicial, BigInteger secEmpresa);

   public void reversarTempProrrateos(EntityManager em, String documentoSoporte);
}
