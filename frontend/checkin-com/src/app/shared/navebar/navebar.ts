import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { UsuarioService } from 'src/app/core/services/usuario/usuario-service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navebar',
  imports: [RouterModule, CommonModule],
  templateUrl: './navebar.html',
  styleUrl: './navebar.scss'
})
export class Navebar  implements OnInit {
  username = '';

  constructor(private usuarioService: UsuarioService, private router: Router) {}

  ngOnInit(): void {
    const user = this.usuarioService.getUserFromToken();

    if(user === null) this.logout();
    this.username = user?.name || user?.userName || '';
  }

  logout(): void {
    this.usuarioService.logout();
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    return this.usuarioService.isLoggedIn();
  }

}
