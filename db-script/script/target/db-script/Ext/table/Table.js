Ext.Loader.setConfig({ enabled: true,});
Ext.Loader.setPath({
    'Ext':'Ext'
})
Ext.require([
    'Ext.table.store.FieldStore',
]);

Ext.onReady(function () {
    Ext.QuickTips.init();

    var obj = JSON.parse(localStorage.getItem('obj'));
    var tableName = obj.tableName;
    var FileName = obj.FileName + '.table.xml';

    var fieldStore = Ext.create('Ext.table.store.FieldStore');
    var foreignKeyStore = Ext.create('Ext.table.store.ForeignKeyStore');
    var indexStore = Ext.create( 'Ext.table.store.IndexStore');
    var indexFieldStore = Ext.create( 'Ext.table.store.IndexFieldStore');
    var standardFieldIdStore = Ext.create('Ext.table.store.StandardFieldIdStore');
    var booleanStore = Ext.create('Ext.table.store.BooleanStore');

    standardFieldIdStore.load({params:{FileName: FileName}});
    var fieldGrid = Ext.create('Ext.table.grid.FieldGrid', {
        fieldStore: fieldStore,
        standardFieldIdStore: standardFieldIdStore
    });
    var foreignGrid = Ext.create('Ext.table.grid.ForeignGrid', { store: foreignKeyStore});
    var indexGrid = Ext.create('Ext.table.grid.IndexGrid', {
        store: indexStore,
        indexFieldStore: indexFieldStore,
        FileName: FileName,
        tableName: tableName
    });

    var panel = Ext.create('Ext.panel.Panel',{
        renderTo: 'bookTable',
        width: 600,
        height: 600,
        title: 'Table',
        titleAlign: 'center',
        layout: 'form',
        items: [
            {
                xtype: 'form',
                id: 'tableForm',
                frame: true,
                labelWidth: 30,
                layout: 'column',
                url: 'http://localhost:8080/dbscript/table/save',
                reader: new Ext.data.JsonReader({
                    type: 'json',
                    totalProperty: 'total',
                    root: 'data',
                }, [
                    { name: 'fileName', mapping: 'fileName'},
                    { name: 'package_name', mapping: 'package_name'},
                    { name: 'table_id', mapping: 'table_id'},
                    { name: 'table_name', mapping: 'table_name'},
                    { name: 'table_title', mapping: 'table_title'},
                    { name: 'table_description', mapping: 'table_description'}
                ]),
                items: [
                    {
                        layout: 'form',
                        columnWidth: 0.45,
                        frame: true,
                        items: [
                            { fieldLabel: '文件名', name: 'fileName', xtype: 'textfield', readOnly: true},
                            { fieldLabel: 'id', name: 'table_id', xtype: 'textfield', readOnly: true},
                            { fieldLabel: 'title', name: 'table_title', xtype: 'textfield', allowBlank: false}
                        ]
                    },
                    {
                        layout: 'form',
                        columnWidth: 0.45,
                        frame: true,
                        border: false,
                        items: [
                            { fieldLabel: 'package-name', name: 'package_name', xtype: 'textfield'},
                            { fieldLabel: 'name', name: 'table_name', xtype: 'textfield'},
                            { fieldLabel: 'description', name: 'table_description', xtype: 'textfield' },
                        ]
                    }
                ]
            },{
                xtype: 'tabpanel',
                frame: true,
                heigth: 350,
                items:[
                    fieldGrid,
                    foreignGrid,
                    indexGrid,
                ]
            },{
                xtype: 'toolbar',
                items: [
                    {
                        xtype: 'tbfill'
                    },
                    {
                        xtype: 'button',
                        id: 'btn_save',
                        text: '保存',
                        width: 60
                    }
                ]
            }

        ]

    });
    Ext.Ajax.request({
        url: 'http://localhost:8080/dbscript//table/tableBase',
        method: 'post',
        reader:{
            type: 'json',
            root: 'data',
            totalProperty: 'total'
        },
        params: {
          tableName: tableName,
          FileName: FileName
        },
        success: function (response) {
            var result = Ext.JSON.decode(response.responseText).data;
            var record = result[0];
            Ext.getCmp('tableForm').getForm().setValues(record);
        },
        failure:function () {
        }
    });
    fieldGrid.store.load({params:{FileName: FileName, tableName: tableName}});
    foreignGrid.store.load({params:{FileName: FileName, tableName: tableName}});
    indexGrid.store.load({params:{FileName: FileName, tableName: tableName}});
    indexFieldStore.load({params:{FileName: FileName, tableName: tableName}});


    /**
     * 新增按钮的实现
     * */
    Ext.getCmp("btn_tableFieldAdd").on("click", function () {
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
                            allowBlank: false, store: standardFieldIdStore, editable: false, displayField: 'name',
                            mode: 'remote'},
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
                        fieldStore.insert(0,record);
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
    });
    Ext.getCmp("btn_foreignAdd").on('click', function () {
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
                        foreignKeyStore.insert(0,record);
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
            items: [foreignForm]
        });
        win.show();
    });
    Ext.getCmp("btn_indexAdd").on("click", function () {
        var indexForm = new Ext.FormPanel({
            //labelAlign: 'top',
            bodyStyle: 'padding:5px 5px 0',
            layout: 'column',
            title: '索引新增',
            items: [
                {
                    layout: 'form',
                    columnWidth: 0.5,
                    frame: true,
                    items: [
                        { fieldLabel: 'name', name: 'index_name', xtype: 'textfield'},
                        { fieldLabel: 'description', name: 'index_description', xtype: 'textfield'}
                    ]
                },
                {
                    layout: 'form',
                    columnWidth: 0.5,
                    frame: true,
                    border: false,
                    items: [
                        { fieldLabel: 'unique', name: 'index_unique', xtype: 'combobox',
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
                        indexStore.insert(0,record);
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
            items: [indexForm]
        });
        win.show();
    });
    Ext.getCmp('btn_save').on('click', function () {
        var tableReturn = {};
        var formValues = Ext.getCmp('tableForm').getForm().getValues();
        tableReturn.tableBase = formValues;
        /**
         * 获取字段信息转化为Json数组
         */
        var tableFieldRecords = fieldStore.getRange();
        var tableFieldList = [];
        for( var i in tableFieldRecords){
            tableFieldList.push({
                'standardFieldId': tableFieldRecords[i].get('standardFieldId'),
                'id': tableFieldRecords[i].get('id'),
                'primary': tableFieldRecords[i].get('primary'),
                'notNull': tableFieldRecords[i].get('notNull'),
                'unique': tableFieldRecords[i].get('unique'),
                'autoIncrease': tableFieldRecords[i].get('autoIncrease')
            });
        };
        tableReturn.tableFieldList = tableFieldList;

        /**
         * 获取外键信息转化为Json数组
         */
        var foreignRecords = foreignKeyStore.getRange();
        console.log('foreignStore.getRange:',foreignRecords);
        var foreignList = [];
        for( var i in foreignRecords){
            foreignList.push({
                'key_name': foreignRecords[i].get('key_name'),
                'foreign_field': foreignRecords[i].get('foreign_field'),
                'main_table': foreignRecords[i].get('main_table'),
                'reference_field': foreignRecords[i].get('reference_field')
            });
        };
        console.log('foreignList:', foreignList);
        tableReturn.foreignReferences = foreignList;

        /**
         * 获取index信息转化为Json数组
         */
        var indexRecords = indexStore.getRange();
        var indexList = [];
        for( var i in indexRecords){
            indexList.push({
                'index_name': indexRecords[i].get('index_name'),
                'index_unique': indexRecords[i].get('index_unique'),
                'index_description': indexRecords[i].get('index_description')
            });
        };
        tableReturn.indexList = indexList;

        /**
         * 获取indexField信息转化为Json数组
         */
        var indexFieldRecords = indexFieldStore.getRange();
        var indexFieldList = [];
        for( var i in indexFieldRecords){
            indexFieldList.push({
                'index_name': indexFieldRecords[i].get('index_name'),
                'field': indexFieldRecords[i].get('field'),
                'direction': indexFieldRecords[i].get('direction')
            });
        };
        tableReturn.indexFieldList = indexFieldList;
        console.log('tableReturn:', tableReturn);
        Ext.Ajax.request({
            url: 'http://localhost:8080/dbscript//table/tableSave',
            headers: {'ContentType': 'application/json;charset=UTF-8',
                'Content-Type': 'application/json'
            },
            ContentType : 'application/json;charset=UTF-8',
            dataType: 'json',
            params: JSON.stringify(tableReturn),
            method: 'Post',
        });
    });
});