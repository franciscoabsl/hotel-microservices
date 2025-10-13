import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthUserApi } from 'src/app/core/services/auth-user/auth-user-api';
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

  constructor(private auth: AuthUserApi, private router: Router) {}

  ngOnInit(): void {
    console.log('ngOnInit');
    const user = this.auth.getUserFromToken();
    console.log('user', user);

    if(user === null) this.logout();
    this.username = user?.name || user?.userName || '';
  }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    return this.auth.isLoggedIn();
  }

}
