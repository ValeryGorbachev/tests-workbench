import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { PageModel } from 'src/interfaces/page-model';
import { GoodsShipment } from 'src/interfaces/goods-shipment';
import { AbstractCrudService } from 'src/services/AbstractCrudService';

@Injectable({ providedIn: 'root' })
export class GoodsShipmentService extends AbstractCrudService<GoodsShipment> {
    private readonly _subUrl: string;
    constructor(protected override http: HttpClient) {
        super(http);
        this._subUrl = environment.apiBaseUrl + '/goods-shipment';
    }
    protected override entityUrl(): string { return this._subUrl; }

    public find(query = '', page?: number, size?: number): Observable<PageModel<GoodsShipment>> {
        let params = new HttpParams();
        if (query) params = params.set('query', query);
        if (page!==undefined) params = params.append('page', page);
        if (size!==undefined) params = params.append('size', size); else params = params.append('size', 1000);
        return this.http.get<PageModel<GoodsShipment>>(this._subUrl, { params });
    }

    public conduct(id: number): Observable<void> {
        return this.http.post<void>(`${this._subUrl}/${id}/conduct`, {});
    }
    public unConduct(id: number): Observable<void> {
        return this.http.post<void>(`${this._subUrl}/${id}/unconduct`, {});
    }
}
