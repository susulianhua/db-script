Ext.define('Ext.procedure.store.DialectNameStore', {
    extend: 'Ext.data.Store',
    fields: ['dialectTypeName', 'name'],
    data: [
        { 'dialectTypeName': 'oracle', 'name': 'oracle'},
        { 'dialectTypeName': 'mysql', 'name': 'mysql'},
        { 'dialectTypeName': 'sqlserver', 'name': 'sqlserver'},
        { 'dialectTypeName': 'db2', 'name': 'db2'},
        { 'dialectTypeName': 'h2', 'name': 'h2'},
        { 'dialectTypeName': 'sybase', 'name': 'sybase'},
        { 'dialectTypeName': 'derby', 'name': 'derby'}
    ]
})