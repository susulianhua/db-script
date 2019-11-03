Ext.define('Ext.standardType.store.PlaceholderStore', {
    extend: 'Ext.data.Store',
    model: 'Ext.standardType.model.PlaceholderModel',
    autoLoad: false,
    disabledSelection: false,
    data: [],
    proxy: {
        type: 'ajax',
        url: 'http://localhost:8080//dbscript/standardType/getPlaceHolderStore',
        reader: {
            type: 'json',
            root: 'data',
            totalProperty: 'total'
        }
    }
})