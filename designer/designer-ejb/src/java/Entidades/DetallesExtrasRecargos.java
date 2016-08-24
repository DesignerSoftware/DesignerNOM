package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author PROYECTO01
 */
@Entity
@Table(name = "DETALLESEXTRASRECARGOS")
public class DetallesExtrasRecargos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "HORASFINAL")
    private BigDecimal horasfinal;
    @Column(name = "HORASINICIAL")
    private BigDecimal horasinicial;
    @Size(max = 3)
    @Column(name = "DIA")
    private String dia;
    @Size(max = 1)
    @Column(name = "ADICIONAL")
    private String adicional;
    @Column(name = "MINIMOMINUTOSPAGAR")
    private BigInteger minimominutospagar;
    @Size(max = 1)
    @Column(name = "PAGOTIEMPOCOMPLETO")
    private String pagotiempocompleto;
    @Size(max = 3)
    @Column(name = "ADICIONALINICIOFINJORNADA")
    private String adicionaliniciofinjornada;
    @Size(max = 1)
    @Column(name = "GARANTIZAADICIONAL")
    private String garantizaadicional;
    @JoinColumn(name = "EXTRARECARGO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private ExtrasRecargos extrarecargo;
    @JoinColumn(name = "CONCEPTO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Conceptos concepto;
    @Transient
    private boolean checkAdicional;
    @Transient
    private boolean checkPagoTiempo;
    @Transient
    private boolean checkGarantizaAdicional;

    public DetallesExtrasRecargos() {
    }

    public DetallesExtrasRecargos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public DetallesExtrasRecargos(BigInteger secuencia, BigDecimal horasfinal, BigDecimal horasinicial) {
        this.secuencia = secuencia;
        this.horasfinal = horasfinal;
        this.horasinicial = horasinicial;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigDecimal getHorasfinal() {
        return horasfinal;
    }

    public void setHorasfinal(BigDecimal horasfinal) {
        this.horasfinal = horasfinal;
    }

    public BigDecimal getHorasinicial() {
        return horasinicial;
    }

    public void setHorasinicial(BigDecimal horasinicial) {
        this.horasinicial = horasinicial;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getAdicional() {
        if (adicional == null) {
            adicional = "N";
        }
        return adicional;
    }

    public void setAdicional(String adicional) {
        this.adicional = adicional;
    }

    public BigInteger getMinimominutospagar() {
        return minimominutospagar;
    }

    public void setMinimominutospagar(BigInteger minimominutospagar) {
        this.minimominutospagar = minimominutospagar;
    }

    public String getPagotiempocompleto() {
        if (pagotiempocompleto == null) {
            pagotiempocompleto = "N";
        }
        return pagotiempocompleto;
    }

    public void setPagotiempocompleto(String pagotiempocompleto) {
        this.pagotiempocompleto = pagotiempocompleto;
    }

    public String getAdicionaliniciofinjornada() {
        return adicionaliniciofinjornada;
    }

    public void setAdicionaliniciofinjornada(String adicionaliniciofinjornada) {
        this.adicionaliniciofinjornada = adicionaliniciofinjornada;
    }

    public ExtrasRecargos getExtrarecargo() {
        return extrarecargo;
    }

    public void setExtrarecargo(ExtrasRecargos extrarecargo) {
        this.extrarecargo = extrarecargo;
    }

    public Conceptos getConcepto() {
        return concepto;
    }

    public void setConcepto(Conceptos concepto) {
        this.concepto = concepto;
    }

    public boolean isCheckAdicional() {
        if (adicional == null || adicional.equalsIgnoreCase("N")) {
            checkAdicional = false;
        } else {
            checkAdicional = true;
        }
        return checkAdicional;
    }

    public void setCheckAdicional(boolean checkAdicional) {
        if (checkAdicional == false) {
            adicional = "N";
        } else {
            adicional = "S";
        }
        this.checkAdicional = checkAdicional;
    }

    public boolean isCheckPagoTiempo() {
        if (pagotiempocompleto == null || pagotiempocompleto.equalsIgnoreCase("N")) {
            checkPagoTiempo = false;
        } else {
            checkPagoTiempo = true;
        }
        return checkPagoTiempo;
    }

    public void setCheckPagoTiempo(boolean checkPagoTiempo) {
        if (checkPagoTiempo == false) {
            pagotiempocompleto = "N";
        } else {
            pagotiempocompleto = "S";
        }
        this.checkPagoTiempo = checkPagoTiempo;
    }

    public boolean isCheckGarantizaAdicional() {
        if (garantizaadicional == null || garantizaadicional.equalsIgnoreCase("N")) {
            checkGarantizaAdicional = false;
        } else {
            checkGarantizaAdicional = true;
        }
        return checkGarantizaAdicional;
    }

    public void setCheckGarantizaAdicional(boolean checkGarantizaAdicional) {
        if (checkGarantizaAdicional == false) {
            garantizaadicional = "N";
        } else {
            garantizaadicional = "S";
        }
        this.checkGarantizaAdicional = checkGarantizaAdicional;
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
        if (!(object instanceof DetallesExtrasRecargos)) {
            return false;
        }
        DetallesExtrasRecargos other = (DetallesExtrasRecargos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.DetallesExtrasRecargos[ secuencia=" + secuencia + " ]";
    }

    public String getGarantizaadicional() {
        if (garantizaadicional == null) {
            garantizaadicional = "N";
        }
        return garantizaadicional;
    }

    public void setGarantizaadicional(String garantizaadicional) {
        this.garantizaadicional = garantizaadicional;
    }
}
