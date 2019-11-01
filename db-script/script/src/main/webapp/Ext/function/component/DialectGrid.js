Ext.define('Ext.function.component.DialectGrid', {
    extend: 'Ext.grid.Panel',
    height: 270,
    title: 'Dialect',
    disableSelection: false,
    loadMask: true,
    selType: 'rowmodel',
    autoScroll:true,
    dialectStore: null,
    initComponent: function(){
        this.columns = this.createColumns();
        this.store = this.dialectStore;
        this.dockedItems = this.createDockedItems();
        this.callParent(arguments);
    },
    createColumns: function(){
        var me = this;
        return [
            { dataIndex: 'name', text: 'name', width: 120, align: 'center'},
            { dataIndex: 'functionName', text: 'functionName' , width: 120, align: 'center'},
            {
                text: '操作',
                xtype: 'actioncolumn',
                align:"center",
                width: 90,
                items: [
                    {
                        tooltip: '编辑',
                        icon: "css/images/editor/edit.png",
                        handler: function(dialectGrid, rowIndex, colIndex) {
                            me.editDialect(dialectGrid, rowIndex, colIndex);
                        }
                    },'-',
                    {
                        tooltip: '删除',
                        icon: "css/images/grid/delete.gif",
                        handler: function(dialectGrid, rowIndex) {
                            me.deleteDialect(dialectGrid, rowIndex);
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
                        handler: function(){
                            me.dialectAdd()
                        },
                        border: '1px',
                        width: 60,
                        text:'新增'
                    }
                ]
            }
        ]
    },

    dialectAdd: function(){
        var me = this;
        var dialectForm = new Ext.FormPanel({
            //labelAlign: 'top',
            bodyStyle: 'padding:5px 5px 0',
            layout: 'column',
            title: 'dialect新增',
            layout: 'form',
            columnWidth: 0.5,
            frame: true,
            items: [
                { fieldLabel: 'name', name: 'name', xtype: 'textfield'},
                { fieldLabel: 'functionName', name: 'functionName', xtype: 'textfield'}
            ],
            buttonAlign: 'center',
            buttons: [
                {
                    text: '保存',
                    handler: function () {
                        var record = this.up('form').getForm().getValues();
                        me.store.insert(0,record);
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
        var win = Ext.create("Ext.window.Window", {

            draggable: true,
            height: 300,                          //高度
            width: 500,                           //宽度
            layout: "fit",                        //窗口布局类型
            modal: true, //是否模态窗口，默认为false
            resizable: false,
            items: [dialectForm]
        });
        win.show();
    },

    deleteDialect:function(dialectGrid, rowIndex){
        Ext.MessageBox.confirm('提示','是否确认删除该dialect',function (btn) {
            if(btn == 'yes'){
                var record = dialectGrid.getStore().getAt(rowIndex);
                dialectGrid.store.remove(record);
                Ext.Msg.alert('成功','删除成功');
            };
        });
    },

    editDialect: function(dialectGrid, rowIndex) {
        var record = dialectGrid.getStore().getAt(rowIndex).data;
        var dialectForm = new Ext.FormPanel({
            bodyStyle: 'padding:5px 5px 0',
            layout: 'column',
            title: 'dialect修改',
            layout: 'form',
            columnWidth: 0.5,
            frame: true,
            items: [
                {fieldLabel: 'name', name: 'name', xtype: 'textfield'},
                {fieldLabel: 'functionName', name: 'functionName', xtype: 'textfield'}
            ],
            buttonAlign: 'center',
            buttons: [
                {
                    text: '保存',
                    handler: function () {
                        var del = dialectGrid.getStore().getAt(rowIndex);
                        var record = this.up('form').getForm().getValues();
                        dialectGrid.store.remove(del);
                        dialectGrid.store.insert(rowIndex,record);
                        win.close(this);
                    }
                },
                {
                    text: '关闭',
                    handler: function () {
                        win.close(this);
                    }
                }
            ]
        });
        dialectForm.getForm().setValues(record);
        var win = Ext.create("Ext.window.Window", {
            draggable: true,
            height: 300,                          //高度
            width: 500,
            layout: "fit",
            modal: true, //是否模态窗口，默认为false
            resizable: false,
            items: [dialectForm]
        });
        win.show();
    }
})