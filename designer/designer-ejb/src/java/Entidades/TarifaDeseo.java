package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author user
 */
@Entity
@SqlResultSetMapping(
        name = "TarifaDeseoRetencionMinima",
        entities = {
            @EntityResult(
                    entityClass = TarifaDeseo.class,
                    fields = {
                        @FieldResult(name = "retencion", column = "RETENCION"),
                        @FieldResult(name = "id", column = "ID"),
                        @FieldResult(name = "vigencia", column = "VIGENCIA"),
                        @FieldResult(name = "secuenciaretencion", column = "SECUENCIARETENCION"),
                        @FieldResult(name = "equivalencia", column = "EQUIVALENCIA"),
                        @FieldResult(name = "ingresos", column = "INGRESOS"),}
            )
        }
)
public class TarifaDeseo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;
    private BigInteger secuenciaRetencion;
    private Integer retencion;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date vigencia;
    private String equivalencia;
    private String ingresos;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public Integer getRetencion() {
        return retencion;
    }

    public void setRetencion(Integer retencion) {
        this.retencion = retencion;
    }

    public Date getVigencia() {
        return vigencia;
    }

    public void setVigencia(Date vigencia) {
        this.vigencia = vigencia;
    }

    public String getEquivalencia() {
        return equivalencia;
    }

    public void setEquivalencia(String equivalencia) {
        this.equivalencia = equivalencia;
    }

    public String getIngresos() {
        return ingresos;
    }

    public void setIngresos(String ingresos) {
        this.ingresos = ingresos;
    }

    public BigInteger getSecuenciaRetencion() {
        return secuenciaRetencion;
    }

    public void setSecuenciaRetencion(BigInteger secuenciaRetencion) {
        this.secuenciaRetencion = secuenciaRetencion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TarifaDeseo)) {
            return false;
        }
        TarifaDeseo other = (TarifaDeseo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.TarifaDeseo[ id=" + id + " ]";
    }

}
