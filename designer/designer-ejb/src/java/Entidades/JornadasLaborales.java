package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "JORNADASLABORALES")
public class JornadasLaborales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "CODIGO")
    private Short codigo;
    @Basic(optional = false)
    @NotNull
    @Size(max = 30)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "HORASDIARIAS")
    private BigDecimal horasdiarias;
    @Column(name = "HORASMENSUALES")
    private Short horasmensuales;
    @Size(max = 1)
    @Column(name = "ROTATIVO")
    private String rotativo;
    @Size(max = 1)
    @Column(name = "TURNORELATIVO")
    private String turnorelativo;
    @Column(name = "CUADRILLAHORASDIARIAS")
    private BigInteger cuadrillahorasdiarias;
    @JoinColumn(name = "JORNADA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Jornadas jornada;
    @Transient
    private boolean estadoRotativo;
    @Transient
    private boolean estadoTurnoRelativo;

    public JornadasLaborales() {
    }

    public JornadasLaborales(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public JornadasLaborales(BigInteger secuencia, String descripcion) {
        this.secuencia = secuencia;
        this.descripcion = descripcion;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Short getCodigo() {
        return codigo;
    }

    public void setCodigo(Short codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        if (descripcion == null) {
            descripcion = "";
        }
        return descripcion.toUpperCase();
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getHorasdiarias() {
        return horasdiarias;
    }

    public void setHorasdiarias(BigDecimal horasdiarias) {
        this.horasdiarias = horasdiarias;
    }

    public Short getHorasmensuales() {
        return horasmensuales;
    }

    public void setHorasmensuales(Short horasmensuales) {
        this.horasmensuales = horasmensuales;
    }

    public String getRotativo() {
        return rotativo;
    }

    public void setRotativo(String rotativo) {
        this.rotativo = rotativo;
    }

    public String getTurnorelativo() {
        return turnorelativo;
    }

    public void setTurnorelativo(String turnorelativo) {
        this.turnorelativo = turnorelativo;
    }

    public BigInteger getCuadrillahorasdiarias() {
        return cuadrillahorasdiarias;
    }

    public void setCuadrillahorasdiarias(BigInteger cuadrillahorasdiarias) {
        this.cuadrillahorasdiarias = cuadrillahorasdiarias;
    }

    public boolean isEstadoRotativo() {
        if (rotativo != null) {
            if (rotativo.equals("S")) {
                estadoRotativo = true;
            } else {
                estadoRotativo = false;
            }
        } else {
            estadoRotativo = false;
        }
        return estadoRotativo;
    }

    public void setEstadoRotativo(boolean estadoRotativo) {
        if (estadoRotativo == true) {
            rotativo = "S";
        } else {
            rotativo = "N";
        }
        this.estadoRotativo = estadoRotativo;
    }

    public boolean isEstadoTurnoRelativo() {
        if (turnorelativo != null) {
            if (turnorelativo.equals("S")) {
                estadoTurnoRelativo = true;
            } else {
                estadoTurnoRelativo = false;
            }
        } else {
            estadoTurnoRelativo = false;
        }
        return estadoTurnoRelativo;
    }

    public void setEstadoTurnoRelativo(boolean estadoTurnoRelativo) {
        if (estadoTurnoRelativo == true) {
            turnorelativo = "S";
        } else {
            turnorelativo = "N";
        }
        this.estadoTurnoRelativo = estadoTurnoRelativo;
    }

    public Jornadas getJornada() {
        if (jornada == null) {
            jornada = new Jornadas();
        }
        return jornada;
    }

    public void setJornada(Jornadas jornada) {
        this.jornada = jornada;
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
        if (!(object instanceof JornadasLaborales)) {
            return false;
        }
        JornadasLaborales other = (JornadasLaborales) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Jornadaslaborales[ secuencia=" + secuencia + " ]";
    }

}
