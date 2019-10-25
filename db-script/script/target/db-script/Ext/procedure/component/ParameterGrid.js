Ext.define('Ext.procedure.component.ParameterGrid', {
    extend: 'Ext.grid.Panel',
    moduleName: null,
    disableSelection: false,
    loadMask: true,
    selType: 'rowmodel',
    autoScroll:true,
    parameterStore: null,

    initComponent: function(){
        this.columns = this.createColumns();
        this.dockedItems = this.createDockedItems();
        this.callParent(arguments);
    },

    createColumns: function () {
        var me = this;
        return [
            { dataIndex: 'standardFieldId', text: 'standardFieldId', width: 60, align: 'center'},
            { dataIndex: 'parameterType', text: 'parameterType' , width: 140, align: 'center'},
            {
                text: '操作',
                xtype: 'actioncolumn',
                align:"center",
                width: 90,
                items: [
                    {
                        tooltip: '编辑',
                        icon: "css/images/editor/edit.png",
                        handler: function(parameterGrid, rowIndex, colIndex) {
                            me.editParameter(parameterGrid, rowIndex, colIndex);
                        }
                    },'-',
                    {
                        tooltip: '删除',
                        icon: "css/images/grid/delete.gif",
                        handler: function(parameterGrid, rowIndex) {
                            me.deleteParameter(parameterGrid, rowIndex);
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
                            me.addParameter()
                        },
                        border: '1px',
                        width: 60,
                        text:'新增'
                    }
                ]
            }
        ]
    },

    addParameter: function(){
        var me = this;
        var parameterForm = new Ext.FormPanel({
            //labelAlign: 'top',
            bodyStyle: 'padding:5px 5px 0',
            layout: 'column',
            title: 'parameter新增',
            items: [
                {
                    layout: 'form',
                    columnWidth: 0.5,
                    frame: true,
                    items: { fieldLabel: 'standardFieldId', name: 'standardFieldId', xtype: 'textfiled'}
                },
                {
                    layout: 'form',
                    columnWidth: 0.5,
                    frame: true,
                    border: false,
                    items: { fieldLabel: 'parameterType', name: 'parameterType', xtype: 'combobox'}
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
            items: [parameterForm]
        });
        win.show();
    },

    deleteParameter:function(parameterGrid, rowIndex){
        Ext.MessageBox.confirm('提示','是否确认删除该parameter',function (btn) {
            if(btn == 'yes'){
                var record = parameterGrid.getStore().getAt(rowIndex);
                parameterGrid.store.remove(record);
                Ext.Msg.alert('成功','删除成功');
            };
        });
    },

    editParameter: function(parameterGrid, rowIndex) {
        var me = this;
        var record = parameterGrid.getStore().getAt(rowIndex).data;
        var parameterForm = new Ext.FormPanel({
            bodyStyle: 'padding:5px 5px 0',
            layout: 'column',
            title: 'parameter修改',
            items: [
                {
                    layout: 'form',
                    columnWidth: 0.5,
                    frame: true,
                    items: { fieldLabel: 'standardFieldId', name: 'standardFieldId', xtype: 'textfield'},
                },
                {
                    layout: 'form',
                    columnWidth: 0.5,
                    frame: true,
                    border: false,
                    items:
                        { fieldLabel: 'parameterType', name: 'parameterType', xtype: 'combobox'},
                }
            ],
            buttonAlign: 'center',
            buttons: [
                {
                    text: '保存',
                    handler: function () {
                        var del = parameterGrid.getStore().getAt(rowIndex);
                        var record = this.up('form').getForm().getValues();
                        parameterGrid.store.remove(del);
                        parameterGrid.store.insert(rowIndex,record);
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
        parameterForm.getForm().setValues(record);
        var win = Ext.create("Ext.window.Window", {

            draggable: true,
            height: 300,                          //高度
            width: 500,
            layout: "fit",
            modal: true, //是否模态窗口，默认为false
            resizable: false,
            items: [parameterForm]
        });
        win.show();
    }
})