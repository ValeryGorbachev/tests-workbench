import { BaseDto } from './base-dto';
import { SelectListItemDto } from './select-list-item-dto';

export interface GoodsShipmentItem extends BaseDto {
    itemType: SelectListItemDto;
    quantity: number;
    price: number;
    totalSum: number;
}

export interface GoodsShipment extends BaseDto {
    documentDate: string;
    documentNumber: string;
    contractor: SelectListItemDto | null;
    contract?: SelectListItemDto | null;
    warehouse: SelectListItemDto | null;
    conducted: boolean;
    items: GoodsShipmentItem[];
}
