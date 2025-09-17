import { Component } from '@angular/core';
import { TableLazyLoadEvent, TableModule } from 'primeng/table';
import { FormsModule } from '@angular/forms';
import { Button } from 'primeng/button';
import { InputText } from 'primeng/inputtext';
import { GoodsShipment } from 'src/interfaces/goods-shipment';
import { GoodsShipmentService } from '../service/goods-shipment.service';
import { DialogService } from 'primeng/dynamicdialog';
import { GoodsShipmentEditorComponent } from '@/components/goods-shipment-editor/goods-shipment-editor.component';

@Component({
    selector: 'app-goods-shipments',
    templateUrl: './goods-shipments.component.html',
    styleUrls: ['./goods-shipments.component.css'],
    imports: [TableModule, FormsModule, InputText, Button],
    providers: [DialogService]
})
export class GoodsShipmentsComponent {
    data!: GoodsShipment[];
    totalRows!: number;
    query = '';
    first = 0;
    pageSize = 10;

    constructor(private service: GoodsShipmentService, private dialogService: DialogService) {}

    loadData(event: TableLazyLoadEvent) {
        if (event.first !== undefined) this.first = event.first;
        if (event.rows != null && event.rows !== 0) this.pageSize = event.rows;
        const pageNumber = this.first / this.pageSize;
        this.service.find(this.query, pageNumber, this.pageSize).subscribe((res) => {
            this.data = res.content;
            this.totalRows = res.page.totalElements;
            if (event.forceUpdate) event.forceUpdate();
        });
    }

    edit(documentId?: number) {
        this.dialogService.open(GoodsShipmentEditorComponent, {
            width: '60vw',
            height: '70vh',
            modal: true,
            closable: true,
            breakpoints: { '960px': '60vw', '640px': '90vw' },
            data: { documentId }
        }).onClose.subscribe((res: boolean) => { if (res) this.loadData({} as any); });
    }

    delete(id: number) {
        if (!id) return;
        if (confirm('Вы уверены, что хотите удалить этот документ? Это действие нельзя отменить.')) {
            this.service.delete(id).subscribe({
                next: () => {
                    this.loadData({} as any);
                    alert('Документ успешно удален');
                },
                error: (error) => {
                    console.error('Ошибка при удалении документа:', error);
                    alert('Ошибка при удалении документа');
                }
            });
        }
    }

    conduct(id: number) {
        if (!id) return;
        if (confirm('Вы уверены, что хотите провести этот документ? Это создаст складские операции.')) {
            this.service.conduct(id).subscribe({
                next: () => {
                    this.loadData({} as any);
                    alert('Документ успешно проведен');
                },
                error: (error) => {
                    console.error('Ошибка при проведении документа:', error);
                    alert('Ошибка при проведении документа');
                }
            });
        }
    }

    unConduct(id: number) {
        if (!id) return;
        if (confirm('Вы уверены, что хотите отменить проведение этого документа? Это удалит связанные складские операции.')) {
            this.service.unConduct(id).subscribe({
                next: () => {
                    this.loadData({} as any);
                    alert('Проведение документа успешно отменено');
                },
                error: (error) => {
                    console.error('Ошибка при отмене проведения документа:', error);
                    alert('Ошибка при отмене проведения документа');
                }
            });
        }
    }
}
