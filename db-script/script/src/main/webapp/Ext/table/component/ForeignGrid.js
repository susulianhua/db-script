Ext.define('Ext.table.component.ForeignGrid',{
    extend: 'Ext.grid.Panel',
    title: '外键',
    height: 270,
    bodyStyle: 'padding: 0px',
    disableSelection: false,
    loadMask: true,
    selType: 'rowmodel',
    autoScroll:true,
    initComponent: function(){
        this.columns = this.createColumns();
        this.dockedItems = this.createDockedItems();
        this.callParent(arguments);
    },
    createColumns: function(){
        var me = this;
        return [
            { dataIndex: 'key_name', text: 'name', align: 'center'},
            { dataIndex: 'foreign_field', text: 'foreign-field', align: 'center'},
            { dataIndex: 'main_table', text: 'main-table', align: 'center'},
            { dataIndex: 'reference_field', text: 'reference-field', align: 'center', width: 110},
            {
                text: '操作',
                xtype: 'actioncolumn',
                align:"center",
                width: 90,
                items: [
                    {
                        tooltip: '编辑',
                        icon: "css/images/editor/edit.png",
                        handler: function(foreignGrid, rowIndex) {
                            me.editForeign(foreignGrid, rowIndex);
                        }
                    },'-',
                    {
                        tooltip: '删除',
                        icon: "css/images/grid/delete.gif",
                        handler: function(grid, rowIndex, colIndex) {
                            me.deleteForeign(grid, rowIndex, colIndex);
                        }
                    }
                ]
            }
        ]
    },
    createDockedItems: function(){
        var me = this;
        return  [
            {
                xtype:'toolbar',
                dock: 'top',
                items: [
                    { xtype: 'tbfill' },
                    {
                        xtype:'button',
                        id: 'btn_foreignAdd',
                        handler: function(){
                            me.foreignAdd()
                        },
                        border: '1px',
                        width: 60,
                        text:'新增'
                    }
                    ]
            }
            ]
    },

    foreignAdd: function(){
        var me = this;
        var foreignForm = new Ext.FormPanel({
            //labelAlign: 'top',
            bodyStyle: 'padding:5px 5px 0',
            layout: 'column',
            title: '外键新增',
            items: [
                {
                    layout: 'form',
                    columnWidth: 0.5,
                    frame: true,
                    items: [
                        { fieldLabel: 'name', name: 'key_name', xtype: 'textfield'},
                        { fieldLabel: 'foreign-field', name: 'foreign_field', xtype: 'textfield'}
                    ]
                },
                {
                    layout: 'form',
                    columnWidth: 0.5,
                    frame: true,
                    border: false,
                    items: [
                        { fieldLabel: 'main-table', name: 'main_table', xtype: 'textfield'},
                        { fieldLabel: 'reference-field', name: 'reference_field', xtype: 'textfield'}
                    ]
                }
            ],
            buttonAlign: 'center',
            buttons: [
                {
                    text: '保存',
                    handler: function () {
                        var record = this.up('form').getForm().getValues();
                        me.store.insert(0,record);
                        win.close();
                    }
                }, {
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
            items: [foreignForm]
        });
        win.show();
    },
    editForeign: function(foreignGrid, rowIndex){
        var record = foreignGrid.getStore().getAt(rowIndex).data;
        var foreignForm = new Ext.FormPanel({
            bodyStyle: 'padding:5px 5px 0',
            layout: 'column',
            title: '外键修改',
            items: [
                {
                    layout: 'form',
                    columnWidth: 0.5,
                    frame: true,
                    items: [
                        { fieldLabel: 'key-name', name: 'key_name', xtype: 'textfield',
                            regex: /^\w+$/, allowBlank: false},
                        { fieldLabel: 'foreign-field', name: 'foreign_field', xtype: 'textfield',
                            regex: /^\w+$/, allowBlank: false}
                            ]
                },
                {
                    layout: 'form',
                    columnWidth: 0.5,
                    frame: true,
                    border: false,
                    items: [
                        { fieldLabel: 'main-table', name: 'main_table', xtype: 'textfield',
                            regex: /^\w+$/, allowBlank: false},
                        { fieldLabel: 'reference-field', name: 'reference_field', xtype: 'textfield',
                            regex: /^\w+$/, allowBlank: false}
                            ]
                }
                ],
            buttonAlign: 'center',
            buttons: [
                {
                    text: '保存',
                    handler: function () {
                        var del = foreignGrid.getStore().getAt(rowIndex);
                        var record = this.up('form').getForm().getValues();
                        foreignGrid.store.remove(del);
                        foreignGrid.store.insert(rowIndex,record);
                        win.close(this);
                    }
                    }, {
                text: '关闭',
                handler: function () {
                    win.close(this);
                }
            }
        ]
    });
        foreignForm.getForm().setValues(record);
        var win = Ext.create("Ext.window.Window", {
            draggable: true,
            height: 300,                          //高度
            width: 500,                           //宽度
            layout: "fit",                        //窗口布局类型
            modal: true, //是否模态窗口，默认为false
            resizable: false,
            items: [foreignForm]
         });
        win.show();
        },
    deleteForeign: function(foreignGrid, rowIndex) {
        Ext.MessageBox.confirm('提示','是否确认删除该外键',function (btn) {
            if(btn == 'yes'){
                var record = foreignGrid.getStore().getAt(rowIndex);
                foreignGrid.store.remove(record);
                Ext.Msg.alert('成功','删除成功');
            };
        });
    }
})