Ext.define('Ext.standardType.store.PlaceholderValueStore', {
    extend: 'Ext.data.Store',
    autoLoad: false,
    fields: [ 'id', 'name', 'value'],
    proxy: {
        type: 'ajax',
        url: 'http://localhost:8080//dbscript/standardType/getPlaceholderValueStore',
        data: [],
        reader: {
            type: 'json',
            root: 'data',
            totalProperty: 'total'
        }
    }
})