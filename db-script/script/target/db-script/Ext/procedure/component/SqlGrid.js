Ext.define('Ext.procedure.component.SqlGrid',{
    extend: 'Ext.gird.Panel',
    moduleName: null,
    disableSelection: false,
    loadMask: true,
    selType: 'rowmodel',
    autoScroll:true,
    sqlStore: null,

    initComponent: function(){
        this.columns = this.createColumns();
        this.dockedItems = this.createDockedItems();
        this.callParent(arguments);
    },

    createColumns: function () {
        var me = this;
        return [
            { dataIndex: 'dialectTypeName', text: 'dialectTypeName', width: 60, align: 'center'},
            { dataIndex: 'content', text: 'content' , width: 140, align: 'center'},
            {
                text: '操作',
                xtype: 'actioncolumn',
                align:"center",
                width: 90,
                items: [
                    {
                        tooltip: '编辑',
                        icon: "css/images/editor/edit.png",
                        handler: function(sqlGrid, rowIndex, colIndex) {
                            me.editSqlBody(sqlGrid, rowIndex, colIndex);
                        }
                    },'-',
                    {
                        tooltip: '删除',
                        icon: "css/images/grid/delete.gif",
                        handler: function(sqlGrid, rowIndex) {
                            me.deleteSqlBody(sqlGrid, rowIndex);
                        }
                    }
                ]
            }
        ]
    },

    createDockedItems: function(){
        var me = this;
        return [
            {
                xtype:'toolbar',
                dock: 'top',
                items: [
                    { xtype: 'tbfill' },
                    {
                        xtype:'button',
                        handler: function(){
                            me.addSqlBody()
                        },
                        border: '1px',
                        width: 60,
                        text:'新增'
                    }
                ]
            }
        ]
    },

    addSqlBody: function(){
        var me = this;
        var sqlBodyForm = new Ext.FormPanel({
            //labelAlign: 'top',
            bodyStyle: 'padding:5px 5px 0',
            layout: 'column',
            title: 'sql新增',
            items: [
                {
                    layout: 'form',
                    columnWidth: 0.5,
                    frame: true,
                    items: { fieldLabel: 'dialectTypeName', name: 'dialectTypeName', xtype: 'textfiled'}
                },
                {
                    layout: 'form',
                    columnWidth: 0.5,
                    frame: true,
                    border: false,
                    items: { fieldLabel: 'Content', name: 'content', xtype: 'textarea'}
                }
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
            items: [sqlBodyForm]
        });
        win.show();
    },

    deleteSqlBody:function(sqlBodyGrid, rowIndex){
        Ext.MessageBox.confirm('提示','是否确认删除该sql',function (btn) {
            if(btn == 'yes'){
                var record = sqlBodyGrid.getStore().getAt(rowIndex);
                sqlBodyGridGrid.store.remove(record);
                Ext.Msg.alert('成功','删除成功');
            };
        });
    },

    editSqlBody: function(sqlBodyGrid, rowIndex) {
        var me = this;
        var record = sqlBodyGrid.getStore().getAt(rowIndex).data;
        var sqlBodyForm = new Ext.FormPanel({
            bodyStyle: 'padding:5px 5px 0',
            layout: 'column',
            title: 'sql修改',
            items: [
                {
                    layout: 'form',
                    columnWidth: 0.5,
                    frame: true,
                    items: { fieldLabel: 'dialectTypeName', name: 'dialectTypeName', xtype: 'textfield'},
                },
                {
                    layout: 'form',
                    columnWidth: 0.5,
                    frame: true,
                    border: false,
                    items:
                        { fieldLabel: 'content', name: 'content', xtype: 'textarea'},
                }
            ],
            buttonAlign: 'center',
            buttons: [
                {
                    text: '保存',
                    handler: function () {
                        var del = sqlBodyGrid.getStore().getAt(rowIndex);
                        var record = this.up('form').getForm().getValues();
                        sqlBodyGrid.store.remove(del);
                        sqlBodyGrid.store.insert(rowIndex,record);
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
        sqlBodyForm.getForm().setValues(record);
        var win = Ext.create("Ext.window.Window", {

            draggable: true,
            height: 300,                          //高度
            width: 500,
            layout: "fit",
            modal: true, //是否模态窗口，默认为false
            resizable: false,
            items: [sqlBodyForm]
        });
        win.show();
    }

})