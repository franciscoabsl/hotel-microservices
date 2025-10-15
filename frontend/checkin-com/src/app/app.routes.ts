import { Routes } from '@angular/router';
import { Login } from './components/usuario/login/login';
import { CadastrarUsuario } from './components/usuario/cadastrar/cadastrar-usuario';
import { Home } from './components/home/home';
import { Reserva } from './components/reserva/reserva';
import { Propriedade } from './components/propriedade/propriedade';
import { CadastrarPropriedade } from './components/propriedade/cadastrar/cadastrar-propriedade';
import { EditarPropriedade } from './components/propriedade/editar/editar-propriedade';
import { PropriedadeResolver } from './core/resolvers/propriedade.resolver';
import { AuthGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'login', component: Login },
  { path: 'register', component: CadastrarUsuario },
  { path: 'home', component: Home, canActivate: [AuthGuard] },
  { path: 'reservas', component: Reserva, canActivate: [AuthGuard] },
  { path: 'propriedades', component: Propriedade, canActivate: [AuthGuard] },
  { path: 'propriedades/cadastrar', component: CadastrarPropriedade, canActivate: [AuthGuard] },
  {
    path: 'propriedades/:id/editar',
    component: EditarPropriedade,
    resolve: { propriedade: PropriedadeResolver },
    canActivate: [AuthGuard]
  },
];
