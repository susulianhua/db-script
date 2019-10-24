Ext.define('Ext.businessType.store.BusinessTypeStore', {
    extend:  'Ext.data.Store',
    model: 'Ext.businessType.model.BusinessTypeModel',
    autoLoad: false,
    disableSelection: false,
    data: [],
    proxy: {
        type: 'ajax',
        url: 'http://localhost:8080/dbscript/businessType/getBusinessTypeStore',
        reader: {
            type: 'json',
            root: 'data',
            totalProperty: 'total'
        }
    }
})