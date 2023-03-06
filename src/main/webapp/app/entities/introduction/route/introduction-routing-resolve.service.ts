import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIntroduction, Introduction } from '../introduction.model';
import { IntroductionService } from '../service/introduction.service';

@Injectable({ providedIn: 'root' })
export class IntroductionRoutingResolveService implements Resolve<IIntroduction> {
  constructor(protected service: IntroductionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIntroduction> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((introduction: HttpResponse<Introduction>) => {
          if (introduction.body) {
            return of(introduction.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Introduction());
  }
}
