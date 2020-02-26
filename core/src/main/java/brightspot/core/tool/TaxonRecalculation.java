package brightspot.core.tool;

import java.util.Date;

import com.psddev.dari.db.Database;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Record;

/**
 * Class for describing a taxon recalculation event. Create one to recalculate the {{@code methodName}} for all results
 * of the {{@code ancesstorQuery}}. Ingested then deleted by {{@link TaxonRecalculateTask}}.
 */
public class TaxonRecalculation extends Record {

    private Query<Object> ancestorQuery;

    private String methodName;

    @Indexed
    private Date calculationSaveDate;

    public TaxonRecalculation() {
    }

    public TaxonRecalculation(Query<Object> ancestorQuery, String methodName) {
        super();
        this.ancestorQuery = ancestorQuery;
        this.methodName = methodName;
        this.calculationSaveDate = new Date(Database.Static.getDefault().now());
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Date getCalculationSaveDate() {
        return calculationSaveDate;
    }

    public void setCalculationSaveDate(Date calculationSaveDate) {
        this.calculationSaveDate = calculationSaveDate;
    }

    public Query<Object> getAncestorQuery() {
        return ancestorQuery;
    }

    public void setAncestorQuery(Query<Object> ancestorQuery) {
        this.ancestorQuery = ancestorQuery;
    }
}
