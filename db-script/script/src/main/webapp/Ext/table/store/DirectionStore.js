Ext.define('Ext.table.store.DirectionStore',{
    extend: 'Ext.data.Store',
    fields: [ 'direction', 'name'],
    data: [
        { 'direction' : 'asc', 'name': 'asc'},
        { 'direction' : 'desc', 'name': 'desc'}
    ]
})