import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { UsuarioService } from '../services/usuario/usuario-service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: UsuarioService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {

    const isLoggedIn = this.authService.isLoggedIn(); // Verifica se o usuário está logado
    const tryingToAccessLogin = state.url === '/login'; // Verifica se o usuário está tentando acessar login
    const tryingToAccessRegister = state.url === '/register'; // Verifica se o usuário está tentando acessar login

    if (!isLoggedIn && !tryingToAccessLogin && !tryingToAccessRegister) {
      // Usuário não logado tentando acessar qualquer rota que não seja login → redireciona para login
      return this.router.createUrlTree(['/login']);
    }

    if (isLoggedIn && tryingToAccessLogin) {
      // Usuário logado tentando acessar login → redireciona para home
      return this.router.createUrlTree(['/home']);
    }

    // Caso contrário, pode acessar a rota normalmente
    return true;
  }
}
