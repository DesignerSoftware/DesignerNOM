/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Entity
@Cacheable(false)
public class VWDSolucionesNodosN implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "Y")
    private BigDecimal SecuenciaFiltro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DIMENSION")
    private String dimension;
    @Column(name = "ESTADO")
    private String estado;
    @Column(name = "Z")
    @Temporal(TemporalType.DATE)
    private Date z;
    @Column(name = "VALOR")
    private BigDecimal valor;

    @Column(name = "CONJUNTO1")
    private BigDecimal conjunto1;
    @Column(name = "CONJUNTO2")
    private BigDecimal conjunto2;
    @Column(name = "CONJUNTO3")
    private BigDecimal conjunto3;
    @Column(name = "CONJUNTO4")
    private BigDecimal conjunto4;
    @Column(name = "CONJUNTO5")
    private BigDecimal conjunto5;
    @Column(name = "CONJUNTO6")
    private BigDecimal conjunto6;
    @Column(name = "CONJUNTO7")
    private BigDecimal conjunto7;
    @Column(name = "CONJUNTO8")
    private BigDecimal conjunto8;
    @Column(name = "CONJUNTO9")
    private BigDecimal conjunto9;
    @Column(name = "CONJUNTO10")
    private BigDecimal conjunto10;
    @Column(name = "CONJUNTO11")
    private BigDecimal conjunto11;
    @Column(name = "CONJUNTO12")
    private BigDecimal conjunto12;
    @Column(name = "CONJUNTO13")
    private BigDecimal conjunto13;
    @Column(name = "CONJUNTO14")
    private BigDecimal conjunto14;
    @Column(name = "CONJUNTO15")
    private BigDecimal conjunto15;
    @Column(name = "CONJUNTO16")
    private BigDecimal conjunto16;
    @Column(name = "CONJUNTO17")
    private BigDecimal conjunto17;
    @Column(name = "CONJUNTO18")
    private BigDecimal conjunto18;
    @Column(name = "CONJUNTO19")
    private BigDecimal conjunto19;
    @Column(name = "CONJUNTO20")
    private BigDecimal conjunto20;
    @Column(name = "CONJUNTO21")
    private BigDecimal conjunto21;
    @Column(name = "CONJUNTO22")
    private BigDecimal conjunto22;
    @Column(name = "CONJUNTO23")
    private BigDecimal conjunto23;
    @Column(name = "CONJUNTO24")
    private BigDecimal conjunto24;
    @Column(name = "CONJUNTO25")
    private BigDecimal conjunto25;
    @Column(name = "CONJUNTO26")
    private BigDecimal conjunto26;
    @Column(name = "CONJUNTO27")
    private BigDecimal conjunto27;
    @Column(name = "CONJUNTO28")
    private BigDecimal conjunto28;
    @Column(name = "CONJUNTO29")
    private BigDecimal conjunto29;
    @Column(name = "CONJUNTO30")
    private BigDecimal conjunto30;
    @Column(name = "CONJUNTO31")
    private BigDecimal conjunto31;
    @Column(name = "CONJUNTO32")
    private BigDecimal conjunto32;
    @Column(name = "CONJUNTO33")
    private BigDecimal conjunto33;
    @Column(name = "CONJUNTO34")
    private BigDecimal conjunto34;
    @Column(name = "CONJUNTO35")
    private BigDecimal conjunto35;
    @Column(name = "CONJUNTO36")
    private BigDecimal conjunto36;
    @Column(name = "CONJUNTO37")
    private BigDecimal conjunto37;
    @Column(name = "CONJUNTO38")
    private BigDecimal conjunto38;
    @Column(name = "CONJUNTO39")
    private BigDecimal conjunto39;
    @Column(name = "CONJUNTO40")
    private BigDecimal conjunto40;
    @Column(name = "CONJUNTO41")
    private BigDecimal conjunto41;
    @Column(name = "CONJUNTO42")
    private BigDecimal conjunto42;
    @Column(name = "CONJUNTO43")
    private BigDecimal conjunto43;
    @Column(name = "CONJUNTO44")
    private BigDecimal conjunto44;
    @Column(name = "CONJUNTO45")
    private BigDecimal conjunto45;

    @Column(name = "UNIDADESCONJUNTO1")
    private BigDecimal unidadConjunto1;
    @Column(name = "UNIDADESCONJUNTO2")
    private BigDecimal unidadConjunto2;
    @Column(name = "UNIDADESCONJUNTO3")
    private BigDecimal unidadConjunto3;
    @Column(name = "UNIDADESCONJUNTO4")
    private BigDecimal unidadConjunto4;
    @Column(name = "UNIDADESCONJUNTO5")
    private BigDecimal unidadConjunto5;
    @Column(name = "UNIDADESCONJUNTO6")
    private BigDecimal unidadConjunto6;
    @Column(name = "UNIDADESCONJUNTO7")
    private BigDecimal unidadConjunto7;
    @Column(name = "UNIDADESCONJUNTO8")
    private BigDecimal unidadConjunto8;
    @Column(name = "UNIDADESCONJUNTO9")
    private BigDecimal unidadConjunto9;
    @Column(name = "UNIDADESCONJUNTO10")
    private BigDecimal unidadConjunto10;
    @Column(name = "UNIDADESCONJUNTO11")
    private BigDecimal unidadConjunto11;
    @Column(name = "UNIDADESCONJUNTO12")
    private BigDecimal unidadConjunto12;
    @Column(name = "UNIDADESCONJUNTO13")
    private BigDecimal unidadConjunto13;
    @Column(name = "UNIDADESCONJUNTO14")
    private BigDecimal unidadConjunto14;
    @Column(name = "UNIDADESCONJUNTO15")
    private BigDecimal unidadConjunto15;
    @Column(name = "UNIDADESCONJUNTO16")
    private BigDecimal unidadConjunto16;
    @Column(name = "UNIDADESCONJUNTO17")
    private BigDecimal unidadConjunto17;
    @Column(name = "UNIDADESCONJUNTO18")
    private BigDecimal unidadConjunto18;
    @Column(name = "UNIDADESCONJUNTO19")
    private BigDecimal unidadConjunto19;
    @Column(name = "UNIDADESCONJUNTO20")
    private BigDecimal unidadConjunto20;
    @Column(name = "UNIDADESCONJUNTO21")
    private BigDecimal unidadConjunto21;
    @Column(name = "UNIDADESCONJUNTO22")
    private BigDecimal unidadConjunto22;
    @Column(name = "UNIDADESCONJUNTO23")
    private BigDecimal unidadConjunto23;
    @Column(name = "UNIDADESCONJUNTO24")
    private BigDecimal unidadConjunto24;
    @Column(name = "UNIDADESCONJUNTO25")
    private BigDecimal unidadConjunto25;
    @Column(name = "UNIDADESCONJUNTO26")
    private BigDecimal unidadConjunto26;
    @Column(name = "UNIDADESCONJUNTO27")
    private BigDecimal unidadConjunto27;
    @Column(name = "UNIDADESCONJUNTO28")
    private BigDecimal unidadConjunto28;
    @Column(name = "UNIDADESCONJUNTO29")
    private BigDecimal unidadConjunto29;
    @Column(name = "UNIDADESCONJUNTO30")
    private BigDecimal unidadConjunto30;
    @Column(name = "UNIDADESCONJUNTO31")
    private BigDecimal unidadConjunto31;
    @Column(name = "UNIDADESCONJUNTO32")
    private BigDecimal unidadConjunto32;
    @Column(name = "UNIDADESCONJUNTO33")
    private BigDecimal unidadConjunto33;
    @Column(name = "UNIDADESCONJUNTO34")
    private BigDecimal unidadConjunto34;
    @Column(name = "UNIDADESCONJUNTO35")
    private BigDecimal unidadConjunto35;
    @Column(name = "UNIDADESCONJUNTO36")
    private BigDecimal unidadConjunto36;
    @Column(name = "UNIDADESCONJUNTO37")
    private BigDecimal unidadConjunto37;
    @Column(name = "UNIDADESCONJUNTO38")
    private BigDecimal unidadConjunto38;
    @Column(name = "UNIDADESCONJUNTO39")
    private BigDecimal unidadConjunto39;
    @Column(name = "UNIDADESCONJUNTO40")
    private BigDecimal unidadConjunto40;
    @Column(name = "UNIDADESCONJUNTO41")
    private BigDecimal unidadConjunto41;
    @Column(name = "UNIDADESCONJUNTO42")
    private BigDecimal unidadConjunto42;
    @Column(name = "UNIDADESCONJUNTO43")
    private BigDecimal unidadConjunto43;
    @Column(name = "UNIDADESCONJUNTO44")
    private BigDecimal unidadConjunto44;
    @Column(name = "UNIDADESCONJUNTO45")
    private BigDecimal unidadConjunto45;

    @Transient
    private BigDecimal totalPagos;
    @Transient
    private BigDecimal totalDescuentos;
    @Transient
    private BigDecimal totalGastos;
    @Transient
    private BigDecimal totalPasivos;
    @Transient
    private BigDecimal granTotal;
    @Transient
    private BigDecimal netos;
    @Column(name = "CARGO")
    private String cargo;
    @Column(name = "ESTRUCTURA")
    private String estructura;
    @Column(name = "CODIGOEMPLEADO")
    private BigDecimal codigoEmpleado;

    public BigDecimal getSecuenciaFiltro() {
        return SecuenciaFiltro;
    }

    public void setSecuenciaFiltro(BigDecimal SecuenciaFiltro) {
        this.SecuenciaFiltro = SecuenciaFiltro;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dim) {
        this.dimension = dim;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getZ() {
        return z;
    }

    public void setZ(Date z) {
        this.z = z;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getConjunto1() {
        if (conjunto1 == null) {
            conjunto1 = new BigDecimal(0);
        }
        return conjunto1;
    }

    public void setConjunto1(BigDecimal conjunto1) {
        this.conjunto1 = conjunto1;
    }

    public BigDecimal getConjunto2() {
        if (conjunto2 == null) {
            conjunto2 = new BigDecimal(0);
        }
        return conjunto2;
    }

    public void setConjunto2(BigDecimal conjunto2) {
        this.conjunto2 = conjunto2;
    }

    public BigDecimal getConjunto3() {
        if (conjunto3 == null) {
            conjunto3 = new BigDecimal(0);
        }
        return conjunto3;
    }

    public void setConjunto3(BigDecimal conjunto3) {
        this.conjunto3 = conjunto3;
    }

    public BigDecimal getConjunto4() {
        if (conjunto4 == null) {
            conjunto4 = new BigDecimal(0);
        }
        return conjunto4;
    }

    public void setConjunto4(BigDecimal conjunto4) {
        this.conjunto4 = conjunto4;
    }

    public BigDecimal getConjunto5() {
        if (conjunto5 == null) {
            conjunto5 = new BigDecimal(0);
        }
        return conjunto5;
    }

    public void setConjunto5(BigDecimal conjunto5) {
        this.conjunto5 = conjunto5;
    }

    public BigDecimal getConjunto6() {
        if (conjunto6 == null) {
            conjunto6 = new BigDecimal(0);
        }
        return conjunto6;
    }

    public void setConjunto6(BigDecimal conjunto6) {
        this.conjunto6 = conjunto6;
    }

    public BigDecimal getConjunto7() {
        if (conjunto7 == null) {
            conjunto7 = new BigDecimal(0);
        }
        return conjunto7;
    }

    public void setConjunto7(BigDecimal conjunto7) {
        this.conjunto7 = conjunto7;
    }

    public BigDecimal getConjunto8() {
        if (conjunto8 == null) {
            conjunto8 = new BigDecimal(0);
        }
        return conjunto8;
    }

    public void setConjunto8(BigDecimal conjunto8) {
        this.conjunto8 = conjunto8;
    }

    public BigDecimal getConjunto9() {
        if (conjunto9 == null) {
            conjunto9 = new BigDecimal(0);
        }
        return conjunto9;
    }

    public void setConjunto9(BigDecimal conjunto9) {
        this.conjunto9 = conjunto9;
    }

    public BigDecimal getConjunto10() {
        if (conjunto10 == null) {
            conjunto10 = new BigDecimal(0);
        }
        return conjunto10;
    }

    public void setConjunto10(BigDecimal conjunto10) {
        this.conjunto10 = conjunto10;
    }

    public BigDecimal getConjunto11() {
        if (conjunto11 == null) {
            conjunto11 = new BigDecimal(0);
        }
        return conjunto11;
    }

    public void setConjunto11(BigDecimal conjunto11) {
        this.conjunto11 = conjunto11;
    }

    public BigDecimal getConjunto12() {
        if (conjunto12 == null) {
            conjunto12 = new BigDecimal(0);
        }
        return conjunto12;
    }

    public void setConjunto12(BigDecimal conjunto12) {
        this.conjunto12 = conjunto12;
    }

    public BigDecimal getConjunto13() {
        if (conjunto13 == null) {
            conjunto13 = new BigDecimal(0);
        }
        return conjunto13;
    }

    public void setConjunto13(BigDecimal conjunto13) {
        this.conjunto13 = conjunto13;
    }

    public BigDecimal getConjunto14() {
        if (conjunto14 == null) {
            conjunto14 = new BigDecimal(0);
        }
        return conjunto14;
    }

    public void setConjunto14(BigDecimal conjunto14) {
        this.conjunto14 = conjunto14;
    }

    public BigDecimal getConjunto15() {
        if (conjunto15 == null) {
            conjunto15 = new BigDecimal(0);
        }
        return conjunto15;
    }

    public void setConjunto15(BigDecimal conjunto15) {
        this.conjunto15 = conjunto15;
    }

    public BigDecimal getConjunto16() {
        if (conjunto16 == null) {
            conjunto16 = new BigDecimal(0);
        }
        return conjunto16;
    }

    public void setConjunto16(BigDecimal conjunto16) {
        this.conjunto16 = conjunto16;
    }

    public BigDecimal getConjunto17() {
        if (conjunto17 == null) {
            conjunto17 = new BigDecimal(0);
        }
        return conjunto17;
    }

    public void setConjunto17(BigDecimal conjunto17) {
        this.conjunto17 = conjunto17;
    }

    public BigDecimal getConjunto18() {
        if (conjunto18 == null) {
            conjunto18 = new BigDecimal(0);
        }
        return conjunto18;
    }

    public void setConjunto18(BigDecimal conjunto18) {
        this.conjunto18 = conjunto18;
    }

    public BigDecimal getConjunto19() {
        if (conjunto19 == null) {
            conjunto19 = new BigDecimal(0);
        }
        return conjunto19;
    }

    public void setConjunto19(BigDecimal conjunto19) {
        this.conjunto19 = conjunto19;
    }

    public BigDecimal getConjunto20() {
        if (conjunto20 == null) {
            conjunto20 = new BigDecimal(0);
        }
        return conjunto20;
    }

    public void setConjunto20(BigDecimal conjunto20) {
        this.conjunto20 = conjunto20;
    }

    public BigDecimal getConjunto21() {
        if (conjunto21 == null) {
            conjunto21 = new BigDecimal(0);
        }
        return conjunto21;
    }

    public void setConjunto21(BigDecimal conjunto21) {
        this.conjunto21 = conjunto21;
    }

    public BigDecimal getConjunto22() {
        if (conjunto22 == null) {
            conjunto22 = new BigDecimal(0);
        }
        return conjunto22;
    }

    public void setConjunto22(BigDecimal conjunto22) {
        this.conjunto22 = conjunto22;
    }

    public BigDecimal getConjunto23() {
        if (conjunto23 == null) {
            conjunto23 = new BigDecimal(0);
        }
        return conjunto23;
    }

    public void setConjunto23(BigDecimal conjunto23) {
        this.conjunto23 = conjunto23;
    }

    public BigDecimal getConjunto24() {
        if (conjunto24 == null) {
            conjunto24 = new BigDecimal(0);
        }
        return conjunto24;
    }

    public void setConjunto24(BigDecimal conjunto24) {
        this.conjunto24 = conjunto24;
    }

    public BigDecimal getConjunto25() {
        if (conjunto25 == null) {
            conjunto25 = new BigDecimal(0);
        }
        return conjunto25;
    }

    public void setConjunto25(BigDecimal conjunto25) {
        this.conjunto25 = conjunto25;
    }

    public BigDecimal getConjunto26() {
        if (conjunto26 == null) {
            conjunto26 = new BigDecimal(0);
        }
        return conjunto26;
    }

    public void setConjunto26(BigDecimal conjunto26) {
        this.conjunto26 = conjunto26;
    }

    public BigDecimal getConjunto27() {
        if (conjunto27 == null) {
            conjunto27 = new BigDecimal(0);
        }
        return conjunto27;
    }

    public void setConjunto27(BigDecimal conjunto27) {
        this.conjunto27 = conjunto27;
    }

    public BigDecimal getConjunto28() {
        if (conjunto28 == null) {
            conjunto28 = new BigDecimal(0);
        }
        return conjunto28;
    }

    public void setConjunto28(BigDecimal conjunto28) {
        this.conjunto28 = conjunto28;
    }

    public BigDecimal getConjunto29() {
        if (conjunto29 == null) {
            conjunto29 = new BigDecimal(0);
        }
        return conjunto29;
    }

    public void setConjunto29(BigDecimal conjunto29) {
        this.conjunto29 = conjunto29;
    }

    public BigDecimal getConjunto30() {
        if (conjunto30 == null) {
            conjunto30 = new BigDecimal(0);
        }
        return conjunto30;
    }

    public void setConjunto30(BigDecimal conjunto30) {
        this.conjunto30 = conjunto30;
    }

    public BigDecimal getConjunto31() {
        if (conjunto31 == null) {
            conjunto31 = new BigDecimal(0);
        }
        return conjunto31;
    }

    public void setConjunto31(BigDecimal conjunto31) {
        this.conjunto31 = conjunto31;
    }

    public BigDecimal getConjunto32() {
        if (conjunto32 == null) {
            conjunto32 = new BigDecimal(0);
        }
        return conjunto32;
    }

    public void setConjunto32(BigDecimal conjunto32) {
        this.conjunto32 = conjunto32;
    }

    public BigDecimal getConjunto33() {
        if (conjunto33 == null) {
            conjunto33 = new BigDecimal(0);
        }
        return conjunto33;
    }

    public void setConjunto33(BigDecimal conjunto33) {
        this.conjunto33 = conjunto33;
    }

    public BigDecimal getConjunto34() {
        if (conjunto34 == null) {
            conjunto34 = new BigDecimal(0);
        }
        return conjunto34;
    }

    public void setConjunto34(BigDecimal conjunto34) {
        this.conjunto34 = conjunto34;
    }

    public BigDecimal getConjunto35() {
        if (conjunto35 == null) {
            conjunto35 = new BigDecimal(0);
        }
        return conjunto35;
    }

    public void setConjunto35(BigDecimal conjunto35) {
        this.conjunto35 = conjunto35;
    }

    public BigDecimal getConjunto36() {
        if (conjunto36 == null) {
            conjunto36 = new BigDecimal(0);
        }
        return conjunto36;
    }

    public void setConjunto36(BigDecimal conjunto36) {
        this.conjunto36 = conjunto36;
    }

    public BigDecimal getConjunto37() {
        if (conjunto37 == null) {
            conjunto37 = new BigDecimal(0);
        }
        return conjunto37;
    }

    public void setConjunto37(BigDecimal conjunto37) {
        this.conjunto37 = conjunto37;
    }

    public BigDecimal getConjunto38() {
        if (conjunto38 == null) {
            conjunto38 = new BigDecimal(0);
        }
        return conjunto38;
    }

    public void setConjunto38(BigDecimal conjunto38) {
        this.conjunto38 = conjunto38;
    }

    public BigDecimal getConjunto39() {
        if (conjunto39 == null) {
            conjunto39 = new BigDecimal(0);
        }
        return conjunto39;
    }

    public void setConjunto39(BigDecimal conjunto39) {
        this.conjunto39 = conjunto39;
    }

    public BigDecimal getConjunto40() {
        if (conjunto40 == null) {
            conjunto40 = new BigDecimal(0);
        }
        return conjunto40;
    }

    public void setConjunto40(BigDecimal conjunto40) {
        this.conjunto40 = conjunto40;
    }

    public BigDecimal getConjunto41() {
        if (conjunto41 == null) {
            conjunto41 = new BigDecimal(0);
        }
        return conjunto41;
    }

    public void setConjunto41(BigDecimal conjunto41) {
        this.conjunto41 = conjunto41;
    }

    public BigDecimal getConjunto42() {
        if (conjunto42 == null) {
            conjunto42 = new BigDecimal(0);
        }
        return conjunto42;
    }

    public void setConjunto42(BigDecimal conjunto42) {
        this.conjunto42 = conjunto42;
    }

    public BigDecimal getConjunto43() {
        if (conjunto43 == null) {
            conjunto43 = new BigDecimal(0);
        }
        return conjunto43;
    }

    public void setConjunto43(BigDecimal conjunto43) {
        this.conjunto43 = conjunto43;
    }

    public BigDecimal getConjunto44() {
        if (conjunto44 == null) {
            conjunto44 = new BigDecimal(0);
        }
        return conjunto44;
    }

    public void setConjunto44(BigDecimal conjunto44) {
        this.conjunto44 = conjunto44;
    }

    public BigDecimal getConjunto45() {
        if (conjunto45 == null) {
            conjunto45 = new BigDecimal(0);
        }
        return conjunto45;
    }

    public void setConjunto45(BigDecimal conjunto45) {
        this.conjunto45 = conjunto45;
    }

    public BigDecimal getUnidadConjunto1() {
        return unidadConjunto1;
    }

    public void setUnidadConjunto1(BigDecimal unidadConjunto1) {
        this.unidadConjunto1 = unidadConjunto1;
    }

    public BigDecimal getUnidadConjunto2() {
        return unidadConjunto2;
    }

    public void setUnidadConjunto2(BigDecimal unidadConjunto2) {
        this.unidadConjunto2 = unidadConjunto2;
    }

    public BigDecimal getUnidadConjunto3() {
        return unidadConjunto3;
    }

    public void setUnidadConjunto3(BigDecimal unidadConjunto3) {
        this.unidadConjunto3 = unidadConjunto3;
    }

    public BigDecimal getUnidadConjunto4() {
        return unidadConjunto4;
    }

    public void setUnidadConjunto4(BigDecimal unidadConjunto4) {
        this.unidadConjunto4 = unidadConjunto4;
    }

    public BigDecimal getUnidadConjunto5() {
        return unidadConjunto5;
    }

    public void setUnidadConjunto5(BigDecimal unidadConjunto5) {
        this.unidadConjunto5 = unidadConjunto5;
    }

    public BigDecimal getUnidadConjunto6() {
        return unidadConjunto6;
    }

    public void setUnidadConjunto6(BigDecimal unidadConjunto6) {
        this.unidadConjunto6 = unidadConjunto6;
    }

    public BigDecimal getUnidadConjunto7() {
        return unidadConjunto7;
    }

    public void setUnidadConjunto7(BigDecimal unidadConjunto7) {
        this.unidadConjunto7 = unidadConjunto7;
    }

    public BigDecimal getUnidadConjunto8() {
        return unidadConjunto8;
    }

    public void setUnidadConjunto8(BigDecimal unidadConjunto8) {
        this.unidadConjunto8 = unidadConjunto8;
    }

    public BigDecimal getUnidadConjunto9() {
        return unidadConjunto9;
    }

    public void setUnidadConjunto9(BigDecimal unidadConjunto9) {
        this.unidadConjunto9 = unidadConjunto9;
    }

    public BigDecimal getUnidadConjunto10() {
        return unidadConjunto10;
    }

    public void setUnidadConjunto10(BigDecimal unidadConjunto10) {
        this.unidadConjunto10 = unidadConjunto10;
    }

    public BigDecimal getUnidadConjunto11() {
        return unidadConjunto11;
    }

    public void setUnidadConjunto11(BigDecimal unidadConjunto11) {
        this.unidadConjunto11 = unidadConjunto11;
    }

    public BigDecimal getUnidadConjunto12() {
        return unidadConjunto12;
    }

    public void setUnidadConjunto12(BigDecimal unidadConjunto12) {
        this.unidadConjunto12 = unidadConjunto12;
    }

    public BigDecimal getUnidadConjunto13() {
        return unidadConjunto13;
    }

    public void setUnidadConjunto13(BigDecimal unidadConjunto13) {
        this.unidadConjunto13 = unidadConjunto13;
    }

    public BigDecimal getUnidadConjunto14() {
        return unidadConjunto14;
    }

    public void setUnidadConjunto14(BigDecimal unidadConjunto14) {
        this.unidadConjunto14 = unidadConjunto14;
    }

    public BigDecimal getUnidadConjunto15() {
        return unidadConjunto15;
    }

    public void setUnidadConjunto15(BigDecimal unidadConjunto15) {
        this.unidadConjunto15 = unidadConjunto15;
    }

    public BigDecimal getUnidadConjunto16() {
        return unidadConjunto16;
    }

    public void setUnidadConjunto16(BigDecimal unidadConjunto16) {
        this.unidadConjunto16 = unidadConjunto16;
    }

    public BigDecimal getUnidadConjunto17() {
        return unidadConjunto17;
    }

    public void setUnidadConjunto17(BigDecimal unidadConjunto17) {
        this.unidadConjunto17 = unidadConjunto17;
    }

    public BigDecimal getUnidadConjunto18() {
        return unidadConjunto18;
    }

    public void setUnidadConjunto18(BigDecimal unidadConjunto18) {
        this.unidadConjunto18 = unidadConjunto18;
    }

    public BigDecimal getUnidadConjunto19() {
        return unidadConjunto19;
    }

    public void setUnidadConjunto19(BigDecimal unidadConjunto19) {
        this.unidadConjunto19 = unidadConjunto19;
    }

    public BigDecimal getUnidadConjunto20() {
        return unidadConjunto20;
    }

    public void setUnidadConjunto20(BigDecimal unidadConjunto20) {
        this.unidadConjunto20 = unidadConjunto20;
    }

    public BigDecimal getUnidadConjunto21() {
        return unidadConjunto21;
    }

    public void setUnidadConjunto21(BigDecimal unidadConjunto21) {
        this.unidadConjunto21 = unidadConjunto21;
    }

    public BigDecimal getUnidadConjunto22() {
        return unidadConjunto22;
    }

    public void setUnidadConjunto22(BigDecimal unidadConjunto22) {
        this.unidadConjunto22 = unidadConjunto22;
    }

    public BigDecimal getUnidadConjunto23() {
        return unidadConjunto23;
    }

    public void setUnidadConjunto23(BigDecimal unidadConjunto23) {
        this.unidadConjunto23 = unidadConjunto23;
    }

    public BigDecimal getUnidadConjunto24() {
        return unidadConjunto24;
    }

    public void setUnidadConjunto24(BigDecimal unidadConjunto24) {
        this.unidadConjunto24 = unidadConjunto24;
    }

    public BigDecimal getUnidadConjunto25() {
        return unidadConjunto25;
    }

    public void setUnidadConjunto25(BigDecimal unidadConjunto25) {
        this.unidadConjunto25 = unidadConjunto25;
    }

    public BigDecimal getUnidadConjunto26() {
        return unidadConjunto26;
    }

    public void setUnidadConjunto26(BigDecimal unidadConjunto26) {
        this.unidadConjunto26 = unidadConjunto26;
    }

    public BigDecimal getUnidadConjunto27() {
        return unidadConjunto27;
    }

    public void setUnidadConjunto27(BigDecimal unidadConjunto27) {
        this.unidadConjunto27 = unidadConjunto27;
    }

    public BigDecimal getUnidadConjunto28() {
        return unidadConjunto28;
    }

    public void setUnidadConjunto28(BigDecimal unidadConjunto28) {
        this.unidadConjunto28 = unidadConjunto28;
    }

    public BigDecimal getUnidadConjunto29() {
        return unidadConjunto29;
    }

    public void setUnidadConjunto29(BigDecimal unidadConjunto29) {
        this.unidadConjunto29 = unidadConjunto29;
    }

    public BigDecimal getUnidadConjunto30() {
        return unidadConjunto30;
    }

    public void setUnidadConjunto30(BigDecimal unidadConjunto30) {
        this.unidadConjunto30 = unidadConjunto30;
    }

    public BigDecimal getUnidadConjunto31() {
        return unidadConjunto31;
    }

    public void setUnidadConjunto31(BigDecimal unidadConjunto31) {
        this.unidadConjunto31 = unidadConjunto31;
    }

    public BigDecimal getUnidadConjunto32() {
        return unidadConjunto32;
    }

    public void setUnidadConjunto32(BigDecimal unidadConjunto32) {
        this.unidadConjunto32 = unidadConjunto32;
    }

    public BigDecimal getUnidadConjunto33() {
        return unidadConjunto33;
    }

    public void setUnidadConjunto33(BigDecimal unidadConjunto33) {
        this.unidadConjunto33 = unidadConjunto33;
    }

    public BigDecimal getUnidadConjunto34() {
        return unidadConjunto34;
    }

    public void setUnidadConjunto34(BigDecimal unidadConjunto34) {
        this.unidadConjunto34 = unidadConjunto34;
    }

    public BigDecimal getUnidadConjunto35() {
        return unidadConjunto35;
    }

    public void setUnidadConjunto35(BigDecimal unidadConjunto35) {
        this.unidadConjunto35 = unidadConjunto35;
    }

    public BigDecimal getUnidadConjunto36() {
        return unidadConjunto36;
    }

    public void setUnidadConjunto36(BigDecimal unidadConjunto36) {
        this.unidadConjunto36 = unidadConjunto36;
    }

    public BigDecimal getUnidadConjunto37() {
        return unidadConjunto37;
    }

    public void setUnidadConjunto37(BigDecimal unidadConjunto37) {
        this.unidadConjunto37 = unidadConjunto37;
    }

    public BigDecimal getUnidadConjunto38() {
        return unidadConjunto38;
    }

    public void setUnidadConjunto38(BigDecimal unidadConjunto38) {
        this.unidadConjunto38 = unidadConjunto38;
    }

    public BigDecimal getUnidadConjunto39() {
        return unidadConjunto39;
    }

    public void setUnidadConjunto39(BigDecimal unidadConjunto39) {
        this.unidadConjunto39 = unidadConjunto39;
    }

    public BigDecimal getUnidadConjunto40() {
        return unidadConjunto40;
    }

    public void setUnidadConjunto40(BigDecimal unidadConjunto40) {
        this.unidadConjunto40 = unidadConjunto40;
    }

    public BigDecimal getUnidadConjunto41() {
        return unidadConjunto41;
    }

    public void setUnidadConjunto41(BigDecimal unidadConjunto41) {
        this.unidadConjunto41 = unidadConjunto41;
    }

    public BigDecimal getUnidadConjunto42() {
        return unidadConjunto42;
    }

    public void setUnidadConjunto42(BigDecimal unidadConjunto42) {
        this.unidadConjunto42 = unidadConjunto42;
    }

    public BigDecimal getUnidadConjunto43() {
        return unidadConjunto43;
    }

    public void setUnidadConjunto43(BigDecimal unidadConjunto43) {
        this.unidadConjunto43 = unidadConjunto43;
    }

    public BigDecimal getUnidadConjunto44() {
        return unidadConjunto44;
    }

    public void setUnidadConjunto44(BigDecimal unidadConjunto44) {
        this.unidadConjunto44 = unidadConjunto44;
    }

    public BigDecimal getUnidadConjunto45() {
        return unidadConjunto45;
    }

    public void setUnidadConjunto45(BigDecimal unidadConjunto45) {
        this.unidadConjunto45 = unidadConjunto45;
    }

    public BigDecimal getTotalPagos() {
        totalPagos = conjunto1.add(conjunto2);
        totalPagos = totalPagos.add(conjunto3);
        totalPagos = totalPagos.add(conjunto4);
        totalPagos = totalPagos.add(conjunto5);
        totalPagos = totalPagos.add(conjunto6);
        totalPagos = totalPagos.add(conjunto7);
        totalPagos = totalPagos.add(conjunto8);
        totalPagos = totalPagos.add(conjunto9);
        totalPagos = totalPagos.add(conjunto10);
        totalPagos = totalPagos.add(conjunto11);
        totalPagos = totalPagos.add(conjunto12);
        totalPagos = totalPagos.add(conjunto13);
        totalPagos = totalPagos.add(conjunto14);
        totalPagos = totalPagos.add(conjunto15);
        totalPagos = totalPagos.add(conjunto16);
        totalPagos = totalPagos.add(conjunto17);
        totalPagos = totalPagos.add(conjunto18);
        totalPagos = totalPagos.add(conjunto19);
        totalPagos = totalPagos.add(conjunto20);
        return totalPagos;
    }

    public void setTotalPagos(BigDecimal totalPagos) {
        this.totalPagos = totalPagos;
    }

    public BigDecimal getTotalDescuentos() {
        totalDescuentos = conjunto21.add(conjunto22);
        totalDescuentos = totalDescuentos.add(conjunto23);
        totalDescuentos = totalDescuentos.add(conjunto24);
        totalDescuentos = totalDescuentos.add(conjunto25);
        totalDescuentos = totalDescuentos.add(conjunto26);
        totalDescuentos = totalDescuentos.add(conjunto27);
        totalDescuentos = totalDescuentos.add(conjunto28);
        totalDescuentos = totalDescuentos.add(conjunto29);
        totalDescuentos = totalDescuentos.add(conjunto30);
        return totalDescuentos;
    }

    public void setTotalDescuentos(BigDecimal totalDescuentos) {
        this.totalDescuentos = totalDescuentos;
    }

    public BigDecimal getTotalGastos() {
        totalGastos = conjunto31.add(conjunto32);
        totalGastos = totalGastos.add(conjunto33);
        totalGastos = totalGastos.add(conjunto34);
        totalGastos = totalGastos.add(conjunto35);
        totalGastos = totalGastos.add(conjunto36);
        totalGastos = totalGastos.add(conjunto37);
        return totalGastos;
    }

    public void setTotalGastos(BigDecimal totalGastos) {
        this.totalGastos = totalGastos;
    }

    public BigDecimal getTotalPasivos() {
        totalPasivos = conjunto38.add(conjunto39);
        totalPasivos = totalPasivos.add(conjunto40);
        totalPasivos = totalPasivos.add(conjunto41);
        totalPasivos = totalPasivos.add(conjunto42);
        totalPasivos = totalPasivos.add(conjunto43);
        totalPasivos = totalPasivos.add(conjunto44);
        return totalPasivos;
    }

    public void setTotalPasivos(BigDecimal totalPasivos) {
        this.totalPasivos = totalPasivos;
    }

    public BigDecimal getGranTotal() {
        granTotal = conjunto45.add(getTotalDescuentos());
        granTotal = granTotal.add(getTotalGastos());
        granTotal = granTotal.add(getTotalPagos());
        granTotal = granTotal.add(getTotalPasivos());
        return granTotal;
    }

    public void setGranTotal(BigDecimal granTotal) {
        this.granTotal = granTotal;
    }

    public BigDecimal getNetos() {
        netos = totalPagos.subtract(totalDescuentos);
        return netos;
    }

    public void setNetos(BigDecimal netos) {
        this.netos = netos;
    }

    public String getCargo() {
        if(cargo == null){
            cargo = "";
        }
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getEstructura() {
        if(estructura == null){
            estructura = "";
        }
        return estructura;
    }

    public void setEstructura(String estructura) {
        this.estructura = estructura;
    }

    public BigDecimal getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(BigDecimal codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (SecuenciaFiltro != null ? SecuenciaFiltro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VWDSolucionesNodosN)) {
            return false;
        }
        VWDSolucionesNodosN other = (VWDSolucionesNodosN) object;
        if ((this.SecuenciaFiltro == null && other.SecuenciaFiltro != null) || (this.SecuenciaFiltro != null && !this.SecuenciaFiltro.equals(other.SecuenciaFiltro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.VWDSolucionesNodosN[ id=" + SecuenciaFiltro + " ]";
    }

}
