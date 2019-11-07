Ext.Loader.setConfig({ enabled: true,});
Ext.Loader.setPath({
    'Ext':'Ext'
});
Ext.onReady(function () {
    var store = Ext.create('Ext.comparison.store.SqlResultStore');
    var metadataGrid = Ext.create('Ext.grid.Panel', {
        id: 'metadataGrid',
        disableSelection: false,
        store: store,
        loadMask: true,
        height: 400,
        selType: 'rowmodel',
        autoScroll:true,
        columns: [
            { dataIndex: 'databaseObject', text: '数据库对象', align: 'center', width: 100},
            { dataIndex: 'databaseType', text: '对象类型', align: 'center', width: 100},
            { dataIndex: 'differentType', text: '差异类型', align: 'center', width: 100},
            { dataIndex: 'differentDetail', text: '差异细节', align: 'center', width: 200},
            {
                text: '操作',
                xtype: 'actioncolumn',
                align:"center",
                width: 100,
                items: [
                    {
                        tooltip: '查看',
                        icon: "css/images/editor/edit.png",
                        handler: function(metadataGrid, rowIndex) {
                            readGridDetail(metadataGrid, rowIndex)
                        }
                    },'-',
                    {
                        tooltip: '删除',
                        icon: "css/images/grid/delete.gif",
                        handler: function(metadataGrid, rowIndex) {
                            Ext.MessageBox.confirm('提示','是否确定删除该语句', function (btn) {
                                if(btn == 'yes'){
                                    var record = metadataGrid.getStore().getAt(rowIndex);
                                    metadataGrid.store.remove(record);
                                }
                            })
                        }
                    }
                ]
            }
        ]
    })
    var panel = Ext.create('Ext.panel.Panel',{
        width: 700,
        height: 450,
        renderTo: 'comparison',
        frame: true,
        dockedItems: [
            {
                xtype: 'toolbar',
                dock: 'top',
                items: [
                    { xtype: 'tbfill'},
                    { xtype: 'button', text: '增量生成', handler: function () {
                            store.load({params: { type: 'getChangeSql'}});
                        }},'-',,'         ',
                    { xtype: 'button', text: '全量生成', handler: function () {
                        store.load({params: { type: 'getFullSql'}})
                        }},'-','          ',
                    { xtype: 'button', text: '导出', handler: function () {

                        }},'-','          ',
                    { xtype: 'button', text: '返回', handler: function () {
                            document.location.href="index.html";
                        }}
                ]
            }
        ],
        items: [
            metadataGrid
        ]
    });

    function readGridDetail(metadataGrid, rowIndex) {
        var record = metadataGrid.getStore().getAt(rowIndex).data;
        var differentDetail = record.differentDetail;
        var sqlResultGrid = Ext.create('Ext.form.Panel', {
            bodyStyle: 'padding:5px 5px 0',
            height: 250,
            width: 400,
            title: '差异细节',
            items: [
                { name: 'differentDetail', fieldLabel: 'sql语句', readyOnly: true, value: differentDetail,
                  xtype: 'textarea', width: 400, height: 200}
            ],
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
        var win = Ext.create("Ext.window.Window", {
            draggable: true,
            height: 300,                          //高度
            width: 500,                           //宽度
            layout: "fit",                        //窗口布局类型
            modal: true, //是否模态窗口，默认为false
            resizable: false,
            items: [sqlResultGrid]
        });
        win.show();
    }

})