Ext.define( 'Ext.table.store.ForeignKeyStore', {
    extend: 'Ext.data.Store',
    model: 'Ext.table.model.ForeignKeyModel',
    autoLoad: false,
    disableSelection: false,
    data: [],
    proxy: {
        type: 'ajax',
        url: 'http://localhost:8080/dbscript//table/foreign',
        reader: {
            type: 'json',
            root: 'data',
            totalProperty: 'total'
        }
    }
});