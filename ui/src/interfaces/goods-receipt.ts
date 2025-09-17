import { BaseDto } from './base-dto';
import { SelectListItemDto } from './select-list-item-dto';

export interface GoodsReceiptItem extends BaseDto {
    itemType: SelectListItemDto;
    quantity: number;
    price: number;
    totalSum: number;
}

export interface GoodsReceipt extends BaseDto {
    documentDate: string;
    documentNumber: string;
    contractor: SelectListItemDto | null;
    contract?: SelectListItemDto | null;
    warehouse: SelectListItemDto | null;
    conducted: boolean;
    items: GoodsReceiptItem[];
}


