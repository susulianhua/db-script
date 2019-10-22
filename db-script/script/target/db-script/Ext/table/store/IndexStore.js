Ext.define( 'Ext.table.store.IndexStore', {
    extend: 'Ext.data.Store'
,    model: 'Ext.table.model.IndexModel',
    autoLoad: true,
    method: 'post',
    disableSelection: false,
    data: [],
    proxy: {
        type: 'ajax',
        url: 'http://localhost:8080/dbscript//table/index',
        reader: {
            type: 'json',
            root: 'data',
            totalProperty: 'total'
        }
    }
});