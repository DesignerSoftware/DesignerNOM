/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "PARAMETROSCONJUNTOS")
public class ParametrosConjuntos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigDecimal secuencia;
    @Column(name = "FECHADESDE")
    @Temporal(TemporalType.DATE)
    private Date fechaDesde;
    @Column(name = "FECHAHASTA")
    @Temporal(TemporalType.DATE)
    private Date fechaHasta;
    @Column(name = "FECHADESDELINEABASE")
    @Temporal(TemporalType.DATE)
    private Date fechaDesdeLineaBase;
    @Column(name = "FECHAHASTALINEABASE")
    @Temporal(TemporalType.DATE)
    private Date fechaHastaLineaBase;
    @Size(max = 2)
    @Column(name = "CONJUNTO")
    private int conjunto;
    @Size(max = 20)
    @Column(name = "ESTADO")
    private String estado;
    @Size(max = 100)
    @Column(name = "DIMENSION")
    private String dimension;
    @Size(max = 30)
    @Column(name = "USUARIOBD")
    private String usuarioBD;
    @Size(max = 3)
    @Column(name = "TIPORESUMEN")
    private String tipoResumen;
    @Size(max = 20)
    @Column(name = "ESTADOLINEABASE")
    private String estadolineaBase;

    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO1")
    private String tituloConjunto1;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO2")
    private String tituloConjunto2;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO3")
    private String tituloConjunto3;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO4")
    private String tituloConjunto4;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO5")
    private String tituloConjunto5;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO6")
    private String tituloConjunto6;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO7")
    private String tituloConjunto7;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO8")
    private String tituloConjunto8;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO9")
    private String tituloConjunto9;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO10")
    private String tituloConjunto10;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO11")
    private String tituloConjunto11;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO12")
    private String tituloConjunto12;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO13")
    private String tituloConjunto13;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO14")
    private String tituloConjunto14;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO15")
    private String tituloConjunto15;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO16")
    private String tituloConjunto16;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO17")
    private String tituloConjunto17;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO18")
    private String tituloConjunto18;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO19")
    private String tituloConjunto19;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO20")
    private String tituloConjunto20;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO21")
    private String tituloConjunto21;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO22")
    private String tituloConjunto22;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO23")
    private String tituloConjunto23;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO24")
    private String tituloConjunto24;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO25")
    private String tituloConjunto25;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO26")
    private String tituloConjunto26;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO27")
    private String tituloConjunto27;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO28")
    private String tituloConjunto28;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO29")
    private String tituloConjunto29;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO30")
    private String tituloConjunto30;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO31")
    private String tituloConjunto31;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO32")
    private String tituloConjunto32;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO33")
    private String tituloConjunto33;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO34")
    private String tituloConjunto34;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO35")
    private String tituloConjunto35;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO36")
    private String tituloConjunto36;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO37")
    private String tituloConjunto37;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO38")
    private String tituloConjunto38;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO39")
    private String tituloConjunto39;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO40")
    private String tituloConjunto40;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO41")
    private String tituloConjunto41;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO42")
    private String tituloConjunto42;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO43")
    private String tituloConjunto43;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO44")
    private String tituloConjunto44;
    @Size(max = 30)
    @Column(name = "TITULOCONJUNTO45")
    private String tituloConjunto45;

    @Column(name = "DESVIACION1")
    private BigDecimal desviacion1;
    @Column(name = "DESVIACION2")
    private BigDecimal desviacion2;
    @Column(name = "DESVIACION3")
    private BigDecimal desviacion3;
    @Column(name = "DESVIACION4")
    private BigDecimal desviacion4;
    @Column(name = "DESVIACION5")
    private BigDecimal desviacion5;
    @Column(name = "DESVIACION6")
    private BigDecimal desviacion6;
    @Column(name = "DESVIACION7")
    private BigDecimal desviacion7;
    @Column(name = "DESVIACION8")
    private BigDecimal desviacion8;
    @Column(name = "DESVIACION9")
    private BigDecimal desviacion9;
    @Column(name = "DESVIACION10")
    private BigDecimal desviacion10;
    @Column(name = "DESVIACION11")
    private BigDecimal desviacion11;
    @Column(name = "DESVIACION12")
    private BigDecimal desviacion12;
    @Column(name = "DESVIACION13")
    private BigDecimal desviacion13;
    @Column(name = "DESVIACION14")
    private BigDecimal desviacion14;
    @Column(name = "DESVIACION15")
    private BigDecimal desviacion15;
    @Column(name = "DESVIACION16")
    private BigDecimal desviacion16;
    @Column(name = "DESVIACION17")
    private BigDecimal desviacion17;
    @Column(name = "DESVIACION18")
    private BigDecimal desviacion18;
    @Column(name = "DESVIACION19")
    private BigDecimal desviacion19;
    @Column(name = "DESVIACION20")
    private BigDecimal desviacion20;
    @Column(name = "DESVIACION21")
    private BigDecimal desviacion21;
    @Column(name = "DESVIACION22")
    private BigDecimal desviacion22;
    @Column(name = "DESVIACION23")
    private BigDecimal desviacion23;
    @Column(name = "DESVIACION24")
    private BigDecimal desviacion24;
    @Column(name = "DESVIACION25")
    private BigDecimal desviacion25;
    @Column(name = "DESVIACION26")
    private BigDecimal desviacion26;
    @Column(name = "DESVIACION27")
    private BigDecimal desviacion27;
    @Column(name = "DESVIACION28")
    private BigDecimal desviacion28;
    @Column(name = "DESVIACION29")
    private BigDecimal desviacion29;
    @Column(name = "DESVIACION30")
    private BigDecimal desviacion30;
    @Column(name = "DESVIACION31")
    private BigDecimal desviacion31;
    @Column(name = "DESVIACION32")
    private BigDecimal desviacion32;
    @Column(name = "DESVIACION33")
    private BigDecimal desviacion33;
    @Column(name = "DESVIACION34")
    private BigDecimal desviacion34;
    @Column(name = "DESVIACION35")
    private BigDecimal desviacion35;
    @Column(name = "DESVIACION36")
    private BigDecimal desviacion36;
    @Column(name = "DESVIACION37")
    private BigDecimal desviacion37;
    @Column(name = "DESVIACION38")
    private BigDecimal desviacion38;
    @Column(name = "DESVIACION39")
    private BigDecimal desviacion39;
    @Column(name = "DESVIACION40")
    private BigDecimal desviacion40;
    @Column(name = "DESVIACION41")
    private BigDecimal desviacion41;
    @Column(name = "DESVIACION42")
    private BigDecimal desviacion42;
    @Column(name = "DESVIACION43")
    private BigDecimal desviacion43;
    @Column(name = "DESVIACION44")
    private BigDecimal desviacion44;
    @Column(name = "DESVIACION45")
    private BigDecimal desviacion45;

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Date getFechaDesdeLineaBase() {
        return fechaDesdeLineaBase;
    }

    public void setFechaDesdeLineaBase(Date fechaDesdeLineaBase) {
        this.fechaDesdeLineaBase = fechaDesdeLineaBase;
    }

    public Date getFechaHastaLineaBase() {
        return fechaHastaLineaBase;
    }

    public void setFechaHastaLineaBase(Date fechaHastaLineaBase) {
        this.fechaHastaLineaBase = fechaHastaLineaBase;
    }

    public int getConjunto() {
        return conjunto;
    }

    public void setConjunto(int conjunto) {
        this.conjunto = conjunto;
    }

    public String getEstado() {
        if( estado == null){
            estado = "";
        }
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getUsuarioBD() {
        return usuarioBD;
    }

    public void setUsuarioBD(String usuarioBD) {
        this.usuarioBD = usuarioBD;
    }

    public String getTipoResumen() {
        return tipoResumen;
    }

    public void setTipoResumen(String tipoResumen) {
        this.tipoResumen = tipoResumen;
    }

    public String getEstadolineaBase() {
        return estadolineaBase;
    }

    public void setEstadolineaBase(String estadolineaBase) {
        this.estadolineaBase = estadolineaBase;
    }

    public String getTituloConjunto1() {
        return tituloConjunto1;
    }

    public void setTituloConjunto1(String tituloConjunto1) {
        this.tituloConjunto1 = tituloConjunto1;
    }

    public String getTituloConjunto2() {
        return tituloConjunto2;
    }

    public void setTituloConjunto2(String tituloConjunto2) {
        this.tituloConjunto2 = tituloConjunto2;
    }

    public String getTituloConjunto3() {
        return tituloConjunto3;
    }

    public void setTituloConjunto3(String tituloConjunto3) {
        this.tituloConjunto3 = tituloConjunto3;
    }

    public String getTituloConjunto4() {
        return tituloConjunto4;
    }

    public void setTituloConjunto4(String tituloConjunto4) {
        this.tituloConjunto4 = tituloConjunto4;
    }

    public String getTituloConjunto5() {
        return tituloConjunto5;
    }

    public void setTituloConjunto5(String tituloConjunto5) {
        this.tituloConjunto5 = tituloConjunto5;
    }

    public String getTituloConjunto6() {
        return tituloConjunto6;
    }

    public void setTituloConjunto6(String tituloConjunto6) {
        this.tituloConjunto6 = tituloConjunto6;
    }

    public String getTituloConjunto7() {
        return tituloConjunto7;
    }

    public void setTituloConjunto7(String tituloConjunto7) {
        this.tituloConjunto7 = tituloConjunto7;
    }

    public String getTituloConjunto8() {
        return tituloConjunto8;
    }

    public void setTituloConjunto8(String tituloConjunto8) {
        this.tituloConjunto8 = tituloConjunto8;
    }

    public String getTituloConjunto9() {
        return tituloConjunto9;
    }

    public void setTituloConjunto9(String tituloConjunto9) {
        this.tituloConjunto9 = tituloConjunto9;
    }

    public String getTituloConjunto10() {
        return tituloConjunto10;
    }

    public void setTituloConjunto10(String tituloConjunto10) {
        this.tituloConjunto10 = tituloConjunto10;
    }

    public String getTituloConjunto11() {
        return tituloConjunto11;
    }

    public void setTituloConjunto11(String tituloConjunto11) {
        this.tituloConjunto11 = tituloConjunto11;
    }

    public String getTituloConjunto12() {
        return tituloConjunto12;
    }

    public void setTituloConjunto12(String tituloConjunto12) {
        this.tituloConjunto12 = tituloConjunto12;
    }

    public String getTituloConjunto13() {
        return tituloConjunto13;
    }

    public void setTituloConjunto13(String tituloConjunto13) {
        this.tituloConjunto13 = tituloConjunto13;
    }

    public String getTituloConjunto14() {
        return tituloConjunto14;
    }

    public void setTituloConjunto14(String tituloConjunto14) {
        this.tituloConjunto14 = tituloConjunto14;
    }

    public String getTituloConjunto15() {
        return tituloConjunto15;
    }

    public void setTituloConjunto15(String tituloConjunto15) {
        this.tituloConjunto15 = tituloConjunto15;
    }

    public String getTituloConjunto16() {
        return tituloConjunto16;
    }

    public void setTituloConjunto16(String tituloConjunto16) {
        this.tituloConjunto16 = tituloConjunto16;
    }

    public String getTituloConjunto17() {
        return tituloConjunto17;
    }

    public void setTituloConjunto17(String tituloConjunto17) {
        this.tituloConjunto17 = tituloConjunto17;
    }

    public String getTituloConjunto18() {
        return tituloConjunto18;
    }

    public void setTituloConjunto18(String tituloConjunto18) {
        this.tituloConjunto18 = tituloConjunto18;
    }

    public String getTituloConjunto19() {
        return tituloConjunto19;
    }

    public void setTituloConjunto19(String tituloConjunto19) {
        this.tituloConjunto19 = tituloConjunto19;
    }

    public String getTituloConjunto20() {
        return tituloConjunto20;
    }

    public void setTituloConjunto20(String tituloConjunto20) {
        this.tituloConjunto20 = tituloConjunto20;
    }

    public String getTituloConjunto21() {
        return tituloConjunto21;
    }

    public void setTituloConjunto21(String tituloConjunto21) {
        this.tituloConjunto21 = tituloConjunto21;
    }

    public String getTituloConjunto22() {
        return tituloConjunto22;
    }

    public void setTituloConjunto22(String tituloConjunto22) {
        this.tituloConjunto22 = tituloConjunto22;
    }

    public String getTituloConjunto23() {
        return tituloConjunto23;
    }

    public void setTituloConjunto23(String tituloConjunto23) {
        this.tituloConjunto23 = tituloConjunto23;
    }

    public String getTituloConjunto24() {
        return tituloConjunto24;
    }

    public void setTituloConjunto24(String tituloConjunto24) {
        this.tituloConjunto24 = tituloConjunto24;
    }

    public String getTituloConjunto25() {
        return tituloConjunto25;
    }

    public void setTituloConjunto25(String tituloConjunto25) {
        this.tituloConjunto25 = tituloConjunto25;
    }

    public String getTituloConjunto26() {
        return tituloConjunto26;
    }

    public void setTituloConjunto26(String tituloConjunto26) {
        this.tituloConjunto26 = tituloConjunto26;
    }

    public String getTituloConjunto27() {
        return tituloConjunto27;
    }

    public void setTituloConjunto27(String tituloConjunto27) {
        this.tituloConjunto27 = tituloConjunto27;
    }

    public String getTituloConjunto28() {
        return tituloConjunto28;
    }

    public void setTituloConjunto28(String tituloConjunto28) {
        this.tituloConjunto28 = tituloConjunto28;
    }

    public String getTituloConjunto29() {
        return tituloConjunto29;
    }

    public void setTituloConjunto29(String tituloConjunto29) {
        this.tituloConjunto29 = tituloConjunto29;
    }

    public String getTituloConjunto30() {
        return tituloConjunto30;
    }

    public void setTituloConjunto30(String tituloConjunto30) {
        this.tituloConjunto30 = tituloConjunto30;
    }

    public String getTituloConjunto31() {
        return tituloConjunto31;
    }

    public void setTituloConjunto31(String tituloConjunto31) {
        this.tituloConjunto31 = tituloConjunto31;
    }

    public String getTituloConjunto32() {
        return tituloConjunto32;
    }

    public void setTituloConjunto32(String tituloConjunto32) {
        this.tituloConjunto32 = tituloConjunto32;
    }

    public String getTituloConjunto33() {
        return tituloConjunto33;
    }

    public void setTituloConjunto33(String tituloConjunto33) {
        this.tituloConjunto33 = tituloConjunto33;
    }

    public String getTituloConjunto34() {
        return tituloConjunto34;
    }

    public void setTituloConjunto34(String tituloConjunto34) {
        this.tituloConjunto34 = tituloConjunto34;
    }

    public String getTituloConjunto35() {
        return tituloConjunto35;
    }

    public void setTituloConjunto35(String tituloConjunto35) {
        this.tituloConjunto35 = tituloConjunto35;
    }

    public String getTituloConjunto36() {
        return tituloConjunto36;
    }

    public void setTituloConjunto36(String tituloConjunto36) {
        this.tituloConjunto36 = tituloConjunto36;
    }

    public String getTituloConjunto37() {
        return tituloConjunto37;
    }

    public void setTituloConjunto37(String tituloConjunto37) {
        this.tituloConjunto37 = tituloConjunto37;
    }

    public String getTituloConjunto38() {
        return tituloConjunto38;
    }

    public void setTituloConjunto38(String tituloConjunto38) {
        this.tituloConjunto38 = tituloConjunto38;
    }

    public String getTituloConjunto39() {
        return tituloConjunto39;
    }

    public void setTituloConjunto39(String tituloConjunto39) {
        this.tituloConjunto39 = tituloConjunto39;
    }

    public String getTituloConjunto40() {
        return tituloConjunto40;
    }

    public void setTituloConjunto40(String tituloConjunto40) {
        this.tituloConjunto40 = tituloConjunto40;
    }

    public String getTituloConjunto41() {
        return tituloConjunto41;
    }

    public void setTituloConjunto41(String tituloConjunto41) {
        this.tituloConjunto41 = tituloConjunto41;
    }

    public String getTituloConjunto42() {
        return tituloConjunto42;
    }

    public void setTituloConjunto42(String tituloConjunto42) {
        this.tituloConjunto42 = tituloConjunto42;
    }

    public String getTituloConjunto43() {
        return tituloConjunto43;
    }

    public void setTituloConjunto43(String tituloConjunto43) {
        this.tituloConjunto43 = tituloConjunto43;
    }

    public String getTituloConjunto44() {
        return tituloConjunto44;
    }

    public void setTituloConjunto44(String tituloConjunto44) {
        this.tituloConjunto44 = tituloConjunto44;
    }

    public String getTituloConjunto45() {
        return tituloConjunto45;
    }

    public void setTituloConjunto45(String tituloConjunto45) {
        this.tituloConjunto45 = tituloConjunto45;
    }

    public BigDecimal getDesviacion1() {
        if (desviacion1 == null) {
            desviacion1 = new BigDecimal("0.00");
        }
        return desviacion1;
    }

    public void setDesviacion1(BigDecimal desviacion1) {
        this.desviacion1 = desviacion1;
    }

    public BigDecimal getDesviacion2() {
        if (desviacion2 == null) {
            desviacion2 = new BigDecimal("0.00");
        }
        return desviacion2;
    }

    public void setDesviacion2(BigDecimal desviacion2) {
        this.desviacion2 = desviacion2;
    }

    public BigDecimal getDesviacion3() {
        if (desviacion3 == null) {
            desviacion3 = new BigDecimal("0.00");
        }
        return desviacion3;
    }

    public void setDesviacion3(BigDecimal desviacion3) {
        this.desviacion3 = desviacion3;
    }

    public BigDecimal getDesviacion4() {
        if (desviacion4 == null) {
            desviacion4 = new BigDecimal("0.00");
        }
        return desviacion4;
    }

    public void setDesviacion4(BigDecimal desviacion4) {
        this.desviacion4 = desviacion4;
    }

    public BigDecimal getDesviacion5() {
        if (desviacion5 == null) {
            desviacion5 = new BigDecimal("0.00");
        }
        return desviacion5;
    }

    public void setDesviacion5(BigDecimal desviacion5) {
        this.desviacion5 = desviacion5;
    }

    public BigDecimal getDesviacion6() {
        if (desviacion6 == null) {
            desviacion6 = new BigDecimal("0.00");
        }
        return desviacion6;
    }

    public void setDesviacion6(BigDecimal desviacion6) {
        this.desviacion6 = desviacion6;
    }

    public BigDecimal getDesviacion7() {
        if (desviacion7 == null) {
            desviacion7 = new BigDecimal("0.00");
        }
        return desviacion7;
    }

    public void setDesviacion7(BigDecimal desviacion7) {
        this.desviacion7 = desviacion7;
    }

    public BigDecimal getDesviacion8() {
        if (desviacion8 == null) {
            desviacion8 = new BigDecimal("0.00");
        }
        return desviacion8;
    }

    public void setDesviacion8(BigDecimal desviacion8) {
        this.desviacion8 = desviacion8;
    }

    public BigDecimal getDesviacion9() {
        if (desviacion9 == null) {
            desviacion9 = new BigDecimal("0.00");
        }
        return desviacion9;
    }

    public void setDesviacion9(BigDecimal desviacion9) {
        this.desviacion9 = desviacion9;
    }

    public BigDecimal getDesviacion10() {
        if (desviacion10 == null) {
            desviacion10 = new BigDecimal("0.00");
        }
        return desviacion10;
    }

    public void setDesviacion10(BigDecimal desviacion10) {
        this.desviacion10 = desviacion10;
    }

    public BigDecimal getDesviacion11() {
        if (desviacion11 == null) {
            desviacion11 = new BigDecimal("0.00");
        }
        return desviacion11;
    }

    public void setDesviacion11(BigDecimal desviacion11) {
        this.desviacion11 = desviacion11;
    }

    public BigDecimal getDesviacion12() {
        if (desviacion12 == null) {
            desviacion12 = new BigDecimal("0.00");
        }
        return desviacion12;
    }

    public void setDesviacion12(BigDecimal desviacion12) {
        this.desviacion12 = desviacion12;
    }

    public BigDecimal getDesviacion13() {
        if (desviacion13 == null) {
            desviacion13 = new BigDecimal("0.00");
        }
        return desviacion13;
    }

    public void setDesviacion13(BigDecimal desviacion13) {
        this.desviacion13 = desviacion13;
    }

    public BigDecimal getDesviacion14() {
        if (desviacion14 == null) {
            desviacion14 = new BigDecimal("0.00");
        }
        return desviacion14;
    }

    public void setDesviacion14(BigDecimal desviacion14) {
        this.desviacion14 = desviacion14;
    }

    public BigDecimal getDesviacion15() {
        if (desviacion15 == null) {
            desviacion15 = new BigDecimal("0.00");
        }
        return desviacion15;
    }

    public void setDesviacion15(BigDecimal desviacion15) {
        this.desviacion15 = desviacion15;
    }

    public BigDecimal getDesviacion16() {
        if (desviacion16 == null) {
            desviacion16 = new BigDecimal("0.00");
        }
        return desviacion16;
    }

    public void setDesviacion16(BigDecimal desviacion16) {
        this.desviacion16 = desviacion16;
    }

    public BigDecimal getDesviacion17() {
        if (desviacion17 == null) {
            desviacion17 = new BigDecimal("0.00");
        }
        return desviacion17;
    }

    public void setDesviacion17(BigDecimal desviacion17) {
        this.desviacion17 = desviacion17;
    }

    public BigDecimal getDesviacion18() {
        if (desviacion18 == null) {
            desviacion18 = new BigDecimal("0.00");
        }
        return desviacion18;
    }

    public void setDesviacion18(BigDecimal desviacion18) {
        this.desviacion18 = desviacion18;
    }

    public BigDecimal getDesviacion19() {
        if (desviacion19 == null) {
            desviacion19 = new BigDecimal("0.00");
        }
        return desviacion19;
    }

    public void setDesviacion19(BigDecimal desviacion19) {
        this.desviacion19 = desviacion19;
    }

    public BigDecimal getDesviacion20() {
        if (desviacion20 == null) {
            desviacion20 = new BigDecimal("0.00");
        }
        return desviacion20;
    }

    public void setDesviacion20(BigDecimal desviacion20) {
        this.desviacion20 = desviacion20;
    }

    public BigDecimal getDesviacion21() {
        if (desviacion21 == null) {
            desviacion21 = new BigDecimal("0.00");
        }
        return desviacion21;
    }

    public void setDesviacion21(BigDecimal desviacion21) {
        this.desviacion21 = desviacion21;
    }

    public BigDecimal getDesviacion22() {
        if (desviacion22 == null) {
            desviacion22 = new BigDecimal("0.00");
        }
        return desviacion22;
    }

    public void setDesviacion22(BigDecimal desviacion22) {
        this.desviacion22 = desviacion22;
    }

    public BigDecimal getDesviacion23() {
        if (desviacion23 == null) {
            desviacion23 = new BigDecimal("0.00");
        }
        return desviacion23;
    }

    public void setDesviacion23(BigDecimal desviacion23) {
        this.desviacion23 = desviacion23;
    }

    public BigDecimal getDesviacion24() {
        if (desviacion24 == null) {
            desviacion24 = new BigDecimal("0.00");
        }
        return desviacion24;
    }

    public void setDesviacion24(BigDecimal desviacion24) {
        this.desviacion24 = desviacion24;
    }

    public BigDecimal getDesviacion25() {
        if (desviacion25 == null) {
            desviacion25 = new BigDecimal("0.00");
        }
        return desviacion25;
    }

    public void setDesviacion25(BigDecimal desviacion25) {
        this.desviacion25 = desviacion25;
    }

    public BigDecimal getDesviacion26() {
        if (desviacion26 == null) {
            desviacion26 = new BigDecimal("0.00");
        }
        return desviacion26;
    }

    public void setDesviacion26(BigDecimal desviacion26) {
        this.desviacion26 = desviacion26;
    }

    public BigDecimal getDesviacion27() {
        if (desviacion27 == null) {
            desviacion27 = new BigDecimal("0.00");
        }
        return desviacion27;
    }

    public void setDesviacion27(BigDecimal desviacion27) {
        this.desviacion27 = desviacion27;
    }

    public BigDecimal getDesviacion28() {
        if (desviacion28 == null) {
            desviacion28 = new BigDecimal("0.00");
        }
        return desviacion28;
    }

    public void setDesviacion28(BigDecimal desviacion28) {
        this.desviacion28 = desviacion28;
    }

    public BigDecimal getDesviacion29() {
        if (desviacion29 == null) {
            desviacion29 = new BigDecimal("0.00");
        }
        return desviacion29;
    }

    public void setDesviacion29(BigDecimal desviacion29) {
        this.desviacion29 = desviacion29;
    }

    public BigDecimal getDesviacion30() {
        if (desviacion30 == null) {
            desviacion30 = new BigDecimal("0.00");
        }
        return desviacion30;
    }

    public void setDesviacion30(BigDecimal desviacion30) {
        this.desviacion30 = desviacion30;
    }

    public BigDecimal getDesviacion31() {
        if (desviacion31 == null) {
            desviacion31 = new BigDecimal("0.00");
        }
        return desviacion31;
    }

    public void setDesviacion31(BigDecimal desviacion31) {
        this.desviacion31 = desviacion31;
    }

    public BigDecimal getDesviacion32() {
        if (desviacion32 == null) {
            desviacion32 = new BigDecimal("0.00");
        }
        return desviacion32;
    }

    public void setDesviacion32(BigDecimal desviacion32) {
        this.desviacion32 = desviacion32;
    }

    public BigDecimal getDesviacion33() {
        if (desviacion33 == null) {
            desviacion33 = new BigDecimal("0.00");
        }
        return desviacion33;
    }

    public void setDesviacion33(BigDecimal desviacion33) {
        this.desviacion33 = desviacion33;
    }

    public BigDecimal getDesviacion34() {
        if (desviacion34 == null) {
            desviacion34 = new BigDecimal("0.00");
        }
        return desviacion34;
    }

    public void setDesviacion34(BigDecimal desviacion34) {
        this.desviacion34 = desviacion34;
    }

    public BigDecimal getDesviacion35() {
        if (desviacion35 == null) {
            desviacion35 = new BigDecimal("0.00");
        }
        return desviacion35;
    }

    public void setDesviacion35(BigDecimal desviacion35) {
        this.desviacion35 = desviacion35;
    }

    public BigDecimal getDesviacion36() {
        if (desviacion36 == null) {
            desviacion36 = new BigDecimal("0.00");
        }
        return desviacion36;
    }

    public void setDesviacion36(BigDecimal desviacion36) {
        this.desviacion36 = desviacion36;
    }

    public BigDecimal getDesviacion37() {
        if (desviacion37 == null) {
            desviacion37 = new BigDecimal("0.00");
        }
        return desviacion37;
    }

    public void setDesviacion37(BigDecimal desviacion37) {
        this.desviacion37 = desviacion37;
    }

    public BigDecimal getDesviacion38() {
        if (desviacion38 == null) {
            desviacion38 = new BigDecimal("0.00");
        }
        return desviacion38;
    }

    public void setDesviacion38(BigDecimal desviacion38) {
        this.desviacion38 = desviacion38;
    }

    public BigDecimal getDesviacion39() {
        if (desviacion39 == null) {
            desviacion39 = new BigDecimal("0.00");
        }
        return desviacion39;
    }

    public void setDesviacion39(BigDecimal desviacion39) {
        this.desviacion39 = desviacion39;
    }

    public BigDecimal getDesviacion40() {
        if (desviacion40 == null) {
            desviacion40 = new BigDecimal("0.00");
        }
        return desviacion40;
    }

    public void setDesviacion40(BigDecimal desviacion40) {
        this.desviacion40 = desviacion40;
    }

    public BigDecimal getDesviacion41() {
        if (desviacion41 == null) {
            desviacion41 = new BigDecimal("0.00");
        }
        return desviacion41;
    }

    public void setDesviacion41(BigDecimal desviacion41) {
        this.desviacion41 = desviacion41;
    }

    public BigDecimal getDesviacion42() {
        if (desviacion42 == null) {
            desviacion42 = new BigDecimal("0.00");
        }
        return desviacion42;
    }

    public void setDesviacion42(BigDecimal desviacion42) {
        this.desviacion42 = desviacion42;
    }

    public BigDecimal getDesviacion43() {
        if (desviacion43 == null) {
            desviacion43 = new BigDecimal("0.00");
        }
        return desviacion43;
    }

    public void setDesviacion43(BigDecimal desviacion43) {
        this.desviacion43 = desviacion43;
    }

    public BigDecimal getDesviacion44() {
        if (desviacion44 == null) {
            desviacion44 = new BigDecimal("0.00");
        }
        return desviacion44;
    }

    public void setDesviacion44(BigDecimal desviacion44) {
        this.desviacion44 = desviacion44;
    }

    public BigDecimal getDesviacion45() {
        if (desviacion45 == null) {
            desviacion45 = new BigDecimal("0.00");
        }
        return desviacion45;
    }

    public void setDesviacion45(BigDecimal desviacion45) {
        this.desviacion45 = desviacion45;
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
        if (!(object instanceof ParametrosConjuntos)) {
            return false;
        }
        ParametrosConjuntos other = (ParametrosConjuntos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.PersistenciaParametrosConjuntos[ id=" + secuencia + " ]";
    }

}
