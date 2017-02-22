package ie.clients.gdma2.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.avnet.cs.commons.dao.BaseEntity;

/**
 * @author rgill
 *
 */
/**
 * @author ronan
 * 
 */
@Entity
@javax.persistence.Table(name = "columns")
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "columns_id_seq", allocationSize = 1)
public class Column extends BaseEntity{


	@javax.persistence.Column(name = "name")
    private String name;

	@javax.persistence.Column(name = "column_type")
    private int columnType;

	@javax.persistence.Column(name = "column_type_string")
    private String columnTypeString;

	@ManyToOne
	@JoinColumn(name = "drop_down_column_display", nullable = false)
    private Column dropDownColumnDisplay;

	@ManyToOne
	@JoinColumn(name = "drop_down_column_store", nullable = false)
    private Column dropDownColumnStore;

	@javax.persistence.Column(name = "displayed")
    private boolean displayed;

	@javax.persistence.Column(name = "allow_insert")
    private boolean allowInsert;

	@javax.persistence.Column(name = "allow_update")
    private boolean allowUpdate;

	@javax.persistence.Column(name = "nullable_column")
    private boolean nullable;

	@javax.persistence.Column(name = "column_primary_key")
    private boolean primarykey;

	@javax.persistence.Column(name = "special")
    private String special;

	@ManyToOne
	@JoinColumn(name = "table_id", nullable = false)
    private Table table;

	@javax.persistence.Column(name = "min_width")
    private Integer minWidth;

	@javax.persistence.Column(name = "max_width")
    private Integer maxWidth;

	@javax.persistence.Column(name = "order_by")
    private Integer orderby;
    
	@javax.persistence.Column(name = "column_size")
    private Integer columnSize;
    
	@javax.persistence.Column(name = "active")
    private boolean active;
    
    public Integer getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(Integer columnSize) {
        this.columnSize = columnSize;
    }

    
    

    public Integer getOrderby() {
        return orderby;
    }

    public void setOrderby(Integer orderby) {
        this.orderby = orderby;
    }

    public Integer getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(Integer minWidth) {
        this.minWidth = minWidth;
    }

    public Integer getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(Integer maxWidth) {
        this.maxWidth = maxWidth;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColumnType() {
        return columnType;
    }

    public void setColumnType(int columnType) {
        this.columnType = columnType;
    }

    public String getColumnTypeString() {
        return columnTypeString;
    }

    public void setColumnTypeString(String columnTypeString) {
        this.columnTypeString = columnTypeString;
    }

    public Column getDropDownColumnDisplay() {
        return dropDownColumnDisplay;
    }

    public void setDropDownColumnDisplay(Column dropDownColumnDisplay) {
        this.dropDownColumnDisplay = dropDownColumnDisplay;
    }

    public Column getDropDownColumnStore() {
        return dropDownColumnStore;
    }

    public void setDropDownColumnStore(Column dropDownColumnStore) {
        this.dropDownColumnStore = dropDownColumnStore;
    }

    public boolean isDisplayed() {
        return displayed;
    }

    public void setDisplayed(boolean displayed) {
        this.displayed = displayed;
    }

    public boolean isAllowInsert() {
        return allowInsert;
    }

    public void setAllowInsert(boolean allowInsert) {
        this.allowInsert = allowInsert;
    }

    public boolean isAllowUpdate() {
        return allowUpdate;
    }

    public void setAllowUpdate(boolean allowUpdate) {
        this.allowUpdate = allowUpdate;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Column))
            return false;
        final Column that = (Column) other;
        return this.name.equals(that.getName());
    }

    public int hashCode() {
        return name.hashCode();
    }

    public boolean isPrimarykey() {
        return primarykey;
    }

    public void setPrimarykey(boolean primarykey) {
        this.primarykey = primarykey;
    }
    
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String toString() {
        return "Column [allowInsert=" + allowInsert + ", allowUpdate="
                + allowUpdate + ", columnType=" + columnType
                + ", columnTypeString=" + columnTypeString + ", displayed="
                + displayed + ", dropDownColumnDisplay="
                + dropDownColumnDisplay + ", dropDownColumnStore="
                + dropDownColumnStore + ", id=" + getId() + ", maxWidth=" + maxWidth
                + ", minWidth=" + minWidth + ", name=" + name + ", nullable="
                + nullable + ", orderby=" + orderby + ", primarykey="
                + primarykey + ", special=" + special + ", table=" + table
                + "]";
    }

}
