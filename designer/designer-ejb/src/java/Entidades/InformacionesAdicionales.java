package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "INFORMACIONESADICIONALES")
public class InformacionesAdicionales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Size(max = 50)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "FECHAFINAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechafinal;
    @Column(name = "FECHAINICIAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechainicial;
    @Size(min = 1, max = 1)
    @Column(name = "TIPODATO")
    private String tipodato;
    @Transient
    private String tipodatoTr;
    @Column(name = "RESULTADONUMERICO")
    private Long resultadonumerico;
    @Size(max = 100)
    @Column(name = "RESULTADOCARACTER")
    private String resultadocaracter;
    @Column(name = "RESULTADOFECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date resultadofecha;
    @JoinColumn(name = "GRUPO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private GruposInfAdicionales grupo;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empleados empleado;
//    @Transient
//    private String tipoDatoCompleto;

    public InformacionesAdicionales() {
    }

    public InformacionesAdicionales(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public InformacionesAdicionales(BigInteger secuencia, Date fechafinal, Date fechainicial, String tipodato) {
        this.secuencia = secuencia;
        this.fechafinal = fechafinal;
        this.fechainicial = fechainicial;
        this.tipodato = tipodato;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechafinal() {
        return fechafinal;
    }

    public void setFechafinal(Date fechafinal) {
        this.fechafinal = fechafinal;
    }

    public Date getFechainicial() {
        return fechainicial;
    }

    public void setFechainicial(Date fechainicial) {
        this.fechainicial = fechainicial;
    }

    public String getTipodato() {
        return tipodato;
    }

    public void setTipodato(String tipodato) {
        this.tipodato = tipodato;
    }

    public Long getResultadonumerico() {
        return resultadonumerico;
    }

    public void setResultadonumerico(Long resultadonumerico) {
        this.resultadonumerico = resultadonumerico;
    }

    public String getResultadocaracter() {
        return resultadocaracter;
    }

    public void setResultadocaracter(String resultadocaracter) {
        this.resultadocaracter = resultadocaracter;
    }

    public Date getResultadofecha() {
        return resultadofecha;
    }

    public void setResultadofecha(Date resultadofecha) {
        this.resultadofecha = resultadofecha;
    }

    public GruposInfAdicionales getGrupo() {
        return grupo;
    }

    public void setGrupo(GruposInfAdicionales grupo) {
        this.grupo = grupo;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public String getTipodatoTr() {
        if (getTipodato().equalsIgnoreCase("C")) {
            tipodatoTr = "CARACTER";
        }
        if (getTipodato().equalsIgnoreCase("F")) {
            tipodatoTr = "FECHA";
        }
        if (getTipodato().equalsIgnoreCase("N")) {
            tipodatoTr = "NUMERICO";
        }
        return tipodatoTr;
    }

    public void setTipodatoTr(String tipodatotr) {

        this.tipodatoTr = tipodatotr;
        if (tipodatoTr.equals("CARACTER")) {
            tipodato = "C";
        }
        if (tipodatoTr.equals("FECHA")) {
            tipodato = "F";
        }
        if (tipodatoTr.equals("NUMERICO")) {
            tipodato = "N";
        }
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
        if (!(object instanceof InformacionesAdicionales)) {
            return false;
        }
        InformacionesAdicionales other = (InformacionesAdicionales) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.InformacionesAdicionales[ secuencia=" + secuencia + " ]";
    }
}
