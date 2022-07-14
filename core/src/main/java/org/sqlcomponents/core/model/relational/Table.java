package org.sqlcomponents.core.model.relational;

import org.sqlcomponents.core.model.relational.enums.Flag;
import org.sqlcomponents.core.model.relational.enums.TableType;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Table {
    private final Database database;
    private String tableName;
    private String sequenceName;
    private String categoryName;
    private String schemaName;
    private TableType tableType;
    private String remarks;
    private String categoryType;
    private String schemaType;
    private String nameType;
    private String selfReferencingColumnName;
    private String referenceGeneration;
    private List<Column> columns;
    private List<Index> indices;
    private List<UniqueConstraint> uniqueColumns;

    public List<UniqueConstraint> getUniqueColumns() {
        return uniqueColumns;
    }

    public void setUniqueColumns(List<UniqueConstraint> uniqueColumns) {
        this.uniqueColumns = uniqueColumns;
    }

    public Table(final Database database) {
        this.database = database;
    }

    public boolean getHasPrimaryKey() {
        return this.getColumns().stream().filter(column -> column.getPrimaryKeyIndex() != 0).findFirst().isPresent();
    }

    public boolean getHasAutoGeneratedPrimaryKey() {
        return this.getColumns().stream()
                .filter(column -> column.getPrimaryKeyIndex() != 0 && column.getAutoIncrement() == Flag.YES).findFirst()
                .isPresent();
    }

    public String getEscapedName() {
        return this.database.escapedName(this.getTableName());
    }

    public int getHighestPKIndex() {
        int highestPKIndex = 0;
        for (Column column : columns) {
            if (highestPKIndex < column.getPrimaryKeyIndex()) {
                highestPKIndex = column.getPrimaryKeyIndex();
            }
        }
        return highestPKIndex;
    }

    public List<String> getUniqueConstraintGroupNames() {
        List<String> uniqueConstraintGroupNames = new ArrayList<String>();
        String prevUniqueConstraintGroupName = null;
        String uniqueConstraintGroupName = null;
        for (Column column : columns) {
            uniqueConstraintGroupName = column.getUniqueConstraintName();
            if (uniqueConstraintGroupName != null && !uniqueConstraintGroupName.equals(prevUniqueConstraintGroupName)) {
                uniqueConstraintGroupNames.add(uniqueConstraintGroupName);
                prevUniqueConstraintGroupName = uniqueConstraintGroupName;
            }
        }
        return uniqueConstraintGroupNames;
    }

    public SortedSet<String> getDistinctCustomColumnTypeNames() {
        SortedSet<String> distinctColumnTypeNames = new TreeSet<>();

        distinctColumnTypeNames.addAll(columns.stream()
                .filter(column -> Table.class.getResource(
                        "/template/java/custom-object/" + column.getTypeName().toLowerCase() + ".ftl") != null)
                .map(column -> column.getTypeName()).distinct().collect(Collectors.toList()));

        return distinctColumnTypeNames;
    }

    public SortedSet<String> getDistinctColumnTypeNames() {
        SortedSet<String> distinctColumnTypeNames = new TreeSet<>();

        distinctColumnTypeNames
                .addAll(columns.stream().map(column -> column.getTypeName()).distinct().collect(Collectors.toList()));

        return distinctColumnTypeNames;
    }

    public Database getDatabase() {
        return database;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public TableType getTableType() {
        return tableType;
    }

    public void setTableType(TableType tableType) {
        this.tableType = tableType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getSchemaType() {
        return schemaType;
    }

    public void setSchemaType(String schemaType) {
        this.schemaType = schemaType;
    }

    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    public String getSelfReferencingColumnName() {
        return selfReferencingColumnName;
    }

    public void setSelfReferencingColumnName(String selfReferencingColumnName) {
        this.selfReferencingColumnName = selfReferencingColumnName;
    }

    public String getReferenceGeneration() {
        return referenceGeneration;
    }

    public void setReferenceGeneration(String referenceGeneration) {
        this.referenceGeneration = referenceGeneration;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<Index> getIndices() {
        return indices;
    }

    public void setIndices(List<Index> indices) {
        this.indices = indices;
    }

    @Override
    public String toString() {
        return tableName + "::" + tableType;
    }
}
