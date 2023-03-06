import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISalonMetier, getSalonMetierIdentifier } from '../salon-metier.model';

export type EntityResponseType = HttpResponse<ISalonMetier>;
export type EntityArrayResponseType = HttpResponse<ISalonMetier[]>;

@Injectable({ providedIn: 'root' })
export class SalonMetierService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/salon-metiers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(salonMetier: ISalonMetier): Observable<EntityResponseType> {
    return this.http.post<ISalonMetier>(this.resourceUrl, salonMetier, { observe: 'response' });
  }

  update(salonMetier: ISalonMetier): Observable<EntityResponseType> {
    return this.http.put<ISalonMetier>(`${this.resourceUrl}/${getSalonMetierIdentifier(salonMetier) as number}`, salonMetier, {
      observe: 'response',
    });
  }

  partialUpdate(salonMetier: ISalonMetier): Observable<EntityResponseType> {
    return this.http.patch<ISalonMetier>(`${this.resourceUrl}/${getSalonMetierIdentifier(salonMetier) as number}`, salonMetier, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISalonMetier>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISalonMetier[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSalonMetierToCollectionIfMissing(
    salonMetierCollection: ISalonMetier[],
    ...salonMetiersToCheck: (ISalonMetier | null | undefined)[]
  ): ISalonMetier[] {
    const salonMetiers: ISalonMetier[] = salonMetiersToCheck.filter(isPresent);
    if (salonMetiers.length > 0) {
      const salonMetierCollectionIdentifiers = salonMetierCollection.map(salonMetierItem => getSalonMetierIdentifier(salonMetierItem)!);
      const salonMetiersToAdd = salonMetiers.filter(salonMetierItem => {
        const salonMetierIdentifier = getSalonMetierIdentifier(salonMetierItem);
        if (salonMetierIdentifier == null || salonMetierCollectionIdentifiers.includes(salonMetierIdentifier)) {
          return false;
        }
        salonMetierCollectionIdentifiers.push(salonMetierIdentifier);
        return true;
      });
      return [...salonMetiersToAdd, ...salonMetierCollection];
    }
    return salonMetierCollection;
  }
}
