Ext.define('Ext.trigger.store.SqlBodyStore',{
    extend: 'Ext.data.Store',
    model: 'Ext.procedure.model.SqlModel',
    autoLoad: false,
    disableSelection: false,
    data: [],
    proxy:{
        type: 'ajax',
        url: 'http://localhost:8080//dbscript/trigger/getSqlBodyStore',
        reader: {
            type: 'json',
            root: 'data',
            totalProperty: 'total'
        }
    }
})