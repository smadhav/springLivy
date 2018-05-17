package saju.livy.spring.entity.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class Analysis implements Serializable {
    private static final long serialVersionUID = -4792225545555119811L;
    private List<AnalysisMeasure> measures;
    private List<AnalysisAttribute> attributes;
    private String filterType;
    private Long filterCount;
    private Boolean displayNull = Boolean.valueOf(true);

    public Analysis() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<AnalysisMeasure> getMeasures() {
        return measures;
    }

    public void setMeasures(List<AnalysisMeasure> measures) {
        this.measures = measures;
    }

    public List<AnalysisAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AnalysisAttribute> attributes) {
        this.attributes = attributes;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public Long getFilterCount() {
        return filterCount;
    }

    public void setFilterCount(Long filterCount) {
        this.filterCount = filterCount;
    }

    public Boolean getDisplayNull() {
        return displayNull;
    }

    public void setDisplayNull(Boolean displayNull) {
        this.displayNull = displayNull;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Analysis{");
        sb.append("measures=").append(measures);
        sb.append(", attributes=").append(attributes);
        sb.append(", filterType='").append(filterType).append('\'');
        sb.append(", filterCount=").append(filterCount);
        sb.append(", displayNull=").append(displayNull);
        sb.append('}');
        return sb.toString();
    }
}
