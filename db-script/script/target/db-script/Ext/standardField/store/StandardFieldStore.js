Ext.define('Ext.standardField.store.StandardFieldStore',{
    extend: 'Ext.data.Store',
    model: 'Ext.standardField.model.StandardFieldModel',
    autoLoad: false,
    disableSelection: false,
    data: [],
    proxy: {
        type: 'ajax',
        url: 'http://localhost:8080/dbscript/standardField/getStdidStore',
        reader: {
            type: 'json',
            root: 'data',
            totalProperty: 'total'
        }
    }
})