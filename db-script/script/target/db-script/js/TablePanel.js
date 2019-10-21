Ext.define('js.TablePanel', {
    extend: 'Ext.window.Window',
    title: 'table',
    height: 600,
    weight: 600,
    initComponent: function () {
        var fieldModel = Ext.define('Field', {
            extend: 'Ext.data.Model',
            fields: [
                { name: 'standardFieldId', type: 'string'},
                { name: 'id', type: 'string' },
                { name: 'primary', type: 'boolean'},
                { name: 'notNull', type: 'boolean'},
                { name: 'unique', type: 'boolean'},
                { name: 'autoIncrease', type: 'boolean'}
            ]
        });
        var foreignModel = Ext.define('Foreign', {
            extend: 'Ext.data.Model',
            fields:[
                { name: 'key_name', type: 'string'},
                { name: 'foreign_field', type: 'string'},
                { name: 'main_table', type: 'string'},
                { name: 'reference_field', type: 'string'}
            ]
        });
        var indexFieldModel = Ext.define('Index_field',{
            extend: 'Ext.data.Model',
            fields: [
                { name: 'field', type: 'string'},
                { name: 'direction', type: 'string'},
                { name: 'index_name', type: 'string'}
            ]
        });
        var indexModel = Ext.define('Index', {
            extend: 'Ext.data.Model',
            fields: [
                { name: 'index_name', type: 'string'},
                { name: 'index_unique', type: 'string'},
                { name: 'index_description', type: 'string'}
            ],
            hasMany: {
                model: 'indexFieldModel',
                foreignKey: 'index_name'
            }
        });

        var fieldStore = Ext.create( 'Ext.data.Store', {
            model: fieldModel,
            autoLoad: false,
            disableSelection: false,
            data:[],
            proxy: {
                type: 'ajax',
                url: 'http://localhost:8080/dbscript//table/field',
                reader: {
                    type: 'json',
                    root: 'data',
                    totalProperty: 'total'
                }
            }
        });
        var foreignStore = Ext.create( 'Ext.data.Store', {
            model: foreignModel,
            autoLoad: false,
            disableSelection: false,
            data: [],
            proxy: {
                type: 'ajax',
                url: 'http://localhost:8080/dbscript//table/foreign',
                reader: {
                    type: 'json',
                    root: 'data',
                    totalProperty: 'total'
                }
            }
        });
        var indexStore = Ext.create( 'Ext.data.Store', {
            model: indexModel,
            autoLoad: true,
            method: 'post',
            disableSelection: false,
            data: [],
            proxy: {
                type: 'ajax',
                url: 'http://localhost:8080/dbscript//table/index',
                reader: {
                    type: 'json',
                    root: 'data',
                    totalProperty: 'total'
                }
            }
        });
        var indexFieldStore = Ext.create( 'Ext.data.Store', {
            model: indexFieldModel,
            autoLoad: false,
            disableSelection: false,
            proxy: {
                type: 'ajax',
                url: 'http://localhost:8080/dbscript//table/indexField',
                reader: {
                    root: 'data',
                    type: 'json',
                    totalProperty: 'total'
                }
            }
        });
        var uniqueStore = Ext.create('Ext.data.Store', {
            fields: [ 'unique', 'name'],
            data:[
                {'unique': 'true', 'name': 'true'},
                {'unique': 'false', 'name': 'false'}
            ]
        });
        var primaryStore = Ext.create('Ext.data.Store', {
            fields: [ 'primary', 'name'],
            data:[
                {'primary': 'true', 'name': 'true'},
                {'primary': 'false', 'name': 'false'}
            ]
        });
        var notNullStore = Ext.create('Ext.data.Store', {
            fields: [ 'notNull', 'name'],
            data:[
                {'notNull': 'true', 'name': 'true'},
                {'notNull': 'false', 'name': 'false'}
            ]
        });
        var autoIncreaseStore = Ext.create('Ext.data.Store', {
            fields: [ 'autoIncrease', 'name'],
            data:[
                {'autoIncrease': 'true', 'name': 'true'},
                {'autoIncrease': 'false', 'name': 'false'}
            ]
        });
        var directionStore = Ext.create('Ext.data.Store', {
            fields: [ 'direction', 'name'],
            data: [
                { 'direction' : 'asc', 'name': 'asc'},
                { 'direction' : 'desc', 'name': 'desc'}
            ]
        });
        var obj = JSON.parse(localStorage.getItem('obj'));
        var tableName = obj.tableName;
        var FileName = obj.FileName + '.table.xml';
        var standardFieldIdStore = Ext.create('Ext.data.Store',{
            fields:[ 'standardFieldId','name' ],
            proxy: {
                type: 'ajax',
                url: 'http://localhost:8080/dbscript//table/stdid',
                reader: {
                    type: 'json',
                    root: 'data',
                    totalProperty: 'total'
                }
            },
            data: [],
        });
        standardFieldIdStore.load({params:{FileName: FileName}});

        var panel = Ext.create('Ext.panel.Panel',{
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
                        {
                            xtype: 'grid',
                            height: 250,
                            title: '字段',
                            id: 'tableFieldGrid',
                            disableSelection: false,
                            store: fieldStore,
                            loadMask: true,
                            selType: 'rowmodel',
                            autoScroll:true,
                            columns: [
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
                                                editTableField(tableFieldGrid, rowIndex, colIndex);
                                            }
                                        },'-',
                                        {
                                            tooltip: '删除',
                                            icon: "css/images/grid/delete.gif",
                                            handler: function(tableFieldGrid, rowIndex) {
                                                deleteTableField(tableFieldGrid, rowIndex);
                                            }
                                        }
                                    ]
                                }
                            ],
                            dockedItems: [
                                {
                                    xtype:'toolbar',
                                    dock: 'top',
                                    items: [
                                        { xtype: 'tbfill' },
                                        {
                                            xtype:'button',
                                            id: 'btn_tableFieldAdd',
                                            border: '1px',
                                            width: 60,
                                            text:'新增'
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            id: 'foreignGrid',
                            xtype: 'grid',
                            title: '外键',
                            height: 250,
                            bodyStyle: 'padding: 0px',
                            disableSelection: false,
                            store: foreignStore,
                            loadMask: true,
                            selType: 'rowmodel',
                            autoScroll:true,
                            columns:[
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
                                                editForeign(foreignGrid, rowIndex);
                                            }
                                        },'-',
                                        {
                                            tooltip: '删除',
                                            icon: "css/images/grid/delete.gif",
                                            handler: function(grid, rowIndex, colIndex) {
                                                deleteForeign(grid, rowIndex, colIndex);
                                            }
                                        }
                                    ]
                                }
                            ],
                            dockedItems: [
                                {
                                    xtype:'toolbar',
                                    dock: 'top',
                                    items: [
                                        { xtype: 'tbfill' },
                                        {
                                            xtype:'button',
                                            id: 'btn_foreignAdd',
                                            border: '1px',
                                            width: 60,
                                            text:'新增'
                                        }
                                    ]
                                }
                            ]
                        },
                        {
                            id: 'indexGrid',
                            xtype: 'grid',
                            title: '索引',
                            height: 250,
                            disableSelection: false,
                            store: indexStore,
                            loadMask: true,
                            selType: 'rowmodel',
                            autoScroll:true,
                            columns: [
                                { dataIndex: 'index_name', text: 'name', align: 'center'},
                                { dataIndex: 'index_unique', text: 'unique', align: 'center'},
                                { dataIndex: 'index_description', text: 'description', align: 'center'},
                                {   text: 'index-field',
                                    xtype: 'gridcolumn',
                                    width: 150,
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
                                                text: '编辑',
                                                handler: function () {
                                                    indexFieldEdit(record);
                                                }
                                            });
                                        }, 50);
                                        return Ext.String.format('<div id="{0}"></div>', id);
                                    }},
                                {
                                    text: '操作',
                                    xtype: 'actioncolumn',
                                    align:"center",
                                    width: 90,
                                    items: [
                                        {
                                            tooltip: '编辑',
                                            icon: "css/images/editor/edit.png",
                                            handler: function(indexGrid, rowIndex) {
                                                editIndex(indexGrid, rowIndex);
                                            }
                                        },'-',
                                        {
                                            tooltip: '删除',
                                            icon: "css/images/grid/delete.gif",
                                            handler: function(indexGrid, rowIndex) {
                                                deleteIndex(indexGrid, rowIndex);
                                            }
                                        }
                                    ]

                                }
                            ],
                            dockedItems: [
                                {
                                    xtype:'toolbar',
                                    dock: 'top',
                                    items: [
                                        { xtype: 'tbfill' },
                                        {
                                            xtype:'button',
                                            id: 'btn_indexAdd',
                                            border: '1px',
                                            width: 60,
                                            text:'新增'
                                        }
                                    ]
                                }
                            ],
                        }
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
        fieldStore.load({params:{FileName: FileName, tableName: tableName}});
        indexStore.load({params:{FileName: FileName, tableName: tableName}});
        foreignStore.load({params:{FileName: FileName, tableName: tableName}});
        indexFieldStore.load({params:{FileName: FileName, tableName: tableName}});

        /**
         * 字段grid中编辑修改图标的实现
         * */
        function editTableField(tableFieldGrid, rowIndex) {
            var record = tableFieldGrid.getStore().getAt(rowIndex).data;
            var tableFieldForm = new Ext.FormPanel({
                //labelAlign: 'top',
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
                                allowBlank: false, store: standardFieldIdStore, displayField:'name'},
                            { fieldLabel: 'unique', name: 'unique', xtype: 'combobox', store: uniqueStore,
                                displayField: 'name', editable: false},
                            { fieldLabel: 'primary', name: 'primary', xtype: 'combobox', store: primaryStore,
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
                            { fieldLabel: 'not-null', name: 'notNull', xtype: 'combobox', store: notNullStore,
                                displayField: 'name', editable: false},
                            { fieldLabel: 'auto-increase', name: 'autoIncrease', xtype: 'combobox', displayField: 'name',
                                store: autoIncreaseStore, editable: false}
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
                            fieldStore.remove(del);
                            fieldStore.insert(rowIndex,record);
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
            tableFieldForm.getForm().setValues(record);
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
        };
        function editForeign(foreignGrid, rowIndex){
            var record = foreignGrid.getStore().getAt(rowIndex).data;
            var foreignForm = new Ext.FormPanel({
                //labelAlign: 'top',
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
                            foreignStore.remove(del);
                            foreignStore.insert(rowIndex,record);
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
        };
        function editIndexField(indexFieldGrid, rowIndex){
            var record = indexFieldGrid.getStore().getAt(rowIndex).data;
            var indexFieldForm = new Ext.FormPanel({
                //labelAlign: 'top',
                bodyStyle: 'padding:5px 5px 0',
                layout: 'column',
                title: 'index-field修改',
                items: [
                    {
                        layout: 'form',
                        columnWidth: 0.5,
                        frame: true,
                        items: [
                            { fieldLabel: 'field', name: 'field', xtype: 'textfield',
                                allowBlank: false, regex: /^\w+$/}
                        ]
                    },
                    {
                        layout: 'form',
                        columnWidth: 0.5,
                        frame: true,
                        border: false,
                        items: [
                            { fieldLabel: 'direction', name: 'direction', xtype: 'combobox', editable: false,
                                store: directionStore, displayField: 'name', allowBlank: false}
                        ]
                    }
                ],
                buttonAlign: 'center',
                buttons: [
                    {
                        text: '保存',
                        handler: function () {
                            var del = indexFieldGrid.getStore().getAt(rowIndex);
                            var indexName = del.data.index_name;
                            var record = this.up('form').getForm().getValues();
                            indexFieldStore.remove(del);
                            var indexField_model = new indexFieldModel;
                            indexField_model.data.field = record.field;
                            indexField_model.data.direction = record.direction;
                            indexField_model.data.index_name = indexName;
                            indexFieldStore.insert(rowIndex,indexField_model.data);
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
            indexFieldForm.getForm().setValues(record);
            var win = Ext.create("Ext.window.Window", {

                draggable: true,
                height: 200,                          //高度
                width: 400,                           //宽度
                layout: "fit",                        //窗口布局类型
                modal: true, //是否模态窗口，默认为false
                resizable: false,
                items: [indexFieldForm]
            });
            win.show();
        };
        function editIndex(indexGrid, rowIndex) {
            var record = indexGrid.getStore().getAt(rowIndex).data;
            var indexForm = new Ext.FormPanel({
                //labelAlign: 'top',
                bodyStyle: 'padding:5px 5px 0',
                layout: 'column',
                title: '外键修改',
                items: [
                    {
                        layout: 'form',
                        columnWidth: 0.5,
                        frame: true,
                        items: [
                            { fieldLabel: 'name', name: 'index_name', xtype: 'textfield', readOnly: true,
                                regex: /^\w+$/, allowBlank: false
                            },
                            { fieldLabel: 'unique', name: 'index_unique', xtype: 'combobox', displayField: 'name',
                                allowBlank: false, store: uniqueStore, editable: false}
                        ]
                    },
                    {
                        layout: 'form',
                        columnWidth: 0.5,
                        frame: true,
                        border: false,
                        items: [
                            { fieldLabel: 'description', name: 'index_description', xtype: 'textfield'}
                        ]
                    }
                ],
                buttonAlign: 'center',
                buttons: [
                    {
                        text: '保存',
                        handler: function () {
                            var del = indexGrid.getStore().getAt(rowIndex);
                            var record = this.up('form').getForm().getValues();
                            console.log('record',record);
                            console.log('del', del);
                            indexStore.remove(del);
                            indexStore.insert(rowIndex,record);
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
            indexForm.getForm().setValues(record);
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
        }
        /**
         * 索引grid中编辑（新增indexField）按钮的实现 弹窗
         * */
        function indexFieldEdit(record) {
            var indexName = record.data.index_name;
            var indexFieldGrid = Ext.create('Ext.grid.Panel', {
                title: "index:" + indexName,
                store: indexFieldStore,
                loadMask: true,
                selType: 'rowmodel',
                autoScroll: true,
                columns:[
                    { dataIndex: 'field', text: 'field', align: 'center' },
                    { dataIndex: 'direction', text: 'direction', align: 'center'},
                    {
                        text: '操作1',
                        xtype: 'actioncolumn',
                        align:"center",
                        width: 70,
                        items: [
                            {
                                tooltip: '编辑',
                                icon: "css/images/editor/edit.png",
                                handler: function(indexFieldGrid, rowIndex, colIndex) {
                                    editIndexField(indexFieldGrid, rowIndex, colIndex);
                                }
                            }]

                    },
                    {
                        text: '操作2',
                        xtype: 'actioncolumn',
                        align:"center",
                        width: 70,
                        items: [
                            {
                                tooltip: '删除',
                                icon: "css/images/grid/delete.gif",
                                handler: function(indexFieldGrid, rowIndex) {
                                    deleteIndexField(indexFieldGrid, rowIndex);
                                }
                            }]

                    }
                ],
                dockedItems: [
                    {
                        xtype:'toolbar',
                        dock: 'top',
                        items: [
                            {
                                xtype: 'tbfill'
                            },
                            {
                                xtype:'button',
                                border: '1px',
                                width: 60,
                                text:'新增',
                                handler: function () {
                                    indexFieldAdd();
                                }
                            }
                        ]
                    }
                ],
                buttonAlign: 'center',
                buttons: [{
                    text: '关闭',
                    handler: function () {
                        win.close(this);
                    }
                }
                ]

            });

            /**
             * 清空过滤器，如果不清空，将在上次过滤的基础上再次过滤
             * */
            indexFieldStore.clearFilter();

            /**
             * 过滤store显示对应数据
             * */
            indexFieldStore.filter('index_name', indexName);
            var win = Ext.create("Ext.window.Window", {

                draggable: true,
                height: 300,                          //高度
                width: 400,                           //宽度
                layout: "fit",                        //窗口布局类型
                modal: true, //是否模态窗口，默认为false
                resizable: false,
                items: [indexFieldGrid]
            });
            win.show();

        };

        /**
         * grid的删除按钮的实现
         * */
        function deleteTableField(tableFieldGrid, rowIndex){
            Ext.MessageBox.confirm('提示','是否确认删除该字段',function (btn) {
                if(btn == 'yes'){
                    var record = tableFieldGrid.getStore().getAt(rowIndex);
                    fieldStore.remove(record);
                    Ext.Msg.alert('成功','删除成功');
                };
            });
        };
        function deleteForeign(foreignGrid, rowIndex) {
            Ext.MessageBox.confirm('提示','是否确认删除该外键',function (btn) {
                if(btn == 'yes'){
                    var record = foreignGrid.getStore().getAt(rowIndex);
                    foreignStore.remove(record);
                    Ext.Msg.alert('成功','删除成功');
                };
            });
        };
        function deleteIndexField(indexFieldGrid ,rowIndex) {
            Ext.MessageBox.confirm('提示', '是否确认删除该索引字段', function (btn) {
                if(btn == 'yes'){
                    var record = indexFieldGrid.getStore().getAt(rowIndex);
                    indexFieldStore.remove(record);
                    Ext.Msg.alert('成功','删除成功');
                };
            });
        };
        function deleteIndex(indexGrid, rowIndex){
            Ext.MessageBox.confirm('提示','是否确认删除该索引及其相关索引字段', function (btn) {
                if(btn == 'yes'){
                    var indexName = indexGrid.getStore().getAt(rowIndex).data.index_name;
                    var record = indexGrid.getStore().getAt(rowIndex);
                    var recordArray = indexFieldStore.getRange();
                    for(var i in recordArray){
                        var char = recordArray[i].get('index_name');
                        if(char == indexName){
                            indexFieldStore.remove(recordArray[i]);
                        };
                    };
                    indexStore.remove(record);
                    Ext.Msg.alter('提示','删除成功');
                }
            })
        };

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
                            { fieldLabel: 'unique', name: 'unique', xtype: 'combobox', store: uniqueStore,
                                allowBlank: false, displayField: 'name', editable: false},
                            { fieldLabel: 'primary', name: 'primary', xtype: 'combobox', store: primaryStore,
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
                                store: notNullStore, displayField: 'name' ,editable: false},
                            { fieldLabel: 'auto-increase', name: 'autoIncrease', xtype: 'combobox',
                                displayField: 'name', store: autoIncreaseStore, editable: false}
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
                            foreignStore.insert(0,record);
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
                                displayField: 'name', store: uniqueStore, editable: false}
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
        function indexFieldAdd() {
            var indexFieldAddForm = new Ext.FormPanel({
                //labelAlign: 'top',
                bodyStyle: 'padding:5px 5px 0',
                layout: 'column',
                columnWidth: 0.45,
                frame: true,
                title: 'index-field新增',
                items: [
                    { fieldLabel: 'field', name: 'field', xtype: 'textfield'},
                    { fieldLabel: 'direction', name: 'direction', xtype: 'combobox', store: directionStore,
                        displayField: 'name' , editable: false},
                ],
                buttonAlign: 'center',
                buttons: [
                    {
                        text: '保存',
                        handler: function () {
                            var record = this.up('form').getForm().getValues();
                            var indexField_model = new indexFieldModel;
                            indexField_model.data.field = record.field;
                            indexField_model.data.direction = record.direction;
                            indexField_model.data.index_name = indexFieldStore.getAt(0).data.index_name;
                            indexFieldStore.insert(0,indexField_model.data);
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
                height: 200,                          //高度
                width: 400,                           //宽度
                layout: "fit",                        //窗口布局类型
                modal: true, //是否模态窗口，默认为false
                resizable: false,
                items: [indexFieldAddForm]
            });
            win.show();
        };

        Ext.getCmp('btn_save').on('click', function () {
            var tableReturn = {};
            var formValues = Ext.getCmp('tableForm').getForm().getValues();
            console.log('formValues:', formValues);
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
            var foreignRecords = foreignStore.getRange();
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
    }
})