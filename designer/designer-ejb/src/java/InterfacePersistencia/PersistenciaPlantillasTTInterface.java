/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.Contratos;
import Entidades.NormasLaborales;
import Entidades.ReformasLaborales;
import Entidades.TiposContratos;
import Entidades.TiposSueldos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaPlantillasTTInterface {

    public List<ReformasLaborales> consultarReformasLaboralesValidas(EntityManager em, BigInteger secTipoT);

    public List<TiposSueldos> consultarTiposSueldosValidos(EntityManager em, BigInteger secTipoT);

    public List<TiposContratos> consultarTiposContratosValidos(EntityManager em, BigInteger secTipoT);

    public List<NormasLaborales> consultarNormasLaboralesValidas(EntityManager em, BigInteger secTipoT);

    public List<Contratos> consultarContratosValidos(EntityManager em, BigInteger secTipoT);

}
