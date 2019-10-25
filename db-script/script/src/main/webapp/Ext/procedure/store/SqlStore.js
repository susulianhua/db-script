Ext.define('Ext.procedure.store.SqlStore', {
    extend: 'Ext.data.Store',
    model: 'Ext.procedure.model.SqlModel',
    autoLoad: false,
    disableSelection: false,
    data: [],
    proxy:{
        type: 'ajax',
        url: 'http://localhost:8080//dbscript/procedure/getSqlStore',
        reader: {
            type: 'json',
            root: 'data',
            totalProperty: 'total'
        }
    }
})