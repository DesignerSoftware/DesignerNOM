package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Entity
@Table(name = "FORMULASCONTRATOS")
public class Formulascontratos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "FECHAINICIAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechainicial;
    @Column(name = "FECHAFINAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechafinal;
    @JoinColumn(name = "TERCERO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Terceros tercero;
    @JoinColumn(name = "PERIODICIDAD", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Periodicidades periodicidad;
    @JoinColumn(name = "FORMULA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Formulas formula;
    @JoinColumn(name = "CONTRATO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Contratos contrato;
    @Transient
    private String strFechaIni;
    @Transient
    private String strFechaFin;
    @Transient
    private String strConcepto;

    public Formulascontratos() {
    }

    public Formulascontratos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Formulascontratos(BigInteger secuencia, Date fechainicial) {
        this.secuencia = secuencia;
        this.fechainicial = fechainicial;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
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

    public String getStrFechaIni() {
        if (fechainicial != null) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            strFechaIni = formatoFecha.format(fechainicial);
        } else {
            strFechaIni = " ";
        }
        return strFechaIni;
    }

    public void setStrFechaIni(String strFechaIni) throws ParseException {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        fechainicial = formatoFecha.parse(strFechaIni);
        this.strFechaIni = strFechaIni;
    }

    public String getStrFechaFin() {
        if (fechafinal != null) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            strFechaFin = formatoFecha.format(fechafinal);
        } else {
            strFechaFin = " ";
        }
        return strFechaFin;
    }

    public void setStrFechaFin(String strFechaFin) throws ParseException {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        fechafinal = formatoFecha.parse(strFechaFin);
        this.strFechaFin = strFechaFin;
    }

    public Terceros getTercero() {
        return tercero;
    }

    public void setTercero(Terceros tercero) {
        this.tercero = tercero;
    }

    public Periodicidades getPeriodicidad() {
        return periodicidad;
    }

    public void setPeriodicidad(Periodicidades periodicidad) {
        this.periodicidad = periodicidad;
    }

    public Formulas getFormula() {
        return formula;
    }

    public void setFormula(Formulas formula) {
        this.formula = formula;
    }

    public Contratos getContrato() {
        return contrato;
    }

    public void setContrato(Contratos contrato) {
        this.contrato = contrato;
    }

    public String getStrConcepto() {
        return strConcepto;
    }

    public void setStrConcepto(String strConcepto) {
        this.strConcepto = strConcepto;
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
        if (!(object instanceof Formulascontratos)) {
            return false;
        }
        Formulascontratos other = (Formulascontratos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Formulascontratos[ secuencia=" + secuencia + " ]";
    }

}
