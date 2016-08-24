package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author Administrator
 */
@Entity
@SqlResultSetMapping(
    name="ConsultasLiquidacionesEmpresa",
    entities={
        @EntityResult(
            entityClass=ConsultasLiquidaciones.class,
            fields={
                @FieldResult(name="corte", column="CORTE"),
                @FieldResult(name="empresaCodigo", column="EMPRESACODIGO"),
                @FieldResult(name="Descripcion", column="PROCESO"),
                @FieldResult(name="total", column="TOTAL"),
                @FieldResult(name="observaciones", column="OBSERVACIONES")
            }
        )
    }
)
public class ConsultasLiquidaciones implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date corte;
    private String empresaCodigo;
    private String Descripcion;
    private BigInteger total;
    private String observaciones;
    

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public Date getCorte() {
        return corte;
    }

    public void setCorte(Date corte) {
        this.corte = corte;
    }

    public String getEmpresaCodigo() {
        return empresaCodigo;
    }

    public void setEmpresaCodigo(String empresaCodigo) {
        this.empresaCodigo = empresaCodigo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public BigInteger getTotal() {
        return total;
    }

    public void setTotal(BigInteger total) {
        this.total = total;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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
        if (!(object instanceof ConsultasLiquidaciones)) {
            return false;
        }
        ConsultasLiquidaciones other = (ConsultasLiquidaciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.ConsultasLiquidaciones[ id=" + id + " ]";
    }
    
}
