package ie.clients.gdma2.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.UniqueConstraint;

import com.avnet.cs.commons.dao.BaseEntity;

import ie.clients.gdma2.domain.ui.DataTableDropDown;

/**
 * @author rgill
 *
 */
/**
 * @author ronan
 * 
 */
@Entity
@javax.persistence.Table(name = "COLUMN_GDMA2", uniqueConstraints = {
		@UniqueConstraint(columnNames={"NAME","table_id"})
	})
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "SEQ_COLUMN_GDMA2", allocationSize = 1)
public class Column extends BaseEntity{

	@ManyToOne
	@JoinColumn(name = "table_id", nullable = false)
	private Table table;

	@javax.persistence.Column(name = "NAME", nullable = false)
	private String name;
	
	@javax.persistence.Column(name = "COLUMN_TYPE", nullable = false )
	private int columnType;

	@javax.persistence.Column(name = "COLUMN_TYPE_STR")
	private String columnTypeString;

	@ManyToOne
	@JoinColumn(name = "DD_LOOKUP_DISPLAY")
	private Column dropDownColumnDisplay;

	@ManyToOne
	@JoinColumn(name = "DD_LOOKUP_STORE")
	private Column dropDownColumnStore;

	@javax.persistence.Column(name = "DISPLAYED", nullable = false)
	private boolean displayed = false;

	@javax.persistence.Column(name = "ALLOW_INSERT", nullable = false)
	private boolean allowInsert = false;

	@javax.persistence.Column(name = "ALLOW_UPDATE", nullable = false)
	private boolean allowUpdate = false;

	@javax.persistence.Column(name = "IS_NULLABLE", nullable = false)
	private boolean nullable = false;

	@javax.persistence.Column(name = "IS_PRIMARY_KEY", nullable = false)
	private boolean primarykey = false;

	/*none or special type User or special type Date*/
	@javax.persistence.Column(name = "SPECIAL", nullable = false)
	private String special;

	@javax.persistence.Column(name = "MIN_WIDTH")
	private Integer minWidth;

	@javax.persistence.Column(name = "MAX_WIDTH")
	private Integer maxWidth;

	@javax.persistence.Column(name = "ORDER_BY")
	private Integer orderby;

	@javax.persistence.Column(name = "COLUMN_SIZE")
	private Integer columnSize;

	@javax.persistence.Column(name = "ACTIVE", nullable = false)
	private boolean active = true;

	@javax.persistence.Column(name = "SEARCHABLE", nullable = false)
	private boolean searchable = true;

	@javax.persistence.Column(name = "COLUMN_ALIAS")
	private String alias;
	
	@javax.persistence.Transient
	private Map<Integer, String> columnValues = new LinkedHashMap<Integer, String>();

	@javax.persistence.Transient
	private List<DataTableDropDown> datatableEditorFieldOptions = new ArrayList<DataTableDropDown>();
	
	public List<DataTableDropDown> getDatatableEditorFieldOptions() {
		return datatableEditorFieldOptions;
	}
	
	public void setDatatableEditorFieldOptions(List<DataTableDropDown> datatableEditorFieldOptions) {
		this.datatableEditorFieldOptions = datatableEditorFieldOptions;
	}
	
	public Map<Integer, String> getColumnValues() {
		return columnValues;
	}

	public void setColumnValues(Map<Integer, String> columnValues) {
		this.columnValues = columnValues;
	}
	
	
	
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


	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public boolean isSearchable() {
		return searchable;
	}

	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof Column))
			return false;
		final Column that = (Column) other;
		return this.name.equals(that.getName());
	}

	public String toString() {
		return "Column [table=" + table + ", name=" + name + ", columnType="
				+ columnType + ", columnTypeString=" + columnTypeString
				+ ", dropDownColumnDisplay=" + dropDownColumnDisplay
				+ ", dropDownColumnStore=" + dropDownColumnStore
				+ ", displayed=" + displayed + ", allowInsert=" + allowInsert
				+ ", allowUpdate=" + allowUpdate + ", nullable=" + nullable
				+ ", primarykey=" + primarykey + ", special=" + special
				+ ", minWidth=" + minWidth + ", maxWidth=" + maxWidth
				+ ", orderby=" + orderby + ", columnSize=" + columnSize
				+ ", active=" + active + ", searchable=" + searchable + ", alias=" + alias + ", getId()="
				+ getId() + "]";
	}



}
