package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author PROYECTO01
 */
@Entity
@Table(name = "TIPOSJORNADAS")
public class TiposJornadas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigDecimal secuencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "CODIGO")
    private Short codigo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MINUTOINICIAL")
    private short minutoinicial;
    @Basic(optional = false)
    @NotNull
    @Column(name = "HORAFINAL")
    private short horafinal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MINUTOFINAL")
    private short minutofinal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "HORAINICIAL")
    private short horainicial;

    public TiposJornadas() {
    }

    public TiposJornadas(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public TiposJornadas(BigDecimal secuencia, String descripcion, short minutoinicial, short horafinal, short minutofinal, short horainicial) {
        this.secuencia = secuencia;
        this.descripcion = descripcion;
        this.minutoinicial = minutoinicial;
        this.horafinal = horafinal;
        this.minutofinal = minutofinal;
        this.horainicial = horainicial;
    }

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public String getDescripcion() {
        if (descripcion == null) {
            descripcion = " ";
        }
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Short getCodigo() {
        return codigo;
    }

    public void setCodigo(Short codigo) {
        this.codigo = codigo;
    }

    public short getMinutoinicial() {
        return minutoinicial;
    }

    public void setMinutoinicial(short minutoinicial) {
        this.minutoinicial = minutoinicial;
    }

    public short getHorafinal() {
        return horafinal;
    }

    public void setHorafinal(short horafinal) {
        this.horafinal = horafinal;
    }

    public short getMinutofinal() {
        return minutofinal;
    }

    public void setMinutofinal(short minutofinal) {
        this.minutofinal = minutofinal;
    }

    public short getHorainicial() {
        return horainicial;
    }

    public void setHorainicial(short horainicial) {
        this.horainicial = horainicial;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secuencia != null ? secuencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TiposJornadas)) {
            return false;
        }
        TiposJornadas other = (TiposJornadas) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Tiposjornadas[ secuencia=" + secuencia + " ]";
    }

}
