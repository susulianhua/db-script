Ext.define('Ext.function.store.DialectStore', {
    extend: 'Ext.data.Store',
    autoLoad: false,
    fields: [
        { name: 'name', type: 'string'},
        { name: 'functionName', type: 'string'}
    ],
    proxy:{
        url: 'http://localhost:8080//dbscript/function/getDialectStore',
        type: 'ajax',
        reader:{
            type: 'json',
            root: 'data',
            totalProperty: 'total'
        }
    }

})