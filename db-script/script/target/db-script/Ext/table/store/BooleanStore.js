Ext.define('Ext.table.store.BooleanStore',{
    extend: 'Ext.data.Store',
    fields: [ 'boolean', 'name'],
    data:[
        {'boolean': 'true', 'name': 'true'},
        {'boolean': 'false', 'name': 'false'}
    ]
})