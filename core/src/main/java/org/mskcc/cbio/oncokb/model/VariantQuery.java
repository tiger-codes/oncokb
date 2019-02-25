package org.mskcc.cbio.oncokb.model;



import org.mskcc.cbio.oncokb.model.oncotree.TumorType;;

import java.util.List;

/**
 * TumorType generated by hbm2java
 */
public class VariantQuery implements java.io.Serializable {
    private String id; //Optional, This id is passed from request. The identifier used to distinguish the query
    private Gene gene;
    private String queryAlteration;
    private String queryTumorType;
    private String consequence;
    private Integer proteinStart;
    private Integer proteinEnd;
    private Alteration exactMatchAlteration;
    private List<Alteration> alterations;
    private List<TumorType> tumorTypes;

    public VariantQuery() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Gene getGene() {
        return gene;
    }

    public void setGene(Gene gene) {
        this.gene = gene;
    }

    public String getQueryAlteration() {
        return queryAlteration;
    }

    public void setQueryAlteration(String queryAlteration) {
        this.queryAlteration = queryAlteration;
    }

    public Alteration getExactMatchAlteration() {
        return exactMatchAlteration;
    }

    public void setExactMatchAlteration(Alteration exactMatchAlteration) {
        this.exactMatchAlteration = exactMatchAlteration;
    }

    public List<Alteration> getAlterations() {
        return alterations;
    }

    public void setAlterations(List<Alteration> alterations) {
        this.alterations = alterations;
    }

    public String getQueryTumorType() {
        return queryTumorType;
    }

    public void setQueryTumorType(String queryTumorType) {
        this.queryTumorType = queryTumorType;
    }

    public List<TumorType> getTumorTypes() {
        return tumorTypes;
    }

    public void setTumorTypes(List<TumorType> tumorTypes) {
        this.tumorTypes = tumorTypes;
    }

    public String getConsequence() {
        return consequence;
    }

    public void setConsequence(String consequence) {
        this.consequence = consequence;
    }

    public Integer getProteinStart() {
        return proteinStart;
    }

    public void setProteinStart(Integer proteinStart) {
        this.proteinStart = proteinStart;
    }

    public Integer getProteinEnd() {
        return proteinEnd;
    }

    public void setProteinEnd(Integer proteinEnd) {
        this.proteinEnd = proteinEnd;
    }
}


