// resolvers/propriedade.resolver.ts
import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { PropriedadeService } from '../services/propriedade-service/propriedade-service';

@Injectable({ providedIn: 'root' })
export class PropriedadeResolver implements Resolve<any> {
  constructor(private service: PropriedadeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<any> | Observable<never> {
    const id = route.paramMap.get('id')!;
    return this.service.obterPorId(id).pipe(
      catchError(err => {
        // se erro (404, etc.), redireciona pra lista
        this.router.navigate(['/propriedades']);
        return EMPTY;
      })
    );
  }
}
