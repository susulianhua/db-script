Ext.define('Ext.standardField.store.BusinessTypeIdStore', {
    extend: 'Ext.data.Store',
    autoLoad: false,
    fields: ['typeId', 'name'],
    proxy: {
        type: 'ajax',
        url: 'http://localhost:8080/dbscript/standardField//getBusinessTypeId',
        reader: {
            type: 'json',
            root: 'data',
            totalProperty: 'total'
        }
    },
    data: []
})