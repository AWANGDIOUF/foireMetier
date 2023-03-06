import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIntroduction, getIntroductionIdentifier } from '../introduction.model';

export type EntityResponseType = HttpResponse<IIntroduction>;
export type EntityArrayResponseType = HttpResponse<IIntroduction[]>;

@Injectable({ providedIn: 'root' })
export class IntroductionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/introductions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(introduction: IIntroduction): Observable<EntityResponseType> {
    return this.http.post<IIntroduction>(this.resourceUrl, introduction, { observe: 'response' });
  }

  update(introduction: IIntroduction): Observable<EntityResponseType> {
    return this.http.put<IIntroduction>(`${this.resourceUrl}/${getIntroductionIdentifier(introduction) as number}`, introduction, {
      observe: 'response',
    });
  }

  partialUpdate(introduction: IIntroduction): Observable<EntityResponseType> {
    return this.http.patch<IIntroduction>(`${this.resourceUrl}/${getIntroductionIdentifier(introduction) as number}`, introduction, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIntroduction>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIntroduction[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addIntroductionToCollectionIfMissing(
    introductionCollection: IIntroduction[],
    ...introductionsToCheck: (IIntroduction | null | undefined)[]
  ): IIntroduction[] {
    const introductions: IIntroduction[] = introductionsToCheck.filter(isPresent);
    if (introductions.length > 0) {
      const introductionCollectionIdentifiers = introductionCollection.map(
        introductionItem => getIntroductionIdentifier(introductionItem)!
      );
      const introductionsToAdd = introductions.filter(introductionItem => {
        const introductionIdentifier = getIntroductionIdentifier(introductionItem);
        if (introductionIdentifier == null || introductionCollectionIdentifiers.includes(introductionIdentifier)) {
          return false;
        }
        introductionCollectionIdentifiers.push(introductionIdentifier);
        return true;
      });
      return [...introductionsToAdd, ...introductionCollection];
    }
    return introductionCollection;
  }
}
