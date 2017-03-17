/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.TempSoAusentismos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaTempSoAusentismosInterface {

    public void crear(EntityManager em, TempSoAusentismos tempAusentismos);

    public void editar(EntityManager em, TempSoAusentismos tempAusentismos);

    public void borrar(EntityManager em, TempSoAusentismos tempAusentismos);
    
    public void borrarRegistrosTempNovedades(EntityManager em, String usuarioBD);

    public List<TempSoAusentismos> obtenerTempAusentismos(EntityManager em, String usuarioBD);

    public List<String> obtenerDocumentosSoporteCargados(EntityManager em, String usuarioBD);

    public void cargarTempAusentismos(EntityManager em, String fechaInicial, BigInteger secEmpresa);

    public void reversarTempAusentismos(EntityManager em, String usuarioBD, String documentoSoporte);

}
