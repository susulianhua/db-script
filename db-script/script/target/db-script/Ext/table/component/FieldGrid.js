Ext.define('Ext.table.component.FieldGrid',{
    extend: 'Ext.grid.Panel',
    height: 270,
    title: '字段',
    disableSelection: false,
    loadMask: true,
    selType: 'rowmodel',
    autoScroll:true,
    fieldStore: null,
    standardFieldIdStore: null,
    initComponent: function(){
        this.columns = this.createColumns();
        this.store = this.fieldStore;
        this.dockedItems = this.createDockedItems();
        this.callParent(arguments);
    },
    createColumns: function(){
        var me = this;
        return [
            { dataIndex: 'standardFieldId', text: 'standard-field-id', width: 110, align: 'center'},
            { dataIndex: 'id', text: 'id' , width: 50, align: 'center'},
            { dataIndex: 'primary', text: 'primary', width: 70, align: 'center'},
            { dataIndex: 'unique', text: 'unique',width: 70, align: 'center'},
            { dataIndex: 'notNull',text: 'not-null', width: 70, align: 'center'},
            { dataIndex: 'autoIncrease', text: 'auto-increase',align: 'center' },
            {
                text: '操作',
                xtype: 'actioncolumn',
                align:"center",
                width: 90,
                items: [
                    {
                        tooltip: '编辑',
                        icon: "css/images/editor/edit.png",
                        handler: function(tableFieldGrid, rowIndex, colIndex) {
                            me.editTableField(tableFieldGrid, rowIndex, colIndex);
                        }
                    },'-',
                    {
                        tooltip: '删除',
                        icon: "css/images/grid/delete.gif",
                        handler: function(tableFieldGrid, rowIndex) {
                            me.deleteTableField(tableFieldGrid, rowIndex);
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
                        id: 'btn_tableFieldAdd',
                        handler: function(){
                            me.tableFieldAdd()
                        },
                        border: '1px',
                        width: 60,
                        text:'新增'
                    }
                ]
            }
        ]
    },

    //新增表格filed信息，弹出form
    tableFieldAdd: function(){
        var me = this;
        var booleanStore = Ext.create('Ext.table.store.BooleanStore');
        var tableFieldForm = new Ext.FormPanel({
            //labelAlign: 'top',
            bodyStyle: 'padding:5px 5px 0',
            layout: 'column',
            title: '字段新增',
            items: [
                {
                    layout: 'form',
                    columnWidth: 0.5,
                    frame: true,
                    items: [
                        { fieldLabel: 'standard-field-id', name: 'standardFieldId', xtype: 'combobox',
                            allowBlank: false, store: me.standardFieldIdStore, valueField: 'standardFieldId',
                            displayField: 'name', mode: 'remote', typeAhead: true,
                            listeners : {
                                'beforequery':function(e){

                                    var combo = e.combo;
                                    if(!e.forceAll){
                                        var input = e.query;
                                        // 检索的正则
                                        var regExp = new RegExp(".*" + input + ".*");
                                        // 执行检索
                                        combo.store.filterBy(function(record,id){
                                            // 得到每个record的项目名称值
                                            var text = record.get(combo.displayField);
                                            return regExp.test(text);
                                        });
                                        combo.expand();
                                        return false;
                                    }
                                }
                            }
                        },
                        { fieldLabel: 'unique', name: 'unique', xtype: 'combobox', store: booleanStore,
                            allowBlank: false, displayField: 'name', editable: false},
                        { fieldLabel: 'primary', name: 'primary', xtype: 'combobox', store: booleanStore,
                            allowBlank: false, displayField: 'name', editable: false}
                    ]
                },
                {
                    layout: 'form',
                    columnWidth: 0.5,
                    frame: true,
                    border: false,
                    items: [
                        { fieldLabel: 'id', name: 'id', xtype: 'textfield'},
                        { fieldLabel: 'not-null', name: 'notNull', xtype: 'combobox',
                            store: booleanStore, displayField: 'name' ,editable: false},
                        { fieldLabel: 'auto-increase', name: 'autoIncrease', xtype: 'combobox',
                            displayField: 'name', store: booleanStore, editable: false}
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
            items: [tableFieldForm]
        });
        win.show();
    },

    //删除grid中所选行field信息
    deleteTableField:function(tableFieldGrid, rowIndex){
    Ext.MessageBox.confirm('提示','是否确认删除该字段',function (btn) {
        if(btn == 'yes'){
            var record = tableFieldGrid.getStore().getAt(rowIndex);
            tableFieldGrid.store.remove(record);
            Ext.Msg.alert('成功','删除成功');
        };
    });
    },

    editTableField: function(tableFieldGrid, rowIndex) {
        var me = this;
        var record = tableFieldGrid.getStore().getAt(rowIndex).data;
        var booleanStore = Ext.create('Ext.table.store.BooleanStore');
        var standardFieldIdStore = Ext.create('Ext.table.store.StandardFieldIdStore');
        var tableFieldForm = new Ext.FormPanel({
            bodyStyle: 'padding:5px 5px 0',
            layout: 'column',
            title: '字段修改',
            items: [
                {
                layout: 'form',
                columnWidth: 0.5,
                frame: true,
                items: [
                    { fieldLabel: 'standard-field-id', name: 'standardFieldId', xtype: 'combobox',
                        allowBlank: false, store: me.standardFieldIdStore, displayField:'name'},
                    { fieldLabel: 'unique', name: 'unique', xtype: 'combobox', store: booleanStore,
                        displayField: 'name', editable: false},
                    { fieldLabel: 'primary', name: 'primary', xtype: 'combobox', store: booleanStore,
                        displayField: 'name', editable: false}
                ]
            },
                {
                layout: 'form',
                columnWidth: 0.5,
                frame: true,
                border: false,
                items: [
                    { fieldLabel: 'id', name: 'id', xtype: 'textfield', regex: /^\w+$/, allowBlank: false},
                    { fieldLabel: 'not-null', name: 'notNull', xtype: 'combobox', store: booleanStore,
                        displayField: 'name', editable: false},
                    { fieldLabel: 'auto-increase', name: 'autoIncrease', xtype: 'combobox', displayField: 'name',
                        store: booleanStore, editable: false}
                ]
            }
            ],
            buttonAlign: 'center',
            buttons: [
                {
                text: '保存',
                handler: function () {
                    var del = tableFieldGrid.getStore().getAt(rowIndex);
                    var record = this.up('form').getForm().getValues();
                    tableFieldGrid.store.remove(del);
                    tableFieldGrid.store.insert(rowIndex,record);
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
        tableFieldForm.getForm().setValues(record);
        var win = Ext.create("Ext.window.Window", {

            draggable: true,
            height: 300,                          //高度
            width: 500,
            layout: "fit",
            modal: true, //是否模态窗口，默认为false
            resizable: false,
            items: [tableFieldForm]
        });
        win.show();
    }
})