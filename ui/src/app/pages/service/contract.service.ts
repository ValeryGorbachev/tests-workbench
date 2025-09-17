import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Contract } from 'src/interfaces/contract';
import { PageModel } from 'src/interfaces/page-model';
import { SelectListItemDto } from 'src/interfaces/select-list-item-dto';
import { AbstractCrudService } from 'src/services/AbstractCrudService';

@Injectable({
  providedIn: 'root'
})
export class ContractService extends AbstractCrudService<Contract> {
    private readonly _subUrl: string;
    constructor(protected override http: HttpClient) {
        super(http);
        this._subUrl = environment.apiBaseUrl + '/contracts';
    }
    protected override entityUrl(): string {
        return this._subUrl;
    }

    public find(query = '', contractorId?:number, page?: number, size?: number): Observable<PageModel<Contract>> {
        let params = new HttpParams();
        if (query) {
            params = params.set('query', query);
        }
        if (contractorId!==undefined) {
            params = params.append('contractorId', contractorId);
        }
        if (page!==undefined) {
            params = params.append('page', page);
        }
        if (size!==undefined) {
            params = params.append('size', size);
        } else {
            params = params.append('size', 1000);
        }

        return this.http.get<PageModel<Contract>>(this._subUrl, { params });
    }

    public findSimple(contractorId: number, query = ''): Observable<SelectListItemDto[]> {
        let params = new HttpParams();
        params = params.set('contractorId', contractorId);
        if (query) {
            params = params.set('query', query);
        }
        return this.http.get<SelectListItemDto[]>(this._subUrl + '/simple', { params });
    }

}
