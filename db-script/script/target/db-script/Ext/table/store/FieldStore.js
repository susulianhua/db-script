Ext.define( 'Ext.table.store.FieldStore', {
    extend: 'Ext.data.Store',
    model: 'Ext.table.model.FieldModel',
    autoLoad: false,
    disableSelection: false,
    data:[],
    proxy: {
        type: 'ajax',
        url: 'http://localhost:8080/dbscript//table/field',
        reader: {
            type: 'json',
            root: 'data',
            totalProperty: 'total'
        }
    }
});