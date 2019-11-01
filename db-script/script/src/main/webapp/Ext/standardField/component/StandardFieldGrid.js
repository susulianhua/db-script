Ext.define('Ext.standardField.component.StandardFieldGrid',{
    extend: 'Ext.grid.Panel',
    title: '标准字段',
    height: 350,
    disableSelection: false,
    loadMask: true,
    selType: 'rowmodel',
    autoScroll:true,
    businessTypeIdStore: null,

    initComponent: function(){
        this.columns = this.createColumns();
        this.dockedItems = this.createDockedItems();
        this.callParent(arguments);
    },

    createColumns: function () {
        var me = this;
        return [
                { dataIndex: 'typeId', text: 'businessTypeId', width: 110, align: 'center'},
                { dataIndex: 'id', text: 'id', width: 80, align: 'center'},
                { dataIndex: 'name', text: 'name', width: 90, align: 'center'},
                { dataIndex: 'title', text: 'title', width: 110, align: 'center'},
                { dataIndex: 'description', text: 'description', width: 100, align: 'center'},
                    {
                        text: '操作',
                        xtype: 'actioncolumn',
                        align:"center",
                        width: 90,
                        items: [
                            {
                                tooltip: '编辑',
                                icon: "css/images/editor/edit.png",
                                handler: function(standardFieldGrid, rowIndex) {
                                    me.editStandardField(standardFieldGrid, rowIndex);
                                }
                            },'-',
                            {
                                tooltip: '删除',
                                icon: "css/images/grid/delete.gif",
                                handler: function(standardFieldGrid, rowIndex) {
                                    me.deleteStandardField(standardFieldGrid, rowIndex);
                                }
                            }
                        ]
                    }
            ];
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
                            me.standardFieldAdd()
                        },
                        border: '1px',
                        width: 60,
                        text:'新增'
                    }
                ]
            }
        ]
    },

    standardFieldAdd: function(){
        var me = this;
        var standardFieldForm = new Ext.FormPanel({
            //labelAlign: 'top',
            bodyStyle: 'padding:5px 5px 0',
            layout: 'column',
            title: '标准字段新增',
            items: [
                {
                    layout: 'form',
                    columnWidth: 0.5,
                    frame: true,
                    items: [
                        { fieldLabel: 'businessTypeId', name: 'typeId', xtype: 'combobox', mode: 'remote',
                        store: me.businessTypeIdStore, displayField: 'name', valueFiled: 'typeId' },
                        { fieldLabel: 'name', name: 'name', xtype: 'textfield'},
                        { fieldLabel: 'description', name: 'description', xtype: 'textfield'}
                    ]
                },
                {
                    layout: 'form',
                    columnWidth: 0.5,
                    frame: true,
                    border: false,
                    items: [
                        { fieldLabel: 'id', name: 'id', xtype: 'textfield', regex: /^\w+$/, allowBlank: false},
                        { fieldLabel: 'title', name: 'title', xtype: 'textfield'},
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
            items: [standardFieldForm]
        });
        win.show();
    },

    deleteStandardField: function(standardFieldGrid, rowIndex){
        Ext.MessageBox.confirm('提示','是否确认删除该标准字段',function (btn) {
            if(btn == 'yes'){
                var record = standardFieldGrid.getStore().getAt(rowIndex);
                standardFieldGrid.store.remove(record);
                Ext.Msg.alert('成功','删除成功');
            };
        });
    },

    editStandardField: function (standardFieldGrid, rowIndex) {
        var record = standardFieldGrid.getStore().getAt(rowIndex).data;
        var standardFieldForm = new Ext.FormPanel({
            bodyStyle: 'padding:5px 5px 0',
            layout: 'column',
            title: '标准字段修改',
            items: [
                {
                    layout: 'form',
                    columnWidth: 0.5,
                    frame: true,
                    items: [
                        { fieldLabel: 'businessTypeId', name: 'typeId', xtype: 'combobox', store: me.businessTypeIdStore,
                          displayField: 'name', fieldValue: 'typeId', mode: 'remote',},
                        { fieldLabel: 'name', name: 'name', xtype: 'textfield'},
                        { fieldLabel: 'description', name: 'description', xtype: 'textfield'}
                    ]
                },
                {
                    layout: 'form',
                    columnWidth: 0.5,
                    frame: true,
                    border: false,
                    items: [
                        { fieldLabel: 'id', name: 'id', xtype: 'textfield', regex: /^\w+$/, allowBlank: false},
                        { fieldLabel: 'title', name: 'title', xtype: 'textfield'}
                    ]
                }
            ],
            buttonAlign: 'center',
            buttons: [
                {
                    text: '保存',
                    handler: function () {
                        var del = standardFieldGrid.getStore().getAt(rowIndex);
                        var record = this.up('form').getForm().getValues();
                        standardFieldGrid.store.remove(del);
                        standardFieldGrid.store.insert(rowIndex,record);
                        win.close();
                    }
                },
                {
                    text: '关闭',
                    handler: function () {
                        win.close();
                    }
                }
            ]
        });
        standardFieldForm.getForm().setValues(record);
        var win = Ext.create("Ext.window.Window", {

            draggable: true,
            height: 300,                          //高度
            width: 500,
            layout: "fit",
            modal: true, //是否模态窗口，默认为false
            resizable: false,
            items: [standardFieldForm]
        });
        win.show();
    },



})