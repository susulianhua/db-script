Ext.define('Ext.comparison.store.SqlResultStore', {
    extend: 'Ext.data.Store',
    model: 'Ext.comparison.model.SqlResultModel',
    data: [],
    autoLoad: false,
    proxy: {
        url: 'http://localhost:8080//dbscript/comparison/getChangeSql',
        type: 'ajax',
        reader: {
            type: 'json',
            root: 'data',
            totalProperty: 'total'
        }
    }
})