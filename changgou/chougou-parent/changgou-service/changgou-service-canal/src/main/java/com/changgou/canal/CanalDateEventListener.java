package com.changgou.canal;


import com.alibaba.otter.canal.protocol.CanalEntry;
import com.xpand.starter.canal.annotation.*;


@CanalEventListener
public class CanalDateEventListener {

    @InsertListenPoint
    public void onEventInsert(CanalEntry.EntryType eventType,CanalEntry.RowData rowData){
        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
            System.out.println("列名"+column.getName()+"----变更的数据"+column.getValue());
        }
    }

    @UpdateListenPoint
    public void onEventUpdate(CanalEntry.EntryType eventType,CanalEntry.RowData rowData){
        for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
            System.out.println("修改前列名"+column.getName()+"----变更的数据"+column.getValue());
        }

        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
            System.out.println("修改后列名"+column.getName()+"----变更的数据"+column.getValue());
        }
    }

    @DeleteListenPoint
    public void onEventDelete(CanalEntry.EntryType eventType,CanalEntry.RowData rowData){
        for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
            System.out.println("删除前列名"+column.getName()+"----变更的数据"+column.getValue());
        }
    }

    @ListenPoint(
            eventType={CanalEntry.EventType.DELETE,CanalEntry.EventType.UPDATE},   //监听类型
            schema={"changgou_content"},
            table = {"tb_content"},
            destination = "example"
    )
    public void onEventCustomUpdate(CanalEntry.EventType eventType,CanalEntry.RowData rowData){
        for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
            System.out.println("===自定义前列名"+column.getName()+"----变更的数据"+column.getValue());
        }

        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
            System.out.println("===自定义后列名"+column.getName()+"----变更的数据"+column.getValue());
        }
    }
}
