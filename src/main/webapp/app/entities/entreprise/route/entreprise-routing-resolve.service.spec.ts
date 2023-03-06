import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IEntreprise, Entreprise } from '../entreprise.model';
import { EntrepriseService } from '../service/entreprise.service';

import { EntrepriseRoutingResolveService } from './entreprise-routing-resolve.service';

describe('Entreprise routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: EntrepriseRoutingResolveService;
  let service: EntrepriseService;
  let resultEntreprise: IEntreprise | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(EntrepriseRoutingResolveService);
    service = TestBed.inject(EntrepriseService);
    resultEntreprise = undefined;
  });

  describe('resolve', () => {
    it('should return IEntreprise returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEntreprise = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEntreprise).toEqual({ id: 123 });
    });

    it('should return new IEntreprise if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEntreprise = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultEntreprise).toEqual(new Entreprise());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Entreprise })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEntreprise = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEntreprise).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
