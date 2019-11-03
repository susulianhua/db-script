Ext.define('Ext.standardType.component.DialectTypeGrid', {
    extend: 'Ext.grid.Panel',
    disableSelection: false,
    loadMask: true,
    height: 200,
    title: 'DialectType',
    selType: 'rowmodel',
    autoScroll:true,
    standardTypeId: null,

    initComponent: function(){
        this.columns = this.createColumns();
        this.callParent(arguments);
    },

    createColumns: function () {
        var me = this;
        return [
            { dataIndex: 'language', text: 'languageType', width: 120, align: 'center'},
            { dataIndex: 'baseType', text: 'baseType' , width: 120, align: 'center'},
            { dataIndex: 'extType', text: 'extType', width :90, align: 'center'},
            { dataIndex: 'defaultValue', text: 'defaultValue', width: 90, align: 'center'},
            {   text: 'index-field',
                xtype: 'gridcolumn',
                width: 120,
                align: 'center',
                renderer: function (value, metaData, record) {
                    var id = metaData.record.id;
                    metaData.tdAttr = 'data-qtip="新增编辑index-field"';
                    Ext.defer(function () {
                        Ext.widget('button', {
                            renderTo: id,
                            height: 20,
                            width: 50,
                            // style:"margin-left:5px;background:blue;",
                            text: '查看',
                            handler: function () {
                                me.getPlaceholderValueGrid(record);
                            }
                        });
                    }, 50);
                    return Ext.String.format('<div id="{0}"></div>', id);
                }}
        ]
    },

    getPlaceholderValueGrid: function (record) {
         var me = this;
        var placeholderValueStore = Ext.create('Ext.standardType.store.PlaceholderValueStore');
        placeholderValueStore.load({params: {standardTypeId: me.standardTypeId, languageType: record.data.language}})
         var placeholderValueGrid = Ext.create('Ext.grid.Panel', {
             title: 'placeholderValue',
             height: 280,
             store: placeholderValueStore,
             columns: [
                 { dataIndex: 'id', text: 'id', align: 'center', width: 100},
                 { dataIndex: 'name', text: 'name', align: 'center', width: 100},
                 { dataIndex: 'value', text: 'value', align: 'centre', width: 100}
             ]
         })

        var win = Ext.create("Ext.window.Window", {
            draggable: true,
            height: 300,                          //高度
            width: 400,                           //宽度
            layout: "fit",                        //窗口布局类型
            modal: true, //是否模态窗口，默认为false
            resizable: false,
            items: [placeholderValueGrid],
            buttonAlign: 'center',
            buttons: [
                {
                    text: '关闭',
                    handler: function () {
                        win.close();
                    }
                }
            ]
        });
        win.show();
    }
})