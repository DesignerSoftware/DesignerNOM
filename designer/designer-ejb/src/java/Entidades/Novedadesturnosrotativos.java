package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "NOVEDADESTURNOSROTATIVOS")
public class Novedadesturnosrotativos implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigDecimal secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAINICIAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechainicial;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHAFINAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechafinal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "HORAINICIAL")
    private short horainicial;
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
    @Size(max = 1)
    @Column(name = "COMPENSATORIO")
    private String compensatorio;
    @Size(max = 1)
    @Column(name = "ROTATIVO")
    private String rotativo;
    @Size(max = 1)
    @Column(name = "EXTRAS")
    private String extras;
    @JoinColumn(name = "TURNOROTATIVO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Turnosrotativos turnorotativo;

    public Novedadesturnosrotativos() {
    }

    public Novedadesturnosrotativos(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public Novedadesturnosrotativos(BigDecimal secuencia, Date fechainicial, Date fechafinal, short horainicial, short minutoinicial, short horafinal, short minutofinal) {
        this.secuencia = secuencia;
        this.fechainicial = fechainicial;
        this.fechafinal = fechafinal;
        this.horainicial = horainicial;
        this.minutoinicial = minutoinicial;
        this.horafinal = horafinal;
        this.minutofinal = minutofinal;
    }

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public Date getFechainicial() {
        return fechainicial;
    }

    public void setFechainicial(Date fechainicial) {
        this.fechainicial = fechainicial;
    }

    public Date getFechafinal() {
        return fechafinal;
    }

    public void setFechafinal(Date fechafinal) {
        this.fechafinal = fechafinal;
    }

    public short getHorainicial() {
        return horainicial;
    }

    public void setHorainicial(short horainicial) {
        this.horainicial = horainicial;
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

    public String getCompensatorio() {
        return compensatorio;
    }

    public void setCompensatorio(String compensatorio) {
        this.compensatorio = compensatorio;
    }

    public String getRotativo() {
        return rotativo;
    }

    public void setRotativo(String rotativo) {
        this.rotativo = rotativo;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public Turnosrotativos getTurnorotativo() {
        return turnorotativo;
    }

    public void setTurnorotativo(Turnosrotativos turnorotativo) {
        this.turnorotativo = turnorotativo;
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
        if (!(object instanceof Novedadesturnosrotativos)) {
            return false;
        }
        Novedadesturnosrotativos other = (Novedadesturnosrotativos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Novedadesturnosrotativos[ secuencia=" + secuencia + " ]";
    }
    
}
