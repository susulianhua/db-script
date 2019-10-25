Ext.define('Ext.procedure.store.ParameterStore', {
    extend: 'Ext.data.Store',
    model: 'Ext.procedure.model.ParameterModel',
    autoLoad: false,
    disabledSelection: false,
    data: [],
    proxy: {
        type: 'ajax',
        url: 'http://localhost:8080//dbscript/procedure/getParameterStore',
        reader: {
            type: 'json',
            root: 'data',
            totalProperty: 'total'
        }
    }
})