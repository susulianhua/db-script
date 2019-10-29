Ext.define('Ext.procedure.store.ParameterTypeStore', {
    extend: 'Ext.data.Store',
    fields: ['parameterType', 'name'],
    data: [
        { parameterType: 'IN', name: 'IN'},
        { parameterType: 'OUT', name: 'OUT'},
        { parameterType: 'INOUT', name: 'INOUT'}
    ]
})