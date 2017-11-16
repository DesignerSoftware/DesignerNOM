package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
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
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODIGO")
    private Integer codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MINUTOINICIAL")
    private Integer minutoinicial;
    @Basic(optional = false)
    @NotNull
    @Column(name = "HORAFINAL")
    private Integer horafinal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MINUTOFINAL")
    private Integer minutofinal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "HORAINICIAL")
    private Integer horainicial;

    public TiposJornadas() {
    }

    public TiposJornadas(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public TiposJornadas(BigInteger secuencia, Integer codigo, String descripcion, Integer minutoinicial, Integer horafinal, Integer minutofinal, Integer horainicial) {
        this.secuencia = secuencia;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.minutoinicial = minutoinicial;
        this.horafinal = horafinal;
        this.minutofinal = minutofinal;
        this.horainicial = horainicial;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getMinutoinicial() {
        return minutoinicial;
    }

    public void setMinutoinicial(Integer minutoinicial) {
        this.minutoinicial = minutoinicial;
    }

    public Integer getHorafinal() {
        return horafinal;
    }

    public void setHorafinal(Integer horafinal) {
        this.horafinal = horafinal;
    }

    public Integer getMinutofinal() {
        return minutofinal;
    }

    public void setMinutofinal(Integer minutofinal) {
        this.minutofinal = minutofinal;
    }

    public Integer getHorainicial() {
        return horainicial;
    }

    public void setHorainicial(Integer horainicial) {
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
