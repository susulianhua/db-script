Ext.define( 'Ext.table.store.IndexFieldStore', {
    extend: 'Ext.data.Store',
    model: 'Ext.table.model.IndexFieldModel',
    autoLoad: false,
    disableSelection: false,
    proxy: {
        type: 'ajax',
        url: 'http://localhost:8080/dbscript//table/indexField',
        reader: {
            root: 'data',
            type: 'json',
            totalProperty: 'total'
        }
    }
});