import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpErrorResponse,
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { AuthUserApi } from '../services/auth-user-api';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private authUserApi: AuthUserApi, private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.authUserApi.getToken();


    // Clone request e adiciona header Authorization se houver token
    let authReq = req;
    if (token) {
      authReq = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      });
    }

    return next.handle(authReq).pipe(
      catchError((err: unknown) => {
        // Tratamento centralizado de erros de autenticação
        if (err instanceof HttpErrorResponse) {
          if (err.status === 401) {
            // Token inválido/expirado — fazer logout local e redirecionar para login
            this.authUserApi.logout();
            // Opcional: adicionar query param com returnUrl
            const returnUrl = this.router.url;
            this.router.navigate(['/login'], { queryParams: { returnUrl } });
          }
        }
        return throwError(() => err);
      })
    );
  }
}
