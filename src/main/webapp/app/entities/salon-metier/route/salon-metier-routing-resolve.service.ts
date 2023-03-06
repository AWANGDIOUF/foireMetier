import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISalonMetier, SalonMetier } from '../salon-metier.model';
import { SalonMetierService } from '../service/salon-metier.service';

@Injectable({ providedIn: 'root' })
export class SalonMetierRoutingResolveService implements Resolve<ISalonMetier> {
  constructor(protected service: SalonMetierService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISalonMetier> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((salonMetier: HttpResponse<SalonMetier>) => {
          if (salonMetier.body) {
            return of(salonMetier.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SalonMetier());
  }
}
