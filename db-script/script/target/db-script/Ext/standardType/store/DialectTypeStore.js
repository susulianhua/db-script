Ext.define('Ext.standardType.store.DialectTypeStore', {
    extend: 'Ext.data.Store',
    model: 'Ext.standardType.model.DialectTypeModel',
    autoLoad: false,
    disabledSelection: false,
    data: [],
    proxy: {
        type: 'ajax',
        url: 'http://localhost:8080//dbscript/standardType/getDialectTypeStore',
        reader: {
            type: 'json',
            root: 'data',
            totalProperty: 'total'
        }
    }
})