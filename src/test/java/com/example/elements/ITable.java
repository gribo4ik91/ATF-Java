package com.example.elements;

import java.util.List;

import io.cucumber.datatable.DataTable;

public interface ITable {


    List<List<String>> getRowsDataByColumns(final List<String> columns) throws Exception;

    List<List<String>> filterRowsData(DataTable dataTable) throws Exception;



}
