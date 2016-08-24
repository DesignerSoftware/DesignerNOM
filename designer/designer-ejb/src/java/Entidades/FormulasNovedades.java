package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "FORMULASNOVEDADES")
public class FormulasNovedades implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Size(max = 1)
    @Column(name = "SUGERIDA")
    private String sugerida;
    @Size(max = 1)
    @Column(name = "CARGUE")
    private String cargue;
    @Size(max = 1)
    @Column(name = "USAORDENFORMULACONCEPTO")
    private String usaordenformulaconcepto;
    @Size(max = 1)
    @Column(name = "GARANTIZADA")
    private String garantizada;
    @JoinColumn(name = "FORMULA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Formulas formula;
    @Transient
    private boolean checkSugerida;
    @Transient
    private boolean checkCargue;
    @Transient
    private boolean checkUsa;
    @Transient
    private boolean checkGarantiza;

    public FormulasNovedades() {
    }

    public FormulasNovedades(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getSugerida() {
        if (sugerida == null) {
            sugerida = "N";
        }
        return sugerida;
    }

    public void setSugerida(String sugerida) {
        this.sugerida = sugerida;
    }

    public String getCargue() {
        if (cargue == null) {
            cargue = "N";
        }
        return cargue;
    }

    public void setCargue(String cargue) {
        this.cargue = cargue;
    }

    public String getUsaordenformulaconcepto() {
        if (usaordenformulaconcepto == null) {
            usaordenformulaconcepto = "N";
        }
        return usaordenformulaconcepto;
    }

    public void setUsaordenformulaconcepto(String usaordenformulaconcepto) {
        this.usaordenformulaconcepto = usaordenformulaconcepto;
    }

    public String getGarantizada() {
        if (garantizada == null) {
            garantizada = "N";
        }
        return garantizada;
    }

    public void setGarantizada(String garantizada) {
        this.garantizada = garantizada;
    }

    public Formulas getFormula() {
        return formula;
    }

    public void setFormula(Formulas formula) {
        this.formula = formula;
    }

    public boolean isCheckSugerida() {
        getSugerida();
        if (sugerida == null || sugerida.equalsIgnoreCase("N")) {
            checkSugerida = false;
        } else {
            checkSugerida = true;
        }
        return checkSugerida;
    }

    public void setCheckSugerida(boolean checkSugerida) {
        if (checkSugerida == false) {
            sugerida = "N";
        } else {
            sugerida = "S";
        }
        this.checkSugerida = checkSugerida;
    }

    public boolean isCheckCargue() {
        getCargue();
        if (cargue == null || cargue.equalsIgnoreCase("N")) {
            checkCargue = false;
        } else {
            checkCargue = true;
        }
        return checkCargue;
    }

    public void setCheckCargue(boolean check) {
        if (check == false) {
            cargue = "N";
        } else {
            cargue = "S";
        }
        this.checkCargue = check;
    }

    public boolean isCheckUsa() {
        getUsaordenformulaconcepto();
        if (usaordenformulaconcepto == null || usaordenformulaconcepto.equalsIgnoreCase("N")) {
            checkUsa = false;
        } else {
            checkUsa = true;
        }
        return checkUsa;
    }

    public void setCheckUsa(boolean checkUsa) {
        if (checkUsa == false) {
            usaordenformulaconcepto = "N";
        } else {
            usaordenformulaconcepto = "S";
        }
        this.checkUsa = checkUsa;
    }

    public boolean isCheckGarantiza() {
        getGarantizada();
        if (garantizada == null || garantizada.equalsIgnoreCase("N")) {
            checkGarantiza = false;
        } else {
            checkGarantiza = true;
        }
        return checkGarantiza;
    }

    public void setCheckGarantiza(boolean checkGarantiza) {
        if (checkGarantiza == false) {
            garantizada = "N";
        } else {
            garantizada = "S";
        }
        this.checkGarantiza = checkGarantiza;
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
        if (!(object instanceof FormulasNovedades)) {
            return false;
        }
        FormulasNovedades other = (FormulasNovedades) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Formulasnovedades[ secuencia=" + secuencia + " ]";
    }

}
