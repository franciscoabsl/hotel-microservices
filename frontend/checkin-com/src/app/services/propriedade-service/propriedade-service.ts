import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders  } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})export class PropriedadeService {
  private url = environment.apiUrl + 'propriedades';

  constructor(private http: HttpClient) {}

  getDados(): Observable<any> {
    return this.http.get<any>(this.url);
  }

  cadastrar(propriedade: any): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
          'Content-Type': 'application/json',
          ...(token ? { Authorization: `Bearer ${token}` } : {})
        });
    return this.http.post<any>(this.url, propriedade, { headers });
  }

  atualizar(propriedade: any): Observable<any> {
    return this.http.put<any>(this.url + `/${propriedade.id}`, propriedade);
  }

  excluir(id: number): Observable<any> {
    return this.http.delete<any>(this.url + `/${id}`);
  }

  obterPorId(id: number): Observable<any> {
    return this.http.get<any>(this.url + `/${id}`);
  }
}
