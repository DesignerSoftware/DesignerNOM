/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Entidades.TarifaDeseo;
import InterfacePersistencia.PersistenciaTarifaDeseoInterface;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import javax.persistence.Query;

/**
 *
 * @author user
 */
@Stateless
public class PersistenciaTarifaDeseo implements PersistenciaTarifaDeseoInterface {

   private static Logger log = Logger.getLogger(PersistenciaTarifaDeseo.class);

    @Override
    public List<TarifaDeseo> retenciones(EntityManager em) {
        try {
            em.clear();
            String sqlQuery = ("select rownum id,rm.retencion retencion,rm.secuencia secuenciaretencion, vrm.fechavigencia vigencia,\n"
                    + "to_char(\n"
                    + "retenciones_pkg.uvt(sysdate)*decode(rm.mensualizado,0,0,\n"
                    + "decode(rm.retencion,0,99999,rm.retencion)),'999,999,999,999') equivalencia,\n"
                    + "to_char(\n"
                    + "retenciones_pkg.uvt(sysdate)*decode(rm.mensualizado,0,0,\n"
                    + "decode(rm.retencion,0,99999,rm.mensualizado)),'999,999,999,999') ingresos\n"
                    + "from retencionesminimas rm,vigenciasretencionesminimas vrm \n"
                    + "where rm.vigenciaretencionminima = vrm.secuencia \n"
                    + "and vrm.fechavigencia=(select max(vrmi.fechavigencia)\n"
                    + "from vigenciasretencionesminimas vrmi where vrmi.fechavigencia<=nvl('31129999',sysdate))\n"
                    + "order by decode(rm.mensualizado,0,0,decode(rm.retencion,0,9999999,rm.retencion))");
            Query query = em.createNativeQuery(sqlQuery, "TarifaDeseoRetencionMinima");
            List<TarifaDeseo> resultado = query.getResultList();
            return resultado;

        } catch (Exception e) {
            log.error("Error: ( Persistencia Tarifa Deseo) ", e);
            return null;
        }
    }
}
