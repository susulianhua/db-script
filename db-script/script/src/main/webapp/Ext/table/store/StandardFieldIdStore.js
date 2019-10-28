Ext.define('Ext.table.store.StandardFieldIdStore',{
    extend: 'Ext.data.Store',
    autoLoad: false,
    fields:[ 'standardFieldId','name' ],
    proxy: {
        type: 'ajax',
        url: 'http://localhost:8080/dbscript/table//stdid',
        reader: {
            type: 'json',
            root: 'data',
            totalProperty: 'total'
        }
    },
    data: []
});