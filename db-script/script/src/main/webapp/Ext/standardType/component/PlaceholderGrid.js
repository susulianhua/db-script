Ext.define('Ext.standardType.component.PlaceholderGrid', {
    extend: 'Ext.grid.Panel',
    title: 'Placeholder',
    height: 170,
    disableSelection: false,
    loadMask: true,
    frame: true,

    initComponent: function(){
      this.columns = this.createColumns();
      this.callParent(arguments);
    },

    createColumns: function () {
        return [
            { dataIndex: 'name', text: 'name', width: 115, align: 'center'},
            { dataIndex: 'title', text: 'title' , width: 120, align: 'center'},
            { dataIndex: 'description', text: 'description', width : 120, align: 'center'},
        ]
    },
})