import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { catchError, take } from 'rxjs/operators';
import { PropriedadeService } from '../services/propriedade/propriedade-service';

@Injectable({ providedIn: 'root' })
export class PropriedadeResolver implements Resolve<any> {
  constructor(private service: PropriedadeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<any> {
    const id = route.paramMap.get('id');
    if (!id) {
      console.warn('PropriedadeResolver: id ausente nos params — redirecionando.');
      this.router.navigate(['/propriedades']);
      return of(null);
    }

    return this.service.obterPorId(id).pipe(
      take(1),
      catchError(err => {
        console.error('Erro ao obter propriedade no resolver:', err);
        // redireciona para a lista em caso de erro (404, 500, etc.)
        this.router.navigate(['/propriedades']);
        return of(null);
      })
    );
  }
}
