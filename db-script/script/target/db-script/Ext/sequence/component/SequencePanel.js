Ext.define('Ext.sequence.component.Panel', {
    extend: 'Ext.panel.Panel',
    title: 'Sequence',
    layout: 'form',
    width: 570,
    height: 600,
    frame: true,
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
        items = [
            this.sequenceForm,
            this.valueConfigForm,
            this.seqCacheConfigForm
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
            url: 'http://localhost:8080/dbscript//sequence/getSeqCcheConfigForm',
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
    }
})