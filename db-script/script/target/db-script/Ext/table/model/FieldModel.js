Ext.define('Ext.table.model.FieldModel', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'standardFieldId', type: 'string'},
        { name: 'id', type: 'string' },
        { name: 'primary', type: 'boolean'},
        { name: 'notNull', type: 'boolean'},
        { name: 'unique', type: 'boolean'},
        { name: 'autoIncrease', type: 'boolean'}
    ]
});