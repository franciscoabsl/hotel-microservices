import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptorFn: HttpInterceptorFn = (req, next) => {
  console.log('[authInterceptorFn] intercept ->', req.url);
  const token = localStorage.getItem('auth_token');

  const modifiedReq = token
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;

  return next(modifiedReq);
};
