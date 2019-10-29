Ext.define('Ext.view.store.RefViewIdStore', {
    extend: 'Ext.data.Store',
    autoLoad: false,
    model: 'Ext.view.model.RefViewIdModel',
    data: [],
    proxy: {
        url: 'http://localhost:8080//dbscript/view/getRefViewIdStore',
        type: 'ajax',
        reader: {
            type: 'json',
            root: 'data',
            totalProperty: 'total'
        }
    }
})