Ext.define('Ext.businessType.store.StandardTypeIdStore', {
    extend: 'Ext.data.Store',
    autoLoad: false,
    fields: [ 'standardTypeId', 'name'],
    proxy: {
        data:[],
        type: 'ajax',
        url: 'http://localhost:8080//dbscript/businessType/getStandardTypeIdStore',
        reader: {
            type: 'json',
            root: 'data',
            totalProperty: 'total'
        }
    }
})