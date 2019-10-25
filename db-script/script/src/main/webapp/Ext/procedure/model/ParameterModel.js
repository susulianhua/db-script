Ext.define('Ext.procedure.model.ParameterModel', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'name', type: 'string'},
        { name: 'standardFieldId', type: 'string'},
        { name: 'parameterType', type: 'string'}
    ]
})