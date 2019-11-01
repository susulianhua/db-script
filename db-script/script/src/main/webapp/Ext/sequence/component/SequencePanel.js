Ext.define('Ext.sequence.component.SequencePanel', {
    extend: 'Ext.panel.Panel',
    width: 570,
    height: 600,
    title: 'Sequence',
    layout: 'form',
    moduleName: null,
    sequenceName: null,

    initComponent: function () {
        this.sequenceForm = Ext.create('Ext.sequence.component.SequenceForm');
        this.valueConfigForm = Ext.create('Ext.sequence.component.ValueConfigForm');
        this.seqCacheConfigForm = Ext.create('Ext.sequence.component.SeqCacheConfigForm');
        this.items = this.getItems();
        this.callParent(arguments)
    },

    getItems: function () {
        var me = this;
        var items = [
            me.sequenceForm,
            me.valueConfigForm,
            me.seqCacheConfigForm
        ];

        Ext.Ajax.request({
            url: 'http://localhost:8080/dbscript//sequence/getSequenceForm',
            method: 'post',
            reader:{
                type: 'json',
                root: 'data',
                totalProperty: 'total'
            },
            params: {
                moduleName: me.moduleName,
                sequenceName: me.sequenceName
            },
            success: function (response) {
                var result = Ext.JSON.decode(response.responseText).data;
                var record = result[0];
                me.sequenceForm.getForm().setValues(record);
            },
            failure:function () {
            }
        });
        Ext.Ajax.request({
            url: 'http://localhost:8080/dbscript//sequence/getValueConfigForm',
            method: 'post',
            reader:{
                type: 'json',
                root: 'data',
                totalProperty: 'total'
            },
            params: {
                moduleName: me.moduleName,
                sequenceName: me.sequenceName
            },
            success: function (response) {
                var result = Ext.JSON.decode(response.responseText).data;
                var record = result[0];
                me.valueConfigForm.getForm().setValues(record);
            },
            failure:function () {
            }
        });
        Ext.Ajax.request({
            url: 'http://localhost:8080/dbscript//sequence/getSeqCacheConfigForm',
            method: 'post',
            reader:{
                type: 'json',
                root: 'data',
                totalProperty: 'total'
            },
            params: {
                moduleName: me.moduleName,
                sequenceName: me.sequenceName
            },
            success: function (response) {
                var result = Ext.JSON.decode(response.responseText).data;
                var record = result[0];
                me.seqCacheConfigForm.getForm().setValues(record);
            },
            failure:function () {
            }
        });
        return items;
    },

    savePanel: function () {
        var me = this;
        var sequenceWithModuleName = {};
        var sequence = {};
        var sequenceFormValues = this.sequenceForm.getForm().getValues();
        sequenceWithModuleName.moduleName = sequenceFormValues.moduleName;
        sequence.name = sequenceFormValues.name;
        sequence.dataType = sequenceFormValues.dataType;
        sequence.incrementBy = sequenceFormValues.incrementBy;
        sequence.startWith = sequenceFormValues.startWith;
        sequence.cycle = sequenceFormValues.cycle;
        sequence.order = sequenceFormValues.order;
        var valueConfigFormValues = this.valueConfigForm.getForm().getValues();
        var seqCacheConfigFormValues = this.seqCacheConfigForm.getForm().getValues();
        var valueConfig = {};
        var seqCacheConfig = {};
        valueConfig.minValue = valueConfigFormValues.minValue;
        valueConfig.maxValue = valueConfigFormValues.maxValue;
        seqCacheConfig.cache = seqCacheConfigFormValues.cache;
        seqCacheConfig.number = seqCacheConfigFormValues.number;
        sequence.valueConfig = valueConfig;
        sequence.seqCacheConfig = seqCacheConfig;
        sequenceWithModuleName.sequence = sequence;

        Ext.Ajax.request({
            url: 'http://localhost:8080/dbscript//sequence/saveSequence',
            headers: {'ContentType': 'application/json;charset=UTF-8',
                'Content-Type': 'application/json'
            },
            ContentType : 'application/json;charset=UTF-8',
            dataType: 'json',
            params: JSON.stringify(sequenceWithModuleName),
            method: 'Post',
            success: function () {
                Ext.Msg.alert('成功', '添加成功');
            },
            failure: function () {
                Ext.Msg.alert('失败', '添加失败，请重试');
            }
        })
    }
})