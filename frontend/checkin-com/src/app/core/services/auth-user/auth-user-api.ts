import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { map, tap, catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

export interface LoginPayload {
  email: string;
  senha: string;
}

export interface LoginResponse {
  token: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthUserApi {
  private url = environment.apiUrl;
  private tokenKey = 'auth_token';

  constructor(private http: HttpClient) {}

  login(payload: LoginPayload): Observable<void> {

    return this.http.post<LoginResponse>(this.url+'auth/login', payload).pipe(
      tap((res) => {
        if (!res || !res.token) {
          throw new Error('Resposta de login inválida: token ausente.');
        }
      }),
      map((res) => res.token),
      tap((token) => this.saveToken(token)),
      map(() => void 0),
      catchError(this.handleError)
    );
  }

  /** Salva token (localStorage) */
  private saveToken(token: string) {
    try {
      localStorage.setItem(this.tokenKey, token);
    } catch (err) {
      // localStorage pode falhar em modos privados / quotas — capture para debug.
      console.error('Erro ao salvar token no localStorage', err);
    }
  }

  /** Retorna token atual (ou null) */
  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  /** Remove token (logout local) */
  logout(): void {
    localStorage.removeItem(this.tokenKey);
    // você pode também querer redirecionar para a rota de login no componente que chama logout
  }

  /** Indica se há token salvo (simples verificação). Para validação mais robusta, decodifique/valide expiry. */
  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  /** Erro padrão para requisições de autenticação */
  private handleError(error: HttpErrorResponse) {
    // Normaliza mensagens de erro para o componente que vai exibir
    let message = 'Erro desconhecido.';

    if (error.error && typeof error.error === 'string') {
      // alguns backends retornam string
      message = error.error;
    } else if (error.error && error.error.message) {
      message = error.error.message;
    } else if (error.status === 0) {
      message = 'Não foi possível conectar ao servidor (verifique se o backend está rodando).';
    } else if (error.status >= 400 && error.status < 500) {
      message = 'Credenciais inválidas ou requisição inválida.';
    } else if (error.status >= 500) {
      message = 'Erro no servidor. Tente novamente mais tarde.';
    }

    // Retorna um Observable de erro com a mensagem legível
    return throwError(() => new Error(message));
  }

  /** ✅ Decodifica o token JWT e retorna os dados do usuário */
  getUserFromToken(): any | null {
    const token = this.getToken();
    if (!token) return null;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload;
    } catch (e) {
      console.error('Erro ao decodificar token', e);
      return null;
    }
  }

  /** ✅ Retorna o nome do usuário logado (se existir no token) */
  getUsername(): string {
    const user = this.getUserFromToken();
    return user?.name || user?.username || '';
  }
}
