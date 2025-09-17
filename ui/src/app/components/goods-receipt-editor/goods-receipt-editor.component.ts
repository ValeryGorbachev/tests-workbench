import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormArray, Validators, ReactiveFormsModule } from '@angular/forms';
import { DynamicDialogRef, DynamicDialogConfig } from 'primeng/dynamicdialog';
import { InputText } from 'primeng/inputtext';
import { Button } from 'primeng/button';
import { DatePickerModule } from 'primeng/datepicker';
import { AutoCompleteModule, AutoCompleteCompleteEvent } from 'primeng/autocomplete';
import { TableModule } from 'primeng/table';
import { CommonModule } from '@angular/common';

import { GoodsReceipt, GoodsReceiptItem } from 'src/interfaces/goods-receipt';
import { SelectListItemDto } from 'src/interfaces/select-list-item-dto';
import { GoodsReceiptService } from '@/pages/service/goods-receipt.service';
import { ContractorService } from '@/pages/service/contractor.service';
import { ContractService } from '@/pages/service/contract.service';
import { WarehouseService } from '@/pages/service/warehouse.service';
import { WarehouseItemTypeService } from '@/pages/service/warehouse-item-type.service';
import { forkJoin } from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
    selector: 'app-goods-receipt-editor',
    templateUrl: './goods-receipt-editor.component.html',
    styleUrls: ['./goods-receipt-editor.component.css'],
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        InputText,
        Button,
        DatePickerModule,
        AutoCompleteModule,
        TableModule
    ]
})
export class GoodsReceiptEditorComponent implements OnInit {

    form: FormGroup = new FormGroup({
        documentNumber: new FormControl('', Validators.required),
        documentDate: new FormControl(new Date(), Validators.required),
        contractor: new FormControl<SelectListItemDto | null>(null, Validators.required),
        contract: new FormControl<SelectListItemDto | null>(null),
        warehouse: new FormControl<SelectListItemDto | null>(null, Validators.required),
        items: new FormArray([])
    });

    contractors: SelectListItemDto[] = [];
    contracts: SelectListItemDto[] = [];
    warehouses: SelectListItemDto[] = [];
    itemTypes: SelectListItemDto[] = [];

    constructor(
        public ref: DynamicDialogRef,
        private config: DynamicDialogConfig,
        private service: GoodsReceiptService,
        private contractorService: ContractorService,
        private contractService: ContractService,
        private warehouseService: WarehouseService,
        private itemTypeService: WarehouseItemTypeService
    ) {}

    ngOnInit() {
        this.loadInitialData();

        const documentId = this.config?.data?.documentId as number | undefined;
        if (documentId) {
            this.service.findById(documentId).subscribe(d => {
                const documentDate = d.documentDate ? new Date(d.documentDate) : null;

                this.form.patchValue({
                    documentNumber: d.documentNumber,
                    documentDate: documentDate,
                    contractor: d.contractor,
                    contract: d.contract,
                    warehouse: d.warehouse
                });
                d.items.forEach(item => this.addRow(item));
            });
        }

        this.form.get('contractor')!.valueChanges.subscribe(contractor => {
            this.form.patchValue({ contract: null });
            this.contracts = [];
            if (contractor && contractor.id) {
                this.contractService.findSimple(contractor.id, '').subscribe(r => this.contracts = r);
            }
        });
    }

    get items(): FormArray {
        return this.form.get('items') as FormArray;
    }

    addRow(item?: Partial<GoodsReceiptItem>) {
        const group = new FormGroup({
            itemType: new FormControl<SelectListItemDto | null>(item?.itemType ?? null, Validators.required),
            quantity: new FormControl<number>(item?.quantity ?? 0, [Validators.required, Validators.min(0)]),
            price: new FormControl<number>(item?.price ?? 0, [Validators.required, Validators.min(0)]),
            totalSum: new FormControl<number>({ value: item?.totalSum ?? 0, disabled: true })
        });
        this.items.push(group);
    }

    removeRow(index: number) {
        this.items.removeAt(index);
    }

    recalcRow(index: number) {
        const row = this.items.at(index);
        const q = Number(row.get('quantity')!.value) || 0;
        let p = Number(row.get('price')!.value) || 0;

        p = Number(p.toFixed(2));
        row.get('price')!.setValue(p, { emitEvent: false });

        const total = Number((q * p).toFixed(2));
        row.get('totalSum')!.setValue(total, { emitEvent: false });
    }

    save() {
        const value = this.form.getRawValue();

        const documentDate = value.documentDate instanceof Date
            ? value.documentDate.toISOString().split('T')[0]
            : value.documentDate;

        const data: GoodsReceipt = {
            id: this.config?.data?.documentId ?? undefined,
            documentNumber: value.documentNumber,
            documentDate: documentDate,
            contractor: value.contractor ? { id: value.contractor.id } as any : null,
            contract: value.contract ? { id: value.contract.id } as any : null,
            warehouse: value.warehouse ? { id: value.warehouse.id } as any : null,
            conducted: false,
            items: value.items.map((item: GoodsReceiptItem) => ({
                id: item.id ?? undefined,        // новые строки
                quantity: item.quantity,
                price: item.price,
                totalSum: item.totalSum,
                itemType: item.itemType ? { id: item.itemType.id } as any : null,
            }))
        };

        if (data.id) {
            this.service.update(data.id, data).subscribe(() => this.ref.close(true));
        } else {
            this.service.create(data).subscribe(() => this.ref.close(true));
        }
    }




    cancel() {
        this.ref.close(false);
    }

    completeContractors(event: AutoCompleteCompleteEvent) {
        this.contractorService.findSimple(event.query).subscribe(r => this.contractors = r);
    }

    completeContracts(event: AutoCompleteCompleteEvent) {
        const contractor = this.form.get('contractor')!.value;
        if (!contractor) return;
        this.contractService.findSimple(contractor.id!, event.query).subscribe(r => this.contracts = r);
    }

    completeWarehouses(event: AutoCompleteCompleteEvent) {
        this.warehouseService.find(event.query, 0, 20).subscribe(page => {
            this.warehouses = page.content.map(w => ({ id: w.id!, title: w.title }));
        });
    }

    completeItemTypes(event: AutoCompleteCompleteEvent) {
        this.itemTypeService.find(event.query, 0, 20).subscribe(r => {
            this.itemTypes = r.content.map(x => ({ id: x.id!, title: x.title }));
        });
    }


    uploadCsv() {
        const input = document.createElement('input');
        input.type = 'file';
        input.accept = '.csv,.txt';
        input.onchange = (e: any) => {
            const file = e.target.files && e.target.files[0];
            if (!file) return;

            const reader = new FileReader();
            reader.onload = (ev: any) => {
                const text = (ev.target.result as string) || '';
                const lines = text.split(/\r?\n/).map(l => l.trim()).filter(l => l !== '');
                if (lines.length === 0) return;

                const parsed = lines.map(line => {
                    const parts = line.split(/[;,]/).map(p => p.trim());
                    return {
                        title: parts[0] ?? '',
                        quantity: Number(parts[1]) || 0,
                        price: Number(parts[2]) || 0
                    };
                }).filter(p => p.title);

                const requests = parsed.map(p =>
                    this.itemTypeService.find(p.title, 0, 1).pipe(
                        map((resp: any) => {
                            const found = resp?.content?.[0];
                            return { parsed: p, found };
                        })
                    )
                );

                forkJoin(requests).subscribe({
                    next: (results) => {
                        results.forEach(r => {
                            if (!r.found) {
                                console.warn(`Не найден itemType для "${r.parsed.title}", пропускаем`);
                                return;
                            }

                            const item = {
                                itemType: {
                                    id: r.found.id,
                                    title: r.found.title
                                } as any,
                                quantity: r.parsed.quantity,
                                price: r.parsed.price,
                                totalSum: +(r.parsed.quantity * r.parsed.price).toFixed(2)
                            };

                            this.addRow(item);
                        });
                    },
                    error: (err) => {
                        console.error('Ошибка при поиске itemType', err);
                        alert('Ошибка при поиске itemType');
                    }
                });
            };
            reader.readAsText(file);
        };
        input.click();
    }


    private loadInitialData() {
        this.contractorService.findSimple('').subscribe({
            next: (r) => (this.contractors = r),
            error: (err) => console.error('Ошибка загрузки контрагентов:', err)
        });

        this.warehouseService.find('', 0, 100).subscribe({
            next: (page) => {
                this.warehouses = page.content.map((w) => ({ id: w.id!, title: w.title }));
            },
            error: (err) => console.error('Ошибка загрузки складов:', err)
        });

        this.itemTypeService.find('', 0, 100).subscribe({
            next: (r) => {
                this.itemTypes = r.content.map((x) => ({ id: x.id!, title: x.title }));
            },
            error: (err) => console.error('Ошибка загрузки типов номенклатуры:', err)
        });
    }
}
