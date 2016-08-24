/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package InterfacePersistencia;

import Entidades.FormasDtos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;


public interface PersistenciaFormasDtosInterface {
    public void crear(EntityManager em,FormasDtos formasDtos);
    public void editar(EntityManager em,FormasDtos formasDtos);
    public void borrar(EntityManager em,FormasDtos formasDtos);
    public List<FormasDtos> formasDescuentos(EntityManager em,BigInteger tipoEmbargo);
}
